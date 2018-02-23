package net.yaiba.money.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LoginDB extends SQLiteOpenHelper{
	
	private final static String DATABASE_NAME = "MONEY.db";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME_LOGIN = "login_master";
	private final static String ID = "id";
	private final static String PASSWORD = "password";
	private final static String FORGET_ASK = "forget_ask";
	private final static String FORGET_ANSWER = "forget_answer";
	private final static String TYPE = "type";
	private final static String CREATE_TIME = "create_time";

	private final static String TABLE_NAME_RECORD = "record_master";
	private final static String CATEGORY_ID = "category_id";
	private final static String PAY_ID = "pay_id";
	private final static String MEMBER_ID = "member_id";
	private final static String AMOUNTS = "amounts";
	private final static String REMARK = "remark";

	private final static String TABLE_NAME_CATEGORY = "category_master";
	private final static String PID = "pid";
	private final static String CATEGORY_NAME = "category_name";
	private final static String FAVORITE = "favorite";
	private final static String RANK = "rank";

	private final static String TABLE_NAME_PAY = "pay_master";
	private final static String PAY_NAME = "pay_name";

	 
	public LoginDB(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//创建table
	@Override
	public void onCreate(SQLiteDatabase db) {
		//登录用 DB:login_master
		String sql_login_master = "CREATE TABLE "
				+ TABLE_NAME_LOGIN
				+ " (" + ID + " INTEGER primary key autoincrement, "
				+ PASSWORD + " NVARCHAR(100), "
				+ FORGET_ASK +" NVARCHAR(100), "
				+ FORGET_ANSWER +" NVARCHAR(100), "
				+ TYPE +" NVARCHAR(100), "
				+ CREATE_TIME +" NVARCHAR(100) "
				+");";
		db.execSQL(sql_login_master);

		//DB:record_master
		String sql_record_master = "CREATE TABLE "
				+ TABLE_NAME_RECORD
				+ " (" + ID + " INTEGER primary key autoincrement, "
				+ CATEGORY_ID + " NVARCHAR(20), "
				+ PAY_ID +" NVARCHAR(10), "
				+ MEMBER_ID +" NVARCHAR(10), "
				+ AMOUNTS +" NVARCHAR(50), "
				+ REMARK +" TEXT NULL, "
				+ CREATE_TIME +" NVARCHAR(100) "
				+");";
		db.execSQL(sql_record_master);

		//DB:category_master
		String sql_category_master = "CREATE TABLE "
				+ TABLE_NAME_CATEGORY
				+ " (" + ID + " INTEGER primary key autoincrement, "
				+ PID + " INTEGER , "
				+ CATEGORY_NAME +" NVARCHAR(100), "
				+ FAVORITE +" NVARCHAR(10) default '0', "
				+ RANK +" NVARCHAR(10) "
				+");";
		db.execSQL(sql_category_master);

		//DB:pay_master
		String sql_pay_master = "CREATE TABLE "
				+ TABLE_NAME_PAY
				+ " (" + ID + " INTEGER primary key autoincrement, "
				+ PAY_NAME +" NVARCHAR(100), "
				+ FAVORITE +" NVARCHAR(10) default '0', "
				+ RANK +" NVARCHAR(10) "
				+");";
		db.execSQL(sql_pay_master);




	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN;
//		db.execSQL(sql);
//
//		String sql2 = "DROP TABLE IF EXISTS " + TABLE_NAME_PASSWORD;
//		db.execSQL(sql2);
		
		onCreate(db);
	}
	 
	public Cursor getAll() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME_LOGIN, null, null, null, null, null, null);
		return cursor;
	}
	
	 public Cursor getOne(long rowId) {
		 SQLiteDatabase db = this.getReadableDatabase();
		 Cursor cursor = db.query(true, TABLE_NAME_LOGIN, new String[] {ID, PASSWORD, TYPE}, ID + "=" + rowId, null, null, null, null, null);
         if(cursor != null) {
        	 cursor.moveToFirst();
         }
         return cursor;
     }
	 
	 public int isCurrentPassword(String passwordstr) {
		 SQLiteDatabase db = this.getReadableDatabase();
		 Cursor cursor = db.query(true, TABLE_NAME_LOGIN, new String[] {ID, PASSWORD, TYPE}, PASSWORD + "='" + passwordstr+"'", null, null, null, null, null);
         if(cursor.getCount()>0) {
        	 cursor.moveToFirst();
        	 if(passwordstr.equals(cursor.getString(1))){
        		//path for 0.1.3
        		 deleteOthers(db, cursor.getInt(0));
        		 return cursor.getInt(0);
        	 }
         }
         return -1;
     }
	
	public void insert(String password){
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(PASSWORD, password);
		cv.put(TYPE, "normal");
		db.insert(TABLE_NAME_LOGIN, null, cv);
	}
	
	public void delete(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = ID + " = ?";
		String[] whereValue ={ Integer.toString(id) };
		db.delete(TABLE_NAME_LOGIN, where, whereValue);
	}
	
	public void update(int id, String newPassword){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(PASSWORD, newPassword);
		cv.put(TYPE, "normal");
		db.update(TABLE_NAME_LOGIN, cv, where, whereValue);
	}
	
	//path
	//for 0.1.3
	public void deleteOthers(SQLiteDatabase db, int id){
		
		Cursor cursor = db.query(TABLE_NAME_LOGIN, null, null, null, null, null, null);
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) { 
			
			if(cursor.getInt(0) != id){
				this.delete(id);
			}
		}
	}
	
	

}
