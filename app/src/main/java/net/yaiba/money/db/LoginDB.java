package net.yaiba.money.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static net.yaiba.money.db.BaseConfig.*;

public class LoginDB extends SQLiteOpenHelper{
	



	 
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
				+ " (" + LOGIN_ID + " INTEGER primary key autoincrement, "
				+ PASSWORD + " NVARCHAR(100), "
				+ FORGET_ASK +" NVARCHAR(100), "
				+ FORGET_ANSWER +" NVARCHAR(100), "
				+ LOGIN_TYPE +" NVARCHAR(100), "
				+ LOGIN_CREATE_TIME +" NVARCHAR(100) "
				+");";
		db.execSQL(sql_login_master);

		//DB:record_master
		String sql_record_master = "CREATE TABLE "
				+ TABLE_NAME_RECORD
				+ " (" + RECORD_ID + " INTEGER primary key autoincrement, "
				+ RECORD_CATEGORY_ID + " INTEGER, "
				+ RECORD_PAY_ID +" INTEGER, "
				+ RECORD_MEMBER_ID +" INTEGER, "
				+ AMOUNTS +" NVARCHAR(50), "
				+ REMARK +" TEXT NULL, "
				+ RECORD_CREATE_TIME +" NVARCHAR(100) "
				+");";
		db.execSQL(sql_record_master);

		//DB:category_master
		String sql_category_master = "CREATE TABLE "
				+ TABLE_NAME_CATEGORY
				+ " (" + CATEGORY_ID + " INTEGER primary key autoincrement, "
				+ PID + " INTEGER , "
				+ CATEGORY_NAME +" NVARCHAR(100), "
				+ CATEGORY_FAVORITE +" NVARCHAR(10) default '0', "
				+ CATEGORY_RANK +" NVARCHAR(10), "
				+ CATEGORY_TYPE +" INTEGER "
				+");";
		db.execSQL(sql_category_master);

		//DB:pay_master
		String sql_pay_master = "CREATE TABLE "
				+ TABLE_NAME_PAY
				+ " (" + PAY_ID + " INTEGER primary key autoincrement, "
				+ PAY_NAME +" NVARCHAR(100), "
				+ PAY_FAVORITE +" NVARCHAR(10) default '0', "
				+ PAY_RANK +" NVARCHAR(10) "
				+");";
		db.execSQL(sql_pay_master);

		//DB:member_master
		String sql_member_master = "CREATE TABLE "
				+ TABLE_NAME_MEMBER
				+ " (" + MEMBER_ID + " INTEGER primary key autoincrement, "
				+ MEMBER_NAME +" NVARCHAR(100), "
				+ MEMBER_FAVORITE +" NVARCHAR(10) default '0', "
				+ MEMBER_RANK +" NVARCHAR(10) "
				+");";
		db.execSQL(sql_member_master);


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
		 Cursor cursor = db.query(true, TABLE_NAME_LOGIN, new String[] {LOGIN_ID, PASSWORD, LOGIN_TYPE}, LOGIN_ID + "=" + rowId, null, null, null, null, null);
         if(cursor != null) {
        	 cursor.moveToFirst();
         }
         return cursor;
     }
	 
	 public int isCurrentPassword(String passwordstr) {
		 SQLiteDatabase db = this.getReadableDatabase();
		 Cursor cursor = db.query(true, TABLE_NAME_LOGIN, new String[] {LOGIN_ID, PASSWORD, LOGIN_TYPE}, PASSWORD + "='" + passwordstr+"'", null, null, null, null, null);
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
		cv.put(LOGIN_TYPE, "normal");
		db.insert(TABLE_NAME_LOGIN, null, cv);
	}
	
	public void delete(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = LOGIN_ID + " = ?";
		String[] whereValue ={ Integer.toString(id) };
		db.delete(TABLE_NAME_LOGIN, where, whereValue);
	}
	
	public void update(int id, String newPassword){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = LOGIN_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(PASSWORD, newPassword);
		cv.put(LOGIN_TYPE, "normal");
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
