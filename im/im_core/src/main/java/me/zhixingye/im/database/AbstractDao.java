package me.zhixingye.im.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by YZX on 2017年11月17日.
 * 每一个不曾起舞的日子,都是对生命的辜负.
 */


public abstract class AbstractDao<T> {

    protected static final String COLUMN_NAME_RowID = "ROWID";

    protected SQLiteService.ReadWriteHelper mReadWriteHelper;

    @NonNull
    protected abstract String getTableName();

    @Nullable
    protected abstract String getViewTableView();

    @NonNull
    protected abstract String getPrimaryKeySelection();

    protected abstract String[] getPrimaryKeySelectionArgs(T entity);

    protected abstract void parseToContentValues(T entity, ContentValues values);

    protected abstract T toEntity(Cursor cursor);

    public AbstractDao(SQLiteService.ReadWriteHelper readWriteHelper) {
        mReadWriteHelper = readWriteHelper;
    }

    @NonNull
    public List<T> loadAll() {
        try (SQLiteService.ReadableDatabase database = mReadWriteHelper.openReadableDatabase()) {
            Cursor cursor = database.query(getViewTableNameIfHas(), null, null, null, null, null, null, null);
            List<T> list = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                list.add(toEntity(cursor));
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    public T loadBy(T entity) {
        if (isIllegalParameter(entity)) {
            return null;
        }
        try (SQLiteService.ReadableDatabase database = mReadWriteHelper.openReadableDatabase()) {
            Cursor cursor = database.query(getViewTableNameIfHas(), null, getPrimaryKeySelection(), getPrimaryKeySelectionArgs(entity), null, null, null, null);
            T result = null;
            if (cursor.moveToFirst()) {
                result = toEntity(cursor);
            }
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(T entity) {
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            return insertToDatabase(entity, database, new ContentValues());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertAll(Iterable<T> entityIterable) {
        if (entityIterable == null) {
            return false;
        }

        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            database.beginTransactionNonExclusive();
            boolean isBeInterrupted = false;
            try {
                ContentValues values = new ContentValues();
                for (T entity : entityIterable) {
                    if (!insertToDatabase(entity, database, values)) {
                        isBeInterrupted = true;
                        break;
                    }
                }
                if (!isBeInterrupted) {
                    database.setTransactionSuccessful();
                }
                return !isBeInterrupted;
            } finally {
                database.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean replace(T entity) {
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            return replaceFromDatabase(entity, database, new ContentValues());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean replaceAll(Iterable<T> entityIterable) {
        if (entityIterable == null) {
            return false;
        }
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            database.beginTransactionNonExclusive();
            boolean isBeInterrupted = false;
            try {
                ContentValues values = new ContentValues();
                for (T entity : entityIterable) {
                    if (!replaceFromDatabase(entity, database, values)) {
                        isBeInterrupted = true;
                        break;
                    }
                }
                if (!isBeInterrupted) {
                    database.setTransactionSuccessful();
                }
                return !isBeInterrupted;
            } finally {
                database.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(T entity) {
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            return updateFromDatabase(entity, database, new ContentValues());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateAll(Iterable<T> entityIterable) {
        if (entityIterable == null) {
            return false;
        }
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            database.beginTransactionNonExclusive();
            boolean isBeInterrupted = false;
            try {
                ContentValues values = new ContentValues();
                for (T entity : entityIterable) {
                    if (!updateFromDatabase(entity, database, values)) {
                        isBeInterrupted = true;
                        break;
                    }
                }
                if (!isBeInterrupted) {
                    database.setTransactionSuccessful();
                }
                return !isBeInterrupted;
            } finally {
                database.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(T entity) {
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            return deleteFromDatabase(entity, database);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAll(Iterable<T> entityIterable) {
        if (entityIterable == null) {
            return false;
        }
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            database.beginTransactionNonExclusive();
            boolean isBeInterrupted = false;
            try {
                for (T entity : entityIterable) {
                    if (!deleteFromDatabase(entity, database)) {
                        isBeInterrupted = true;
                        break;
                    }
                }
                if (!isBeInterrupted) {
                    database.setTransactionSuccessful();
                }
                return !isBeInterrupted;
            } finally {
                database.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void cleanTable() {
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            database.delete(getTableName(), null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(T entity) {
        if (entity == null) {
            return false;
        }

        String[] whereArgsOfKey = getPrimaryKeySelectionArgs(entity);
        if (whereArgsOfKey == null || whereArgsOfKey.length == 0) {
            return false;
        }

        try (SQLiteService.ReadableDatabase database = mReadWriteHelper.openReadableDatabase()) {
            Cursor cursor = database.query(getViewTableNameIfHas(), null, getPrimaryKeySelection(), getPrimaryKeySelectionArgs(entity), null, null, null, null);
            boolean result = (cursor.getCount() > 0);
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean insertToDatabase(T entity, SQLiteService.WritableDatabase database, ContentValues values) {
        if (isIllegalParameter(entity)) {
            return false;
        }
        values.clear();
        parseToContentValues(entity, values);
        return database.insert(getTableName(), null, values) > 0;
    }

    protected boolean insertAllToDatabase(List<T> entityList, SQLiteService.WritableDatabase database, ContentValues values) {
        if (entityList == null) {
            return false;
        }
        boolean isBeInterrupted = false;
        for (T entity : entityList) {
            if (!insertToDatabase(entity, database, values)) {
                isBeInterrupted = true;
                break;
            }
        }
        return !isBeInterrupted;
    }

    protected boolean replaceFromDatabase(T entity, SQLiteService.WritableDatabase database, ContentValues values) {
        if (isIllegalParameter(entity)) {
            return false;
        }
        values.clear();
        parseToContentValues(entity, values);
        return database.replace(getTableName(), null, values) > 0;
    }

    protected boolean replaceAllFromDatabase(List<T> entityList, SQLiteService.WritableDatabase database, ContentValues values) {
        if (entityList == null) {
            return false;
        }
        boolean isBeInterrupted = false;
        for (T entity : entityList) {
            if (!replaceFromDatabase(entity, database, values)) {
                isBeInterrupted = true;
                break;
            }
        }
        return !isBeInterrupted;
    }

    protected boolean updateFromDatabase(T entity, SQLiteService.WritableDatabase database, ContentValues values) {
        if (isIllegalParameter(entity)) {
            return false;
        }
        values.clear();
        parseToContentValues(entity, values);
        return database.update(getTableName(), values, getPrimaryKeySelection(), getPrimaryKeySelectionArgs(entity)) > 0;
    }

    protected boolean updateAllFromDatabase(List<T> entityList, SQLiteService.WritableDatabase database, ContentValues values) {
        if (entityList == null) {
            return false;
        }
        boolean isBeInterrupted = false;
        for (T entity : entityList) {
            if (!updateFromDatabase(entity, database, values)) {
                isBeInterrupted = true;
                break;
            }
        }
        return !isBeInterrupted;
    }

    protected boolean deleteFromDatabase(T entity, SQLiteService.WritableDatabase database) {
        if (isIllegalParameter(entity)) {
            return false;
        }
        return database.delete(getTableName(), getPrimaryKeySelection(), getPrimaryKeySelectionArgs(entity)) >= 0;
    }

    protected boolean deleteAllFromDatabase(List<T> entityList, SQLiteService.WritableDatabase database) {
        if (entityList == null) {
            return false;
        }
        boolean isBeInterrupted = false;
        for (T entity : entityList) {
            if (!deleteFromDatabase(entity, database)) {
                isBeInterrupted = true;
                break;
            }
        }
        return !isBeInterrupted;
    }

    protected boolean isIllegalParameter(T entity) {
        if (entity == null) {
            return true;
        }

        String[] whereArgsOfKey = getPrimaryKeySelectionArgs(entity);
        return whereArgsOfKey == null || whereArgsOfKey.length == 0;
    }

    protected String getViewTableNameIfHas() {
        String viewTableName = getViewTableView();
        if (TextUtils.isEmpty(viewTableName)) {
            return getTableName();
        } else {
            return viewTableName;
        }
    }

    protected static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    protected static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    protected static long getLong(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
}
