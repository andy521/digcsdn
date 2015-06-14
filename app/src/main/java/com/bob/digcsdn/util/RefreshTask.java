package com.bob.digcsdn.util;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bob.digcsdn.adapter.BlogListAdapter;
import com.bob.digcsdn.bean.BlogItem;
import com.bob.digcsdn.db.BlogService;
import com.bob.digcsdn.fragment.BlogFragment;
import com.bob.digcsdn.view.LoadMoreListView;

import java.util.List;

/**
 * Created by bob on 15-5-10.
 */
public class RefreshTask{

    private View noBlogLayout;
    private ProgressBar progressBar;
    private BlogListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView listView;
    private BlogService service;
    private Context context;
    private int blogType= 0;//默认博客类型为首页

    public static final int REFRESH = 0;
    public static final int LOAD= 1;

    public RefreshTask(Context context, BlogService service, int blogType, BlogListAdapter adapter, SwipeRefreshLayout swipeLayout, LoadMoreListView listView, View noBlogLayout, ProgressBar progressBar) {
        this.context = context;
        this.blogType = blogType;
        this.adapter = adapter;
        this.swipeLayout = swipeLayout;
        this.listView = listView;
        this.service= service;
        this.noBlogLayout= noBlogLayout;
        this.progressBar= progressBar;
    }

    public void execute(String url, final int taskType){
       // listView.setCanLoadMore(true);
        StringRequest htmlRequest= new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String html) {
                progressBar.setVisibility(View.INVISIBLE);
                listView.setCanLoadMore(true);
                noBlogLayout.setVisibility(View.INVISIBLE);
                List<BlogItem> list= JsoupUtil.getBlogItemList(blogType, html);//Jsoup解析html
                LogUtil.i("加载的数量", list.size()+"");
                if (list.size()== 0||list.size()> 15){//重复或者空列表，则停止加载
                    listView.setCanLoadMore(false);//停止加载
                }
                if (taskType== REFRESH) {//刷新,这里只会存上第一页的博客，因为在加载的时候，并没有存库
                    adapter.setList(list);//将listView里的数据重置为解析得到的list
                    adapter.notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);//刷新完毕，停止刷新动画
                    service.delete(blogType);//先删除库中已有的博客内容
                    service.insert(adapter.getList());//存库

                    if (adapter.getCount()== 0)
                        noBlogLayout.setVisibility(View.VISIBLE);
                }else{//加载
                    /**
                     * 在主页中加载超页之后，不再分页显示,分页的时候按照每页15条显示
                     */
                    LogUtil.i("加载的数量", list.size()+"");
                    if (list.size()<= 15) {
                        adapter.addList(list);
                        adapter.notifyDataSetChanged();
                    }
                    listView.onLoadMoreComplete();//本次加载完毕
                    BlogFragment.page.addPage();//加载完毕后指向下一页
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

        VolleyUtil.getQueue().add(htmlRequest);//队列里肯定是按顺序执行的，也不用担心多线程访问的线程安全问题
    }

}