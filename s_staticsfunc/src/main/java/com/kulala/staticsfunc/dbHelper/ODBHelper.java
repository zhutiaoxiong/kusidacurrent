package com.kulala.staticsfunc.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.SystemMe;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/12/28.
 * NULL：空值
 * integer： 带符号的整数，具体取决于存入数字的范围大小
 * smallint 16位整数
 * int 32位整数
 * <p>
 * real：浮点数，存储为8-bytes的浮点数
 * double 64位浮点数
 * float 32位浮点数
 * <p>
 * text：字符串文本
 * blob：二进制对象
 * char(n) n不能炒作254
 * varchar(n) n不能超过4000
 * <p>
 * date
 * time
 * limestamp
 String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("brandsList");
 this.brandsList = DataBrands.fromJsonArray(ODBHelper.convertJsonArray(result));
 */

public class ODBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME_WARNING = "warnings";
    private static      String DB_NAME            = "kusida.kusida.db";
    private static      int    DB_VERSION         = 2;
    //=============================================
    private static ODBHelper      _instance;
    public static ODBHelper getInstance(Context context) {
        if (_instance == null)
            _instance = new ODBHelper(context);
        return _instance;
    }
    public ODBHelper(Context context) {
        //第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
        super(context, DB_NAME, null, DB_VERSION);
//        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("commonShare");
//        JsonObject share = ODBHelper.convertJsonObject(result);
//
//        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryCommonInfo("isOpenGesture");
//        isOpenGesture = ODBHelper.queryResult2Integer(result,0);
//
//        ODBHelper.getInstance(GlobalContext.getContext()).changeCommonInfo("commonShare", ODBHelper.convertString(share));
//
//        String result = ODBHelper.getInstance(GlobalContext.getContext()).queryUserInfo(userId,"simpleArr");
//        JsonObject obj1 = ODBHelper.convertJsonObject(result);
//        ODBHelper.getInstance(GlobalContext.getContext()).changeUserInfo(userId,"simpleArr", ODBHelper.convertString(obj1));
    }
    //=============================================
    private void doCreateTable(SQLiteDatabase dbb){
        //警告消息列表
        dbb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_WARNING + "(createTime TEXT PRIMARY KEY,carId TEXT,alertType TEXT,content TEXT,alertId TEXT,isNew TEXT)");
        //用户表 users
        dbb.execSQL("CREATE TABLE IF NOT EXISTS users (uid LONG PRIMARY KEY,token varchar(36))");
        //用户信息表 userinfo
        dbb.execSQL("CREATE TABLE IF NOT EXISTS userinfo (uidkey LONG PRIMARY KEY, valuee TEXT)");//uid+"UIOI,UIOI"+key
        //通用信息表 commoninfo
        dbb.execSQL("CREATE TABLE IF NOT EXISTS commoninfo (keyy TEXT PRIMARY KEY, valuee TEXT)");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//         if (BuildConfig.DEBUG) Log.e("DBHelper", "onCreate 从没调用过");
        doCreateTable(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//         if (BuildConfig.DEBUG) Log.e("DBHelper", "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_WARNING);
        db.execSQL("DROP TABLE IF EXISTS " + "users");
        db.execSQL("DROP TABLE IF EXISTS " + "userinfo");
        db.execSQL("DROP TABLE IF EXISTS " + "commoninfo");
        doCreateTable(db);
//        db.execSQL("ALTER TABLE person ADD phone VARCHAR(12)"); //往表中增加一列
//        db.execSQL("insert into person(name, age) values('炸死特', 4)");
//        db.execSQL("insert into person(name, age) values(?,?)", new Object[]{"炸死特", 4});
//        db.close();
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
    /**只能是单主渐，联合主渐无用*/
    @Override
    public void onOpen(SQLiteDatabase dbb) {
//         if (BuildConfig.DEBUG) Log.e("DBHelper", "onOpen");
        super.onOpen(dbb);
    }
    public void onExit() {
        SQLiteDatabase db = getWritableDatabase();
        if (db != null) db.close();
    }
    public void execSQL(String sql) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }
    public Cursor query(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        return c;
    }
    //====================================增==========================================
    public void changeUserToken(long uid, String token) {
        if (uid == 0) return;
        SQLiteDatabase db = getWritableDatabase();
        //replace 没有就插，有就改
        ContentValues cv = new ContentValues();
        cv.put("uid", uid);
        cv.put("token", token);
        db.replace("users", "uid", cv);
//         if (BuildConfig.DEBUG) Log.e("DBHelper", "changeUserToken:" + uid + "  " + token);
//        db.close();
    }
    public void changeUserInfo(long uid, String key, String value) {
        if (uid == 0 || key == null || key.length() == 0) return;
        SQLiteDatabase db = getWritableDatabase();
        //replace 没有就插，有就改
        ContentValues cv = new ContentValues();
        cv.put("uidkey", uid+"UIOI,UIOI"+key);
        cv.put("valuee", value);
        db.replace("userinfo", "uidkey", cv);

//        String sql = "REPLACE INTO userinfo (uid,keyy,valuee) VALUES (" + uid + ",'" + key + "','" + value + "');";
//        db.execSQL(sql);
//         if (BuildConfig.DEBUG) Log.e("DBHelper", "changeUserInfo:" + key + "  " + value);
//        db.close();
    }
    public void changeCommonInfo(String key, String value) {
        if (key == null || key.length() == 0) return;
//         if (BuildConfig.DEBUG) Log.e("DBHelper", "changeCommonInfo:" + key + "   " + value);
        SQLiteDatabase db = getWritableDatabase();
        //replace 没有就插，有就改
        ContentValues cv = new ContentValues();
        cv.put("keyy", key);
        cv.put("valuee", value);
        db.replace("commoninfo", "keyy", cv);
//        db.close();
    }

    //====================================查==========================================
    public String queryUserToken(long uid) {
        if (uid == 0) return null;
        Cursor cursor = null;
        try {
            String token = null;
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query("users", new String[]{"token"}, "uid=?", new String[]{"" + uid}, null, null, null);
            while (cursor.moveToNext()) {
                token = cursor.getString(0);
//                 if (BuildConfig.DEBUG) Log.e("DBHelper", "queryUserToken:" + uid + "  " + token);
            }
            cursor = null;
//            db.close();
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }finally {
            if(cursor!=null)cursor.close();
        }
    }
    public String queryUserInfo(long uid, String key) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query("userinfo", new String[]{"valuee"}, "uidkey=?", new String[]{uid+"UIOI,UIOI"+key}, null, null, null);
            String result = "";
            while (cursor.moveToNext()) {
                result = cursor.getString(0);
//                 if (BuildConfig.DEBUG) Log.e("DBHelper", "queryUserInfo:" + key + "  " + result);
            }
