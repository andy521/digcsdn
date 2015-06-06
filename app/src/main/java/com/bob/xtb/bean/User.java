package com.bob.xtb.bean;

/**
 * Created by bob on 15-4-22.
 */
public class User {
    private long id;//表的主键id
    private String userCount;
    private String userPsd;

    public User(){}
    public User(String userCount, String userPsd){
        this.userCount= userCount;
        this.userPsd= userPsd;
    }

    public static final String ID= "_id";//与数据表所对应的几个字段
    public static final String USER_COUNT= "userCount";
    public static final String USER_PSD= "userPsd";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getUserPsd() {
        return userPsd;
    }

    public void setUserPsd(String userPsd) {
        this.userPsd = userPsd;
    }
}
