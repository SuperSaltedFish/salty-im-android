package me.zhixingye.im.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.salty.protos.ContactProfile;
import com.salty.protos.ContactRemark;
import com.salty.protos.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.zhixingye.im.service.impl.SQLiteServiceImpl;

/**
 * Created by YZX on 2017年11月24日.
 * 每一个不曾起舞的日子,都是对生命的辜负.
 */


public class ContactDao extends AbstractDao<ContactProfile> {

    static final String TABLE_NAME = "Contact";
    static final String VIEW_TABLE_NAME = "ContactView";

    static final String COLUMN_NAME_ContactId = UserDao.COLUMN_NAME_UserId;
    static final String COLUMN_NAME_RemarkName = "RemarkName";
    static final String COLUMN_NAME_Description = "Description";
    static final String COLUMN_NAME_Telephones = "Telephones";
    static final String COLUMN_NAME_Tags = "Tags";

    static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_NAME_ContactId + " TEXT NOT NULL , "
                    + COLUMN_NAME_RemarkName + " TEXT,"
                    + COLUMN_NAME_Description + " TEXT,"
                    + COLUMN_NAME_Telephones + " TEXT,"
                    + COLUMN_NAME_Tags + " TEXT,"
                    + "PRIMARY KEY (" + COLUMN_NAME_ContactId + ")"
                    + ")";

    public static final String CREATE_VIEW_SQL =
            "CREATE VIEW " + VIEW_TABLE_NAME
                    + " AS "
                    + "SELECT * FROM " + TABLE_NAME + " INNER JOIN " + UserDao.TABLE_NAME
                    + " USING(" + UserDao.COLUMN_NAME_UserId + ")";

    private UserDao mUserDao;

    public ContactDao(SQLiteServiceImpl.ReadWriteHelper helper) {
        super(helper);
        mUserDao = new UserDao(helper);
    }

    @Override
    protected boolean insertToDatabase(ContactProfile entity, SQLiteServiceImpl.WritableDatabase database, ContentValues values) {
        database.beginTransactionNonExclusive();
        try {
            UserProfile profile = entity.getUserProfile();
            if (super.insertToDatabase(entity, database, values) && mUserDao.replaceFromDatabase(profile, database, values)) {
                database.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    protected boolean replaceFromDatabase(ContactProfile entity, SQLiteServiceImpl.WritableDatabase database, ContentValues values) {
        database.beginTransactionNonExclusive();
        try {
            UserProfile profile = entity.getUserProfile();
            if (super.replaceFromDatabase(entity, database, values) && mUserDao.replaceFromDatabase(profile, database, values)) {
                database.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    protected boolean updateFromDatabase(ContactProfile entity, SQLiteServiceImpl.WritableDatabase database, ContentValues values) {
        database.beginTransactionNonExclusive();
        try {
            UserProfile profile = entity.getUserProfile();
            if (super.updateFromDatabase(entity, database, values) && mUserDao.updateFromDatabase(profile, database, values)) {
                database.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            database.endTransaction();
        }
    }

    @NonNull
    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Nullable
    @Override
    protected String getViewTableView() {
        return VIEW_TABLE_NAME;
    }

    @NonNull
    @Override
    protected String getPrimaryKeySelection() {
        return COLUMN_NAME_ContactId + " = ?";
    }

    @Override
    protected String[] getPrimaryKeySelectionArgs(ContactProfile entity) {
        return new String[]{entity.getUserProfile().getUserId()};
    }

    @Override
    protected void parseToContentValues(ContactProfile entity, ContentValues values) {
        values.put(COLUMN_NAME_ContactId, entity.getUserProfile().getUserId());
        values.put(COLUMN_NAME_RemarkName, entity.getRemarkInfo().getRemarkName());
        values.put(COLUMN_NAME_Description, entity.getRemarkInfo().getDescription());
        values.put(COLUMN_NAME_Telephones, listToString(entity.getRemarkInfo().getTelephonesList()));
        values.put(COLUMN_NAME_Tags, listToString(entity.getRemarkInfo().getTagsList()));
    }

    @Override
    protected ContactProfile toEntity(Cursor cursor) {
        ContactRemark remark = ContactRemark.newBuilder()
                .setRemarkName(getString(cursor, COLUMN_NAME_RemarkName))
                .setDescription(getString(cursor, COLUMN_NAME_Description))
                .addAllTelephones(stringToList(getString(cursor, COLUMN_NAME_Telephones)))
                .addAllTags(stringToList(getString(cursor, COLUMN_NAME_Tags)))
                .build();
        return ContactProfile.newBuilder()
                .setUserProfile(UserDao.toEntityFromCursor(cursor))
                .setRemarkInfo(remark)
                .build();
    }

    private static String listToString(List<String> strList) {
        int size = strList == null ? 0 : strList.size();
        if (size > 0) {
            StringBuilder stringBuilder = new StringBuilder(13 * size);
            for (int i = 0; i < size; i++) {
                stringBuilder.append(strList.get(i));
                if (i != size - 1) {
                    stringBuilder.append(";");
                }
            }
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    private static List<String> stringToList(String strList) {
        if (TextUtils.isEmpty(strList)) {
            return new ArrayList<>(0);
        }
        return Arrays.asList(strList.split(";"));
    }
}
