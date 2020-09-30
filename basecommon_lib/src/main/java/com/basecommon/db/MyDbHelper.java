package com.basecommon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.lib.weight.log_collector.CrashHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyDbHelper extends SQLiteOpenHelper {

    private static int version = 6;
    private static final String TAG = "MyDbHelper";
    private Context mContext;
    private static SQLiteDatabase sqLiteDatabase;
    private static final String NEWS_READ = "CREATE TABLE NEWS_READ (READ_ID varchar(50) UNIQUE)";
    private static final String STOCK_NEWS = "CREATE TABLE STOCK_NEWS( ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,CONTENT BLOB,NEWS_FLAG INT,NEWS_ID INT,STOCK_CODE VARCHAR(10));";
    private java.lang.String FINANCIAL_SQL = "CREATE TABLE FINANCIAL(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,PRODUCT_CODE VARCHAR,PRODUCT_TYPE VARCHAR,PRODUCT_NAME VARCHAR,SCHEMAID VARCHAR)";

    public MyDbHelper(Context context, String db_file_name) {
//        super(context, Environment.getExternalStorageDirectory()+"/"+ConstantUtil.DB_FILE_NAME, null, version);
        super(context, db_file_name, null, version);
        mContext = context;
    }

    public SQLiteDatabase getDb(){
        if (sqLiteDatabase==null)
            sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        sqLiteDatabase=db;
        executeAssetsSQL(db, "sql/crebas2.sql");
        initOriginData(db);
        compatiblePreDb();
    }

    private void compatiblePreDb() {
        try {
            String packageName = mContext.getPackageName();
            if (!new File("/data/data/"+packageName+"/mydb.db").exists()){
                return;
            }
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/"+packageName+"/mydb.db", null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery("select * from PUB_USERS where USERID=1", null);

//            if (cursor != null && cursor.moveToNext()) {
//                entity.setUserId(CursorUtils.getString(cursor, "USERID"));
//                entity.setUsername(CursorUtils.getString(cursor, "USERNAME"));
////                entity.setPickname(CursorUtils.getString(cursor, "PICKNAME"));
//                String pickName;
//                if(TextUtils.isEmpty(pickName= CursorUtils.getString(cursor, "PICKNAME"))){
//                    pickName= CursorUtils.getString(cursor, "SCNO");
//                }
//                entity.setPickname(pickName);
//                entity.setMobile(KeyEncryptionUtils.getInstance().localDecryptMobile(CursorUtils.getString(cursor, "MOBILE")));
//                entity.setPlugins(CursorUtils.getString(cursor, "PIUGINS"));
//                entity.setRefreshTime(CursorUtils.getString(cursor, "EXCOLUMN00"));
//                entity.setScno(CursorUtils.getString(cursor, "REGISTERID"));
//                if(TextUtils.isEmpty(entity.getScno()))
//                    entity.setScno(CursorUtils.getString(cursor, "SCNO"));
//                entity.setKeyboard(CursorUtils.getString(cursor, "KEYBOARD"));
//                entity.setCertification(CursorUtils.getString(cursor, "CERTIFICATION"));
//
//                //存资金账号
//                List<UserEntity> list=new ArrayList<>();
//                UserEntity userEntity = new UserEntity();
//                userEntity.setTradescno(CursorUtils.getString(cursor, "TRADESCNO"));
//                list.add(userEntity);
//                List<UserEntity> userEntities = KeyEncryptionUtils.localDecryptTradescno(list);
//                if (userEntities!=null&&userEntities.size()>0&&userEntities.get(0)!=null)
//                    entity.setTradescno(userEntities.get(0).getTradescno());
//
//                entity.setUsertype(CursorUtils.getString(cursor, "EXCOLUMN01"));
//            }
//            if (cursor!=null)
//                cursor.close();
//
//            Db_PUB_USERS.updateUserById(entity.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initOriginData(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", 1);
        db.insert("USER", null, contentValues);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 6){
            //增加自选股新闻阅读状态表
            db.execSQL(NEWS_READ);
            createStockNewsTab(db);
        } else if (oldVersion < 7) {
            db.execSQL(FINANCIAL_SQL);
        }
    }

    private void createStockNewsTab(SQLiteDatabase db){
        db.execSQL(STOCK_NEWS);
    }

    private void executeAssetsSQL(SQLiteDatabase db, String schemaName) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(mContext.getAssets()
                    .open(schemaName)));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            String[] sqls = sb.toString().split(";");
            for (String sql : sqls) {
                db.execSQL(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
            CrashHandler.getInstance(mContext).handleException(e);
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
                CrashHandler.getInstance(mContext).handleException(e);
            }
        }
    }

}
