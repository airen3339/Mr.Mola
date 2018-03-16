package net.yaiba.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.yaiba.money.data.SpinnerData;
import net.yaiba.money.db.MoneyDB;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoryActivity extends Activity {
	private MoneyDB MoneyDB;
	private Cursor mCursor;
	private ListView RecordList;
	private ArrayAdapter<SpinnerData> AdapterCateP;
	private ArrayAdapter<SpinnerData> AdapterCateC;
	private ArrayAdapter<SpinnerData> AdapterCateIncome;
	private Spinner category_child_spinner;
	private Spinner category_parent_spinner;
	private Spinner category_income_spinner;
	private Button bn_category_parent_add;
	private Button bn_category_parent_edit;
	private Button bn_category_parent_del;
	private Button bn_category_child_add;
	private Button bn_category_child_edit;
	private Button bn_category_child_del;
	private Button bn_category_income_add;
	private Button bn_category_income_edit;
	private Button bn_category_income_del;

	private int RECORD_ID = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MoneyDB = new MoneyDB(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_activity);

		setUpViews();

		//收入，大类，添加
		bn_category_parent_add = (Button)findViewById(R.id.category_parent_add);
		bn_category_parent_add.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				final EditText inputCategoryName = new EditText(CategoryActivity.this);
				AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setTitle("添加收入大分类");
				builder.setMessage("请输入收入大分类名称");
				builder.setView(inputCategoryName);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String input = inputCategoryName.getText().toString();
						if (input.equals("")) {
							Toast.makeText(getApplicationContext(), "分类名不能为空！", Toast.LENGTH_LONG).show();
						} else if (MoneyDB.isCategoryPExist(input)){
							Toast.makeText(getApplicationContext(), "分类名已经存在！", Toast.LENGTH_LONG).show();
						} else {
							addCategoryParent(input);
							Toast.makeText(getApplicationContext(), "收入大分类添加完成：" + input, Toast.LENGTH_LONG).show();
							setUpViews();
						}
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		//支出，大类，修改
		bn_category_parent_edit = (Button)findViewById(R.id.category_parent_edit);
		bn_category_parent_edit.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(category_parent_spinner.getAdapter().getCount() != 0){
					final EditText inputCategoryName = new EditText(CategoryActivity.this);
					Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);//取得大分类下拉列表选择的
					//inputCategoryName.setText(((SpinnerData)townSp.getSelectedItem()).getText()+"-"+((SpinnerData)townSp.getSelectedItem()).getValue());
					inputCategoryName.setText(((SpinnerData)cateS.getSelectedItem()).getText());//将选择的名称设置到弹出的对话框文本域中
					AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("修改支出大分类名称");
					builder.setMessage("请输入修改后的大分类名称");
					builder.setView(inputCategoryName);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String input = inputCategoryName.getText().toString();
							Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);
							String id = ((SpinnerData)cateS.getSelectedItem()).getValue();
							if (input.equals("")) {
								Toast.makeText(getApplicationContext(), "分类名不能为空！", Toast.LENGTH_LONG).show();
							} else if (MoneyDB.isCategoryPExist(input)){
								Toast.makeText(getApplicationContext(), "分类名已经存在！", Toast.LENGTH_LONG).show();
							} else {
								editCategoryName(id,input);
								Toast.makeText(getApplicationContext(), "支出大分类名称修改完成：" + input, Toast.LENGTH_LONG).show();
								setUpViews();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "支出大分类数据不存在，不能修改", Toast.LENGTH_LONG).show();
				}

			}
		});

		//支出，大类，删除
		bn_category_parent_del = (Button)findViewById(R.id.category_parent_del);
		bn_category_parent_del.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(category_parent_spinner.getAdapter().getCount() != 0){
					//final EditText inputCategoryName = new EditText(CategoryActivity.this);
					Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);
					AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("选择需要删除的支出大分类");
					builder.setMessage("确定要删除分类：「"+((SpinnerData)cateS.getSelectedItem()).getText()+"」吗");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);
							String id = ((SpinnerData)cateS.getSelectedItem()).getValue();
							if(!MoneyDB.isHaveChildCategory(id)){
								delCategoryName(id);
								Toast.makeText(getApplicationContext(), "删除成功" , Toast.LENGTH_LONG).show();
								setUpViews();
							} else {
								Toast.makeText(getApplicationContext(), "当前分类下含有子分类，无法删除！", Toast.LENGTH_LONG).show();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "支出大分类数据不存在，不能删除", Toast.LENGTH_LONG).show();
				}
			}
		});


		//支出，小类，添加
		bn_category_child_add = (Button)findViewById(R.id.category_child_add);
		bn_category_child_add.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(category_parent_spinner.getAdapter().getCount() != 0){
					final EditText inputCategoryName = new EditText(CategoryActivity.this);
					Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);//取得大分类下拉列表选择的
					AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("添加支出小分类");
					builder.setMessage("在「"+((SpinnerData)cateS.getSelectedItem()).getText()+"」下添加小分类：");
					builder.setView(inputCategoryName);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String input = inputCategoryName.getText().toString();
							Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);
							String pid = ((SpinnerData)cateS.getSelectedItem()).getValue();
							if (input.equals("")) {
								Toast.makeText(getApplicationContext(), "分类名不能为空！", Toast.LENGTH_LONG).show();
							} else if (MoneyDB.isCategoryCExist(input,pid)) {
								Toast.makeText(getApplicationContext(), "分类名已经存在！", Toast.LENGTH_LONG).show();
							} else {
								addCategoryChild(pid, input);
								Toast.makeText(getApplicationContext(), "支出小分类添加完成：" + input, Toast.LENGTH_LONG).show();
								setUpViews();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "支出大分类数据不存在，不能添加小分类", Toast.LENGTH_LONG).show();
				}
			}
		});


		//支出，小类，修改
		bn_category_child_edit = (Button)findViewById(R.id.category_child_edit);
		bn_category_child_edit.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(category_child_spinner.getAdapter().getCount() != 0){
					final EditText inputCategoryName = new EditText(CategoryActivity.this);
					//Spinner cateS = (Spinner)findViewById(R.id.category_child);//取得小分类下拉列表选择的
					inputCategoryName.setText(((SpinnerData)category_child_spinner.getSelectedItem()).getText());//将选择的名称设置到弹出的对话框文本域中
					AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("修改支出小分类名称");
					builder.setMessage("请输入修改后的小分类名称");
					builder.setView(inputCategoryName);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String input = inputCategoryName.getText().toString();
							Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);
							String pid = ((SpinnerData)cateS.getSelectedItem()).getValue();
							String id = ((SpinnerData)category_child_spinner.getSelectedItem()).getValue();
							if (input.equals("")) {
								Toast.makeText(getApplicationContext(), "分类名不能为空！", Toast.LENGTH_LONG).show();
							} else if (MoneyDB.isCategoryCExist(input,pid)) {
								Toast.makeText(getApplicationContext(), "分类名已经存在！", Toast.LENGTH_LONG).show();
							} else {
								editCategoryName(id,input);
								Toast.makeText(getApplicationContext(), "支出小分类名称修改完成：" + input, Toast.LENGTH_LONG).show();
								setUpViews();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "支出小分类数据不存在，不能修改", Toast.LENGTH_LONG).show();
				}
			}
		});


		//支出，小类，删除
		bn_category_child_del = (Button)findViewById(R.id.category_child_del);
		bn_category_child_del.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(category_child_spinner.getAdapter().getCount() != 0){
					final EditText inputCategoryName = new EditText(CategoryActivity.this);
					//Spinner cateS = (Spinner)findViewById(R.id.category_child);
					AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("删除选择的支出小分类");
					builder.setMessage("确定要删除分类：「"+((SpinnerData)category_child_spinner.getSelectedItem()).getText()+"」吗");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							//Spinner cateS = (Spinner)findViewById(R.id.category_child);
							String id = ((SpinnerData)category_child_spinner.getSelectedItem()).getValue();
							if(!MoneyDB.isHaveValidRecordByCategory(id)){
								delCategoryName(id);
								Toast.makeText(getApplicationContext(), "删除成功" , Toast.LENGTH_LONG).show();
								setUpViews();
							} else {
								Toast.makeText(getApplicationContext(), "当前分类下含有有效记录，无法删除！", Toast.LENGTH_LONG).show();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "支出小分类数据不存在，不能删除", Toast.LENGTH_LONG).show();
				}
			}
		});


		//对于切换大类下拉列表，设置监听器，动态更新小分类下拉列表
		//Spinner categorySpinner = (Spinner)findViewById(R.id.category_parent);
		category_parent_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);
				Cursor categoryCListCursor = MoneyDB.getCategoryCList(((SpinnerData)cateS.getSelectedItem()).getValue(),"id asc");
				category_child_spinner = (Spinner) findViewById(R.id.category_child_spinner);
				List<SpinnerData> categoryCListItem = new ArrayList<SpinnerData>();

				for(categoryCListCursor.moveToFirst();!categoryCListCursor.isAfterLast();categoryCListCursor.moveToNext()) {
					String cid = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("id"));
					String category_name = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("category_name"));
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("id", id);
					map.put("category_name", category_name);
					SpinnerData c = new SpinnerData(cid, category_name);
					categoryCListItem.add(c);
				}
				ArrayAdapter<SpinnerData> AdapterCateC = new ArrayAdapter<SpinnerData>(CategoryActivity.this, android.R.layout.simple_spinner_item, categoryCListItem);
				AdapterCateC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				category_child_spinner.setAdapter(AdapterCateC);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});



		//收入，添加
		bn_category_income_add = (Button)findViewById(R.id.category_income_add);
		bn_category_income_add.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				final EditText inputCategoryName = new EditText(CategoryActivity.this);
				AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setTitle("添加收入分类");
				builder.setMessage("请输入收入分类名称");
				builder.setView(inputCategoryName);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String input = inputCategoryName.getText().toString();
						if (input.equals("")) {
							Toast.makeText(getApplicationContext(), "支出分类名不能为空！", Toast.LENGTH_LONG).show();
						} else if (MoneyDB.isCategoryIncomeExist(input)){
							Toast.makeText(getApplicationContext(), "支出分类名已经存在！", Toast.LENGTH_LONG).show();
						} else {
							addCategoryIncome(input);
							Toast.makeText(getApplicationContext(), "收入分类添加完成：" + input, Toast.LENGTH_LONG).show();
							setUpViews();
						}
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		//收入，修改
		bn_category_income_edit = (Button)findViewById(R.id.category_income_edit);
		bn_category_income_edit.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(category_income_spinner.getAdapter().getCount() != 0){
					final EditText inputCategoryName = new EditText(CategoryActivity.this);
					//Spinner cateS = (Spinner)findViewById(R.id.category_income_spinner);//取得大分类下拉列表选择的
					//inputCategoryName.setText(((SpinnerData)townSp.getSelectedItem()).getText()+"-"+((SpinnerData)townSp.getSelectedItem()).getValue());
					inputCategoryName.setText(((SpinnerData)category_income_spinner.getSelectedItem()).getText());//将选择的名称设置到弹出的对话框文本域中
					AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("修改收入分类名称");
					builder.setMessage("请输入修改后的收入分类名称");
					builder.setView(inputCategoryName);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String input = inputCategoryName.getText().toString();
							//Spinner cateS = (Spinner)findViewById(R.id.category_parent);
							String id = ((SpinnerData)category_income_spinner.getSelectedItem()).getValue();
							if (input.equals("")) {
								Toast.makeText(getApplicationContext(), "收入分类名不能为空！", Toast.LENGTH_LONG).show();
							} else if (MoneyDB.isCategoryIncomeExist(input)){
								Toast.makeText(getApplicationContext(), "收入分类名不能为空！", Toast.LENGTH_LONG).show();
							} else {
								editCategoryName(id,input);
								Toast.makeText(getApplicationContext(), "收入分类名称修改完成：" + input, Toast.LENGTH_LONG).show();
								setUpViews();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "收入分类数据不存在，不能修改", Toast.LENGTH_LONG).show();
				}

			}
		});

		//收入，删除
		bn_category_income_del = (Button)findViewById(R.id.category_income_del);
		bn_category_income_del.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(category_income_spinner.getAdapter().getCount() != 0){
					final EditText inputCategoryName = new EditText(CategoryActivity.this);
					Spinner cateS = (Spinner)findViewById(R.id.category_income_spinner);
					AlertDialog.Builder builder= new AlertDialog.Builder(CategoryActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("删除选择的收入分类");
					builder.setMessage("确定要删除分类：「"+((SpinnerData)cateS.getSelectedItem()).getText()+"」吗");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							//Spinner cateS = (Spinner)findViewById(R.id.category_parent);
							String id = ((SpinnerData)category_income_spinner.getSelectedItem()).getValue();
							if(!MoneyDB.isHaveValidRecordByIncomeCategory(id)){
								delCategoryName(id);
								Toast.makeText(getApplicationContext(), "删除成功" , Toast.LENGTH_LONG).show();
								setUpViews();
							} else {
								Toast.makeText(getApplicationContext(), "当前收入分类下含有有效记录，无法删除！", Toast.LENGTH_LONG).show();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "收入分类数据不存在，不能删除", Toast.LENGTH_LONG).show();
				}
			}
		});



	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent mainIntent = new Intent(CategoryActivity.this,MainActivity.class);
			startActivity(mainIntent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			setResult(RESULT_OK, mainIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
 	public void setUpViews(){

		bn_category_parent_del = (Button)findViewById(R.id.category_parent_del);
		bn_category_parent_edit = (Button)findViewById(R.id.category_parent_edit);
		bn_category_child_del = (Button)findViewById(R.id.category_child_del);
		bn_category_child_edit = (Button)findViewById(R.id.category_child_edit);
		bn_category_child_add = (Button)findViewById(R.id.category_child_add);
		category_parent_spinner = (Spinner) findViewById(R.id.category_parent_spinner);
		category_child_spinner = (Spinner) findViewById(R.id.category_child_spinner);
		category_income_spinner = (Spinner) findViewById(R.id.category_income_spinner);

		// 支出，大分类下拉列表，初期化
		Cursor categoryPListCursor  = MoneyDB.getCategoryPList("id asc");
		List<SpinnerData> categoryPListItem = new ArrayList<SpinnerData>();

		for(categoryPListCursor.moveToFirst();!categoryPListCursor.isAfterLast();categoryPListCursor.moveToNext()) {
            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
			String id = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("id"));
			String pid = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("pid"));
			String category_name = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("category_name"));
//			String favorite = mCursor.getString(mCursor.getColumnIndex("favorite"));
//			String rank = mCursor.getString(mCursor.getColumnIndex("rank"));
			Log.v("v_record"+id,id+"/"+pid+"/"+category_name+"/");
			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("category_name", id+"-"+pid+"-"+category_name+"-"+favorite+"-"+rank);
			map.put("id", id);
			map.put("pid", pid);
			map.put("category_name", category_name);
//			map.put("favorite", favorite);
//			map.put("rank", rank);

			SpinnerData c = new SpinnerData(id, category_name);
			categoryPListItem.add(c);
		}
		AdapterCateP = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryPListItem);
		AdapterCateP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category_parent_spinner.setAdapter(AdapterCateP);


