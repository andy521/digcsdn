package com.bob.xtb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bob.xtb.bean.BlogItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 用户信息操作的封装
 *
 * @author Bob
 */
public class BlogService {//存的时候不要存入id，获取时需要取出id，因为在必要时，我们需要根据id来显示到listView中
    private DBHelper helper;
    private SQLiteDatabase db;
    private static BlogService blogService;
    private ContentValues values;

    private BlogService(Context context) {
        // TODO Auto-generated constructor stub
        helper = new DBHelper(context);
        values= new ContentValues();
    }

    public static synchronized BlogService getInstance(Context context){//单例获取UserService对象
        if (blogService == null){
            blogService = new BlogService(context);
        }
        return blogService;
    }

    /**
     * 插入某一个类别的所有博客
     * @param list
     */
    public void insert(List<BlogItem> list){
        db= helper.getWritableDatabase();
        values.clear();//清空values

        for (BlogItem item: list){//老规矩，不添加主键
            values.put(BlogItem.TITLE, item.getTitle());
            values.put(BlogItem.CONTENT, item.getContent());
            values.put(BlogItem.DATE, item.getDate());
            values.put(BlogItem.IMG, item.getImgLink());
            values.put(BlogItem.LINK, item.getLink());
            values.put(BlogItem.BLOGTYPE, item.getBlogType());

            db.insert(DBInfo.Table.BLOG_TABLE_NAME, null, values);
        }
        db.close();
    }

    /**
     * 删除一种栏目博客的数据存储
     * @param blogType
     */
    public void delete(int blogType){
        db= helper.getWritableDatabase();
        db.delete(DBInfo.Table.BLOG_TABLE_NAME, "where "+BlogItem.BLOGTYPE+"= ?",new String[]{blogType+""});
    }

    /**
     * 查找某一栏目下的所有博客
     * @param blogType
     * @return
     */
    public List<BlogItem> loadBlog(int blogType){
        List<BlogItem> blogs= new ArrayList<>();
        BlogItem item;
        db= helper.getReadableDatabase();

        Cursor cursor= db.query(DBInfo.Table.BLOG_TABLE_NAME, null, "where " + BlogItem.BLOGTYPE + "= ?", new String[]{blogType + ""}, null, null, null);
        while(cursor.moveToNext()){//第一步就直接转向第一条记录，为空则退出
            item= new BlogItem();
            item.setTitle(cursor.getString(cursor.getColumnIndex(BlogItem.TITLE)));
            item.setContent(cursor.getString(cursor.getColumnIndex(BlogItem.CONTENT)));
            item.setDate(cursor.getString(cursor.getColumnIndex(BlogItem.DATE)));
            item.setImgLink(cursor.getString(cursor.getColumnIndex(BlogItem.IMG)));
            item.setLink(cursor.getString(cursor.getColumnIndex(BlogItem.LINK)));
            item.setBlogType(blogType);

            blogs.add(item);
        }
        cursor.close();
        db.close();
        return blogs;
    }

}
