package com.bob.xtb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bob.xtb.bean.Resource;
import com.bob.xtb.bean.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务操作的封装
 *
 * @author Bob
 */
public class ResService {//存的时候不要存入id，获取时需要取出id，因为在必要时，我们需要根据id来显示到listView中
    private DBHelper helper;
    private SQLiteDatabase db;
    private static ResService resService;

    private ResService(Context context) {
        // TODO Auto-generated constructor stub
        helper = new DBHelper(context);
    }

    public static synchronized ResService getInstance(Context context) {//单例获取UserService对象
        if (resService == null)
            resService = new ResService(context);
        return resService;
    }

    /**
     * 老规矩，插入不插id
     *
     * @param resources
     */
    public void insertResources(List<Resource> resources) {//批量插入任务
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Resource resource : resources) {
            values.clear();
            values.put(Resource.RES_ID, resource.getResId());
            values.put(Resource.RES_NAME, resource.getResName());
            values.put(Resource.RES_TYPE, resource.getResType());
            values.put(Resource.RES_FROM, resource.getResFrom());
            values.put(Resource.FROM_PATH, resource.getFromPath());
            values.put(Resource.RES_TO, resource.getResTo());
            values.put(Resource.TO_PATH, resource.getToPath());
            values.put(Resource.CREATE_TIME, resource.getCreateTime());

            db.insert(DBInfo.Table.TASK_TABLE_NAME, null, values);
        }

        db.close();
    }

    public List<Resource> queryAllResources() {
        List<Resource> resources = new ArrayList<>();
        Resource resource = null;
        db = helper.getReadableDatabase();

        Cursor cursor = db.query(DBInfo.Table.RES_TABLE_CREATE, null,
                null, null, null, null, null);//查询列为null，表示所有列

        if (cursor != null)//查询成功
        {
            while (cursor.moveToNext()) {
                resource = new Resource();
                resource.setId(cursor.getLong(cursor.getColumnIndex(Resource.ID)));
                resource.setResId(cursor.getString(cursor.getColumnIndex(Resource.RES_ID)));
                resource.setResName(cursor.getString(cursor.getColumnIndex(Resource.RES_NAME)));
                resource.setResType(cursor.getString(cursor.getColumnIndex(Resource.RES_TYPE)));
                resource.setResFrom(cursor.getString(cursor.getColumnIndex(Resource.RES_FROM)));
                resource.setFromPath(cursor.getString(cursor.getColumnIndex(Resource.FROM_PATH)));
                resource.setResTo(cursor.getString(cursor.getColumnIndex(Resource.RES_TO)));
                resource.setToPath(cursor.getString(cursor.getColumnIndex(Resource.TO_PATH)));
                resource.setCreateTime(cursor.getString(cursor.getColumnIndex(Resource.CREATE_TIME)));

                resources.add(resource);
            }
        }
        cursor.close();
        db.close();
        return resources;
    }
}
