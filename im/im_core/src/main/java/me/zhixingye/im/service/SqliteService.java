package me.zhixingye.im.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import me.zhixingye.im.database.SQLiteOpenHelperImpl;


/**
 * Created by zhixingye on 2020年01月28日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SqliteService extends BasicService {

    private ReadWriteHelper mReadWriteHelper;
    private SQLiteOpenHelper mOpenHelper;

    public SqliteService(Context context, String name, int version) {
        mOpenHelper = new SQLiteOpenHelperImpl(context, name, version);
        mReadWriteHelper = new ReadWriteHelperImpl(mOpenHelper);
    }

    public ReadWriteHelper getReadWriteHelper() {
        return mReadWriteHelper;
    }

    private class ReadWriteHelperImpl implements ReadWriteHelper {

        private SQLiteOpenHelper mOpenHelper;
        private SQLiteDatabase mReadableDatabase;
        private SQLiteDatabase mWritableDatabase;

        private final ReentrantReadWriteLock mReadWriteLock;
        private final Lock mReadLock;
        private final Lock mWriteLock;

        private boolean isTransactionMode;

        ReadWriteHelperImpl(SQLiteOpenHelper openHelper) {
            mOpenHelper = openHelper;
            mReadWriteLock = new ReentrantReadWriteLock(true);
            mReadLock = mReadWriteLock.readLock();
            mWriteLock = mReadWriteLock.writeLock();
            mWritableDatabase.beginTransactionNonExclusive();
        }

        @Override
        long insert(String table, String nullColumnHack, ContentValues values) {
            mWriteLock.lock();
            try {
                return mWritableDatabase.insert(table, nullColumnHack, values);
            } finally {
                mWriteLock.unlock();
            }
        }

        @Override
        int delete(String table, String whereClause, String[] whereArgs) {
            mWriteLock.lock();
            try {
                return mWritableDatabase.delete(table, whereClause, whereArgs);
            } finally {
                mWriteLock.unlock();
            }
        }

        @Override
        int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
            mWriteLock.lock();
            try {
                return mWritableDatabase.update(table, values, whereClause, whereArgs);
            } finally {
                mWriteLock.unlock();
            }
        }

        @Override
        long replace(String table, String nullColumnHack, ContentValues initialValues) {
            mWriteLock.lock();
            try {
                return mWritableDatabase.replace(table, nullColumnHack, initialValues);
            } finally {
                mWriteLock.unlock();
            }
        }

        @Override
        void query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, QueryCallback callback) {
            if (callback == null) {
                return;
            }
            mReadLock.lock();
            try {
                Cursor cursor = mReadableDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
                callback.onQuery(cursor);
                cursor.close();
            } finally {
                mReadLock.unlock();
            }
        }

        @Override
        void beginTransaction() {
            mWriteLock.lock();
            try {
                mWritableDatabase.beginTransaction();
            } catch (Exception e) {
                mWriteLock.unlock();
            }
        }

        @Override
        void beginTransactionNonExclusive() {
            mWriteLock.lock();
            try {
                mWritableDatabase.beginTransactionNonExclusive();
            } catch (Exception e) {
                mWriteLock.unlock();
            }
        }

        @Override
        void setTransactionSuccessful() {
            mWritableDatabase.setTransactionSuccessful();
        }

        @Override
        void endTransaction() {
            try {
                mWritableDatabase.endTransaction();
            } finally {
                mWriteLock.unlock();
            }
        }

        @Override
        long insertByTransaction(String table, String nullColumnHack, ContentValues values) {
            if (!isTransactionMode) {
                throw new RuntimeException("请先开始事务，再执行插入操作");
            }
            return mWritableDatabase.insert(table, nullColumnHack, values);
        }

        @Override
        int deleteByTransaction(String table, String whereClause, String[] whereArgs) {
            if (!isTransactionMode) {
                throw new RuntimeException("请先开始事务，再执行删除操作");
            }
            return mWritableDatabase.delete(table, whereClause, whereArgs);
        }

        @Override
        int updateByTransaction(String table, ContentValues values, String whereClause, String[] whereArgs) {
            if (!isTransactionMode) {
                throw new RuntimeException("请先开始事务，再执行更新操作");
            }
            return mWritableDatabase.update(table, values, whereClause, whereArgs);
        }

        @Override
        long replaceByTransaction(String table, String nullColumnHack, ContentValues initialValues) {
            if (!isTransactionMode) {
                throw new RuntimeException("请先开始事务，再执行替换操作");
            }
            return mWritableDatabase.replace(table, nullColumnHack, initialValues);
        }
    }

    private class ReadableDatabaseImpl implements ReadableDatabase {

        private ReadWriteHelperImpl mReadWriteHelper;
        private SQLiteDatabase mReadableDatabase;

        public ReadableDatabaseImpl(ReadWriteHelperImpl readWriteHelper, SQLiteDatabase readableDatabase) {
            mReadWriteHelper = readWriteHelper;
            mReadableDatabase = readableDatabase;
        }


        @Override
        public void query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, QueryCallback callback) {
            if (callback == null) {
                return;
            }
            SQLiteDatabase database = mReadWriteHelper.openReadableDatabase();
            Cursor cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            callback.onQuery(cursor);
            cursor.close();
            mReadWriteHelper.closeReadableDatabase();
        }
    }

    public interface ReadWriteHelper {
        long insert(String table, String nullColumnHack, ContentValues values);

        int delete(String table, String whereClause, String[] whereArgs);

        int update(String table, ContentValues values, String whereClause, String[] whereArgs);

        long replace(String table, String nullColumnHack, ContentValues initialValues);

        void query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, QueryCallback callback);

        void beginTransaction();

        void beginTransactionNonExclusive();

        void setTransactionSuccessful();

        void endTransaction();

        long insertByTransaction(String table, String nullColumnHack, ContentValues values);

        int deleteByTransaction(String table, String whereClause, String[] whereArgs);

        int updateByTransaction(String table, ContentValues values, String whereClause, String[] whereArgs);

        long replaceByTransaction(String table, String nullColumnHack, ContentValues initialValues);

    }

    public interface ReadableDatabase {
        void query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, QueryCallback callback);
    }

    public interface WritableDatabase {
        long insert(String table, String nullColumnHack, ContentValues values);

        int delete(String table, String whereClause, String[] whereArgs);

        int update(String table, ContentValues values, String whereClause, String[] whereArgs);

        long replace(String table, String nullColumnHack, ContentValues initialValues);

        void query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, QueryCallback callback);
    }

    public interface QueryCallback {
        void onQuery(Cursor cursor);
    }


}
