package me.zhixingye.im.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.zhixingye.im.service.SQLiteService;

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
    protected abstract String[] getPrimaryKeyColumnsName();

    protected abstract String[] getPrimaryKeyColumnsValue(T entity);

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
        if (entity == null) {
            return null;
        }

        String[] whereArgsOfKey = getPrimaryKeyColumnsValue(entity);
        if (whereArgsOfKey == null || whereArgsOfKey.length == 0) {
            return null;
        }

        try (SQLiteService.ReadableDatabase database = mReadWriteHelper.openReadableDatabase()) {
            Cursor cursor = database.query(getViewTableNameIfHas(), null, generatePrimaryKeySelection(getPrimaryKeyColumnsName()), whereArgsOfKey, null, null, null, null);
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
        if (entity == null) {
            return false;
        }
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            ContentValues values = new ContentValues();
            parseToContentValues(entity, values);
            return database.insert(getTableName(), null, values) > 0;
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
                    values.clear();
                    parseToContentValues(entity, values);
                    if (database.insert(getTableName(), null, values) <= 0) {
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
        if (entity == null) {
            return false;
        }
        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            ContentValues values = new ContentValues();
            parseToContentValues(entity, values);
            return database.replace(getTableName(), null, values) > 0;
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
                    values.clear();
                    parseToContentValues(entity, values);
                    if (database.replace(getTableName(), null, values) <= 0) {
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
        if (entity == null) {
            return false;
        }

        String[] whereArgsOfKey = getPrimaryKeyColumnsValue(entity);
        if (whereArgsOfKey == null || whereArgsOfKey.length == 0) {
            return false;
        }

        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            ContentValues values = new ContentValues();
            parseToContentValues(entity, values);
            return database.update(getTableName(), values, generatePrimaryKeySelection(getPrimaryKeyColumnsName()), whereArgsOfKey) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean update(T entity,SQLiteService.WritableDatabase database,ContentValues values,String primaryKeySelection){
        String[] whereArgsOfKey = getPrimaryKeyColumnsValue(entity);
        if (whereArgsOfKey == null || whereArgsOfKey.length == 0) {
            return false;
        }
        values.clear();
        parseToContentValues(entity, values);
        return database.update(getTableName(), values,primaryKeySelection, whereArgsOfKey) > 0;
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
                String primaryKeySelection = generatePrimaryKeySelection(getPrimaryKeyColumnsName());
                for (T entity : entityIterable) {
                    String[] whereArgsOfKey = getPrimaryKeyColumnsValue(entity);
                    if (whereArgsOfKey == null || whereArgsOfKey.length == 0) {
                        isBeInterrupted = true;
                        break;
                    }
                    values.clear();
                    parseToContentValues(entity, values);
                    if (database.update(getTableName(), values, primaryKeySelection, whereArgsOfKey) <= 0) {
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

    public boolean deleteBy(T entity) {
        if (entity == null) {
            return false;
        }

        String[] whereArgsOfKey = getPrimaryKeyColumnsValue(entity);
        if (whereArgsOfKey == null || whereArgsOfKey.length == 0) {
            return false;
        }

        try (SQLiteService.WritableDatabase database = mReadWriteHelper.openWritableDatabase()) {
            return database.delete(getTableName(), generatePrimaryKeySelection(getPrimaryKeyColumnsName()), whereArgsOfKey) > 0;
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
                    String[] whereArgsOfKey = getPrimaryKeyColumnsValue(entity);
                    if (whereArgsOfKey == null || whereArgsOfKey.length == 0) {
                        isBeInterrupted = true;
                        break;
                    }
                    if (database.delete(getTableName(), generatePrimaryKeySelection(getPrimaryKeyColumnsName()), whereArgsOfKey) <= 0) {
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

        String[] whereArgsOfKey = getPrimaryKeyColumnsValue(entity);
        if (whereArgsOfKey == null || whereArgsOfKey.length == 0) {
            return false;
        }

        try (SQLiteService.ReadableDatabase database = mReadWriteHelper.openReadableDatabase()) {
            Cursor cursor = database.query(getViewTableNameIfHas(), null, generatePrimaryKeySelection(getPrimaryKeyColumnsName()), getPrimaryKeyColumnsValue(entity), null, null, null, null);
            boolean result = (cursor.getCount() > 0);
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String generatePrimaryKeySelection(String[] primaryKey) {
        if (primaryKey == null || primaryKey.length == 0) {
            throw new RuntimeException("主键字段名字不能为空");
        }
        StringBuilder builder = new StringBuilder(primaryKey.length * 18);
        for (int i = 0, size = primaryKey.length; i < size; i++) {
            if (i != 0) {
                builder.append(" AND ");
            }
            builder.append(primaryKey[i]);
            builder.append(" = ?");
        }
        return builder.toString();
    }

    private String getViewTableNameIfHas() {
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
