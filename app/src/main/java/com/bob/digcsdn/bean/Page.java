package com.bob.digcsdn.bean;

/**
 * 页面实体类，神马鬼？？？
 */
public class Page {
    private int page = 1; // 记录页面数

    public boolean contentFirstPage = true; // 是否为内容第一页

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
