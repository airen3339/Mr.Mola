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


public class MemberActivity extends Activity {
	private MoneyDB MoneyDB;
	private Cursor mCursor;
	private ListView RecordList;
	private ArrayAdapter<SpinnerData> MenberSpinnerAdapter;
	private Spinner member_spinner;
	private Button bn_member_name_add;
	private Button bn_member_name_edit;
	private Button bn_member_name_del;

	private int RECORD_ID = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		MoneyDB = new MoneyDB(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_activity);

		setUpViews();

		//成员，添加
		bn_member_name_add.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				final EditText inputMenberName = new EditText(MemberActivity.this);
				AlertDialog.Builder builder= new AlertDialog.Builder(MemberActivity.this);
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setTitle("添加成员");
				builder.setMessage("请输入成员名称");
				builder.setView(inputMenberName);
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String input = inputMenberName.getText().toString();
						if (input.equals("")) {
							Toast.makeText(getApplicationContext(), "成员名不能为空！", Toast.LENGTH_LONG).show();
						}  else {
							addMemberName(input);
							Toast.makeText(getApplicationContext(), "成员添加完成：" + input, Toast.LENGTH_LONG).show();
							setUpViews();
						}
					}
				});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});

		//成员，修改
		bn_member_name_edit.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(member_spinner.getAdapter().getCount() != 0){
					final EditText inputMenberName = new EditText(MemberActivity.this);
					Spinner memberS = (Spinner)findViewById(R.id.member_name);//取得大分类下拉列表选择的
					inputMenberName.setText(((SpinnerData)memberS.getSelectedItem()).getText());//将选择的名称设置到弹出的对话框文本域中
					AlertDialog.Builder builder= new AlertDialog.Builder(MemberActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("修改成员名称");
					builder.setMessage("请输入修改后的成员名称");
					builder.setView(inputMenberName);
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String input = inputMenberName.getText().toString();
							Spinner memberS = (Spinner)findViewById(R.id.member_name);
							String id = ((SpinnerData)memberS.getSelectedItem()).getValue();
							if (input.equals("")) {
								Toast.makeText(getApplicationContext(), "成员名不能为空！", Toast.LENGTH_LONG).show();
							}  else {
								editMemberName(id,input);
								Toast.makeText(getApplicationContext(), "成员名称修改完成：" + input, Toast.LENGTH_LONG).show();
								setUpViews();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "成员数据不存在，不能修改", Toast.LENGTH_LONG).show();
				}

			}
		});

		//成员，删除
		bn_member_name_del.setOnClickListener(new View.OnClickListener() {
			public void  onClick(View v) {
				if(member_spinner.getAdapter().getCount() != 0){
					Spinner memberS = (Spinner)findViewById(R.id.member_name);
					AlertDialog.Builder builder= new AlertDialog.Builder(MemberActivity.this);
					builder.setIcon(android.R.drawable.ic_dialog_info);
					builder.setTitle("删除选择的成员");
					builder.setMessage("确定要删除成员：「"+((SpinnerData)memberS.getSelectedItem()).getText()+"」吗");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Spinner memberS = (Spinner)findViewById(R.id.member_name);
							String id = ((SpinnerData)memberS.getSelectedItem()).getValue();
							if(!MoneyDB.isHaveValidRecordByMemberName(id)){
								delMemberName(id);
								Toast.makeText(getApplicationContext(), "删除成功" , Toast.LENGTH_LONG).show();
								setUpViews();
							} else {
								Toast.makeText(getApplicationContext(), "当前成员下含有数据，无法删除！", Toast.LENGTH_LONG).show();
							}
						}
					});
					builder.setNegativeButton("取消", null);
					builder.create().show();
				} else {
					Toast.makeText(getApplicationContext(), "成员数据不存在，不能删除", Toast.LENGTH_LONG).show();
				}
			}
		});



	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 	{
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent mainIntent = new Intent(MemberActivity.this,MainActivity.class);
			startActivity(mainIntent);
			overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
			setResult(RESULT_OK, mainIntent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
 	public void setUpViews(){
		bn_member_name_add = (Button)findViewById(R.id.member_name_add);
		bn_member_name_edit = (Button)findViewById(R.id.member_name_edit);
		bn_member_name_del = (Button)findViewById(R.id.member_name_del);

		Cursor menberNameListCursor  = MoneyDB.getMemberNameList("id desc");
		member_spinner = (Spinner) findViewById(R.id.member_name);
		List<SpinnerData> memberNameListItem = new ArrayList<SpinnerData>();

		RecordList = (ListView)findViewById(R.id.member_name_list);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

		for(menberNameListCursor.moveToFirst();!menberNameListCursor.isAfterLast();menberNameListCursor.moveToNext()) {
            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
			String id = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("id"));
			String member_name = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("member_name"));

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("member_name", member_name);

			listItem.add(map);

			SpinnerData c = new SpinnerData(id, member_name);
			memberNameListItem.add(c);

		}
		MenberSpinnerAdapter = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, memberNameListItem);
		MenberSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		member_spinner.setAdapter(MenberSpinnerAdapter);


		SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,R.layout.member_list_items,
				new String[] {"member_name"},
				new int[] {R.id.member_name}
		);
		RecordList.setAdapter(listItemAdapter);


	}


	public void addMemberName(String input){
		MoneyDB.insertMember(input);
	}

	public void editMemberName(String id, String input){
		Log.v("v_record_edit",input+"/"+id);
		MoneyDB.editMember(id, input);
	}

	public void delMemberName(String id){
		Log.v("v_record_del",id);
		MoneyDB.delMember(id);
	}


}
