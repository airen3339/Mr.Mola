package net.yaiba.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import net.yaiba.money.data.SpinnerData;
import net.yaiba.money.db.MoneyDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PayActivity extends Activity {
	private MoneyDB MoneyDB;
	private Cursor mCursor;
	private ListView RecordList;
	private ArrayAdapter<SpinnerData> PaySpinnerAdapter;
	private Spinner pay_type_spinner;
	private Button bn_pay_type_add;
	private Button bn_pay_type_edit;
	private Button bn_pay_type_del;

	private int RECORD_ID = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MoneyDB = new MoneyDB(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_activity);

		setUpViews();

		//支付方式，添加
		bn_pay_type_add.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				final EditText inputPayName = new EditText(PayActivity.this);
				AlertDialog.Builder builder= new AlertDialog.Builder(PayActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setTitle("添加支付方式");
				builder.setMessage("请输入支付方式名称");
				builder.setView(inputPayName);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String input = inputPayName.getText().toString();
						if (input.equals("")) {
							Toast.makeText(getApplicationContext(), "支付方式名不能为空！", Toast.LENGTH_LONG).show();
						}  else {
							addPayType(input);
							Toast.makeText(getApplicationContext(), "支付方式添加完成：" + input, Toast.LENGTH_LONG).show();
							setUpViews();
						}
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		//支付方式，修改
		bn_pay_type_edit.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(pay_type_spinner.getAdapter().getCount() != 0){
					final EditText inputPayName = new EditText(PayActivity.this);
					Spinner payS = (Spinner)findViewById(R.id.pay_type);//取得大分类下拉列表选择的
					inputPayName.setText(((SpinnerData)payS.getSelectedItem()).getText());//将选择的名称设置到弹出的对话框文本域中
					AlertDialog.Builder builder= new AlertDialog.Builder(PayActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("修改支付方式名称");
					builder.setMessage("请输入修改后的支付方式名称");
					builder.setView(inputPayName);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String input = inputPayName.getText().toString();
							Spinner payS = (Spinner)findViewById(R.id.pay_type);
							String id = ((SpinnerData)payS.getSelectedItem()).getValue();
							if (input.equals("")) {
								Toast.makeText(getApplicationContext(), "支付方式名不能为空！", Toast.LENGTH_LONG).show();
							}  else {
								editPayType(id,input);
								Toast.makeText(getApplicationContext(), "支付方式名称修改完成：" + input, Toast.LENGTH_LONG).show();
								setUpViews();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "支付方式数据不存在，不能修改", Toast.LENGTH_LONG).show();
				}

			}
		});

		//支付方式，删除
		bn_pay_type_del.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(pay_type_spinner.getAdapter().getCount() != 0){
					Spinner payS = (Spinner)findViewById(R.id.pay_type);
					AlertDialog.Builder builder= new AlertDialog.Builder(PayActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("删除选择的支付方式");
					builder.setMessage("确定要删除支付方式：「"+((SpinnerData)payS.getSelectedItem()).getText()+"」吗");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Spinner payS = (Spinner)findViewById(R.id.pay_type);
							String id = ((SpinnerData)payS.getSelectedItem()).getValue();
							if(!MoneyDB.isHaveValidRecordByPayType(id)){
								delPayType(id);
								Toast.makeText(getApplicationContext(), "删除成功" , Toast.LENGTH_LONG).show();
								setUpViews();
							} else {
								Toast.makeText(getApplicationContext(), "当前支付方式下含有数据，无法删除！", Toast.LENGTH_LONG).show();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "支付方式数据不存在，不能删除", Toast.LENGTH_LONG).show();
				}
			}
		});



	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent mainIntent = new Intent(PayActivity.this,MainActivity.class);
			startActivity(mainIntent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			setResult(RESULT_OK, mainIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
 	public void setUpViews(){
		bn_pay_type_add = (Button)findViewById(R.id.pay_type_add);
		bn_pay_type_edit = (Button)findViewById(R.id.pay_type_edit);
		bn_pay_type_del = (Button)findViewById(R.id.pay_type_del);

		Cursor payTypeListCursor  = MoneyDB.getPayTypeList("id desc");
		pay_type_spinner = (Spinner) findViewById(R.id.pay_type);
		List<SpinnerData> payTypeListItem = new ArrayList<SpinnerData>();

		RecordList = (ListView)findViewById(R.id.pay_type_list);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		for(payTypeListCursor.moveToFirst();!payTypeListCursor.isAfterLast();payTypeListCursor.moveToNext()) {
            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
			String id = payTypeListCursor.getString(payTypeListCursor.getColumnIndex("id"));
			String pay_name = payTypeListCursor.getString(payTypeListCursor.getColumnIndex("pay_name"));

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("pay_name", pay_name);

			listItem.add(map);

			SpinnerData c = new SpinnerData(id, pay_name);
			payTypeListItem.add(c);

		}
		PaySpinnerAdapter = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, payTypeListItem);
		PaySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pay_type_spinner.setAdapter(PaySpinnerAdapter);


		SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.pay_list_items,
				new String[] {"pay_name"},
				new int[] {R.id.pay_name}
		);
		RecordList.setAdapter(listItemAdapter);


	}


	public void addPayType(String input){
		MoneyDB.insertPayType(input);
	}

	public void editPayType(String id, String input){
		Log.v("v_record_edit",input+"/"+id);
		MoneyDB.editPayType(id, input);
	}

	public void delPayType(String id){
		Log.v("v_record_del",id);
		MoneyDB.delPayType(id);
	}


}
