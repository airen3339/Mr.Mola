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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.yaiba.money.db.MoneyDB;

import java.text.ParseException;


public class CategoryActivity extends Activity {
	private MoneyDB MoneyDB;
	private Cursor mCursor;
	private ListView RecordList;


	private int RECORD_ID = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MoneyDB = new MoneyDB(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_activity);



		setUpViews();

//		Button bn_go_edit = (Button)findViewById(R.id.go_edit);
//		bn_go_edit.setOnClickListener(new View.OnClickListener(){
//			public void  onClick(View v)
//			{
//				//画面迁移到edit画面
//				Intent mainIntent = new Intent(DetailActivity.this,EditActivity.class);
//				mainIntent.putExtra("INT", RECORD_ID);
//				startActivity(mainIntent);
//				setResult(RESULT_OK, mainIntent);
//				finish();
//			}
//		});
//
//
//		Button bn_go_del = (Button)findViewById(R.id.go_del);
//		bn_go_del.setOnClickListener(new View.OnClickListener(){
//			public void  onClick(View v)
//			{
//				AlertDialog.Builder builder= new AlertDialog.Builder(DetailActivity.this);
//				builder.setIcon(android.R.drawable.ic_dialog_info);
//				builder.setTitle("确认");
//				builder.setMessage("确定要删除这条记录吗？");
//				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						delete();
//						Intent mainIntent = new Intent(DetailActivity.this,MainActivity.class);
//						startActivity(mainIntent);
//						setResult(RESULT_OK, mainIntent);
//						finish();
//
//					}
//				});
//				builder.setNegativeButton("取消", null);
//				builder.create().show();
//			}
//		});
//
//		Button bn_go_status_used = (Button)findViewById(R.id.go_status_used);
//		bn_go_status_used.setOnClickListener(new View.OnClickListener(){
//			public void  onClick(View v)
//			{
//				AlertDialog.Builder builder= new AlertDialog.Builder(DetailActivity.this);
//				builder.setIcon(android.R.drawable.ic_dialog_info);
//				builder.setTitle("确认");
//				builder.setMessage("确定这个东东已经使用了吗？");
//				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						update_status("1");
//						Intent mainIntent = new Intent(DetailActivity.this,MainActivity.class);
//						startActivity(mainIntent);
//						setResult(RESULT_OK, mainIntent);
//						finish();
//
//					}
//				});
//				builder.setNegativeButton("取消", null);
//				builder.create().show();
//			}
//		});




	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
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

		mCursor = MoneyDB.getCategoryList("id desc");



		//RecordList = (ListView)findViewById(R.id.categoryList);









	}



}
