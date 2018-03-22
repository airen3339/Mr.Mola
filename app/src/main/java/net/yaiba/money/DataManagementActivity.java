package net.yaiba.money;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.yaiba.money.db.MoneyDB;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DataManagementActivity extends Activity {
	private MoneyDB MoneyDB;
	private Cursor mCursor;
	private int RECORD_ID = 0;
	private int selectBakupFileIndex = 0;
	private String[] bakFileArray ;
	private String FILE_DIR_NAME = "yaiba.net//money";
	private String fileNameSuff = ".xml";

	private TextView TotalCount;
	private Long lCount;

	//检测是否有写的权限用
	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			"android.permission.READ_EXTERNAL_STORAGE",
			"android.permission.WRITE_EXTERNAL_STORAGE" };

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MoneyDB = new MoneyDB(this);
		super.onCreate(savedInstanceState);

		//检测是否有写的权限用
		verifyStoragePermissions(DataManagementActivity.this);

		setContentView(R.layout.data_management_activity);

		lCount = MoneyDB.getAllCount("id asc");
		TotalCount = (TextView) findViewById(R.id.redordCount);
		TotalCount.setText("可备份记录数：x".replace("x", lCount.toString()));


		//备份功能
		Button bn_data_bakup = (Button)findViewById(R.id.data_bakup);
		bn_data_bakup.setOnClickListener(new OnClickListener(){
			   public void  onClick(View v) {
				   dataBakup();
			   }  
			  });
		//恢复功能
		Button bn_data_recover = (Button)findViewById(R.id.data_recover);
		bn_data_recover.setOnClickListener(new OnClickListener(){
			   public void  onClick(View v) {
				   
				   //弹出提示，确认后恢复
				   AlertDialog.Builder builder= new AlertDialog.Builder(DataManagementActivity.this);
				   builder.setIcon(android.R.drawable.ic_dialog_info);
				   builder.setTitle("选择要恢复的文件,恢复后原有记录将被清空");

				   List<String> bakupFileList = new ArrayList<String>();
				   String filePath = Environment.getExternalStorageDirectory().toString()  + "//" +FILE_DIR_NAME;
				   File[] files = new File(filePath).listFiles();

				   //判断文件夹不存在或文件夹中没有文件时
				   if(files != null){
					   //存在时

					   for (int i = 0; i < files.length; i++) {
						   File file = files[i];
						   if (checkIsXMLFile(file.getPath())) {
							   //判断文件名中是否不包含20170216020803!!!!!.xml 这种文件，这种文件是未加密的文件，禁止在列表中显示
							   if(file.getName().indexOf("!")==-1){
								   bakupFileList.add(file.getName());
							   }
						   }
					   }

					   if(bakupFileList.size()<=0){

						   builder.setMessage("没有找到可用来恢复的备份文件");
						   builder.setNegativeButton("取消", null);
						   builder.create().show();

					   } else {
						   Collections.sort(bakupFileList);
						   Collections.reverse(bakupFileList);

						   bakFileArray = bakupFileList.toArray(new String[bakupFileList.size()]);

						   builder.setIcon(android.R.drawable.ic_dialog_alert);
						   builder.setSingleChoiceItems(bakFileArray, 0, new DialogInterface.OnClickListener() {
							   public void onClick(DialogInterface dialog, int index) {
								   selectBakupFileIndex = index;
								   //Toast.makeText(DataManagementActivity.this, "selectBakupFileIndex:"+selectBakupFileIndex , Toast.LENGTH_SHORT).show();
							   }
						   });
						   builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							   public void onClick(DialogInterface dialog, int whichButton) {
								   dataRecover(bakFileArray[selectBakupFileIndex]);
								   //Toast.makeText(DataManagementActivity.this, "selectBakupFileIndex:"+selectBakupFileIndex , Toast.LENGTH_SHORT).show();
							   }
						   });
						   builder.setNegativeButton("取消", null);
						   builder.create().show();

					   }
				   } else {
					   //不存在时
					   builder.setMessage("没有找到可用来恢复的备份文件");
					   builder.setNegativeButton("取消", null);
					   builder.create().show();
				   }
			    }
		 });


		//恢复功能
		Button bn_data_from_wacai = (Button)findViewById(R.id.data_from_wacai);
		bn_data_from_wacai.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				//intent.setType(“image/*”);//选择图片
				//intent.setType(“audio/*”); //选择音频
				//intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
				//intent.setType(“video/*;image/*”);//同时选择视频和图片
				intent.setType("*/*");//无类型限制
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(Intent.createChooser(intent, "选择一个本地的xls文件"), 1);


			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent mainIntent = new Intent(DataManagementActivity.this,MainActivity.class);
			startActivity(mainIntent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			setResult(RESULT_OK, mainIntent);  
			finish(); 
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@SuppressWarnings("resource")
	@SuppressLint("SimpleDateFormat")
	public void dataBakup(){
		MoneyDB = new MoneyDB(this);
		mCursor = MoneyDB.getAllForBakup("id asc");

		//检查目录并确定生成目录结构
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + FILE_DIR_NAME;
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm_ss");
		//String fileName = fileNamePre + sdf.format(dt)+ fileNameSuff;

		//showAboutDialog("准备","正在处理未加密数据");
		try {
			if (sdCardExist) {
				String fileName = sdf.format(dt) + fileNameSuff;
				File f = createFile(baseDir, fileName);

				FileWriter fileWriter = new FileWriter(f,false);
				BufferedWriter bf = new BufferedWriter(fileWriter);
				bf.write(writeToString(mCursor));
				bf.flush();
				showAboutDialog("完成","备份文件"+fileName+"已输出到SD卡。");
			}
		} catch (IOException e) {
			showAboutDialog("错误","处理数据时出错，文件未生成");
		}
	}


	public File createFile(String baseDir, String fileName) {
		File f = new File(baseDir + "//"+ fileName );
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}

	public void dataRecover(String fileName){

		showAboutDialog("完成",dataRecoverFromXml("normal",fileName));

	}

    private void showAboutDialog(String title, String msg){
		AlertDialog.Builder builder= new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setPositiveButton("确认", null);
		builder.create().show();
	}

 	private String writeToString(Cursor mCursor){
 	    XmlSerializer serializer = Xml.newSerializer();
 	    StringWriter writer = new StringWriter();
 	    try {
 	        serializer.setOutput(writer);
 	        serializer.startDocument("UTF-8", true);
 	        serializer.startTag("", "resources");;
 	        serializer.attribute("", "count", String.valueOf(mCursor.getCount()));

 	       if(mCursor.moveToFirst()) {
		        while(!mCursor.isAfterLast()) {
					serializer.startTag("", "record");
					serializer.attribute("", "id", mCursor.getString(mCursor.getColumnIndex("id")));
					serializer.attribute("", "category_id", mCursor.getString(mCursor.getColumnIndex("category_id")));
					serializer.attribute("", "pay_id", mCursor.getString(mCursor.getColumnIndex("pay_id")));
					serializer.attribute("", "member_id", mCursor.getString(mCursor.getColumnIndex("member_id")));
					serializer.attribute("", "type_id", mCursor.getString(mCursor.getColumnIndex("type_id")));
					serializer.attribute("", "amounts", mCursor.getString(mCursor.getColumnIndex("amounts")));
					serializer.attribute("", "remark", mCursor.getString(mCursor.getColumnIndex("remark")));
					serializer.attribute("", "create_time", mCursor.getString(mCursor.getColumnIndex("create_time")));
					serializer.endTag("", "record");
		            mCursor.moveToNext();
		         }
		      }
 	        serializer.endTag("", "resources");
 	        serializer.endDocument();
 	        return writer.toString();
 	    } catch (Exception e) {
 	        throw new RuntimeException(e);
 	    } finally {
 	    	mCursor.close();
 	    }
 	}


 	private String dataRecoverFromXml(String recoverType, String fileName)
	{
		MoneyDB = new MoneyDB(this);
		String returnString = "";
		try {

			boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
			
			if (sdCardExist) {
				String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//" + FILE_DIR_NAME;
				
				String fn = "";
				if(recoverType.equals("normal")){
					fn = fileName;
				}
				File f = new File(baseDir + "//"+ fn );
				
				if(f.exists()){
					if(recoverType.equals("normal")){
						Log.v("v_debug","1");
						MoneyDB.deleteAll();
					}
					
					 int counter = 0;   
		               StringBuilder sb = new StringBuilder("");
		               XmlPullParser xrp = Xml.newPullParser();
		               FileInputStream fin = new FileInputStream(f);
		               xrp.setInput(fin, "UTF-8");
					Log.v("v_debug","2");
		                while (xrp.getEventType() != XmlPullParser.END_DOCUMENT) {
							Log.v("v_debug","3");
		                         if (xrp.getEventType() == XmlPullParser.START_TAG) {
									 Log.v("v_debug","4");
		                              String tagName = xrp.getName();
		                              if(tagName.equals("record")){
										  Log.v("v_debug","5");
		                                  counter++;   
		                                  sb.append("第"+counter+"条信息："+"\n");
		                                  //sb.append(xrp.getAttributeValue(0)+"\n"); 
		                                  String category_id = xrp.getAttributeValue(1);
		                                  String pay_id = xrp.getAttributeValue(2);
		                                  String member_id = xrp.getAttributeValue(3);
		                                  String type_id = xrp.getAttributeValue(4);
										  String amounts = xrp.getAttributeValue(5);
										  String remark = xrp.getAttributeValue(6);
										  String create_time = xrp.getAttributeValue(7);
										  if(remark.equals(null)){
		                                	remark="";
										  }
										  Log.v("v_debug","6");
										  MoneyDB.insert(category_id, pay_id,member_id, type_id, amounts, remark,create_time);

		                              }   
		                         } else if (xrp.getEventType() == XmlPullParser.END_TAG) {
		                         } else if (xrp.getEventType() == XmlPullParser.TEXT) {
		                         }    
		                         xrp.next();  
		                    }

					Log.v("v_debug","7");
		                if(recoverType.equals("normal")){
		                	returnString = counter +"条数据已恢复";
						}
		                
		                return returnString;
				}else{
					if(recoverType.equals("normal")){
						//showAboutDialog("错误","未检测到备份程序");
						returnString = "未检测到备份文件"+fileName;
					}
					return returnString;
				}
			}else{
				//showAboutDialog("错误","程序未检测到SD卡");
				returnString = "程序未检测到SD卡";
				return returnString;
			}
			
		} catch (Exception e) {
			return "真的出错了";
		} finally {
			MoneyDB.close();
 	    }
		
	}
 	
 	
 	@SuppressLint("DefaultLocale")
	private boolean checkIsXMLFile(String fName) {
 		 boolean isXMLFile = false;
 		 String fileEnd = fName.substring(fName.lastIndexOf(".") + 1,  fName.length()).toLowerCase();
 		 if (fileEnd.equals("xml")) {
 			isXMLFile = true;
 		 } else {
 			isXMLFile = false;
 		 }
 		 return isXMLFile;
 	}


	//检测是否有写的权限用
	public static void verifyStoragePermissions(Activity activity) {

		try {
			//检测是否有写的权限
			int permission = ActivityCompat.checkSelfPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE");
			if (permission != PackageManager.PERMISSION_GRANTED) {
				// 没有写的权限，去申请写的权限，会弹出对话框
				ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	// 导入挖财xls数据用

	String path;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			if ("file".equalsIgnoreCase(uri.getScheme())) { //使用第三方应用打开
				path = uri.getPath();
				Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
			} else {
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
					path = getPath(this, uri);

					Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
				} else {//4.4以下下系统调用方法
					path = getRealPathFromURI(uri);
					Toast.makeText(DataManagementActivity.this, path+"222222", Toast.LENGTH_SHORT).show();
				}
			}

			AlertDialog.Builder builder= new AlertDialog.Builder(DataManagementActivity.this);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("导入");
			builder.setMessage("下列数据文件将导入到数据库：");
			//设置path的样式。
			final TextView filePathText = new TextView(DataManagementActivity.this);
			filePathText.setText(path);
			filePathText.setPadding(60, 0, 0, 60);
			filePathText.setWidth(300);
			filePathText.setTextSize(20);
			filePathText.setTextColor(Color.RED);
			builder.setView(filePathText);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String input = filePathText.getText().toString();
					if (input.equals("")) {
						Toast.makeText(getApplicationContext(), "请选择导入的xls文件！", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), "文件读取中，请稍后...", Toast.LENGTH_LONG).show();




						try {
							/**
							 * 读取xls数据
							 * **/
							InputStream is = new FileInputStream(path);
							//Workbook book = Workbook.getWorkbook(new File("mnt/sdcard/test.xls"));
							Workbook book = Workbook.getWorkbook(is);

							int num = book.getNumberOfSheets();


							//读取第一个列表，支出
							Sheet sheet = book.getSheet(0);
							int Rows = sheet.getRows();
							int Cols = sheet.getColumns();
							Log.v("v_xls","sheet总数：" + num);
							Log.v("v_xls","当前sheet名：" + sheet.getName());
							Log.v("v_xls","行数：" + Rows);
							Log.v("v_xls","列数：" + Cols);

							for (int i = 1; i < Rows; ++i) {
								ArrayList<String>  strArray = new ArrayList<String> ();
								for (int j = 0; j < Cols; ++j) {



									if(sheet.getCell(j,i).getType() == CellType.DATE){
										String cellcon="";
										DateCell dc = (DateCell)sheet.getCell(j,i);
										Date date = dc.getDate();

										TimeZone zone = TimeZone.getTimeZone("GMT");

										SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										ds.setTimeZone(zone);
										cellcon = ds.format(date);
										strArray.add(cellcon);
										Log.v("v_xls","是DATE");
									} else {
										// getCell(Col,Row)获得单元格的值
										strArray.add(sheet.getCell(j,i).getContents());
										Log.v("v_xls","不是DATE");
									}

									Log.v("v_xls_contents",sheet.getCell(j,i).getContents());
								}
//todo



								String type_id = "0";
								String amounts = strArray.get(8);
								String remark = strArray.get(10);
								String create_time = strArray.get(7);


								String category_p_name = strArray.get(0);
								String category_c_name = strArray.get(1);
								String category_id = MoneyDB.getCategoryCID(category_c_name);//没有分类的数据为-1
								String pay_name = strArray.get(2);
								String pay_id = MoneyDB.getPayID(pay_name);
								String member_id = strArray.get(9);

								if(remark.equals(null)){
									remark="";
								}

								//行数据不为空时，登录数据库
								if(!pay_name.isEmpty() && !amounts.isEmpty() && !create_time.isEmpty() && !strArray.get(9).isEmpty() ){

									Log.v("v_xls_split",strArray.get(7));
									Log.v("v_xls_split",strArray.get(7).split(":")[0]);
									String[] crtimes = strArray.get(7).split(":");
									create_time = crtimes[0]+":"+crtimes[1];

									String[] member_name_amounts = strArray.get(9).split(":");
									member_id = MoneyDB.memberName2Id(member_name_amounts[0]);

									MoneyDB.insert(category_id, pay_id,member_id, type_id, amounts, remark,create_time);
									Log.v("v_xls_insert",category_id+"//"+pay_id+"//"+member_id+"//"+type_id+"//"+ amounts+"//"+ remark+"//"+create_time);
								}

								strArray.clear();


							}
							book.close();
						}catch (IOException e) {
							e.printStackTrace();
						} catch (BiffException e) {
							e.printStackTrace();
						}
					}
				}
			});
			builder.setNegativeButton("取消", null);
			builder.create().show();
		}
	}


	public String getRealPathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
		if(null!=cursor&&cursor.moveToFirst()){;
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
			cursor.close();
		}
		return res;
	}


	/**
	 * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
	 */
	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {


		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;


		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];


				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {


				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));


				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];


				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}


				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{split[1]};


				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}


	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public String getDataColumn(Context context, Uri uri, String selection,
								String[] selectionArgs) {


		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {column};


		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}



	public void readExcel(TextView txt, String path) {

	}




}
