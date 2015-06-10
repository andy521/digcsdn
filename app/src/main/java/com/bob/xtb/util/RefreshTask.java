package com.bob.xtb.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

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
public class RefreshTask extends AsyncTask<String, Void, Integer> {//分别是传入参数类型，显示进度类型，返回结果码

    private BlogListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView listView;
    private BlogService service;
    private Context context;
    private int blogType= 0;//默认博客类型为首页

    public RefreshTask(Context context, BlogService service, int blogType, BlogListAdapter adapter, SwipeRefreshLayout swipeLayout, LoadMoreListView listView) {
        this.context = context;
        this.blogType = blogType;
        this.adapter = adapter;
        this.swipeLayout = swipeLayout;
        this.listView = listView;
        this.service= service;
    }

    @Override
    protected Integer doInBackground(String... params) {//子线程中的耗时任务
        //我们给params[0]存放访问的URL,params[1]存放任务类型
        try {//这是模拟获取数据的耗时任务
            Thread.sleep(1 * 1300);    //sleep 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Constants.TASK_RESULT_CODE.ERROR;
        }

        //请求网页内容，使用基本的HttpURlConnection
        String temp= "";
        if (temp== null){//请求到的html网页字符串
            return Constants.TASK_RESULT_CODE.ERROR;
        }

        List<BlogItem> list= new ArrayList<>();//使用Jsoup解析html字符串获取博客列表对象

        if (list.size()== 0){
            return Constants.TASK_RESULT_CODE.NO_DATA;//解析得到的博客列表条数为0,显示无数据
        }

        //开始刷新
        if (params[1].equals("refresh")) {
            adapter.setList(list);//将listView里的数据重置为解析得到的list
            return Constants.TASK_RESULT_CODE.REFRESH;
        } else {
            adapter.addList(list);//模拟添加数据，不属于耗时任务，可以交给主线程完成
            return Constants.TASK_RESULT_CODE.LOAD;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {//UI主线程回调方法
        //这一步只需要放到主线程里即可
        adapter.notifyDataSetChanged();

        switch (result) {
            case Constants.TASK_RESULT_CODE.ERROR:
                Toast.makeText(context, "网络信号不佳", Toast.LENGTH_SHORT);
                swipeLayout.setRefreshing(false);
                listView.setCanLoadMore(false);//设置为不可加载状态
                break;

            case Constants.TASK_RESULT_CODE.NO_DATA:
                listView.setCanLoadMore(false);//停止加载
                break;

            case Constants.TASK_RESULT_CODE.REFRESH:
                swipeLayout.setRefreshing(false);//刷新完毕，停止刷新动画
                service.delete(blogType);//先删除库中已有的博客内容
                service.insert(adapter.getList());//存库

                if (adapter.getCount()== 0)
                    BlogFragment.noBlogLayout.setVisibility(View.VISIBLE);
                break;

            case Constants.TASK_RESULT_CODE.LOAD:
                listView.onLoadMoreComplete();//本次加载完毕
                BlogFragment.page.addPage();//加载完毕后增加一页
        }
        super.onPostExecute(result);//还得照顾父类默认的回调方法
    }
}