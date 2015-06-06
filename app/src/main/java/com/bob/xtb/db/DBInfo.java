package com.bob.xtb.db;

/**
 * 数据库信息
 *
 * @author Bob
 */

public class DBInfo {

    public static class DB {
        public static String DB_NAME = "xtb";
        public static int VERSION = 1;
    }

    public static class Table {
        /**
         * 用户表的创建,暂时只有账户密码，用户头像使用blob二进制存储
         */
        public static String USER_TABLE_NAME = "UserInfo";
        public static String USER_TABLE_CREATE = "CREATE TABLE IF NOT EXITS"
                + USER_TABLE_NAME + "(_id INTEGER PRIMARY KEY, userCount TEXT, userPsd TEXT )";

    }
}
