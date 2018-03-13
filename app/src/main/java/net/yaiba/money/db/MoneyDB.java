package net.yaiba.money.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static net.yaiba.money.db.BaseConfig.*;


public class MoneyDB extends SQLiteOpenHelper {



    public MoneyDB(Context context) {
        // TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //创建table
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getRecordForMainList(String orderBy, String limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select "+
                TABLE_NAME_RECORD+"."+RECORD_ID +" as "+RECORD_ID +" , "+
                TABLE_NAME_RECORD+"."+RECORD_CATEGORY_ID +" as "+RECORD_CATEGORY_ID +" , "+
                TABLE_NAME_CATEGORY+"."+CATEGORY_NAME +" as "+CATEGORY_NAME +" , "+
                TABLE_NAME_CATEGORY+"."+PID +" as "+PID +" , "+
                TABLE_NAME_RECORD+"."+RECORD_PAY_ID +" as "+RECORD_PAY_ID +" , "+
                TABLE_NAME_RECORD+"."+RECORD_TYPE +" as "+RECORD_TYPE +" , "+
                TABLE_NAME_PAY+"."+PAY_NAME +" as "+PAY_NAME +" , "+
                TABLE_NAME_RECORD+"."+RECORD_MEMBER_ID +" as "+RECORD_MEMBER_ID +" , "+
                TABLE_NAME_MEMBER+"."+MEMBER_NAME +" as "+MEMBER_NAME +" , "+
                TABLE_NAME_RECORD+"."+AMOUNTS +" as "+AMOUNTS +" , "+
                TABLE_NAME_RECORD+"."+REMARK +" as "+REMARK +" , "+
                TABLE_NAME_RECORD+"."+RECORD_CREATE_TIME +" as "+RECORD_CREATE_TIME +"  "+
                " from " +
                TABLE_NAME_RECORD +"," +
                TABLE_NAME_CATEGORY +"," +
                TABLE_NAME_PAY +"," +
                TABLE_NAME_MEMBER  +
                " where " +
                TABLE_NAME_RECORD+"."+RECORD_CATEGORY_ID +" = "+TABLE_NAME_CATEGORY+"."+CATEGORY_ID + " and " +
                TABLE_NAME_RECORD+"."+RECORD_PAY_ID +" = "+TABLE_NAME_PAY+"."+PAY_ID + " and " +
                TABLE_NAME_RECORD+"."+RECORD_MEMBER_ID +" = "+TABLE_NAME_MEMBER+"."+MEMBER_ID +
                " order by " + orderBy +
                " limit " + limit + " ";
        Cursor cursor = db.rawQuery(sql, null);
        Log.v("v_sql",sql);
        return cursor;
    }

    public Cursor getCategoryList(String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
//        return cursor;
        Cursor cursor = db.query(true,
                TABLE_NAME_CATEGORY,
                null,
                null, null, null, null, orderBy, null);
        return cursor;

    }

    public Cursor getCategoryPList(String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true,
                TABLE_NAME_CATEGORY,
                null,
                PID+"='0' and "+CATEGORY_TYPE+ "= '0'" , null, null, null, orderBy, null);
        return cursor;

    }

