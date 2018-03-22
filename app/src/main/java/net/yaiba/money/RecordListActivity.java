package net.yaiba.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.yaiba.money.db.MoneyDB;
import net.yaiba.money.utils.SpecialAdapter;
import net.yaiba.money.utils.UpdateTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static net.yaiba.money.utils.Custom.getAppVersion;
import static net.yaiba.money.utils.Custom.transDate2Date2;


public class RecordListActivity extends Activity {

	private MoneyDB MoneyDB;
	private Cursor mCursor;

	private ListView RecordList;

    private Intent mainIntent;

	 
	private int RECORD_ID = 0;
	//private UpdateTask updateTask;
	private ProgressDialog updateProgressDialog;

	private  ArrayList<HashMap<String, Object>> listItemLike;

    private UpdateTask updateTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        MoneyDB = new MoneyDB(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_list_activity);

        setUpViews();
    }

    public void setUpViews(){

        mCursor  = MoneyDB.getRecordForList("create_time desc","0,180");
        RecordList = (ListView)findViewById(R.id.recordslist);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

        for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()) {
            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
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


            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("category_child_name", category_name);
            map.put("create_time",transDate2Date2(create_time) );
            map.put("type_id",type_id );
            map.put("remark", remark);
            map.put("amounts", "￥"+ amounts);

            listItem.add(map);
            Log.v("v_mainlist",id+"/"+category_name+"/"+create_time+"/"+remark+"/"+amounts+"/"+type_id);
        }

        SpecialAdapter listItemAdapter = new SpecialAdapter(this,listItem,R.layout.main_record_list_items,
                new String[] {"category_child_name","create_time","remark","amounts","type_id"},
                new int[] {R.id.category_child_name, R.id.create_time, R.id.remark, R.id.amounts, R.id.type_id}
        );
        RecordList.setAdapter(listItemAdapter);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent mainIntent = new Intent(RecordListActivity.this,MainActivity.class);
            startActivity(mainIntent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            setResult(RESULT_OK, mainIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }





    public void showAboutDialog(String title,String msg){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("确定", null);
        builder.create().show();
    }


		
}
