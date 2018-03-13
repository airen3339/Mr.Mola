package net.yaiba.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.yaiba.money.data.SpinnerData;
import net.yaiba.money.db.MoneyDB;
import net.yaiba.money.utils.DateTimePickDialog;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.sql.Types.NULL;
import static net.yaiba.money.utils.Custom.getNowDateWithTimes2;


public class RecordActivity extends Activity {
	private MoneyDB MoneyDB;
	private Cursor mCursor;
	private ListView RecordList;
	private ArrayAdapter<SpinnerData> AdapterCateP;
	private ArrayAdapter<SpinnerData> AdapterCateC;
	private ArrayAdapter<SpinnerData> AdapterCateIncome;
	private ArrayAdapter<SpinnerData> PaySpinnerAdapter;
	private RadioGroup radiogroup;
	private RadioButton radio1,radio2;
	private Spinner category_child_spinner,category_parent_spinner,category_income_spinner,pay_type_spinner;
	private EditText amounts_text, remark_text;
	private TextView create_time_text,member_name_text;

	private Button save_bn;


	private int RECORD_ID = 0;

	private String initDateTime = getNowDateWithTimes2(); // 初始化开始时间

	private static final int DECIMAL_DIGITS = 2;//小数的位数
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MoneyDB = new MoneyDB(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_add_activity);

		radiogroup=(RadioGroup)findViewById(R.id.radiogroup);
		radio1=(RadioButton)findViewById(R.id.radiobutton1);
		radio2=(RadioButton)findViewById(R.id.radiobutton2);

		amounts_text=(EditText)findViewById(R.id.amounts_text);
		setPoint(amounts_text);

		category_parent_spinner = (Spinner) findViewById(R.id.category_parent_spinner);
		category_child_spinner = (Spinner) findViewById(R.id.category_child_spinner);
		category_income_spinner = (Spinner) findViewById(R.id.category_income_spinner);
		pay_type_spinner = (Spinner) findViewById(R.id.pay_type_spinner);

		remark_text=(EditText)findViewById(R.id.remark_text);

		create_time_text=(TextView)findViewById(R.id.create_time_text);
		member_name_text=(TextView)findViewById(R.id.member_name_text);

		save_bn=(Button)findViewById(R.id.save_bn);

		setUpViews();

		save_bn.setOnClickListener(new View.OnClickListener(){
			public void  onClick(View v)
			{
				if(addRecord()){
					Intent mainIntent = new Intent(RecordActivity.this,MainActivity.class);
					startActivity(mainIntent);
					overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
					setResult(RESULT_OK, mainIntent);
					finish();
				}
			}
		});

