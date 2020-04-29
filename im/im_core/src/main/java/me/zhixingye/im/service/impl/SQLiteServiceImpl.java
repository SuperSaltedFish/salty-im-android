package me.zhixingye.im.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import me.zhixingye.im.database.AbstractDao;
import me.zhixingye.im.database.SQLiteOpenHelperImpl;
import me.zhixingye.im.service.SQLiteService;


/**
 * Created by zhixingye on 2020年01月28日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */
public class SQLiteServiceImpl implements SQLiteService {


    private ReadWriteHelper mReadWriteHelper;
    private SQLiteOpenHelper mOpenHelper;

    public SQLiteServiceImpl(Context context, String name, int version) {
        mOpenHelper = new SQLiteOpenHelperImpl(context, name, version);
        mReadWriteHelper = new ReadWriteHelperImpl(mOpenHelper);
    }

    public void close() {
        if (mOpenHelper != null) {
            mOpenHelper.close();
            mOpenHelper = null;
        }
    }

    @Override
    public <T extends AbstractDao> T createDao(Class<T> c) {
        if (mReadWriteHelper == null) {
            return null;
        }
        try {
            Constructor<T> constructor = c.getConstructor(ReadWriteHelper.class);
            return constructor.newInstance(mReadWriteHelper);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class ReadWriteHelperImpl implements ReadWriteHelper {

        private SQLiteOpenHelper mOpenHelper;
        private SQLiteDatabase mReadableDatabase;
        private SQLiteDatabase mWritableDatabase;

        private int mReadCount;
        private int mWriteCount;

        ReadWriteHelperImpl(SQLiteOpenHelper openHelper) {
            mOpenHelper = openHelper;
        }

        @Override
        public ReadableDatabase openReadableDatabase() {
            return new ReadableDatabaseImpl(this);
        }

        @Override
        public WritableDatabase openWritableDatabase() {
            return new WritableDatabaseImpl(this);
        }

        synchronized SQLiteDatabase starRead() {
            if (mReadCount == 0) {
                mReadableDatabase = mOpenHelper.getReadableDatabase();
            }
            mReadCount++;
            return mReadableDatabase;
        }

        synchronized void endRead() {
            mReadCount--;
            if (mReadCount == 0) {
                mReadableDatabase.close();
            }
        }

        synchronized SQLiteDatabase startWrite() {
            if (mWriteCount == 0) {
                mWritableDatabase = mOpenHelper.getWritableDatabase();
            }
            mWriteCount++;
            return mWritableDatabase;
        }

        synchronized void endWrite() {
            mWriteCount--;
            if (mWriteCount == 0) {
                mWritableDatabase.close();
            }
        }
    }

    private class ReadableDatabaseImpl implements ReadableDatabase {

        private ReadWriteHelperImpl mReadWriteHelper;
        private SQLiteDatabase mReadableDatabase;

        ReadableDatabaseImpl(ReadWriteHelperImpl readWriteHelper) {
            mReadWriteHelper = readWriteHelper;
            mReadableDatabase = readWriteHelper.starRead();
        }

        @Override
        public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
            if (mReadableDatabase == null) {
                throw new RuntimeException("ReadableDatabase 已经关闭");
            }
            return mReadableDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        }

        @Override
        public void close() {
            if (mReadableDatabase != null) {
                mReadableDatabase = null;
                mReadWriteHelper.endRead();
            }
        }
    }

    private class WritableDatabaseImpl implements WritableDatabase {

        private ReadWriteHelperImpl mReadWriteHelper;
        private SQLiteDatabase mWritableDatabase;

        WritableDatabaseImpl(ReadWriteHelperImpl readWriteHelper) {
            mReadWriteHelper = readWriteHelper;
            mWritableDatabase = readWriteHelper.startWrite();
        }

        @Override
        public long insert(String table, String nullColumnHack, ContentValues values) {
            checkDatabaseState();
            return mWritableDatabase.insert(table, nullColumnHack, values);
        }

        @Override
        public int delete(String table, String whereClause, String[] whereArgs) {
            checkDatabaseState();
            return mWritableDatabase.delete(table, whereClause, whereArgs);
        }

        @Override
        public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
            checkDatabaseState();
            return mWritableDatabase.update(table, values, whereClause, whereArgs);
        }

        @Override
        public long replace(String table, String nullColumnHack, ContentValues initialValues) {
            checkDatabaseState();
            return mWritableDatabase.replace(table, nullColumnHack, initialValues);
        }

        @Override
        public void beginTransaction() {
            checkDatabaseState();
            mWritableDatabase.beginTransaction();
        }

        @Override
        public void beginTransactionNonExclusive() {
            checkDatabaseState();
            mWritableDatabase.beginTransactionNonExclusive();
        }

        @Override
        public void setTransactionSuccessful() {
            checkDatabaseState();
            mWritableDatabase.setTransactionSuccessful();
        }

        @Override
        public void endTransaction() {
            checkDatabaseState();
            mWritableDatabase.endTransaction();
        }

        @Override
        public void close() {
            if (mWritableDatabase != null) {
                mWritableDatabase = null;
                mReadWriteHelper.endWrite();
            }
        }

        private void checkDatabaseState() {
            if (mWritableDatabase == null) {
                throw new RuntimeException("WritableDatabase 已经关闭");
            }
        }
    }

}
