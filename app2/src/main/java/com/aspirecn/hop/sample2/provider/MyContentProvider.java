package com.aspirecn.hop.sample2.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import static com.aspirecn.hop.sample2.provider.ContentData.ContentUri.CONTENT_TYPE;
import static com.aspirecn.hop.sample2.provider.ContentData.ContentUri.CONTENT_TYPE_ITEM;
import static com.aspirecn.hop.sample2.provider.ContentData.ContentUri.TEACHER;
import static com.aspirecn.hop.sample2.provider.ContentData.ContentUri.TEACHERS;
import static com.aspirecn.hop.sample2.provider.ContentData.ContentUri.uriMatcher;

/**
 * 参考：http://www.2cto.com/kf/201404/296974.html
 */
public class MyContentProvider extends ContentProvider {

    private DBOpenHelper dbOpenHelper;

    @Override
    public boolean onCreate() {
        dbOpenHelper = new DBOpenHelper(getContext(), ContentData.USERS_TABLE_NAME, null, ContentData.DATABASE_VERSION);
        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                count = db.delete("teacher", selection, selectionArgs);
                break;
            case TEACHER:
                // 下面的方法用于从URI中解析出id，对这样的路径content://hb.android.teacherProvider/teacher/10
                // 进行解析，返回值为10
                long personId = ContentUris.parseId(uri);
                String where = "_ID=" + personId;   // 删除指定id的记录
                where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";   // 把其它条件附加上
                count = db.delete("teacher", where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        db.close();
        return count;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        //获得一个可写的数据库引用，如果数据库不存在，则根据onCreate的方法里创建；
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long id = 0;

        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                id = db.insert("teacher", null, values);    // 返回的是记录的行号，主键为int，实际上就是主键值
                return ContentUris.withAppendedId(uri, id);
            case TEACHER:
                id = db.insert("teacher", null, values);
                String path = uri.toString();
                return Uri.parse(path.substring(0, path.lastIndexOf("/")) + id); // 替换掉id
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                return db.query("teacher", projection, selection, selectionArgs, null, null, sortOrder);
            case TEACHER:
                // 进行解析，返回值为10
                long personid = ContentUris.parseId(uri);
                String where = "_ID=" + personid;// 获取指定id的记录
                where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
                return db.query("teacher", projection, where, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                count = db.update("teacher", values, selection, selectionArgs);
                break;
            case TEACHER:
                // 下面的方法用于从URI中解析出id，对这样的路径content://com.ljq.provider.personprovider/person/10
                // 进行解析，返回值为10
                long personid = ContentUris.parseId(uri);
                String where = "_ID=" + personid;// 获取指定id的记录
                where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
                count = db.update("teacher", values, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        db.close();
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                return CONTENT_TYPE;
            case TEACHER:
                return CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

}
