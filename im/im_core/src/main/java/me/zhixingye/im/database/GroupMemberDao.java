package me.zhixingye.im.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.salty.protos.GroupMemberProfile;
import com.salty.protos.UserProfile;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.zhixingye.im.service.impl.SQLiteServiceImpl;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */

public class GroupMemberDao extends AbstractDao<GroupMemberProfile> {

    private static final String TABLE_NAME = "GroupMember";
    private static final String VIEW_TABLE_NAME = "GroupMemberView";

    private static final String COLUMN_NAME_GroupId = GroupDao.COLUMN_NAME_GroupId;
    private static final String COLUMN_NAME_UserId = UserDao.COLUMN_NAME_UserId;
    private static final String COLUMN_NAME_Alias = "Alias";
    private static final String COLUMN_NAME_Role = "Role";

    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_NAME_UserId + " TEXT NOT NULL , "
                    + COLUMN_NAME_GroupId + " TEXT NOT NULL,"
                    + COLUMN_NAME_Alias + " TEXT,"
                    + COLUMN_NAME_Role + " INTEGER,"
                    + "PRIMARY KEY (" + COLUMN_NAME_UserId + "," + COLUMN_NAME_GroupId + ")"
                    + ")";

    public static final String CREATE_VIEW_SQL =
            "CREATE VIEW " + VIEW_TABLE_NAME
                    + " AS "
                    + "SELECT * FROM " + TABLE_NAME + " INNER JOIN " + UserDao.TABLE_NAME
                    + " USING(" + UserDao.COLUMN_NAME_UserId + ")";

    private UserDao mUserDao;

    public GroupMemberDao(SQLiteServiceImpl.ReadWriteHelper readWriteHelper) {
        super(readWriteHelper);
        mUserDao = new UserDao(readWriteHelper);
    }

    @NonNull
    public List<GroupMemberProfile> loadAllByGroupId(String groupId) {
        if (!TextUtils.isEmpty(groupId)) {
            try (SQLiteServiceImpl.ReadableDatabase database = mReadWriteHelper.openReadableDatabase()) {
                Cursor cursor = database.query(getViewTableNameIfHas(), null, COLUMN_NAME_GroupId + " = ?", new String[]{groupId}, null, null, null, null);
                List<GroupMemberProfile> list = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    list.add(toEntity(cursor));
                }
                cursor.close();
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(0);
    }

    @Override
    protected boolean insertToDatabase(GroupMemberProfile entity, SQLiteServiceImpl.WritableDatabase database, ContentValues values) {
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
    protected boolean replaceFromDatabase(GroupMemberProfile entity, SQLiteServiceImpl.WritableDatabase database, ContentValues values) {
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
    protected boolean updateFromDatabase(GroupMemberProfile entity, SQLiteServiceImpl.WritableDatabase database, ContentValues values) {
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
        return COLUMN_NAME_GroupId + " = ? AND " + COLUMN_NAME_UserId + " = ?";
    }

    @Override
    protected String[] getPrimaryKeySelectionArgs(GroupMemberProfile entity) {
        return new String[]{entity.getGroupId(), entity.getUserProfile().getUserId()};
    }

    @Override
    protected void parseToContentValues(GroupMemberProfile entity, ContentValues values) {
        values.put(COLUMN_NAME_GroupId, entity.getGroupId());
        values.put(COLUMN_NAME_UserId, entity.getUserProfile().getUserId());
        values.put(COLUMN_NAME_Alias, entity.getAlias());
    }

    @Override
    protected GroupMemberProfile toEntity(Cursor cursor) {
        return toEntityFromCursor(cursor);
    }

    static GroupMemberProfile toEntityFromCursor(Cursor cursor) {
        return GroupMemberProfile.newBuilder()
                .setGroupId(getString(cursor, COLUMN_NAME_GroupId))
                .setAlias(getString(cursor, COLUMN_NAME_Alias))
                .setRoleValue(getInt(cursor, COLUMN_NAME_Role))
                .setUserProfile(UserDao.toEntityFromCursor(cursor))
                .build();
    }
}
