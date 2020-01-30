package me.zhixingye.im.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.salty.protos.ContactProfile;
import com.salty.protos.ContactRemark;
import com.salty.protos.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.zhixingye.im.bean.ContactTag;

/**
 * Created by YZX on 2017年11月24日.
 * 每一个不曾起舞的日子,都是对生命的辜负.
 */


public class ContactDao extends AbstractDao<ContactProfile> {

    static final String TABLE_NAME = "Contact";

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

    private static final String MULTI_TABLE_SELECT = "SELECT * FROM " + TABLE_NAME + " INNER JOIN " + UserDao.TABLE_NAME + " ON " + TABLE_NAME + "." + COLUMN_NAME_ContactId + "=" + UserDao.TABLE_NAME + "." + UserDao.COLUMN_NAME_UserId;

    public ContactDao(ReadWriteHelper helper) {
        super(helper);
    }

    @Override
    public ContactProfile loadByKey(String... keyValues) {
        if (keyValues == null || keyValues.length != 1) {
            return null;
        }

        String contactId = keyValues[0];
        if (TextUtils.isEmpty(contactId)) {
            return null;
        }

        SQLiteDatabase database = mReadWriteHelper.openReadableDatabase();
        Cursor cursor = database.rawQuery(MULTI_TABLE_SELECT + " AND " + TABLE_NAME + "." + COLUMN_NAME_ContactId + "=?", new String[]{contactId});
        ContactProfile contact = null;
        if (cursor.moveToFirst()) {
            contact = toEntity(cursor);
        }
        cursor.close();
        mReadWriteHelper.closeReadableDatabase();
        return contact;
    }


    public ArrayList<ContactProfile> loadAllContacts() {
        SQLiteDatabase database = mReadWriteHelper.openReadableDatabase();
        Cursor cursor = database.rawQuery(MULTI_TABLE_SELECT, null);
        ArrayList<ContactProfile> contactList = new ArrayList<>(cursor.getCount());
        while (cursor.moveToNext()) {
            contactList.add(toEntity(cursor));
        }
        cursor.close();
        mReadWriteHelper.closeReadableDatabase();
        return contactList;
    }

    public boolean insertAllContacts(List<ContactProfile> contactList) {
        if (contactList == null || contactList.size() == 0) {
            return true;
        }
        List<UserProfile> userList = new LinkedList<>();
        for (ContactProfile contact : contactList) {
            userList.add(contact.getProfile());
        }
        return new UserDao(mReadWriteHelper).replaceAll(userList) && insertAll(contactList);
    }

    public HashSet<String> getAllTagsType() {
        SQLiteDatabase database = mReadWriteHelper.openReadableDatabase();
        Cursor cursor = database.query(true, TABLE_NAME, new String[]{COLUMN_NAME_Tags}, COLUMN_NAME_Tags + " IS NOT NULL", null, null, null, null, null);
        HashSet<String> tagsSet = new HashSet<>();
        String tags;
        while (cursor.moveToNext()) {
            tags = cursor.getString(0);
            if (!TextUtils.isEmpty(tags)) {
                tagsSet.addAll(Arrays.asList(tags.split(";")));
            }
        }
        cursor.close();
        mReadWriteHelper.closeReadableDatabase();
        return tagsSet;
    }

    public ArrayList<ContactTag> getAllTagAndMemberCount() {
        SQLiteDatabase database = mReadWriteHelper.openReadableDatabase();
        Cursor cursor = database.query(false, TABLE_NAME, new String[]{COLUMN_NAME_Tags}, COLUMN_NAME_Tags + " IS NOT NULL", null, null, null, null, null);
        HashMap<String, Integer> tagsMap = new HashMap<>(16);
        String tags;
        Integer value;
        while (cursor.moveToNext()) {
            tags = cursor.getString(0);
            if (!TextUtils.isEmpty(tags)) {
                for (String tag : tags.split(";")) {
                    value = tagsMap.get(tag);
                    if (value == null) {
                        tagsMap.put(tag, 1);
                    } else {
                        tagsMap.put(tag, value + 1);
                    }
                }
            }
        }
        cursor.close();
        mReadWriteHelper.closeReadableDatabase();
        ArrayList<ContactTag> tagList = new ArrayList<>(tagsMap.size());
        for (Map.Entry<String, Integer> entry : tagsMap.entrySet()) {
            tagList.add(new ContactTag(entry.getKey(), entry.getValue()));
        }
        return tagList;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getWhereClauseOfKey() {
        return COLUMN_NAME_ContactId + "=?";
    }

    @Override
    protected String[] toWhereArgsOfKey(ContactProfile entity) {
        return new String[]{entity.getProfile().getUserId()};
    }

    @Override
    protected void parseToContentValues(ContactProfile entity, ContentValues values) {
        values.put(COLUMN_NAME_ContactId, entity.getProfile().getUserId());
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
                .setProfile(UserDao.toEntityFromCursor(cursor))
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
