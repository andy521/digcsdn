package com.bob.xtb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bob.xtb.R;
import com.bob.xtb.adapter.BlogListAdapter;
import com.bob.xtb.util.RefreshTask;
import com.bob.xtb.view.LoadMoreListView;

/**
 * Created by bob on 15-4-21.
 */
public class BlogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {
    private int blogType;

    private SwipeRefreshLayout swipeLayout;  //系统带的下拉刷新控件
    private BlogListAdapter adapter; //数据适配器
    private LoadMoreListView listView;  //具有上拉加载的ListView

    public BlogFragment(int blogType) {
        this.blogType = blogType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog_main, container, false);//最后一项为是否有父布局，设为false
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);//下拉组件的事件监听


        // set style for swipeRefreshLayout
        swipeLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright);
        listView = (LoadMoreListView) getView().findViewById(R.id.list_view);
        listView.setOnLoadMoreListener(this);//为listView添加加载监听
        adapter = new BlogListAdapter(getActivity());
        listView.setAdapter(adapter);//设置数据适配器

    }


    @Override
    public void onRefresh() {//刷新监听
        new RefreshTask(getActivity(), "res", adapter, swipeLayout, listView).
                execute("www.baidu.com", "refresh");
    }

    @Override
    public void onLoadMore() {//加载监听
        new RefreshTask(getActivity(), "res", adapter, swipeLayout, listView).
                execute("www.udiab.com", "load");
    }
}
