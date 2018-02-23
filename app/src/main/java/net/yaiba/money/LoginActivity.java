package net.yaiba.money;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.yaiba.money.tool.DES;
import net.yaiba.money.tool.CustomUtils;

import net.yaiba.money.db.LoginDB;

import java.io.IOException;

public class LoginActivity extends Activity {

    private LoginDB LoginDB;
    private Cursor accountCursor;

    private EditText Password;
    private EditText Password2;
    private EditText loginPassword;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LoginDB = new LoginDB(this);
        accountCursor = LoginDB.getAll();

        super.onCreate(savedInstanceState);
        if(accountCursor.getCount()==0){
            setContentView(R.layout.login_init);

            Button bn_reg = (Button)findViewById(R.id.reg);
            bn_reg.setOnClickListener(new OnClickListener(){
                public void  onClick(View v)
                {
                    if(reg()){
                        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(mainIntent);
                        setResult(RESULT_OK, mainIntent);
                        finish();
                    }
                }
            });


        }else{
            setContentView(R.layout.login);

            loginPassword = (EditText) findViewById(R.id.login_password);
            loginPassword.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(loginPassword.getText().toString().length()!=0){
                        try {
                            if(LoginDB.isCurrentPassword(DES.encryptDES(loginPassword.getText().toString())) >= 0){
                                Toast.makeText(LoginActivity.this, "欢迎回来" , Toast.LENGTH_SHORT).show();
                                //Toast.makeText(MainActi.this, "Changed--"+ et1.getText().toString() , Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                                setResult(RESULT_OK, mainIntent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Toast.makeText(LoginActivity.this, "beforeTextChanged", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Toast.makeText(LoginActivity.this, "afterTextChanged", Toast.LENGTH_SHORT).show();
                }
            });

        }

        //设置首页版本
        TextView textView = (TextView) findViewById(R.id.version_id);
        textView.setText(CustomUtils.getVersion(this));

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


    protected Boolean reg(){
        Password = (EditText)findViewById(R.id.password);
        Password2 = (EditText)findViewById(R.id.password2);

        String password = Password.getText().toString();
        String password2 = Password2.getText().toString();


        if (password.equals("")){
            Toast.makeText(this, "[密码]不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() >30){
            Toast.makeText(this, "[密码]长度不能超过30个文字", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password2.equals("")){
            Toast.makeText(this, "[密码]不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(password2)){
            Toast.makeText(this, "两组[密码]不一致", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            LoginDB.insert(DES.encryptDES(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
        return true;
    }


}
