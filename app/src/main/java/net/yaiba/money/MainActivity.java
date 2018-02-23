package net.yaiba.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

//import net.sourceforge.pinyin4j.PinyinHelper;
//import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
//import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
//import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
//import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
//import net.yaiba.keep.PasswordDB;
//import net.yaiba.keep.UpdateTask;
//import net.yaiba.keep.data.ListViewData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
	
	private static final int MENU_ABOUT = 0;
	private static final int MENU_SUPPORT = 1;
	private static final int MENU_WHATUPDATE = 2;
	private static final int MENU_IMPORT_EXPOERT = 3;
	private static final int MENU_CHANGE_LOGIN_PASSWORD = 4;
	private static final int MENU_CHECK_UPDATE = 5;
	//private PasswordDB PasswordDB;
	private Cursor mCursor;
	
	private EditText SiteName;
	private EditText UserName;
	private EditText PasswordValue;
	private EditText Remark;
	private EditText SearchInput;
	private TextView FloatLetter;
	private ListView RecordList;
	//private SlideBar mSlideBar;
	 
	private int RECORD_ID = 0;
	//private UpdateTask updateTask;
	private ProgressDialog updateProgressDialog;

	private  ArrayList<HashMap<String, Object>> listItemLike;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
//		setUpViews("all",null);
//
//		//返回前设置前次的位置值
//		setRecordListPosition();
//
//		Button bn_go_add = (Button)findViewById(R.id.go_add);
//		bn_go_add.setOnClickListener(new OnClickListener(){
//			   public void  onClick(View v)
//			   {
//				   Intent mainIntent = new Intent(MainActivity.this,AddRecordActivity.class);
//				   startActivity(mainIntent);
//				   setResult(RESULT_OK, mainIntent);
//				   finish();
//			   }
//			  });
//
////		mSlideBar.setOnTouchLetterChangeListenner(new SlideBar.OnTouchLetterChangeListenner() {
////
////					@Override
////					public void onTouchLetterChange(MotionEvent event, String s) {
////
////						FloatLetter.setText(s);
////						switch (event.getAction()) {
////							case MotionEvent.ACTION_DOWN:
////							case MotionEvent.ACTION_MOVE:
////								FloatLetter.setVisibility(View.VISIBLE);
////								break;
////
////							case MotionEvent.ACTION_UP:
////							default:
////								FloatLetter.postDelayed(new Runnable() {
////
////									@Override
////									public void run() {
////										FloatLetter.setVisibility(View.GONE);
////									}
////								}, 100);
////								break;
////						}
////						int position  = 0;//这个array就是传给自定义Adapter的
////
////						for(int i=0;i<listItemLike.size();i++) {
////							String siteNamePinYin = getPingYin(listItemLike.get(i).get("site_name").toString());
////							//-----------------------------
////							//Toast.makeText(MainActivity.this, listItemLike.get(i).get("site_name").toString(), Toast.LENGTH_SHORT).show();
//////							System.out.println("=>"+i);
//////							System.out.println("listItemLike.get("+i+").get(site_name).toString()=>"+listItemLike.get(i).get("site_name").toString());
//////							System.out.println("to pinyin=>"+getPingYin(listItemLike.get(i).get("site_name").toString()));
//////							System.out.println("substring(1)=>"+siteNamePinYinOne);
////							//------------------------------
////							String inpitS = getPingYin(s).toLowerCase();
////							int inputSLen = inpitS.length();
////							if(siteNamePinYin.length() > inputSLen ){
////								siteNamePinYin = siteNamePinYin.toLowerCase().substring(0,inputSLen);
////							}
////							if(siteNamePinYin.toLowerCase().equals(getPingYin(s).toLowerCase())){
//////								System.out.println("进入if=>");
//////								System.out.println("siteNamePinYinOne.toLowerCase()=>"+siteNamePinYinOne.toLowerCase());
//////								System.out.println("s.toLowerCase()=>"+s.toLowerCase());
//////								System.out.println("结束if=>");
////								position = i+1;
////								break;
////							}
////						}
////
////						RecordList.setSelection(position-1);//调用ListView的setSelection()方法就可实现了
////					}
////				});
//
//
//		SearchInput = (EditText)findViewById(R.id.searchInput);
//		SearchInput.clearFocus();
//		SearchInput.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            	if(SearchInput.getText().toString().trim().length()!=0){
//            		try {
//            			setUpViews("search",SearchInput.getText().toString().trim());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//            	} else {
//            		setUpViews("all",null);
//            	}
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            	//Toast.makeText(LoginActivity.this, "beforeTextChanged", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            	//Toast.makeText(LoginActivity.this, "afterTextChanged", Toast.LENGTH_SHORT).show();
//            }
//
//		});

	}
	
	private static Boolean isExit = false;
    private static Boolean hasTask = false;
    Timer tExit = new Timer();
    TimerTask task = new TimerTask(){
          
        @Override
        public void run() {
            isExit = true;
            hasTask = true;
        }
    };
	
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isExit == false ) {
                isExit = true;
                Toast.makeText(this, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show();
                if(!hasTask) {
                    tExit.schedule(task, 2000);
                }
            } else {
                finish();
                System.exit(0);
            }
        }
        return false;
    }


