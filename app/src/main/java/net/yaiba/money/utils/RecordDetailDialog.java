package net.yaiba.money.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import net.yaiba.money.R;

public class RecordDetailDialog extends Dialog implements View.OnClickListener {
    //声明xml文件里的组件
    private TextView tv_title,tv_message,tv_recordid,tv_amounts,tv_category_name,tv_create_time,tv_remark;
    private Button bt_cancel,bt_confirm,btn_record_edit,btn_record_del;

    //声明xml文件中组件中的text变量，为string类，方便之后改
    private String title,message,recordid,amounts,category_name,create_time,remark;
    private String cancel,confirm,record_edit,record_del;

    //声明两个点击事件，等会一定要为取消和确定这两个按钮也点击事件
    private IOnCancelListener cancelListener;
    private IOnConfirmListener confirmListener;
    private IOnRecordEditListener recordeditListener;
    private IOnRecordDelListener recorddelListener;

    //设置四个组件的内容
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setRecordId(String recordid) {
        this.recordid = recordid;
    }
    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }
    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }
    public void setCreateTime(String create_time) {
        this.create_time = create_time;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCancel(String cancel,IOnCancelListener cancelListener) {
        this.cancel = cancel;
        this.cancelListener=cancelListener;
    }
    public void setConfirm(String confirm,IOnConfirmListener confirmListener){
        this.confirm=confirm;
        this.confirmListener=confirmListener;
    }

    public void setRecordEdit(String record_edit,IOnRecordEditListener recordeditListener) {
        this.record_edit = record_edit;
        this.recordeditListener=recordeditListener;
    }
    public void setRecordDel(String record_del,IOnRecordDelListener recorddelListener){
        this.record_del=record_del;
        this.recorddelListener=recorddelListener;
    }

    //CustomDialog类的构造方法
    public RecordDetailDialog(@NonNull Context context) {
        super(context);
    }
    public RecordDetailDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    //在app上以对象的形式把xml里面的东西呈现出来的方法！
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Android 自定义Dialog去除title导航栏的解决方法
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //为了锁定app界面的东西是来自哪个xml文件
        setContentView(R.layout.record_detail_dialog);

        //设置弹窗的宽度
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p =getWindow().getAttributes();
        Point size = new Point();
        d.getSize(size);
        p.width = (int)(size.x * 0.8);//是dialog的宽度为app界面的80%
        getWindow().setAttributes(p);

        //找到组件
//        tv_title=findViewById(R.id.tv_title);
//        tv_message=findViewById(R.id.tv_message);
//        tv_recordid=findViewById(R.id.tv_recordid);
//        bt_cancel=findViewById(R.id.bt_cancel);
//        bt_confirm=findViewById(R.id.bt_confirm);
        tv_amounts=findViewById(R.id.tv_amounts);
        tv_category_name=findViewById(R.id.tv_category_name);
        tv_create_time=findViewById(R.id.tv_create_time);
        tv_remark=findViewById(R.id.tv_remark);

        btn_record_edit=findViewById(R.id.btn_record_edit);
        btn_record_del=findViewById(R.id.btn_record_del);

        //设置组件对象的text参数
//        if (!TextUtils.isEmpty(title)){
//            tv_title.setText(title);
//        }
//        if (!TextUtils.isEmpty(message)){
//            tv_message.setText(message);
//        }
//        if (!TextUtils.isEmpty(recordid)){
//            tv_recordid.setText(recordid);
//        }
        if (!TextUtils.isEmpty(amounts)){
            tv_amounts.setText(amounts);
        }
        if (!TextUtils.isEmpty(category_name)){
            tv_category_name.setText(category_name);
        }
        if (!TextUtils.isEmpty(create_time)){
            tv_create_time.setText(create_time);
        }
        if (!TextUtils.isEmpty(remark)){
            tv_remark.setText(remark);
        }

//        if (!TextUtils.isEmpty(cancel)){
//            bt_cancel.setText(cancel);
//        }

        //为两个按钮添加点击事件
//        bt_confirm.setOnClickListener(this);
//        bt_cancel.setOnClickListener(this);
        btn_record_edit.setOnClickListener(this);
        btn_record_del.setOnClickListener(this);
    }

    //重写onClick方法
    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.bt_cancel:
//                if(cancelListener!=null){
//                    cancelListener.onCancel(this);
//                }
//                dismiss();
//                break;
//            case R.id.bt_confirm:
//                if(confirmListener!=null){
//                    confirmListener.onConfirm(this);
//                }
//                dismiss();//按钮按之后会消失
//                break;
//        }
        dismiss();
    }

    //写两个接口，当要创建一个CustomDialog对象的时候，必须要实现这两个接口
    //也就是说，当要弹出一个自定义dialog的时候，取消和确定这两个按钮的点击事件，一定要重写！
    public interface IOnCancelListener{
        void onCancel(RecordDetailDialog dialog);
    }
    public interface IOnConfirmListener{
        void onConfirm(RecordDetailDialog dialog);
    }

    public interface IOnRecordEditListener{
        void onCancel(RecordDetailDialog dialog);
    }
    public interface IOnRecordDelListener{
        void onConfirm(RecordDetailDialog dialog);
    }
}
