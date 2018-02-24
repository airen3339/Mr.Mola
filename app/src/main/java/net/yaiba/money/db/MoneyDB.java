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

    public Cursor getRecordForMainList(String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
//        return cursor;
        Cursor cursor = db.query(true,
                TABLE_NAME_RECORD,
                new String[] {RECORD_ID, RECORD_CATEGORY_ID, RECORD_PAY_ID, RECORD_MEMBER_ID,AMOUNTS, REMARK, RECORD_CREATE_TIME},
                null , null, null, null, orderBy, null);
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
                PID+"=0", null, null, null, orderBy, null);
        return cursor;

    }

    public Cursor getCategoryCList(String pid, String orderBy) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true,
                TABLE_NAME_CATEGORY,
                null,
                PID+"="+pid, null, null, null, orderBy, null);
        return cursor;

    }

    public long insertCategoryP (String p_name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PID, "0");
        cv.put(CATEGORY_NAME, p_name);
        cv.put(CATEGORY_FAVORITE, "0");
        cv.put(CATEGORY_RANK, "0");


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


        long row = db.insert(TABLE_NAME_CATEGORY, null, cv);
        Log.v("v_insertDB",p_id + ""+ c_name+"//"+row);
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
        Cursor cursor = db.query(true, TABLE_NAME_CATEGORY, new String[] {CATEGORY_ID, PID, CATEGORY_NAME}, PID + "=" + id+"", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHaveValidRecord(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(true, TABLE_NAME_RECORD, new String[] {RECORD_ID}, RECORD_CATEGORY_ID + "=" + id+"", null, null, null, null, null);
        if(cursor.getCount() != 0) {
            return true;
        } else {
            return false;
        }
    }



//    public long getAllCount(String orderBy){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME , null);
//        if (cursor.moveToNext()) {
//            return cursor.getLong(0);
//        }
//        return 0;
//    }
//
//    public Cursor getLimit90(String orderBy) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(true,TABLE_NAME, null, null, null, null, null, orderBy,"0,90");
//        return cursor;
//    }
//
//    public Cursor getDay30(String orderBy) {
//
//        String start_date = Custom.getDateToString(Custom.getFrontDay(new Date(),30));
//        String end_date = Custom.getDateToString(new Date());
//
//        String sql_buy_date = "( "+BUY_DATE+">='" +start_date +"' and " + BUY_DATE + "<='" + end_date +"' )";
//        Log.v("debug",sql_buy_date);
//        String where = "";
//        where = sql_buy_date;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(true,TABLE_NAME, null, where , null, null, null, orderBy, null);
//        return cursor;
//    }
//
//    public Cursor getAllForBakup(String orderBy) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);
//        return cursor;
//    }
//
//
//    public Cursor getRecordInfo(long rowId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(true, TABLE_NAME, new String[] {RECORD_ID, GOOD_NAME, PRODUCT_DATE, END_DATE, BUY_DATE, STATUS, REMARK, HP}, RECORD_ID + "=" + rowId, null, null, null, null, null);
//        if(cursor != null) {
//            cursor.moveToFirst();
//        }
//        return cursor;
//    }
//
//    public Cursor getStartUsageDay() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(true, TABLE_NAME, new String[] {BUY_DATE}, null, null, null, null, BUY_DATE+" asc", "0,1");
//        if(cursor != null) {
//            cursor.moveToFirst();
//        }
//        return cursor;
//    }
//
//    //
//    public Cursor getForSearch(String good_name, String buy_date, String status) {
//        String where = "";
//        String sql_goodname ="";
//        if (!good_name.isEmpty()){
//            sql_goodname = "("+GOOD_NAME + " LIKE '%" + good_name + "%' or "  + REMARK + " LIKE '%" + good_name + "%' " + ")";
//        }
//
//        String createtime =  "";
//
//        String orderby = "buy_date desc";
//
//        String start_date = "";
//        String end_date = "";
//
//        if(buy_date.isEmpty()){
//            buy_date = "全部";
//        }
//
//        switch(buy_date) {
//            case "本周":
//                start_date = Custom.getDateToString(Custom.getBeginDayOfWeek());
//                end_date = Custom.getDateToString(Custom.getEndDayOfWeek());
//                break;
//            case "本月":
//                start_date = Custom.getDateToString(Custom.getBeginDayOfMonth());
//                end_date = Custom.getDateToString(Custom.getEndDayOfMonth());
//                break;
//            case "三个月内":
//                start_date = Custom.getDateToString(Custom.getBeginDayOfThreeMonth());
//                end_date = Custom.getDateToString(Custom.getEndDayOfMonth());
//                break;
//            case "六个月内":
//                start_date = Custom.getDateToString(Custom.getBeginDayOfSixMonth());
//                end_date = Custom.getDateToString(Custom.getEndDayOfMonth());
//                break;
//            case "本年":
//                start_date = Custom.getDateToString(Custom.getBeginDayOfYear());
//                end_date = Custom.getDateToString(Custom.getEndDayOfYear());
//                break;
//            case "三年内":
//                start_date = Custom.getDateToString(Custom.getBeginDayOfThreeYear());
//                end_date = Custom.getDateToString(Custom.getEndDayOfYear());
//                break;
//            case "全部":
//                start_date = "1900-01-01";
//                end_date = "9909-12-31";
//                break;
//            default: break;
//        }
//
//        String sql_buy_date = "( "+BUY_DATE+">='" +start_date +"' and " + BUY_DATE + "<='" + end_date +"' )";
//
//        if(!good_name.isEmpty()){
//            where = sql_goodname + " and "+sql_buy_date;
//        } else{
//            where = sql_buy_date;
//        }
//
//
//
//        if(!status.isEmpty()){
//            String sql_status = "( "+STATUS+ " = '"+ status +"' )";
//            where =  where +" and "+sql_status;
//        }
//
//        Log.v("debug",where);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(true,
//                TABLE_NAME,
//                new String[] {RECORD_ID, GOOD_NAME, PRODUCT_DATE, END_DATE,BUY_DATE, STATUS, REMARK, HP},
//                where , null, null, null, orderby, null);
//        return cursor;
//    }
//
//
//    public Cursor getForSearchName(String good_name_or_remark) {
//        String where = "";
//        String sql_namemark ="";
//        if (!good_name_or_remark.isEmpty()){
//            where = "("+GOOD_NAME + " LIKE '%" + good_name_or_remark + "%' or "  + REMARK + " LIKE '%" + good_name_or_remark + "%' " + ")";
//        } else {
//            where = "1=1";
//        }
//
//        String orderby = "id asc";
//
//        Log.v("debug",where);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(true,
//                TABLE_NAME,
//                new String[] {RECORD_ID, GOOD_NAME, PRODUCT_DATE, END_DATE,BUY_DATE, STATUS, REMARK, HP},
//                where , null, null, null, orderby, null);
//        return cursor;
//    }
//
//    public long getForSearchCount(String good_name_or_remark) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME + " where " + GOOD_NAME + " LIKE '%" + good_name_or_remark + "%' or "  + REMARK + " LIKE '%" + good_name_or_remark + "%' ", null);
//        if (cursor.moveToNext()) {
//            return cursor.getLong(0);
//        }
//        return 0;
//    }
//
//    public Cursor getAllGoodName() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(false,TABLE_NAME, new String[] {GOOD_NAME}, null, null, null, null, null ,null);
//        return cursor;
//    }
//
//    public Cursor getForSort(String good_name_or_remark) {
//        String where = "";
//        String sql_namemark ="";
//
//        where = "("+GOOD_NAME + " LIKE '%" + good_name_or_remark + "%' or "  + REMARK + " LIKE '%" + good_name_or_remark + "%' " + ")";
//
//        String where_2 = "(     (   DATEDIFF ("+ END_DATE + " , DATE_FORMAT(CURDATE(), 'yyyy-MM-dd')   )/    ( DATEDIFF ( "+ END_DATE + " , "+ PRODUCT_DATE + ")   )  * 100      )";
//        Log.v("v_debug",where_2);
//        String orderby = "id asc";
//
//        Log.v("debug",where);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(true,
//                TABLE_NAME,
//                new String[] {RECORD_ID, GOOD_NAME, PRODUCT_DATE, END_DATE,BUY_DATE, STATUS, REMARK, HP},
//                where , null, null, null, orderby, null);
//        return cursor;
//    }
//
//
//    public long insert(String good_name, String product_date, String end_date, String buy_date, String status, String remark){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(GOOD_NAME, good_name);
//        cv.put(PRODUCT_DATE, product_date);
//        cv.put(END_DATE, end_date);
//        cv.put(BUY_DATE, buy_date);
//
//        cv.put(STATUS, status);
//        if(remark.equals(null)){
//            remark = "";
//        }
//        cv.put(REMARK, remark);
//
//        long row = db.insert(TABLE_NAME, null, cv);
//        Log.v("v_insertDB",good_name +"/"+product_date+"/"+end_date+"/"+ buy_date+"/"+ status);
//        return row;
//    }
//
//    public void delete(int id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String where = RECORD_ID + " = ?";
//        String[] whereValue ={ Integer.toString(id) };
//        db.delete(TABLE_NAME, where, whereValue);
//    }
//
//    public void deleteAll(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME, null, null);
//
//        //set ai=0
//        String where = "name = ?";
//        String[] whereValue = { TABLE_NAME };
//        ContentValues cv = new ContentValues();
//        cv.put("seq", 0);
//        db.update("sqlite_sequence", cv, where, whereValue);
//    }
//
//    public void update(int id, String good_name, String product_date, String end_date, String buy_date, String status, String remark){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String where = RECORD_ID + " = ?";
//        String[] whereValue = { Integer.toString(id) };
//        ContentValues cv = new ContentValues();
//        cv.put(GOOD_NAME, good_name);
//        if(product_date.equals(null)){
//            product_date = "";
//        }
//
//        cv.put(PRODUCT_DATE, product_date);
//        cv.put(END_DATE, end_date);
//        cv.put(BUY_DATE, buy_date);
//
//        cv.put(STATUS, status);
//        if(remark.equals(null)){
//            remark = "";
//        }
//        cv.put(REMARK, remark);
//        db.update(TABLE_NAME, cv, where, whereValue);
//    }
//
//    public void update_status(int id, String status){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String where = RECORD_ID + " = ?";
//        String[] whereValue = { Integer.toString(id) };
//        ContentValues cv = new ContentValues();
//        cv.put(STATUS, status);
//        db.update(TABLE_NAME, cv, where, whereValue);
//    }




}
