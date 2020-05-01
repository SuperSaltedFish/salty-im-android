package me.zhixingye.im.service;

import android.content.ContentValues;
import android.database.Cursor;

import me.zhixingye.im.database.AbstractDao;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */
public interface SQLiteService extends BasicService {

    <T extends AbstractDao> T createDao(Class<T> c);

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
