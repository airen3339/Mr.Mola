package net.yaiba.money;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.yaiba.money.data.SpinnerData;
import net.yaiba.money.db.MoneyDB;
import net.yaiba.money.utils.DateTimePickDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.yaiba.money.utils.Custom.getNowDateWithTimes2;
import static net.yaiba.money.utils.Custom.getSplitWord;
import static net.yaiba.money.utils.Custom.transDate2Date2;


public class RecordDetailActivity extends Activity {
	private MoneyDB MoneyDB;
	private Cursor mCursor;
	private ListView RecordList;

	private TextView
			RemarkText,
			AmountsText,
			RecordType,
			CreateTimeText,
			MemberNameText,
			CategoryName,
			PayName;

	private Button save_bn;

	private int RECORD_ID = 0;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MoneyDB = new MoneyDB(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_detail_activity);

		setUpViews();

		Button bn_go_edit = (Button)findViewById(R.id.go_edit);
		bn_go_edit.setOnClickListener(new View.OnClickListener(){
			public void  onClick(View v)
			{
				//画面迁移到edit画面
				Intent mainIntent = new Intent(RecordDetailActivity.this,RecordDetailActivity.class);
				mainIntent.putExtra("INT", RECORD_ID);
				startActivity(mainIntent);
				setResult(RESULT_OK, mainIntent);
				finish();
			}
		});


		Button bn_go_del = (Button)findViewById(R.id.go_del);
		bn_go_del.setOnClickListener(new View.OnClickListener(){
			public void  onClick(View v)
			{
				AlertDialog.Builder builder= new AlertDialog.Builder(RecordDetailActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setTitle("确认");
				builder.setMessage("确定要删除这条记录吗？");
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						delete();
						Intent mainIntent = new Intent(RecordDetailActivity.this,MainActivity.class);
						startActivity(mainIntent);
						setResult(RESULT_OK, mainIntent);
						finish();

					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});




	}



	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent mainIntent = new Intent(RecordDetailActivity.this,MainActivity.class);
			startActivity(mainIntent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			setResult(RESULT_OK, mainIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
 	public void setUpViews(){

		RECORD_ID = this.getIntent().getIntExtra("INT", RECORD_ID);
		Log.v("debug","(onCreate)RECORD_ID:"+RECORD_ID);

		RecordType = (TextView)findViewById(R.id.record_type);
		AmountsText = (TextView)findViewById(R.id.amounts_text);
		CategoryName = (TextView)findViewById(R.id.category_name);
		PayName= (TextView)findViewById(R.id.pay_name);
		RemarkText=(TextView)findViewById(R.id.remark_text);
		CreateTimeText=(TextView)findViewById(R.id.create_time_text);
		MemberNameText=(TextView)findViewById(R.id.member_name_text);

		//save_bn=(Button)findViewById(R.id.save_bn);

		mCursor = MoneyDB.getRecordInfo(RECORD_ID);

		String id = mCursor.getString(mCursor.getColumnIndex("id"));
		String category_name = mCursor.getString(mCursor.getColumnIndex("category_name"));
		String category_id = mCursor.getString(mCursor.getColumnIndex("category_id"));
		String pid = mCursor.getString(mCursor.getColumnIndex("pid"));// !=a 时，收入，，=a时，支出
		String type_id = mCursor.getString(mCursor.getColumnIndex("type_id"));//支出 0,收入1
		String pay_id = mCursor.getString(mCursor.getColumnIndex("pay_id"));
		String pay_name = mCursor.getString(mCursor.getColumnIndex("pay_name"));
		String member_id = mCursor.getString(mCursor.getColumnIndex("member_id"));
		String member_name = mCursor.getString(mCursor.getColumnIndex("member_name"));
		String amounts = mCursor.getString(mCursor.getColumnIndex("amounts"));
		String remark = mCursor.getString(mCursor.getColumnIndex("remark"));
		String create_time = mCursor.getString(mCursor.getColumnIndex("create_time"));




		RecordType.setText("0".equals(type_id)?"支出":"收入");
		AmountsText.setText("￥" + amounts);
		CategoryName.setText("0".equals(type_id)?MoneyDB.getCategoryPName(pid) + " - " +category_name:category_name);
		PayName.setText(pay_name);
		RemarkText.setText(remark);
		CreateTimeText.setText(create_time);
		MemberNameText.setText(member_name);



	}



	public void delete() {
		if (RECORD_ID == 0) {
			return;
		}
		MoneyDB.delete(RECORD_ID);
		//mCursor.requery();
		Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
	}



}
