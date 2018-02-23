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
    private static final int MENU_CATEGORY_CONFIG = 6;
    private static final int MENU_PAY_CONFIG = 7;
    private static final int MENU_MEMBER_CONFIG = 8;
	//private PasswordDB PasswordDB;
	private Cursor mCursor;
	
	private EditText SiteName;
	private EditText UserName;
	private EditText PasswordValue;
	private EditText Remark;
	private EditText SearchInput;
	private TextView FloatLetter;
	private ListView RecordList;

	 
	private int RECORD_ID = 0;
	//private UpdateTask updateTask;
	private ProgressDialog updateProgressDialog;

	private  ArrayList<HashMap<String, Object>> listItemLike;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);


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
        menu.add(Menu.NONE, MENU_IMPORT_EXPOERT, 0, "备份与恢复");
        menu.add(Menu.NONE, MENU_CHANGE_LOGIN_PASSWORD, 4, "修改登录密码");
        menu.add(Menu.NONE, MENU_WHATUPDATE, 5, "更新日志");
        menu.add(Menu.NONE, MENU_CHECK_UPDATE, 6, "检查更新");
        menu.add(Menu.NONE, MENU_SUPPORT, 7, "技术支持");
        menu.add(Menu.NONE, MENU_ABOUT, 8, "关于MR.Money");
        menu.add(Menu.NONE, MENU_CATEGORY_CONFIG, 1, "类别管理");
        menu.add(Menu.NONE, MENU_PAY_CONFIG, 2, "付款方式管理");
        menu.add(Menu.NONE, MENU_MEMBER_CONFIG, 3, "成员管理");
        return true;
    }



		
}