		//单选按钮切换时 事件响应
		radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
		    @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
					if(checkedId==radio1.getId()) {
						Toast.makeText(getApplicationContext(), "记录类型："+radio1.getText(), Toast.LENGTH_LONG).show();
						category_parent_spinner.setVisibility(View.VISIBLE);
						category_child_spinner.setVisibility(View.VISIBLE);
						category_income_spinner.setVisibility(View.GONE);
						amounts_text.setTextColor(Color.parseColor("#EE2428"));
					} else {
						Toast.makeText(getApplicationContext(), "记录类型："+radio2.getText(), Toast.LENGTH_LONG).show();
						category_parent_spinner.setVisibility(View.GONE);
						category_child_spinner.setVisibility(View.GONE);
						category_income_spinner.setVisibility(View.VISIBLE);
						amounts_text.setTextColor(Color.parseColor("#228B22"));
					}
				}
        });

		//对于切换大类下拉列表，设置监听器，动态更新小分类下拉列表
		category_parent_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				Spinner cateS = (Spinner)findViewById(R.id.category_parent_spinner);
				Cursor categoryCListCursor = MoneyDB.getCategoryCList(((SpinnerData)cateS.getSelectedItem()).getValue(),"id desc");
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
				ArrayAdapter<SpinnerData> AdapterCateC = new ArrayAdapter<SpinnerData>(RecordActivity.this, android.R.layout.simple_spinner_item, categoryCListItem);
				AdapterCateC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				category_child_spinner.setAdapter(AdapterCateC);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});



		//日期时间初期值及点击的事件响应
		create_time_text.setText(initDateTime);
		create_time_text.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DateTimePickDialog dateTimePicKDialog = new DateTimePickDialog(	RecordActivity.this, initDateTime);
				dateTimePicKDialog.dateTimePicKDialog(create_time_text);

			}
		});


		// 点击设置成员功能事件响应
		member_name_text.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
				builder.setTitle("成员选择");


				Cursor menberNameListCursor  = MoneyDB.getMemberNameList("id desc");

				final ArrayList<String>  strArray = new ArrayList<String> ();
				for(menberNameListCursor.moveToFirst();!menberNameListCursor.isAfterLast();menberNameListCursor.moveToNext()) {

					String id = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("id"));
					String member_name = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("member_name"));

					strArray.add(member_name);
				}

				final String[] items = (String[]) strArray.toArray(new String[0]);

				String newMember = member_name_text.getText().toString();
				int checkedItem = 0;
				for (int i=0;i<strArray.size();i++){
					if(newMember.equals(strArray.get(i))){
						checkedItem = i;
						break;
					}
				}

				builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {//第二个参数是设置默认选中哪一项-1代表默认都不选
					@Override
					public void onClick(DialogInterface dialog, int which) {

						member_name_text.setText(items[which]);

					}
				});

				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

				builder.create().show();;
			}
		});






	}


	public static void setPoint(final EditText editText) {
		editText.setText("");
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + DECIMAL_DIGITS+1);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					editText.setText(s);
					editText.setSelection(2);
				}
				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent mainIntent = new Intent(RecordActivity.this,MainActivity.class);
			startActivity(mainIntent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			setResult(RESULT_OK, mainIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
 	public void setUpViews(){

		setCategoryCostSpinnerDate();
		setCategoryIncomeSpinnerDate();
		setPayTypeSpinnerDate();
		setMemberSpinnerDate();
	}


	public void setCategoryCostSpinnerDate(){
		// 支出，大分类下拉列表，初期化
		Cursor categoryPListCursor  = MoneyDB.getCategoryPList("id desc");
		List<SpinnerData> categoryPListItem = new ArrayList<SpinnerData>();

		for(categoryPListCursor.moveToFirst();!categoryPListCursor.isAfterLast();categoryPListCursor.moveToNext()) {
			String id = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("id"));
			String pid = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("pid"));
			String category_name = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("category_name"));
			Log.v("v_record"+id,id+"/"+pid+"/"+category_name+"/");

			SpinnerData c = new SpinnerData(id, category_name);
			categoryPListItem.add(c);
		}
		AdapterCateP = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryPListItem);
		AdapterCateP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category_parent_spinner.setAdapter(AdapterCateP);

		// 支出，小分类下拉列表，初期化
		if (category_parent_spinner.getAdapter().getCount() != 0){
			//取得大分类第一条数据的id，传递给小分类。
			categoryPListCursor.moveToFirst();
			String SpinnerFirstid = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("id"));
			Log.v("v_SpinnerFirstid",SpinnerFirstid);

			Cursor categoryCListCursor = MoneyDB.getCategoryCList(SpinnerFirstid,"id desc");
			List<SpinnerData> categoryCListItem = new ArrayList<SpinnerData>();
			for(categoryCListCursor.moveToFirst();!categoryCListCursor.isAfterLast();categoryCListCursor.moveToNext()) {
				String id = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("id"));
				String pid = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("pid"));
				String category_name = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("category_name"));

				SpinnerData c = new SpinnerData(id, category_name);
				categoryCListItem.add(c);
			}
			AdapterCateC = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryCListItem);
			AdapterCateC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			category_child_spinner.setAdapter(AdapterCateC);
		}
	}

	public void setCategoryIncomeSpinnerDate(){
		// 收入，分类下拉列表，初期化
		Cursor categoryIncomeListCursor  = MoneyDB.getCategoryIncomeList("id desc");
		List<SpinnerData> categoryIncomeListItem = new ArrayList<SpinnerData>();

		for(categoryIncomeListCursor.moveToFirst();!categoryIncomeListCursor.isAfterLast();categoryIncomeListCursor.moveToNext()) {
			String id = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("id"));
			String pid = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("pid"));
			String category_name = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("category_name"));

			SpinnerData c = new SpinnerData(id, category_name);
			categoryIncomeListItem.add(c);
		}
		AdapterCateIncome = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryIncomeListItem);
		AdapterCateIncome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category_income_spinner.setAdapter(AdapterCateIncome);
	}

	public void setPayTypeSpinnerDate(){
		Cursor payTypeListCursor  = MoneyDB.getPayTypeList("id desc");
		List<SpinnerData> payTypeListItem = new ArrayList<SpinnerData>();

		for(payTypeListCursor.moveToFirst();!payTypeListCursor.isAfterLast();payTypeListCursor.moveToNext()) {
            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
			String id = payTypeListCursor.getString(payTypeListCursor.getColumnIndex("id"));
			String pay_name = payTypeListCursor.getString(payTypeListCursor.getColumnIndex("pay_name"));
			SpinnerData c = new SpinnerData(id, pay_name);
			payTypeListItem.add(c);
		}
		PaySpinnerAdapter = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, payTypeListItem);
		PaySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pay_type_spinner.setAdapter(PaySpinnerAdapter);
	}

	public void setMemberSpinnerDate(){

		Cursor menberNameListCursor  = MoneyDB.getMemberNameList("id asc");

		final ArrayList<String>  strArray = new ArrayList<String> ();
		for(menberNameListCursor.moveToFirst();!menberNameListCursor.isAfterLast();menberNameListCursor.moveToNext()) {

			String id = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("id"));
			String member_name = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("member_name"));

			strArray.add(member_name);
			break;
		}
		if(strArray.isEmpty()){
			member_name_text.setText("-");
		} else {
			member_name_text.setText(strArray.get(0));
		}


	}


	public Boolean addRecord(){

		String recordType = "";//记录类型是支出0，还是收入1
		String amount = "";// 金额
		String categoryId = "";//category_id

		String payType_id = "";//pay_id
		String member_id = "";//member_id
		String remark = "";
		String recordTime = "";

		RadioButton rb = (RadioButton)RecordActivity.this.findViewById(radiogroup.getCheckedRadioButtonId());
		if("支出".equals(rb.getText().toString())){
			recordType = "0";//支出
			if(category_parent_spinner.getAdapter().getCount() != 0){
				if(category_child_spinner.getAdapter().getCount() == 0){
					Toast.makeText(getApplicationContext(), "请先设置小分类数据！", Toast.LENGTH_LONG).show();
					return false;
				} else {
					categoryId = ((SpinnerData)category_child_spinner.getSelectedItem()).getValue();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请先设置大分类数据！", Toast.LENGTH_LONG).show();
				return false;
			}
		} else {
			recordType = "1";//收入
			if(category_income_spinner.getAdapter().getCount() != 0){
				categoryId = ((SpinnerData)category_income_spinner.getSelectedItem()).getValue();
			} else {
				Toast.makeText(getApplicationContext(), "请先设置分类数据！", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		amount = amounts_text.getText().toString();

		if(pay_type_spinner.getAdapter().getCount() != 0){
			payType_id = ((SpinnerData)pay_type_spinner.getSelectedItem()).getValue();
		} else {
			Toast.makeText(getApplicationContext(), "请先设置支付方式数据！", Toast.LENGTH_LONG).show();
			return false;
		}

		member_id = MoneyDB.memberName2Id(member_name_text.getText().toString());
		remark = remark_text.getText().toString();
		recordTime = create_time_text.getText().toString();

		// 2018年12月15日 16:52  -->  2018-12-15 16:52
		recordTime = recordTime.replace("年","-").replace("月","-").replace("日","");

		if(amount.isEmpty()){
			amount = "0.00";
		}

		if(categoryId.isEmpty()){
			Toast.makeText(getApplicationContext(), "分类数据错误！", Toast.LENGTH_LONG).show();
			return false;
		}

		if(payType_id.isEmpty()){
			Toast.makeText(getApplicationContext(), "支付方式数据错误！", Toast.LENGTH_LONG).show();
			return false;
		}

		if("-1".equals(member_id)){
			Toast.makeText(getApplicationContext(), "成员类型错误！", Toast.LENGTH_LONG).show();
			return false;
		}

		if(amount.isEmpty()){
			remark = "";
		}



		try {
			MoneyDB.insertRecord(recordType,amount,categoryId,payType_id,member_id,remark,recordTime);
			Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
			return true;

		} catch (Exception e) {
			return false;
		}


	}




}
