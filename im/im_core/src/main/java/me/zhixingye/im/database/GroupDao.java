package me.zhixingye.im.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.salty.protos.GroupMemberProfile;
import com.salty.protos.GroupProfile;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import me.zhixingye.im.service.SQLiteService;

/**
 * Created by YZX on 2018年03月08日.
 * 优秀的代码是它自己最好的文档,当你考虑要添加一个注释时,问问自己:"如何能改进这段代码，以让它不需要注释？"
 */

public class GroupDao extends AbstractDao<GroupProfile> {
    static final String TABLE_NAME = "ContactGroup";

    static final String COLUMN_NAME_GroupId = "GroupID";
    static final String COLUMN_NAME_Name = "GroupName";
    static final String COLUMN_NAME_CreateTime = "CreateTime";
    static final String COLUMN_NAME_OwnerUserId = "OwnerUserId";
    static final String COLUMN_NAME_Avatar = "Avatar";
    static final String COLUMN_NAME_Notice = "Notice";

    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_NAME_GroupId + " TEXT NOT NULL , "
                    + COLUMN_NAME_Name + " TEXT NOT NULL,"
                    + COLUMN_NAME_CreateTime + " TEXT,"
                    + COLUMN_NAME_OwnerUserId + " TEXT NOT NULL,"
                    + COLUMN_NAME_Avatar + " TEXT,"
                    + COLUMN_NAME_Notice + " TEXT,"
                    + "PRIMARY KEY (" + COLUMN_NAME_GroupId + ")"
                    + ")";

    private GroupMemberDao mGroupMemberDao;

    public GroupDao(SQLiteService.ReadWriteHelper readWriteHelper) {
        super(readWriteHelper);
        mGroupMemberDao = new GroupMemberDao(readWriteHelper);
    }


    @Override
    public GroupProfile loadBy(GroupProfile entity) {
        if (isIllegalParameter(entity)) {
            return null;
        }

        GroupProfile profile = super.loadBy(entity);
        if (profile != null) {
            profile = profile.toBuilder()
                    .addAllMembers(mGroupMemberDao.loadAllByGroupId(profile.getGroupId()))
                    .build();
        }
        return profile;
    }

    @NonNull
    @Override
    public List<GroupProfile> loadAll() {
        List<GroupProfile> groupList = super.loadAll();
        if (groupList.size() == 0) {
            return groupList;
        }

        for (int i = 0, size = groupList.size(); i < size; i++) {
            GroupProfile profile = groupList.get(i);
            profile = profile.toBuilder()
                    .addAllMembers(mGroupMemberDao.loadAllByGroupId(profile.getGroupId()))
                    .build();
            groupList.set(i, profile);
        }

        return groupList;
    }

    @Override
    protected boolean insertToDatabase(GroupProfile entity, SQLiteService.WritableDatabase database, ContentValues values) {
        database.beginTransactionNonExclusive();
        try {
            List<GroupMemberProfile> list = entity.getMembersList();
            if (super.insertToDatabase(entity, database, values) && mGroupMemberDao.insertAllToDatabase(list, database, values)) {
                database.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    protected boolean replaceFromDatabase(GroupProfile entity, SQLiteService.WritableDatabase database, ContentValues values) {
        database.beginTransactionNonExclusive();
        try {
            List<GroupMemberProfile> list = entity.getMembersList();
            if (super.replaceFromDatabase(entity, database, values) && mGroupMemberDao.replaceAllFromDatabase(list, database, values)) {
                database.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    protected boolean updateFromDatabase(GroupProfile entity, SQLiteService.WritableDatabase database, ContentValues values) {
        database.beginTransactionNonExclusive();
        try {
            List<GroupMemberProfile> list = entity.getMembersList();
            if (super.updateFromDatabase(entity, database, values) && mGroupMemberDao.updateAllFromDatabase(list, database, values)) {
                database.setTransactionSuccessful();
                return true;
            }
            return false;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    protected boolean deleteFromDatabase(GroupProfile entity, SQLiteService.WritableDatabase database) {
        database.beginTransactionNonExclusive();
        try {
            List<GroupMemberProfile> list = entity.getMembersList();
            if (super.deleteFromDatabase(entity, database) && mGroupMemberDao.deleteAllFromDatabase(list, database)) {
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
        return null;
    }

    @NonNull
    @Override
    protected String getPrimaryKeySelection() {
        return COLUMN_NAME_GroupId + " = ?";
    }

    @Override
    protected String[] getPrimaryKeySelectionArgs(GroupProfile entity) {
        return new String[]{entity.getGroupId()};
    }

    @Override
    protected void parseToContentValues(GroupProfile entity, ContentValues values) {
        values.put(COLUMN_NAME_GroupId, entity.getGroupId());
        values.put(COLUMN_NAME_Name, entity.getName());
        values.put(COLUMN_NAME_CreateTime, entity.getCreateTime());
        values.put(COLUMN_NAME_OwnerUserId, entity.getOwnerUserId());
        values.put(COLUMN_NAME_Avatar, entity.getAvatar());
        values.put(COLUMN_NAME_Notice, entity.getNotice());
    }

    @Override
    protected GroupProfile toEntity(Cursor cursor) {
        return toEntityFromCursor(cursor);
    }

    static GroupProfile toEntityFromCursor(Cursor cursor) {
        return GroupProfile.newBuilder()
                .setGroupId(getString(cursor, COLUMN_NAME_GroupId))
                .setName(getString(cursor, COLUMN_NAME_Name))
                .setCreateTime(getLong(cursor, COLUMN_NAME_CreateTime))
                .setOwnerUserId(getString(cursor, COLUMN_NAME_OwnerUserId))
                .setAvatar(getString(cursor, COLUMN_NAME_Avatar))
                .setNotice(getString(cursor, COLUMN_NAME_Notice))
                .build();
    }

}
