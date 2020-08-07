package net.yaiba.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.yaiba.money.data.ListViewData;
import net.yaiba.money.data.SpinnerData;
import net.yaiba.money.db.MoneyDB;
import net.yaiba.money.utils.SpecialAdapter;
import net.yaiba.money.utils.UpdateTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static net.yaiba.money.utils.Custom.getAppVersion;
import static net.yaiba.money.utils.Custom.getSplitWord;
import static net.yaiba.money.utils.Custom.transDate2Date2;
import static net.yaiba.money.utils.Custom.transDate2todayormore;


public class RecordListActivity extends Activity implements  AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

	private MoneyDB MoneyDB;
	private Cursor mCursor;

	private ListView RecordList;

    private EditText SearchInput;

    private LinearLayout filtersOption;
    private Button bn_filters;
    private boolean isButton = true;
    private Button bn_search;

    private Intent mainIntent;

    private ArrayAdapter<SpinnerData> AdapterCateP;
    private ArrayAdapter<SpinnerData> AdapterCateC;
    private ArrayAdapter<SpinnerData> AdapterCateIncome;
    private ArrayAdapter<SpinnerData> PaySpinnerAdapter;

    private Spinner record_type_spinner,
            category_child_spinner,
            category_parent_spinner,
            category_income_spinner,
            pay_type_spinner,
            create_time_spinner;
    private EditText amounts_text,
            remark_text;
    private TextView create_time_text,
            member_name_text;
	 
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

        record_type_spinner = (Spinner) findViewById(R.id.filter_record_type);
        category_parent_spinner = (Spinner) findViewById(R.id.category_parent_spinner);
        category_child_spinner = (Spinner) findViewById(R.id.category_child_spinner);
        category_income_spinner = (Spinner) findViewById(R.id.category_income_spinner);
        pay_type_spinner = (Spinner) findViewById(R.id.pay_type_spinner);
        create_time_spinner = (Spinner) findViewById(R.id.filter_create_time);

        member_name_text=(TextView)findViewById(R.id.member_name_text);
        setUpViews("listInit",null);

        //返回前设置前次的位置值
        setRecordListPosition();

        //设置一览数据过滤区域 开和隐藏
        bn_filters = (Button)findViewById(R.id.filters);
        filtersOption = (LinearLayout) findViewById(R.id.filters_option);
        bn_filters.setOnClickListener(new View.OnClickListener(){
            public void  onClick(View v)
            {
                if(isButton){
                    filtersOption.setVisibility(View.VISIBLE);
                    isButton = false;
                }else {
                    filtersOption.setVisibility(View.GONE);
                    isButton = true;
                    SearchInput = (EditText)findViewById(R.id.searchInput);
                    if(SearchInput.getText().toString().trim().isEmpty()){
                        setUpViews("listInit",null);
                    } else{
                        setUpViews("search",SearchInput.getText().toString().trim());
                    }
                }
            }
        });

        //选择item的选择点击监听事件
        record_type_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(arg2 == 0){
                    // 支出
                    category_parent_spinner.setVisibility(View.VISIBLE);
                    category_child_spinner.setVisibility(View.VISIBLE);
                    category_income_spinner.setVisibility(View.GONE);
                } else if (arg2 == 1){
                    // 收入
                    category_parent_spinner.setVisibility(View.GONE);
                    category_child_spinner.setVisibility(View.GONE);
                    category_income_spinner.setVisibility(View.VISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        // 点击设置成员功能事件响应
        member_name_text.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordListActivity.this);
                builder.setTitle("成员选择");

                Cursor menberNameListCursor  = MoneyDB.getMemberNameList("id asc");

                final ArrayList<String>  memberNameListItem = new ArrayList<String> ();
                for(menberNameListCursor.moveToFirst();!menberNameListCursor.isAfterLast();menberNameListCursor.moveToNext()) {
                    String id = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("id"));
                    String member_name = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("member_name"));
                    memberNameListItem.add(member_name);
                }

                // 增加初期化时“全部”选项
                String all_item = "全部";
                memberNameListItem.add(all_item);
                final String[] items = (String[]) memberNameListItem.toArray(new String[0]);

                String newMember = member_name_text.getText().toString();
                int checkedItem = 0;
                for (int i=0;i<memberNameListItem.size();i++){
                    if(newMember.equals(memberNameListItem.get(i))){
                        checkedItem = i;
                        break;
                    }
                }

                builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                    //第二个参数是设置默认选中哪一项-1代表默认都不选
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

    public void setUpViews(String type, String value){
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
            String record_info = transDate2Date2(create_time) + " " + remark;

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("id", id);
            map.put("type_id",type_id );
            map.put("category_child_name", category_name);
            map.put("pay_name", getSplitWord(pay_name,2) );
            map.put("create_time",create_time );//transDate2Date2(create_time)
            map.put("remark", remark);
            map.put("record_info", record_info);
            map.put("amounts", "￥"+ amounts);

            listItem.add(map);
            Log.v("v_recordlist",id+"/"+category_name+"/"+create_time+"/"+remark+"/"+amounts+"/"+type_id);
            Log.v("v_recordlist_record_info",id+"/"+category_name+"/"+record_info+"/"+amounts+"/"+type_id);
        }

        SpecialAdapter listItemAdapter = new SpecialAdapter(this,listItem,R.layout.main_record_list_items,
                new String[] {"category_child_name","pay_name","record_info","amounts","type_id"},
                new int[] {R.id.category_child_name, R.id.pay_name,R.id.record_info, R.id.amounts, R.id.type_id}
        );
        RecordList.setAdapter(listItemAdapter);
        RecordList.setOnItemClickListener(this);

        setCategoryCostSpinnerDate();
        setCategoryIncomeSpinnerDate();
        setPayTypeSpinnerDate();
        setMemberSpinnerDate();
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


    public void setCategoryCostSpinnerDate(){
        // 支出，大分类下拉列表，初期化
        Cursor categoryPListCursor  = MoneyDB.getCategoryPList("id asc");
        List<SpinnerData> categoryPListItem = new ArrayList<SpinnerData>();

        for(categoryPListCursor.moveToFirst();!categoryPListCursor.isAfterLast();categoryPListCursor.moveToNext()) {
            String id = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("id"));
            String pid = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("pid"));
            String category_name = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("category_name"));
            Log.v("v_record"+id,id+"/"+pid+"/"+category_name+"/");

            SpinnerData c = new SpinnerData(id, category_name);
            categoryPListItem.add(c);
        }
        // 增加初期化时“全部”选项
        SpinnerData all_item = new SpinnerData("9999", "全部");
        categoryPListItem.add(all_item);

        AdapterCateP = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryPListItem);
        AdapterCateP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_parent_spinner.setAdapter(AdapterCateP);
        // 设置默认值，最后增加的那个“全部”
        category_parent_spinner.setSelection(AdapterCateP.getCount()-1);

        // 支出，小分类下拉列表，初期化
        if (category_parent_spinner.getAdapter().getCount() != 0){
            //取得大分类第一条数据的id，传递给小分类。
            categoryPListCursor.moveToFirst();
            String SpinnerFirstid = categoryPListCursor.getString(categoryPListCursor.getColumnIndex("id"));
            Log.v("v_SpinnerFirstid",SpinnerFirstid);

            Cursor categoryCListCursor = MoneyDB.getCategoryCList(SpinnerFirstid,"id asc");
            List<SpinnerData> categoryCListItem = new ArrayList<SpinnerData>();
            for(categoryCListCursor.moveToFirst();!categoryCListCursor.isAfterLast();categoryCListCursor.moveToNext()) {
                String id = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("id"));
                String pid = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("pid"));
                String category_name = categoryCListCursor.getString(categoryCListCursor.getColumnIndex("category_name"));

                SpinnerData c = new SpinnerData(id, category_name);
                categoryCListItem.add(c);
            }
            // 增加初期化时“全部”选项
            categoryCListItem.add(all_item);

            AdapterCateC = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryCListItem);
            AdapterCateC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            category_child_spinner.setAdapter(AdapterCateC);
            // 设置默认值，最后增加的那个“全部”
            category_child_spinner.setSelection(AdapterCateC.getCount()-1);
        }
    }

    public void setCategoryIncomeSpinnerDate(){
        // 收入，分类下拉列表，初期化
        Cursor categoryIncomeListCursor  = MoneyDB.getCategoryIncomeList("id asc");
        List<SpinnerData> categoryIncomeListItem = new ArrayList<SpinnerData>();

        for(categoryIncomeListCursor.moveToFirst();!categoryIncomeListCursor.isAfterLast();categoryIncomeListCursor.moveToNext()) {
            String id = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("id"));
            String pid = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("pid"));
            String category_name = categoryIncomeListCursor.getString(categoryIncomeListCursor.getColumnIndex("category_name"));

            SpinnerData c = new SpinnerData(id, category_name);
            categoryIncomeListItem.add(c);
        }

        // 增加初期化时“全部”选项
        SpinnerData all_item = new SpinnerData("9999", "全部");
        categoryIncomeListItem.add(all_item);

        AdapterCateIncome = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, categoryIncomeListItem);
        AdapterCateIncome.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_income_spinner.setAdapter(AdapterCateIncome);
        // 设置默认值，最后增加的那个“全部”
        category_income_spinner.setSelection(AdapterCateIncome.getCount()-1);
    }

    public void setPayTypeSpinnerDate(){
        Cursor payTypeListCursor  = MoneyDB.getPayTypeList("id asc");
        List<SpinnerData> payTypeListItem = new ArrayList<SpinnerData>();

        for(payTypeListCursor.moveToFirst();!payTypeListCursor.isAfterLast();payTypeListCursor.moveToNext()) {
            /*String resNo = "["+mCursor.getString(resNoColumn)+"]"; */
            String id = payTypeListCursor.getString(payTypeListCursor.getColumnIndex("id"));
            String pay_name = payTypeListCursor.getString(payTypeListCursor.getColumnIndex("pay_name"));
            SpinnerData c = new SpinnerData(id, pay_name);
            payTypeListItem.add(c);
        }

        // 增加初期化时“全部”选项
        SpinnerData all_item = new SpinnerData("9999", "全部");
        payTypeListItem.add(all_item);

        PaySpinnerAdapter = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, payTypeListItem);
        PaySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pay_type_spinner.setAdapter(PaySpinnerAdapter);
        Log.v("PaySpinnerAdapter.getCount()",PaySpinnerAdapter.getCount()+"");
        // 设置默认值，最后增加的那个“全部”
        pay_type_spinner.setSelection(PaySpinnerAdapter.getCount()-1);

    }

    public void setMemberSpinnerDate(){

        Cursor menberNameListCursor  = MoneyDB.getMemberNameList("id asc");
        final ArrayList<String>  memberNameListItem = new ArrayList<String> ();
        for(menberNameListCursor.moveToFirst();!menberNameListCursor.isAfterLast();menberNameListCursor.moveToNext()) {
            String id = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("id"));
            String member_name = menberNameListCursor.getString(menberNameListCursor.getColumnIndex("member_name"));
            memberNameListItem.add(member_name);
            break;
        }

        if(memberNameListItem.isEmpty()){
            member_name_text.setText("-");
        } else {
            //member_name_text.setText(memberNameListItem.get(0));
            member_name_text.setText("全部");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //保存当前一览位置
        saveListViewPositionAndTop();
        //迁移到详细页面
        Intent mainIntent = new Intent(RecordListActivity.this,RecordDetailActivity.class);
        mCursor.moveToPosition(position);
        RECORD_ID = mCursor.getInt(0);

        Log.v("v_debug","RECORD_ID:"+RECORD_ID);
        mainIntent.putExtra("INT", RECORD_ID);
        startActivity(mainIntent);
        setResult(RESULT_OK, mainIntent);
        finish();
    }


    public class RecordListAdapter extends BaseAdapter {
        private Context mContext;
        private Cursor mCursor;
        public RecordListAdapter(Context context,Cursor cursor) {
            mContext = context;
            mCursor = cursor;
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView mTextView = new TextView(mContext);
            mCursor.moveToPosition(position);
            mTextView.setText(mCursor.getString(1) + "___" + mCursor.getString(2)+ "___" + mCursor.getString(3)+ "___" + mCursor.getString(4));
            return mTextView;
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mCursor.moveToPosition(position);
        RECORD_ID = mCursor.getInt(0);
        return false;
    }




    //保存当前页签listView的第一个可见的位置和top
    public void saveListViewPositionAndTop() {
        final ListViewData app = (ListViewData)getApplication();
        app.setFirstVisiblePosition(RecordList.getFirstVisiblePosition());
        View item = RecordList.getChildAt(0);
        app.setFirstVisiblePositionTop((item == null) ? 0 : item.getTop());
    }


    //返回前设置前次的位置值
    public void setRecordListPosition(){
        ListViewData app = (ListViewData)getApplication();
        RecordList.setSelectionFromTop(app.getFirstVisiblePosition(), app.getFirstVisiblePositionTop());
    }
		
}
