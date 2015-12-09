package com.aspirecn.hop.sample2.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yinghuihong on 15/12/8.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("create table");
        db.execSQL("create table " + ContentData.UserTableData.TABLE_NAME
                + "(" + ContentData.UserTableData._ID
                + " INTEGER PRIMARY KEY autoincrement,"
                + ContentData.UserTableData.NAME + " varchar(20),"
                + ContentData.UserTableData.TITLE + " varchar(20),"
                + ContentData.UserTableData.DATE_ADDED + " long,"
                + ContentData.UserTableData.SEX + " boolean)" + ";");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
