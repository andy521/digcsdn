package com.bob.xtb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer;
import android.text.style.ForegroundColorSpan;

import com.bob.xtb.bean.Task;
import com.bob.xtb.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务操作的封装
 *
 * @author Bob
 */
public class TaskService {//存的时候不要存入id，获取时需要取出id，因为在必要时，我们需要根据id来显示到listView中
    private DBHelper helper;
    private SQLiteDatabase db;
    private static TaskService taskService;

    private TaskService(Context context) {
        // TODO Auto-generated constructor stub
        helper = new DBHelper(context);
    }

    public static synchronized TaskService getInstance(Context context) {//单例获取UserService对象
        if (taskService == null)
            taskService = new TaskService(context);
        return taskService;
    }

    /**
     * 老规矩，插入不插id
     *
     * @param tasks
     */
    public void insertTasks(List<Task> tasks) {//批量插入任务
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Task task : tasks) {
            values.clear();
            values.put(Task.TASK_ID, task.getTaskId());
            values.put(Task.TASK_NAME, task.getTaskName());
            values.put(Task.TASK_DESC, task.getTaskDesc());
            values.put(Task.TASK_PRIORITY, task.getTaskPriority());
            values.put(Task.TASK_TYPE, task.getTaskType());
            values.put(Task.IS_TIMED, task.getIsTimed());
            values.put(Task.CREATE_TIME, task.getCreateTime());

            db.insert(DBInfo.Table.TASK_TABLE_NAME, null, values);
        }

        db.close();
    }

    public List<Task> queryAllTasks() {
        List<Task> tasks = new ArrayList<>();
        Task task = null;
        db = helper.getReadableDatabase();

        Cursor cursor = db.query(DBInfo.Table.TASK_TABLE_NAME, null,
                null, null, null, null, null);//查询列为null，表示所有列

        if (cursor != null)//查询成功
        {
            while (cursor.moveToNext()) {
                task = new Task();
                task.setId(cursor.getLong(cursor.getColumnIndex(Task.ID)));
                task.setTaskId(cursor.getString(cursor.getColumnIndex(Task.TASK_ID)));
                task.setTaskName(cursor.getString(cursor.getColumnIndex(Task.TASK_NAME)));
                task.setTaskDesc(cursor.getString(cursor.getColumnIndex(Task.TASK_DESC)));
                task.setTaskType(cursor.getString(cursor.getColumnIndex(Task.TASK_TYPE)));
                task.setIsTimed(cursor.getInt(cursor.getColumnIndex(Task.IS_TIMED)));
                task.setTaskPriority(cursor.getInt(cursor.getColumnIndex(Task.TASK_PRIORITY)));
                task.setCreateTime(cursor.getString(cursor.getColumnIndex(Task.CREATE_TIME)));

                tasks.add(task);
            }
        }
        cursor.close();
        db.close();
        return tasks;
    }
}
