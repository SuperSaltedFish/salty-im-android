package me.zhixingye.im.service;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by zhixingye on 2020年02月02日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public interface DatabaseService {
    ReadWriteHelper getReadWriteHelper();

    interface ReadWriteHelper {
        ReadableDatabase openReadableDatabase();

        WritableDatabase openWritableDatabase();
    }

    interface ReadableDatabase extends AutoCloseable {
        Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
    }

    interface WritableDatabase extends AutoCloseable {
        long insert(String table, String nullColumnHack, ContentValues values);

        int delete(String table, String whereClause, String[] whereArgs);

        int update(String table, ContentValues values, String whereClause, String[] whereArgs);

        long replace(String table, String nullColumnHack, ContentValues initialValues);

        void beginTransaction();

        void beginTransactionNonExclusive();

        void setTransactionSuccessful();

        void endTransaction();
    }

}
