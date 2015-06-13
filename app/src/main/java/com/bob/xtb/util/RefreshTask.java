package com.bob.xtb.util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bob.xtb.adapter.BlogListAdapter;
import com.bob.xtb.bean.BlogItem;
import com.bob.xtb.db.BlogService;
import com.bob.xtb.fragment.BlogFragment;
import com.bob.xtb.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 15-5-10.
 */
public class RefreshTask{

    private BlogListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView listView;
    private BlogService service;
    private Context context;
    private int blogType= 0;//默认博客类型为首页

    public static final int REFRESH = 0;
    public static final int LOAD= 1;

    public RefreshTask(Context context, BlogService service, int blogType, BlogListAdapter adapter, SwipeRefreshLayout swipeLayout, LoadMoreListView listView) {
        this.context = context;
        this.blogType = blogType;
        this.adapter = adapter;
        this.swipeLayout = swipeLayout;
        this.listView = listView;
        this.service= service;
    }

    public void execute(String url, final int taskType){
        StringRequest htmlRequest= new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String html) {
                LogUtil.i("html", html);//已经可以获取html了
                List<BlogItem> list= JsoupUtil.getBlogItemList(blogType, html);//Jsoup解析html
                if (list.size()== 0){
                    listView.setCanLoadMore(false);//停止加载
                }
                if (taskType== REFRESH) {
                    adapter.setList(list);//将listView里的数据重置为解析得到的list
                    adapter.notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);//刷新完毕，停止刷新动画
                    service.delete(blogType);//先删除库中已有的博客内容
                    service.insert(adapter.getList());//存库

                    if (adapter.getCount()== 0)
                        BlogFragment.noBlogLayout.setVisibility(View.VISIBLE);
                }
                else{
                    adapter.addList(list);//模拟添加数据，不属于耗时任务，可以交给主线程完成
                    listView.onLoadMoreComplete();//本次加载完毕
                    BlogFragment.page.addPage();//加载完毕后增加一页
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "网络信号不佳", Toast.LENGTH_SHORT);
                swipeLayout.setRefreshing(false);
                listView.setCanLoadMore(false);//设置为不可加载状态
            }
        });
        if (VolleyUtil.getContext()== null){
            LogUtil.i("queue", "is empty");
        }
        VolleyUtil.getQueue().add(htmlRequest);//队列里肯定是按顺序执行的，也不用担心多线程访问的线程安全问题
    }
}