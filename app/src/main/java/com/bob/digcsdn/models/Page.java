package com.bob.digcsdn.models;

/**
 * 页面实体类，神马鬼？？？，用来记录当前申请url的页数，当然啦，前提是当前网页是分页式显示，比如列表和评论页
 */
public class Page {
    private int page = 1; // 记录页面数

    // 设置开始页面
    public void setPageStart() {//默认从第二页开始
        page = 2;
    }

    // 设置页
    public void setPage(int num) {
        page = num;
    }

    // 获取当前页
    public String getCurrentPage() {
        return page + "";
    }

    // 添加页面
    public void addPage() {
        page++;
    }
}
