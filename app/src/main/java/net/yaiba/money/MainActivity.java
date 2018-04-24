package net.yaiba.money;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import net.yaiba.money.db.LoginDB;
import net.yaiba.money.db.MoneyDB;
import net.yaiba.money.utils.SpecialAdapter;
import net.yaiba.money.utils.UpdateTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static net.yaiba.money.utils.Custom.getAppVersion;
import static net.yaiba.money.utils.Custom.transDate2Date2;


public class MainActivity extends Activity {

    private static final int MENU_ABOUT = 0;
    private static final int MENU_SUPPORT = 1;
    private static final int MENU_WHATUPDATE = 2;
    private static final int MENU_IMPORT_EXPOERT = 3;
    private static final int MENU_CHANGE_LOGIN_PASSWORD = 4;
    private static final int MENU_CHECK_UPDATE = 5;
    private static final int MENU_CATEGORY_CONFIG = 6;
    private static final int MENU_PAY_CONFIG = 7;
    private static final int MENU_MEMBER_CONFIG = 8;
    private static final int MENU_CHANGE_LOGIN_TYPE = 9;

    private LoginDB LoginDB;
	private MoneyDB MoneyDB;
	private Cursor mCursor;
	private Long costThisMonth;
	private Long costBeforeMonth;
	private Long income;

    private TextView cost_this_month_text,cost_before_month_text,income_this_month_text,Text_more_info;
	private ListView RecordList;
    private Button bn_record_add;

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
		setContentView(R.layout.main_activity);

        cost_this_month_text = (TextView)findViewById(R.id.cost_this_month);
        cost_before_month_text = (TextView)findViewById(R.id.cost_before_month);
        income_this_month_text = (TextView)findViewById(R.id.income_this_month);
        Text_more_info = (TextView)findViewById(R.id.more_info);

        setUpViews();

        bn_record_add = (Button)findViewById(R.id.record_add);
        bn_record_add.setOnClickListener(new View.OnClickListener(){
            public void  onClick(View v)
            {
                mainIntent = new Intent(MainActivity.this,RecordAddActivity.class);
                startActivity(mainIntent);
                setResult(RESULT_OK, mainIntent);
                finish();
            }
        });

        Text_more_info = (TextView)findViewById(R.id.more_info);
        Text_more_info.setOnClickListener(new View.OnClickListener(){
            public void  onClick(View v)
            {
                mainIntent = new Intent(MainActivity.this,RecordListActivity.class);
                startActivity(mainIntent);
                setResult(RESULT_OK, mainIntent);
                finish();
            }
        });


//        RecordList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem == 0) {
//                    Toast.makeText(MainActivity.this, "##### 滚动到顶部 #####", Toast.LENGTH_SHORT).show();
//                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
//                    Toast.makeText(MainActivity.this, "##### 滚动到底部 #####", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                //do nothing
//            }
//        });


    }

    public void setUpViews(){

        mCursor  = MoneyDB.getRecordForList("create_time desc","0,8");
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



        cost_this_month_text.setText(MoneyDB.getCostThisMonth()+"");
        cost_before_month_text.setText(MoneyDB.getCostBeforeMonth()+"");
        income_this_month_text.setText(MoneyDB.getIncomeThisMonth()+"");

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_IMPORT_EXPOERT, 0, "数据管理");
        menu.add(Menu.NONE, MENU_CATEGORY_CONFIG, 1, "类别管理");
        menu.add(Menu.NONE, MENU_PAY_CONFIG, 2, "付款方式管理");
        menu.add(Menu.NONE, MENU_MEMBER_CONFIG, 3, "成员管理");

        menu.add(Menu.NONE, MENU_CHANGE_LOGIN_PASSWORD, 4, "修改登录密码");
        menu.add(Menu.NONE, MENU_CHANGE_LOGIN_TYPE, 5, "修改登录方式");

        menu.add(Menu.NONE, MENU_WHATUPDATE, 6, "更新日志");
        menu.add(Menu.NONE, MENU_CHECK_UPDATE, 7, "检查更新");
        menu.add(Menu.NONE, MENU_SUPPORT, 8, "技术支持");
        menu.add(Menu.NONE, MENU_ABOUT, 9, "关于MR.Money");

        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)  {
        String title = "";
        String msg = "";
        //Context mContext = null;

        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case MENU_ABOUT:
                title = "关于MR.Money";
                msg = this.getString(R.string.about_app);
                msg = msg + "\n\n";
                msg = msg + "@"+getAppVersion(MainActivity.this);
                showAboutDialog(title,msg);
                break;
            case MENU_SUPPORT://技术支持
                title = this.getString(R.string.menu_support);
                msg = this.getString(R.string.partners);
                showAboutDialog(title,msg);
                break;
            case MENU_WHATUPDATE://更新信息
                title = this.getString(R.string.menu_whatupdate);
                msg = msg + this.getString(R.string.what_updated);
                msg = msg + "\n\n\n";
                showAboutDialog(title,msg);
                break;
            case MENU_CHECK_UPDATE://检查更新
                updateTask = new UpdateTask(MainActivity.this,true);
                updateTask.update();
                break;
            case MENU_IMPORT_EXPOERT://备份与恢复
                Intent mainIntent = new Intent(MainActivity.this, DataManagementActivity.class);
                startActivity(mainIntent);
                setResult(RESULT_OK, mainIntent);
                finish();
                break;
            case MENU_CATEGORY_CONFIG://设置类别信息
                mainIntent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(mainIntent);
                setResult(RESULT_OK, mainIntent);
                finish();
                break;
            case MENU_PAY_CONFIG://设置付款方式
                mainIntent = new Intent(MainActivity.this, PayActivity.class);
                startActivity(mainIntent);
                setResult(RESULT_OK, mainIntent);
                finish();
                break;
            case MENU_MEMBER_CONFIG://设置成员
                mainIntent = new Intent(MainActivity.this, MemberActivity.class);
                startActivity(mainIntent);
                setResult(RESULT_OK, mainIntent);
                finish();
                break;
            case MENU_CHANGE_LOGIN_PASSWORD://修改登录密码
                mainIntent = new Intent(MainActivity.this, PasswordEditActivity.class);
                startActivity(mainIntent);
                setResult(RESULT_OK, mainIntent);
                finish();
                break;

            case MENU_CHANGE_LOGIN_TYPE://设置登录方式

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("设置登录方式");

                String login_type_after = "";
                LoginDB = new LoginDB(MainActivity.this);
                if (LoginDB.isLoginUsePassword() < 0){
                    login_type_after = "免密码登录";
                } else {
                    login_type_after = "凭密码登录";
                }

                final TextView tv_after =new TextView(this);
                tv_after.setText(" 将登录方式改为：“"+login_type_after+"”？");
                tv_after.setTextSize(20);

                builder.setView(tv_after);

                builder.setPositiveButton("確定", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String login_type_after_db = "";
                        if (LoginDB.isLoginUsePassword() < 0){
                            login_type_after_db = "none_password";
                        } else {
                            login_type_after_db = "normal";
                        }
                        LoginDB.updateLoginType(login_type_after_db);
                        Toast.makeText(MainActivity.this, "修改完成" , Toast.LENGTH_SHORT).show();

                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                break;

        }
        return true;
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
