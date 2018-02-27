package net.yaiba.money.db;

/**
 * Created by benyaiba on 2018/02/23.
 */

public class BaseConfig {

    public final static String DATABASE_NAME = "MONEY.db";
    public final static int DATABASE_VERSION = 1;

    //login_master
    public final static String TABLE_NAME_LOGIN = "login_master";
    public final static String LOGIN_ID = "id";
    public final static String PASSWORD = "password";
    public final static String FORGET_ASK = "forget_ask";
    public final static String FORGET_ANSWER = "forget_answer";
    public final static String LOGIN_TYPE = "type";
    public final static String LOGIN_CREATE_TIME = "create_time";

    //record_master
    public final static String TABLE_NAME_RECORD = "record_master";
    public final static String RECORD_ID = "id";
    public final static String RECORD_CATEGORY_ID = "category_id";
    public final static String RECORD_PAY_ID = "pay_id";
    public final static String RECORD_MEMBER_ID = "member_id";
    public final static String AMOUNTS = "amounts";
    public final static String REMARK = "remark";
    public final static String RECORD_CREATE_TIME = "create_time";

    //category_master
    public final static String TABLE_NAME_CATEGORY = "category_master";
    public final static String CATEGORY_ID = "id";
    public final static String PID = "pid";
    public final static String CATEGORY_NAME = "category_name";
    public final static String CATEGORY_FAVORITE = "favorite";
    public final static String CATEGORY_RANK = "rank";
    public final static String CATEGORY_TYPE = "type";//cost 0,income 1

    //pay_master
    public final static String TABLE_NAME_PAY = "pay_master";
    public final static String PAY_ID = "id";
    public final static String PAY_NAME = "pay_name";
    public final static String PAY_FAVORITE = "favorite";
    public final static String PAY_RANK = "rank";

    //member_master
    public final static String TABLE_NAME_MEMBER = "member_master";
    public final static String MEMBER_ID = "id";
    public final static String MEMBER_NAME = "member_name";
    public final static String MEMBER_FAVORITE = "favorite";
    public final static String MEMBER_RANK = "rank";
}