    public Cursor getCategoryCList(String pid, String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true,
                TABLE_NAME_CATEGORY,
                null,
                PID+"='"+pid+"'", null, null, null, orderBy, null);
        return cursor;

    }

    public Cursor getCategoryIncomeList(String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true,
                TABLE_NAME_CATEGORY,
                null,
                PID+"='0' and "+CATEGORY_TYPE+ "= '1'" , null, null, null, orderBy, null);
        return cursor;

    }

    public long insertCategoryP (String p_name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PID, "0");
        cv.put(CATEGORY_NAME, p_name);
        cv.put(CATEGORY_FAVORITE, "0");
        cv.put(CATEGORY_RANK, "0");
        cv.put(CATEGORY_TYPE, "0");

        long row = db.insert(TABLE_NAME_CATEGORY, null, cv);
        Log.v("v_insertDB",p_name+"//"+row);
        return row;
    }

    public long insertCategoryC (String p_id,String c_name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PID, p_id);
        cv.put(CATEGORY_NAME, c_name);
        cv.put(CATEGORY_FAVORITE, "0");
        cv.put(CATEGORY_RANK, "0");
        cv.put(CATEGORY_TYPE, "0");


        long row = db.insert(TABLE_NAME_CATEGORY, null, cv);
        Log.v("v_insertDB",p_id + ""+ c_name+"//"+row);
        return row;
    }

    public long insertCategoryIncome (String name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PID, "0");
        cv.put(CATEGORY_NAME, name);
        cv.put(CATEGORY_FAVORITE, "0");
        cv.put(CATEGORY_RANK, "0");
        cv.put(CATEGORY_TYPE, "1");


        long row = db.insert(TABLE_NAME_CATEGORY, null, cv);
        Log.v("v_insertDB",name+"//"+row);
        return row;
    }

    public void editCategoryName (String id, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = CATEGORY_ID + " = ?";
        String[] whereValue = { id };
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_NAME, value);
        db.update(TABLE_NAME_CATEGORY, cv, where, whereValue);
    }

    public void delCategoryName (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = CATEGORY_ID + " = ?";
        String[] whereValue = { id };
        db.delete(TABLE_NAME_CATEGORY, where, whereValue);
    }


    public boolean isHaveChildCategory(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_CATEGORY, new String[] {CATEGORY_ID, PID, CATEGORY_NAME}, PID + "='" + id+"' ", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHaveValidRecordByCategory(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_RECORD, new String[] {RECORD_ID}, RECORD_CATEGORY_ID + "=" + id+"", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHaveValidRecordByIncomeCategory(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_RECORD, new String[] {RECORD_ID}, RECORD_CATEGORY_ID + "=" + id+"", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getPayTypeList(String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true,
                TABLE_NAME_PAY,
                null,
                null, null, null, null, orderBy, null);
        return cursor;

    }

    public long insertPayType (String pay_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PAY_NAME, pay_name);
        cv.put(PAY_FAVORITE, "0");
        cv.put(PAY_RANK, "0");
        long row = db.insert(TABLE_NAME_PAY, null, cv);
        Log.v("v_insertDB",PAY_NAME+"//"+row);
        return row;
    }

    public void editPayType (String id, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = PAY_ID + " = ?";
        String[] whereValue = { id };
        ContentValues cv = new ContentValues();
        cv.put(PAY_NAME, value);
        db.update(TABLE_NAME_PAY, cv, where, whereValue);
    }

    public void delPayType (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = PAY_ID + " = ?";
        String[] whereValue = { id };
        db.delete(TABLE_NAME_PAY, where, whereValue);
    }

    public boolean isHaveValidRecordByPayType(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_RECORD, new String[] {RECORD_ID}, RECORD_PAY_ID + "=" + id+"", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getMemberNameList(String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true,
                TABLE_NAME_MEMBER,
                null,
                null, null, null, null, orderBy, null);
        return cursor;

    }

    public long insertMember (String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MEMBER_NAME, name);
        cv.put(MEMBER_FAVORITE, "0");
        cv.put(MEMBER_RANK, "0");
        long row = db.insert(TABLE_NAME_MEMBER, null, cv);
        Log.v("v_insertDB",MEMBER_NAME+"//"+row);
        return row;
    }

    public void editMember (String id, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = MEMBER_ID + " = ?";
        String[] whereValue = { id };
        ContentValues cv = new ContentValues();
        cv.put(MEMBER_NAME, value);
        db.update(TABLE_NAME_MEMBER, cv, where, whereValue);
    }

    public void delMember (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = MEMBER_ID + " = ?";
        String[] whereValue = { id };
        db.delete(TABLE_NAME_MEMBER, where, whereValue);
    }

    public boolean isHaveValidRecordByMemberName(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_RECORD, new String[] {RECORD_ID}, RECORD_MEMBER_ID + "=" + id+"", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMenberExist(String input){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_MEMBER, new String[] {MEMBER_ID}, MEMBER_NAME + "='" + input+"'", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPayTypeExist(String input){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_PAY, new String[] {PAY_ID}, PAY_NAME + "='" + input+"'", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCategoryPExist(String input){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_CATEGORY, new String[] {CATEGORY_ID}, CATEGORY_NAME + "='" + input+"' and "+ PID+"='0' and "+CATEGORY_TYPE+ "= '0'" , null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCategoryCExist(String input ,String pid){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_CATEGORY, new String[] {CATEGORY_ID}, CATEGORY_NAME + "='" + input+"' and "+ PID+"='"+pid+"'", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCategoryIncomeExist(String input){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_CATEGORY, new String[] {CATEGORY_ID}, CATEGORY_NAME + "='" + input+"' and " + PID+"='0' and "+CATEGORY_TYPE+ "= '1'", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public String memberName2Id (String input){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_MEMBER, new String[] {MEMBER_ID}, MEMBER_NAME + "='" + input+"'", null, null, null, null, null);
        if (cursor.moveToNext()) {
            return cursor.getString(0);
        } else {
            return "-1";
        }

    }


    public long insertRecord (String recordType, String amount, String categoryId, String payType_id, String member_id, String remark, String recordTime){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RECORD_CATEGORY_ID, categoryId);
        cv.put(RECORD_PAY_ID, payType_id);
        cv.put(RECORD_MEMBER_ID, member_id);
        cv.put(RECORD_TYPE, recordType);
        cv.put(AMOUNTS, amount);
        cv.put(REMARK, remark);
        cv.put(RECORD_CREATE_TIME, recordTime);



        long row = db.insert(TABLE_NAME_RECORD, null, cv);
        Log.v("v_insertDB",TABLE_NAME_RECORD+"/row/"+row+"/categoryId/"+categoryId+"/payType_id/"+payType_id+"/member_id/"+member_id+"/recordType/"+recordType+"/amount/"+amount+"/remark/"+remark+"/recordTime/"+recordTime);
        return row;
    }




}
