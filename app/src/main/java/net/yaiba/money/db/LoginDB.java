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
		//RECORD_TYPE 支出-0，收入-1
		String sql_record_master = "CREATE TABLE "
				+ TABLE_NAME_RECORD
				+ " (" + RECORD_ID + " INTEGER primary key autoincrement, "
				+ RECORD_CATEGORY_ID + " INTEGER, "
				+ RECORD_PAY_ID +" INTEGER, "
				+ RECORD_MEMBER_ID +" INTEGER, "
				+ RECORD_TYPE +" INTEGER, "
				+ AMOUNTS +" NVARCHAR(50), "
				+ REMARK +" TEXT NULL, "
				+ RECORD_CREATE_TIME +" NVARCHAR(100) "
				+");";
		db.execSQL(sql_record_master);

		//DB:category_master
		//PID 父类-0，子类-父类id
		//CATEGORY_TYPE 支出-0，收入-1
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

		//set ai=0
		String where = "name = ?";
		String[] whereValue = { TABLE_NAME_CATEGORY };
		ContentValues cv = new ContentValues();
		cv.put("seq", 1);
		db.update("sqlite_sequence", cv, where, whereValue);

		String sql_category_master_init = "INSERT INTO "	+TABLE_NAME_CATEGORY+ " ("+ CATEGORY_ID + ", "+ PID + ","+ CATEGORY_NAME+","+CATEGORY_FAVORITE+","+CATEGORY_RANK+","+CATEGORY_TYPE+")"
				+ "VALUES " +
				//支出，大类
				" (1,0,'餐饮','0','0',0),"+//1
				" (2,0,'交通','0','1',0),"+//2
				" (3,0,'购物','0','2',0),"+//3
				" (4,0,'娱乐','0','3',0),"+//4
				" (5,0,'医教','0','4',0),"+//5
				" (6,0,'居家','0','5',0),"+//6
				" (7,0,'投资','0','6',0),"+//7
				" (8,0,'人情','0','7',0),"+//8
				" (9,0,'生意','0','7',0),"+//9
				" (10,0,'其他','0','8',0);";
		db.execSQL(sql_category_master_init);
		String sql_category_master_data_init = "INSERT INTO "	+TABLE_NAME_CATEGORY+ " ( "+ CATEGORY_ID + ", "+ PID + ","+ CATEGORY_NAME+","+CATEGORY_FAVORITE+","+CATEGORY_RANK+","+CATEGORY_TYPE+")"
				+ "VALUES " +
				//支出，小类，1-9
				" (11,1,'水果','0','0',0),"+
				" (12,1,'饮料雪糕','0','1',0),"+
				" (13,1,'早餐','0','2',0),"+
				" (14,1,'午餐','0','3',0),"+
				" (15,1,'晚餐','0','4',0),"+
				" (16,1,'宵夜','0','5',0),"+
				" (17,1,'零食','0','6',0),"+
				" (18,1,'买菜原料','0','7',0),"+
				" (19,1,'油盐酱醋','0','8',0),"+
				" (20,1,'餐饮其他','0','9',0),"+
				" (21,2,'班车','0','1',0),"+
				" (22,2,'打车','0','2',0),"+
				" (23,2,'公交','0','3',0),"+
				" (24,2,'加油','0','4',0),"+
				" (25,2,'停车费','0','5',0),"+
				" (26,2,'地铁','0','6',0),"+
				" (27,2,'火车','0','7',0),"+
				" (28,2,'长途汽车','0','8',0),"+
				" (29,2,'飞机','0','9',0),"+
				" (30,2,'自行车','0','10',0),"+
				" (31,2,'船舶','0','11',0),"+
				" (32,2,'保养维修','0','12',0),"+
				" (33,2,'过路过桥','0','13',0),"+
				" (34,2,'罚款赔偿','0','14',0),"+
				" (35,2,'车款车贷','0','15',0),"+
				" (36,2,'车险','0','16',0),"+
				" (37,2,'驾照费用','0','17',0),"+
				" (38,2,'交通其他','0','18',0),"+
				" (39,3,'汽车用品','0','19',0),"+
				" (40,3,'网站','0','20',0),"+
				" (41,3,'服饰鞋包','0','21',0),"+
				" (42,3,'家居百货','0','22',0),"+
				" (43,3,'宝宝用品','0','23',0),"+
				" (44,3,'化妆护肤','0','24',0),"+
				" (45,3,'烟酒','0','25',0),"+
				" (46,3,'电子数码','0','26',0),"+
				" (47,3,'文具玩具','0','27',0),"+
				" (48,3,'报刊书籍','0','28',0),"+
				" (49,3,'珠宝首饰','0','29',0),"+
				" (50,3,'家居家纺','0','30',0),"+
				" (51,3,'保健用品','0','31',0),"+
				" (52,3,'电脑','0','32',0),"+
				" (53,3,'摄影文印','0','33',0),"+
				" (54,3,'购物其他','0','34',0),"+
				" (55,4,'景点门票','0','1',0),"+
				" (56,4,'参团费','0','2',0),"+
				" (57,4,'住宿','0','3',0),"+
				" (58,4,'旅游度假','0','4',0),"+
				" (59,4,'电影','0','5',0),"+
				" (60,4,'网游电玩','0','6',0),"+
				" (61,4,'麻将棋牌','0','7',0),"+
				" (62,4,'洗浴足浴','0','8',0),"+
				" (63,4,'运动健身','0','9',0),"+
				" (64,4,'花鸟宠物','0','10',0),"+
				" (65,4,'聚会玩乐','0','11',0),"+
				" (66,4,'茶酒咖啡','0','12',0),"+
				" (67,4,'卡拉OK','0','13',0),"+
				" (68,4,'歌舞演出','0','14',0),"+
				" (69,4,'电视','0','15',0),"+
				" (70,4,'娱乐其他','0','16',0),"+
				" (71,5,'医疗用品','0','1',0),"+
				" (72,5,'挂号门诊','0','2',0),"+
				" (73,5,'养生保健','0','3',0),"+
				" (74,5,'住院费','0','4',0),"+
				" (75,5,'养老院','0','5',0),"+
				" (76,5,'学杂教材','0','6',0),"+
				" (77,5,'培训考试','0','7',0),"+
				" (78,5,'幼儿教育','0','8',0),"+
				" (79,5,'学费','0','9',0),"+
				" (80,5,'家教补习','0','10',0),"+
				" (81,5,'出国留学','0','11',0),"+
				" (82,5,'助学贷款','0','12',0),"+
				" (83,5,'医教其他','0','13',0),"+
				" (84,6,'手机电话','0','1',0),"+
				" (85,6,'水电燃气','0','2',0),"+
				" (86,6,'美发美容','0','3',0),"+
				" (87,6,'住宿房租','0','4',0),"+
				" (88,6,'材料建材','0','5',0),"+
				" (89,6,'房款贷款','0','6',0),"+
				" (90,6,'快递邮政','0','7',0),"+
				" (91,6,'电脑宽带','0','8',0),"+
				" (92,6,'家政服务','0','9',0),"+
				" (93,6,'物业','0','10',0),"+
				" (94,6,'税费手续费','0','11',0),"+
				" (95,6,'保险费','0','12',0),"+
				" (96,6,'消费贷款','0','13',0),"+
				" (97,6,'婚庆摄影','0','14',0),"+
				" (98,6,'漏记款','0','15',0),"+
				" (99,6,'生活其他','0','16',0),"+
				" (100,7,'彩票','0','1',0),"+
				" (101,7,'利息支出','0','2',0),"+
				" (102,7,'保险','0','3',0),"+
				" (103,7,'出资','0','4',0),"+
				" (104,7,'基金','0','5',0),"+
				" (105,7,'股票','0','6',0),"+
				" (106,7,'P2P','0','7',0),"+
				" (107,7,'余额宝','0','8',0),"+
				" (108,7,'理财产品','0','9',0),"+
				" (109,7,'投资贷款','0','10',0),"+
				" (110,7,'银行存款','0','11',0),"+
				" (111,7,'证券期货','0','12',0),"+
				" (112,7,'外汇','0','13',0),"+
				" (113,7,'贵金属','0','14',0),"+
				" (114,7,'收藏品','0','15',0),"+
				" (115,7,'投资其他','0','16',0),"+
				" (116,8,'借出款','0','1',0),"+
				" (117,8,'礼金红包','0','2',0),"+
				" (118,8,'物品','0','3',0),"+
				" (119,8,'孝敬','0','4',0),"+
				" (120,8,'请客','0','5',0),"+
				" (121,8,'给予','0','6',0),"+
				" (122,8,'代付款','0','7',0),"+
				" (123,8,'慈善捐款','0','8',0),"+
				" (124,8,'人情其他','0','9',0),"+
				" (125,9,'进货采购','0','1',0),"+
				" (126,9,'人工支出','0','2',0),"+
				" (127,9,'材料辅料','0','3',0),"+
				" (128,9,'办公费用','0','4',0),"+
				" (129,9,'交通运输','0','5',0),"+
				" (130,9,'工程付款','0','6',0),"+
				" (131,9,'运营费','0','7',0),"+
				" (132,9,'会务费','0','8',0),"+
				" (133,9,'营销广告','0','9',0),"+
				" (134,9,'店面租金','0','10',0),"+
				" (135,9,'注册登记','0','11',0),"+
				" (136,9,'生意其他','0','12',0),"+
				" (137,10,'其他','0','1',0),"+

				//收入，大类
				" (138,0,'返款','0','0',1),"+
				" (139,0,'工资薪水','0','1',1),"+
				" (140,0,'奖金','0','2',1),"+
				" (141,0,'兼职外快','0','3',1),"+
				" (142,0,'福利补贴','0','4',1),"+
				" (143,0,'生活费','0','5',1),"+
				" (144,0,'公积金','0','6',1),"+
				" (145,0,'退款返款','0','7',1),"+
				" (146,0,'礼金','0','8',1),"+
				" (147,0,'红包','0','9',1),"+
				" (148,0,'赔付款','0','10',1),"+
				" (149,0,'漏记款','0','11',1),"+
				" (150,0,'报销款','0','12',1),"+
				" (151,0,'利息','0','13',1),"+
				" (152,0,'余额宝','0','14',1),"+
				" (153,0,'基金','0','15',1),"+
				" (154,0,'分红','0','16',1),"+
				" (155,0,'租金','0','17',1),"+
				" (156,0,'股票','0','18',1),"+
				" (157,0,'销售款','0','19',1),"+
				" (158,0,'应收款','0','20',1),"+
				" (159,0,'营业收入','0','21',1),"+
				" (160,0,'工程款','0','22',1),"+
				" (161,0,'其他','0','23',1);";

		db.execSQL(sql_category_master_data_init);


		//DB:pay_master
		String sql_pay_master = "CREATE TABLE "
				+ TABLE_NAME_PAY
				+ " (" + PAY_ID + " INTEGER primary key autoincrement, "
				+ PAY_NAME +" NVARCHAR(100), "
				+ PAY_FAVORITE +" NVARCHAR(10) default '0', "
				+ PAY_RANK +" NVARCHAR(10) "
				+");";
		db.execSQL(sql_pay_master);


		String sql_pay_master_init = "INSERT INTO "	+TABLE_NAME_PAY+ " ( "+ PAY_ID + ","+ PAY_NAME+","+PAY_FAVORITE+","+PAY_RANK+")"
				+ "VALUES " +
				" (0,'现金','0','0'),"+
				" (1,'信用卡','0','0'),"+
				" (2,'储蓄卡','0','0'),"+
				" (3,'支付宝','0','0'),"+
				" (4,'微信支付','0','0'),"+
				" (5,'京东白条','0','0');";

		db.execSQL(sql_pay_master_init);


		//DB:member_master
		String sql_member_master = "CREATE TABLE "
				+ TABLE_NAME_MEMBER
				+ " (" + MEMBER_ID + " INTEGER primary key autoincrement, "
				+ MEMBER_NAME +" NVARCHAR(100), "
				+ MEMBER_FAVORITE +" NVARCHAR(10) default '0', "
				+ MEMBER_RANK +" NVARCHAR(10) "
				+");";
		db.execSQL(sql_member_master);

		String sql_init = "INSERT INTO "	+TABLE_NAME_MEMBER+ " ( "+ MEMBER_NAME + ")"	+ "VALUES ('自己'),('宝贝'),('孩子'),('父母'),('朋友'),('同事'),('其他');";
		db.execSQL(sql_init);


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
        		 //deleteOthers(db, cursor.getInt(0));
        		 return cursor.getInt(0);
        	 }
         }
         return -1;
     }

	public int isLoginUsePassword() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(true, TABLE_NAME_LOGIN, new String[] {LOGIN_ID, LOGIN_TYPE}, LOGIN_TYPE + "='none_password'", null, null, null, null, null);
		if(cursor.getCount()>0) {
			cursor.moveToFirst();
			return cursor.getInt(0);
		}
		return -1;
	}



	public void updateLoginType(String type){
		SQLiteDatabase db = this.getWritableDatabase();
		String wv = "";
		if("normal".equals(type)){
			wv = "none_password";
		} else {
			wv = "normal";
		}
		String where = LOGIN_TYPE + " = ?";
		String[] whereValue = { wv };
		ContentValues cv = new ContentValues();

		cv.put(LOGIN_TYPE, type);
		db.update(TABLE_NAME_LOGIN, cv, where, whereValue);
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