//            db.close();
            cursor = null;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }finally {
            if(cursor!=null)cursor.close();
        }
    }
    public String queryCommonInfo(String key) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query("commoninfo", new String[]{"valuee"}, "keyy=?", new String[]{"" + key}, null, null, null);
            String result = "";
            while (cursor.moveToNext()) {
                result = cursor.getString(0);
//                 if (BuildConfig.DEBUG) Log.e("DBHelper", "queryCommonInfo:" + key + "   " + result);
            }
//            db.close();
//            cursor = null;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }finally {
            if(cursor!=null)cursor.close();
        }
    }
    //=========================static==========================================
    public static JsonObject convertJsonObject(String json){
        if(json == null || json.equals(""))return null;
        try {
            Gson       gson   = new Gson();
            JsonObject result = gson.fromJson(json, JsonObject.class);
            return result;
        }catch (Exception e){
            return null;
        }
    }
    public static JsonArray convertJsonArray(String json){
        if(json == null || json.equals(""))return null;
        try {
            Gson       gson   = new Gson();
            JsonArray result = gson.fromJson(json, JsonArray.class);
            return result;
        }catch (Exception e){
            return null;
        }
    }
    public static String convertString(JsonObject jsonObject){
        String result = null;
        try{
            result = new String(jsonObject.toString().getBytes(SystemMe.UTF8), SystemMe.UTF8);
        } catch (UnsupportedEncodingException e) {
//             if (BuildConfig.DEBUG) Log.e("DBHelper","convertString JsonObject->不支持的编码:"+jsonObject.toString());
        }
        return result;
    }
    public static String convertString(JsonArray jsonArray){
        String result = null;
        try{
            result = new String(jsonArray.toString().getBytes(SystemMe.UTF8), SystemMe.UTF8);
        } catch (UnsupportedEncodingException e) {
//             if (BuildConfig.DEBUG) Log.e("DBHelper","convertString JsonArray->不支持的编码"+jsonArray.toString());
        }
        return result;
    }
    public static boolean queryResult2boolean(String result){
        if(TextUtils.isEmpty(result))return false;
        return Boolean.valueOf(result);
    }
    public static int queryResult2Integer(String result,int defaultValue){
        if(TextUtils.isEmpty(result))return defaultValue;
        return Integer.valueOf(result);
    }

}