//	public static String getPingYin(String inputString) {
//		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
//		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
//		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		format.setVCharType(HanyuPinyinVCharType.WITH_V);
//		char[] input = inputString.trim().toCharArray();
//		String output = "";
//		try {
//			for (char curchar : input) {
//				if (Character.toString(curchar).matches("[\u4e00-\u9fa5]+")) {
//					String[] temp = PinyinHelper.toHanyuPinyinStringArray(curchar, format);
//					output += temp[0];
//				} else
//					output += Character.toString(curchar);
//			}
//		} catch (BadHanyuPinyinOutputFormatCombination e) {
//			e.printStackTrace();
//		}
//		return output;
//	}
//
//	public void setUpViews(String type, String value){
//		PasswordDB = new PasswordDB(this);
//		if("all".equals(type)){
//			mCursor = PasswordDB.getAll("site_name asc");
//		} else if("search".equals(type)) {
//			mCursor = PasswordDB.getForSearch(value);
//		}
//
//
//		/*SiteName = (EditText)findViewById(R.id.site_name);
//		UserName = (EditText)findViewById(R.id.login_name);
//		PasswordValue = (EditText)findViewById(R.id.word_value);*/
//		RecordList = (ListView)findViewById(R.id.recordslist);
//		FloatLetter = (TextView)findViewById(R.id.float_letter);
//		//mSlideBar = (SlideBar)findViewById(R.id.slideBar);
//
//		//String[] letters = {  "A", "P", "P", "Z" };
//
//
//
//        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
//		//ArrayList<String> sideBarStrList = new ArrayList<String>();
//
//        for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
//            int nameColumn = mCursor.getColumnIndex("site_name");
//            int phoneColumn = mCursor.getColumnIndex("user_name");
//            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
//            String siteName = mCursor.getString(nameColumn);
//            String userName = mCursor.getString(phoneColumn);
//
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            //map.put("ItemImage", R.drawable.checked);//图像资源的ID
//            /*map.put("res_no", resNo);*/
//            map.put("site_name", siteName);
//            map.put("user_name", userName);
//            listItem.add(map);
//			//sideBarStrList.add(siteName.substring(0,1));
//        }
//
//		//sideBarStrList = new ArrayList<String>(new HashSet<String>(sideBarStrList));
//		//sideBarStrList.remove(null);
//		//Collections.sort(sideBarStrList);
//
////		String[] letters = (String[]) sideBarStrList.toArray(new String[0]);
////        if(letters.length>0){
////            mSlideBar.setLetters(letters);
////        }
//
//		listItemLike = (ArrayList<HashMap<String, Object>>)listItem.clone();
//
//        SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.record_items,
//            /*new String[] {"res_no", "site_name", "user_name"},
//            new int[] {R.id.res_no, R.id.site_name,R.id.user_name} */
//        	new String[] {"site_name", "user_name"},
//        	new int[] {R.id.site_name,R.id.user_name}
//        );
//
//		RecordList.setAdapter(listItemAdapter);
//		RecordList.setOnItemClickListener(this);
//		RecordList.setOnItemLongClickListener(this);
//		RecordList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
//
//	        @Override
//	        public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
//	            //menu.setHeaderTitle("操作");
//	            menu.add(0, 0, 0, "编辑");
//	            menu.add(0, 1, 0, "删除");
//	        }
//        });
//
//		}
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        //setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目");
//        //Toast.makeText(this, "点击了长按菜单里面的第"+item.getItemId()+"个项目", Toast.LENGTH_SHORT).show();
//        super.onContextItemSelected(item);
//        switch (item.getItemId())
//		{
//			case 0:
//				go_update();
//			break;
//			case 1:
//				AlertDialog.Builder builder= new AlertDialog.Builder(this);
//				builder.setIcon(android.R.drawable.ic_dialog_info);
//				builder.setTitle("确认");
//				builder.setMessage("确定要删除这条记录吗？");
//				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                    	delete();
//                    	setUpViews("all",null);
//                    }
//                });
//				builder.setNegativeButton("取消", null);
//				builder.create().show();
//			break;
//		}
//		return true;
//    }
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		menu.add(Menu.NONE, MENU_IMPORT_EXPOERT, 0, this.getString(R.string.menu_inport_export));//备份与恢复
//		menu.add(Menu.NONE, MENU_CHANGE_LOGIN_PASSWORD, 0, this.getString(R.string.menu_change_login_password));//修改登录密码
//		menu.add(Menu.NONE, MENU_WHATUPDATE, 0, this.getString(R.string.menu_whatupdate));//更新信息
//		menu.add(Menu.NONE, MENU_CHECK_UPDATE, 0, this.getString(R.string.menu_checkupdate));//检查更新
//		menu.add(Menu.NONE, MENU_SUPPORT, 0, this.getString(R.string.menu_support));//技术支持
//		menu.add(Menu.NONE, MENU_ABOUT, 0, this.getString(R.string.menu_about));//关于Keep
//		return true;
//	}
//
//	public boolean onOptionsItemSelected(MenuItem item)
//	{
//		String title = "";
//		String msg = "";
//		//Context mContext = null;
//
//		super.onOptionsItemSelected(item);
//		switch (item.getItemId())
//		{
//			case MENU_ABOUT:
//				title = this.getString(R.string.menu_about);//关于Keep
//				msg = this.getString(R.string.about_keep);
//				msg = msg + "\n\n";
//				msg = msg + "@"+getAppVersion();
//				showAboutDialog(title,msg);
//			break;
//			case MENU_SUPPORT:
//				title = this.getString(R.string.menu_support);//技术支持
//				msg = this.getString(R.string.partners);
//				showAboutDialog(title,msg);
//			break;
//			case MENU_WHATUPDATE:
//				title = this.getString(R.string.menu_whatupdate);//更新信息
//				msg = msg + this.getString(R.string.what_updated);
//				msg = msg + "\n\n\n";
//				showAboutDialog(title,msg);
//			break;
//			case MENU_CHECK_UPDATE:
//				title = this.getString(R.string.menu_checkupdate);//检查更新
//				msg = "";//1.增加双服务器检测更新机制\n2.检查更新\n\n以上功能
//
//				updateTask = new UpdateTask(MainActivity.this,true);
//				updateTask.update();
//
//				//showAboutDialog(title,msg);
//			break;
//			case MENU_IMPORT_EXPOERT://备份与恢复
//				Intent mainIntent = new Intent(MainActivity.this, DataManagementActivity.class);
//			    startActivity(mainIntent);
//			    setResult(RESULT_OK, mainIntent);
//			    finish();
//			break;
//			case MENU_CHANGE_LOGIN_PASSWORD:
//				Intent mainIntent2 = new Intent(MainActivity.this, LoginPEditActivity.class);
//			    startActivity(mainIntent2);
//			    setResult(RESULT_OK, mainIntent2);
//			    finish();
//			break;
//		}
//		return true;
//	}
//
//	public void showAboutDialog(String title,String msg){
//		AlertDialog.Builder builder= new AlertDialog.Builder(this);
//		builder.setIcon(android.R.drawable.ic_dialog_info);
//		builder.setTitle(title);
//		builder.setMessage(msg);
//		builder.setPositiveButton("确定", null);
//		builder.create().show();
//	}
//
//	public void showConfirmDialog(String title,String msg){
//		Dialog alertDialog = new AlertDialog.Builder(this).
//            setTitle("确定删除？").
//            setMessage("您确定删除该条信息吗？").
//            setIcon(R.drawable.ic_launcher).
//            setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // TODO Auto-generated method stub
//                }
//            }).
//            setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // TODO Auto-generated method stub
//                }
//            }).
//            create();
//    alertDialog.show();
//	}
//
//
//	@SuppressWarnings("deprecation")
//	public void delete(){
//		if (RECORD_ID == 0) {
//			return;
//		}
//		PasswordDB.delete(RECORD_ID);
//		mCursor.requery();
//		Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
//	}
//
//	public void go_update(){
//		//保存当前位置
//		saveListViewPositionAndTop();
//		//画面迁移到edit画面
//		 Intent mainIntent = new Intent(MainActivity.this,RecordEditActivity.class);
//		 mainIntent.putExtra("INT", RECORD_ID);
//		 startActivity(mainIntent);
//		 setResult(RESULT_OK, mainIntent);
//		 finish();
//	}
//
//
//	@SuppressWarnings("deprecation")
//	public void update(){
//		String sitename = SiteName.getText().toString();
//		String username = UserName.getText().toString();
//		String passwordvalue = PasswordValue.getText().toString();
//		String remark = Remark.getText().toString();
//
//		if (sitename.equals("") || username.equals("") || passwordvalue.equals("")){
//			return;
//		}
//		PasswordDB.update(RECORD_ID, sitename, username, passwordvalue, remark);
//		mCursor.requery();
//		RecordList.invalidateViews();
//		SiteName.setText("");
//		UserName.setText("");
//		PasswordValue.setText("");
//		Remark.setText("");
//		Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		//保存当前一览位置
//		saveListViewPositionAndTop();
//		//迁移到详细页面
//		Intent mainIntent = new Intent(MainActivity.this,RecordDetailActivity.class);
//		 mCursor.moveToPosition(position);
//		 RECORD_ID = mCursor.getInt(0);
//		 mainIntent.putExtra("INT", RECORD_ID);
//		 startActivity(mainIntent);
//		 setResult(RESULT_OK, mainIntent);
//		 finish();
//	}
//
//	/**
//	 * 保存当前页签listView的第一个可见的位置和top
//	 */
//	private void saveListViewPositionAndTop() {
//
//		final ListViewData app = (ListViewData)getApplication();
//
//		app.setFirstVisiblePosition(RecordList.getFirstVisiblePosition());
//		View item = RecordList.getChildAt(0);
//		app.setFirstVisiblePositionTop((item == null) ? 0 : item.getTop());
//	}
//
//
//
//
//	public class RecordListAdapter extends BaseAdapter{
//		private Context mContext;
//		private Cursor mCursor;
//		public RecordListAdapter(Context context,Cursor cursor) {
//
//		mContext = context;
//		mCursor = cursor;
//	}
//
//	@Override
//	public int getCount() {
//		return mCursor.getCount();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return 0;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		TextView mTextView = new TextView(mContext);
//		mCursor.moveToPosition(position);
//		mTextView.setText(mCursor.getString(1) + "___" + mCursor.getString(2)+ "___" + mCursor.getString(3)+ "___" + mCursor.getString(4));
//		return mTextView;
//	}
//}
//
//	@Override
//	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//		mCursor.moveToPosition(position);
//		RECORD_ID = mCursor.getInt(0);
//		return false;
//	}
//
//	private String getAppVersion() {
// 		try {
// 			String pkName = this.getPackageName();
// 			String versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
// 			//int versionCode = this.getPackageManager().getPackageInfo(pkName, 0).versionCode;
// 			return versionName;
// 		} catch (Exception e) {
// 		}
// 		return null;
// 	}
//
//
//	//返回前设置前次的位置值
//	public void setRecordListPosition(){
//		ListViewData app = (ListViewData)getApplication();
//		RecordList.setSelectionFromTop(app.getFirstVisiblePosition(), app.getFirstVisiblePositionTop());
//	}
//
//	public String getWordCount(String str,int len) {
//		try {
//			int counterOfDoubleByte = 0;
//			byte b[] = str.getBytes("GBK");
//			if (b.length <= len)
//				return str;
//			for (int i = 0; i < len; i++) {
//				if (b[i] < 0)
//					counterOfDoubleByte++;
//			}
//			if (counterOfDoubleByte % 2 == 0)
//				return new String(b, 0, len, "GBK");
//			else
//				return new String(b, 0, len - 1, "GBK");
//
//		} catch (UnsupportedEncodingException e) {
//			return "";
//		}
//	}
//
		
}
