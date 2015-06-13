package com.bob.digcsdn.util;

/**
 * Created by bob on 15-6-13.
 */
public class UrlUtil {
    public static String BASE_URL = "http://blog.csdn.net/bob1993_dev/article/list";//首页目录
    public static String ARTICLE_URL_DAILY_LIFE = "http://blog.csdn.net/bob1993_dev/article/category/2794673";
    public static String ARTICLE_URL_ANDROID = "http://blog.csdn.net/bob1993_dev/article/category/2794675";
    public static String ARTICLE_URL_JAVA_ADVANCE = "http://blog.csdn.net/bob1993_dev/article/category/2794677";
    public static String ARTICLE_URL_DATA_STRUCTURE = "http://blog.csdn.net/bob1993_dev/article/category/2794683";
    public static String ARTICLE_URL_WINDOWS = "http://blog.csdn.net/bob1993_dev/article/category/2897663";
    public static String ARTICLE_URL_MAC = "http://blog.csdn.net/bob1993_dev/article/category/2897679";
    public static String ARTICLE_URL_LINUX = "http://blog.csdn.net/bob1993_dev/article/category/2897681";
    public static String ARTICLE_URL_MASTER = "http://blog.csdn.net/bob1993_dev/article/category/2919919";
    public static String ARTICLE_URL_PHOTOGRAPH = "http://blog.csdn.net/bob1993_dev/article/category/5421379";
    public static String ARTICLE_URL_JAPANESE = "http://blog.csdn.net/bob1993_dev/article/category/2967633";

    /**
     * 获取博客列表的URL
     *
     * @param blogType 博客类型
     * @param page     页数
     * @return
     */
    public static String getBlogListURL(int blogType, String page) {
        String url = "";
        switch (blogType) {
            case Constants.DEF_ARTICLE_TYPE.HOME:
                url = BASE_URL;
                break;
            case Constants.DEF_ARTICLE_TYPE.DAILY_LIFE:
                url = ARTICLE_URL_DAILY_LIFE;
                break;
            case Constants.DEF_ARTICLE_TYPE.ANDROID:
                url = ARTICLE_URL_ANDROID;
                break;
            case Constants.DEF_ARTICLE_TYPE.JAVA:
                url = ARTICLE_URL_JAVA_ADVANCE;
                break;
            case Constants.DEF_ARTICLE_TYPE.DATA_STRUCTURE:
                url = ARTICLE_URL_DATA_STRUCTURE;
                break;
            case Constants.DEF_ARTICLE_TYPE.WINDOWS:
                url = ARTICLE_URL_WINDOWS;
                break;
            case Constants.DEF_ARTICLE_TYPE.LINUX:
                url = ARTICLE_URL_LINUX;
                break;
            case Constants.DEF_ARTICLE_TYPE.MAC:
                url = ARTICLE_URL_MAC;
                break;
            case Constants.DEF_ARTICLE_TYPE.MASTER:
                url = ARTICLE_URL_MASTER;
                break;
            case Constants.DEF_ARTICLE_TYPE.PHOTOGRAPH:
                url = ARTICLE_URL_PHOTOGRAPH;
                break;
            case Constants.DEF_ARTICLE_TYPE.JAPANESE:
                url = ARTICLE_URL_JAPANESE;
                break;
            default:
                break;
        }
        url = url + "/" + page;
        return url;
    }

    /**
     * 获取刷新博客的URL,与上边获取url很类似，只是对页数的要求不同而已
     *
     * @param blogType 博客类型
     * @return
     */
    public static String getRefreshBlogListURL(int blogType) {
        String url = "";
        switch (blogType) {
            case Constants.DEF_ARTICLE_TYPE.HOME:
                url = BASE_URL;
                break;
            case Constants.DEF_ARTICLE_TYPE.DAILY_LIFE:
                url = ARTICLE_URL_DAILY_LIFE;
                break;
            case Constants.DEF_ARTICLE_TYPE.ANDROID:
                url = ARTICLE_URL_ANDROID;
                break;
            case Constants.DEF_ARTICLE_TYPE.JAVA:
                url = ARTICLE_URL_JAVA_ADVANCE;
                break;
            case Constants.DEF_ARTICLE_TYPE.DATA_STRUCTURE:
                url = ARTICLE_URL_DATA_STRUCTURE;
                break;
            case Constants.DEF_ARTICLE_TYPE.WINDOWS:
                url = ARTICLE_URL_WINDOWS;
                break;
            case Constants.DEF_ARTICLE_TYPE.LINUX:
                url = ARTICLE_URL_LINUX;
                break;
            case Constants.DEF_ARTICLE_TYPE.MAC:
                url = ARTICLE_URL_MAC;
                break;
            case Constants.DEF_ARTICLE_TYPE.MASTER:
                url = ARTICLE_URL_MASTER;
                break;
            case Constants.DEF_ARTICLE_TYPE.PHOTOGRAPH:
                url = ARTICLE_URL_PHOTOGRAPH;
                break;
            case Constants.DEF_ARTICLE_TYPE.JAPANESE:
                url = ARTICLE_URL_JAPANESE;
                break;
            default:
                break;
        }
        url = url + "/1";
        return url;
    }

    /**
     * 返回博文评论列表链接
     *
     * @param filename  文件名
     * @param pageIndex 页数
     * @return
     */
    public static String getCommentListURL(String filename, String pageIndex) {
        return "http://blog.csdn.net/wwj_748/comment/list/" + filename
                + "?page=" + pageIndex;
    }

}
