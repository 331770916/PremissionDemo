package com.basecommon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BaseTable {

    static SQLiteDatabase getDatabase(Context context, String db_file_name){
        return DbUtil.getDB(context, db_file_name);
    }

}
