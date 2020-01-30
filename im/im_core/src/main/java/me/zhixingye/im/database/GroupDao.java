package me.zhixingye.im.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.salty.protos.GroupProfile;

import java.util.List;

import androidx.annotation.NonNull;

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

    public GroupDao(ReadWriteHelper readWriteHelper) {
        super(readWriteHelper);
        mGroupMemberDao = new GroupMemberDao(readWriteHelper);
    }


    @Override
    public GroupProfile loadByKey(String... keyValues) {
        if (keyValues == null || keyValues.length != 1) {
            return null;
        }

        String groupId = keyValues[0];
        if (TextUtils.isEmpty(groupId)) {
            return null;
        }

        GroupProfile profile = super.loadByKey(keyValues);
        if (profile == null) {
            return null;
        }

        profile = profile.toBuilder()
                .addAllMembers(mGroupMemberDao.loadAll())
                .build();
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
    public boolean insert(GroupProfile entity) {
        if (entity == null) {
            return false;
        }
        ContentValues values = new ContentValues();
        parseToContentValues(entity, values);
        boolean result = mReadWriteHelper.openWritableDatabase().insert(getTableName(), null, values) > 0;
        mReadWriteHelper.closeWritableDatabase();
        return super.insert(entity);
    }

    @Override
    public boolean insertAll(Iterable<GroupProfile> entityIterable) {
        return super.insertAll(entityIterable);
    }

    @Override
    public boolean update(GroupProfile entity) {
        return super.update(entity);
    }

    @Override
    public boolean updateAll(Iterable<GroupProfile> entityIterable) {
        return super.updateAll(entityIterable);
    }

    @Override
    public boolean deleteByKey(String... keyValue) {
        return super.deleteByKey(keyValue);
    }

    @Override
    public boolean deleteAll(Iterable<GroupProfile> entityIterable) {
        return super.deleteAll(entityIterable);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getWhereClauseOfKey() {
        return COLUMN_NAME_GroupId + "=?";
    }

    @Override
    protected String[] toWhereArgsOfKey(GroupProfile entity) {
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