//		SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.category_recent_list_items,
//				new String[] {"category_name"},
//				new int[] {R.id.category_name}
//		);
		// 支出，小分类下拉列表，初期化
		if (category_parent_spinner.getAdapter().getCount() != 0){
			//取得大分类第一条数据的id，传递给小分类。
			categoryPListCursor.moveToFirst();
			String SpinnerFirstid = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("id"));
			Log.v("v_SpinnerFirstid",SpinnerFirstid);

			Cursor categoryCListCursor = MoneyDB.getCategoryCList(SpinnerFirstid,"id asc");
			List<SpinnerData> categoryCListItem = new ArrayList<SpinnerData>();

			for(categoryCListCursor.moveToFirst();!categoryCListCursor.isAfterLast();categoryCListCursor.moveToNext()) {
            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
				String id = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("id"));
				String pid = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("pid"));
				String category_name = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("category_name"));
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("pid", pid);
				map.put("category_name", category_name);

				SpinnerData c = new SpinnerData(id, category_name);
				categoryCListItem.add(c);
			}
			AdapterCateC = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryCListItem);
			AdapterCateC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			category_child_spinner.setAdapter(AdapterCateC);
		}


		// 收入，分类下拉列表，初期化
		Cursor categoryIncomeListCursor  = MoneyDB.getCategoryIncomeList("id asc");
		List<SpinnerData> categoryIncomeListItem = new ArrayList<SpinnerData>();

		for(categoryIncomeListCursor.moveToFirst();!categoryIncomeListCursor.isAfterLast();categoryIncomeListCursor.moveToNext()) {
            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
			String id = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("id"));
			String pid = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("pid"));
			String category_name = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("category_name"));

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("pid", pid);
			map.put("category_name", category_name);

			SpinnerData c = new SpinnerData(id, category_name);
			categoryIncomeListItem.add(c);
		}
		AdapterCateIncome = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryIncomeListItem);
		AdapterCateIncome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category_income_spinner.setAdapter(AdapterCateIncome);

	}


	public void addCategoryParent(String input){
		MoneyDB.insertCategoryP(input);
	}

	public void addCategoryChild(String pid, String input){
		Log.v("v_addCategoryChild",pid+"/"+input);
		MoneyDB.insertCategoryC(pid, input);
	}

	public void addCategoryIncome(String input){
		Log.v("v_addCategoryIncome",input);
		MoneyDB.insertCategoryIncome(input);
	}

	public void editCategoryName(String id, String input){
		Log.v("v_record_editC",input+"/"+id);
		MoneyDB.editCategoryName(id, input);
	}

	public void delCategoryName(String id){
		Log.v("v_record_delC",id);
		MoneyDB.delCategoryName(id);
	}


}
