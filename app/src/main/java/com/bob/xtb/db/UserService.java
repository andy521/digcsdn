package com.bob.xtb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bob.xtb.bean.User;

/**
 * 用户信息操作的封装
 *
 * @author Bob
 */
public class UserService {//存的时候不要存入id，获取时需要取出id，因为在必要时，我们需要根据id来显示到listView中
    private DBHelper helper;
    private SQLiteDatabase db;
    private static UserService userService;

    private UserService(Context context) {
        // TODO Auto-generated constructor stub
        helper = new DBHelper(context);
    }

    public static synchronized UserService getInstance(Context context){//单例获取UserService对象
        if (userService == null)
            userService= new UserService(context);
        return userService;
    }

    public void insertUser(User user) {
        db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.USER_COUNT, user.getUserCount());
        values.put(User.USER_PSD, user.getUserPsd());

        db.insert(DBInfo.Table.USER_TABLE_NAME, null, values);
        db.close();
    }

    public User queryUser(String userCount) {
        User user = null;
        db = helper.getReadableDatabase();

        Cursor cursor = db.query(DBInfo.Table.USER_TABLE_NAME, new String[]{User.ID, User.USER_COUNT, User.USER_PSD},
                User.USER_COUNT + "=?", new String[]{userCount}, null, null, null);

        if (cursor != null)//查询成功
        {
            if (cursor.getCount() > 0)//有记录
            {
                cursor.moveToFirst();
                user = new User();
                user.setId(cursor.getLong(cursor.getColumnIndex(User.ID)));//3个参数的查询
                user.setUserCount(cursor.getString(cursor.getColumnIndex(User.USER_COUNT)));
                user.setUserPsd(cursor.getString(cursor.getColumnIndex(User.USER_PSD)));
            }
        }
        cursor.close();
        db.close();
        return user;
    }

}
