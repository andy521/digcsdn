package com.bob.xtb.db;

/**
 * 数据库信息
 *
 * @author Bob
 */

public class DBInfo {

    public static class DB {
        public static String DB_NAME = "BobBlog";
        public static int VERSION = 1;
    }

    public static class Table {
        /**
         * 用户表的创建,暂时只有账户密码，用户头像使用blob二进制存储
         */
        public static String BLOG_TABLE_NAME = "blogTable";
        public static String BLOG_TABLE_CREATE = "CREATE TABLE "
                + BLOG_TABLE_NAME + "(_id INTEGER PRIMARY KEY, title TEXT, content TEXT, " +
                "date TEXT, img TEXT, link TEXT, blogType INTEGER )";

    }
}
