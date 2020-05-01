package me.zhixingye.im.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public class SQLiteOpenHelperImpl extends SQLiteOpenHelper {


    public SQLiteOpenHelperImpl(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(UserDao.CREATE_TABLE_SQL);
//            db.execSQL(ContactOperationDao.CREATE_TABLE_SQL);
//            db.execSQL(ContactDao.CREATE_TABLE_SQL);
//            db.execSQL(GroupDao.CREATE_TABLE_SQL);
//            db.execSQL(GroupMemberDao.CREATE_TABLE_SQL);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}