package com.bob.digcsdn.models;

/**
 * 对应BlogFragment里的列表条目项
 * Created by bob on 15-6-8.
 */
public class BlogItem {
    private int id;         //数据库主键
    private String title;   //标题
    private String link;    //本篇文章的链接
    private String date;    //博客发布时间
    private String imgLink; //图片链接
    private String content; //文章摘要
    private String msg;     //消息,有待探讨
    private int blogType;       //博客分类

    public static final String ID= "_id";//这几个常量主要用户数据库的操作
    public static final String TITLE= "title";
    public static final String CONTENT= "content";
    public static final String DATE= "date";
    public static final String IMG= "img";
    public static final String LINK= "link";
    public static final String BLOGTYPE= "blogType";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBlogType() {
        return blogType;
    }

    public void setBlogType(int blogType) {
        this.blogType = blogType;
    }
}
