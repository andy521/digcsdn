package com.bob.digcsdn.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bob.digcsdn.R;
import com.bob.digcsdn.activity.BlogDetailActivity;
import com.bob.digcsdn.adapter.BlogListAdapter;
import com.bob.digcsdn.bean.BlogItem;
import com.bob.digcsdn.bean.Page;
import com.bob.digcsdn.db.BlogService;
import com.bob.digcsdn.util.LogUtil;
import com.bob.digcsdn.util.RefreshTask;
import com.bob.digcsdn.util.UrlUtil;
import com.bob.digcsdn.view.LoadMoreListView;

/**
 * Created by bob on 15-4-21.
 */
public class BlogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {
    private int blogType = 0;//默认的是首页
    private boolean isLoad = false;//是否正在处于加载

    private View noBlogLayout;//无数据显示
    private ProgressBar progressBar;//进度条预览
    private SwipeRefreshLayout swipeLayout;  //系统带的下拉刷新控件
    private LoadMoreListView blogListView;  //具有上拉加载的ListView
    private BlogListAdapter adapter; //数据适配器
    private BlogService blogService;//博客数据库服务
    public static Page page;//有甚叼用暂时未知！！！浏览器的博客列表是分页显示的

    public BlogFragment(int blogType) {
        this.blogType = blogType;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog_main, container, false);//最后一项为是否有父布局，设为false
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();//初始化控件
        if (!isLoad) {
            LogUtil.i("flag", "into " + blogType);
            isLoad = true;
            /**
             * 这里只需要在第一次进入的时候访问网络加载数据即可，二次回来的时候，成员变量不会被销毁
             * 因此adapter只要保存完好，数据就不会丢失
             */
            onRefresh();
        } else {
            LogUtil.i("back", "into " + blogType);
            progressBar.setVisibility(View.INVISIBLE);
            if (blogService.loadBlog(blogType).size() == 0)
                noBlogLayout.setVisibility(View.VISIBLE);//恢复noBlogLayout应有的状态
        }

    }

    private void init() {
        blogService = BlogService.getInstance(getActivity());
        adapter = new BlogListAdapter(getActivity());
        page = new Page();
        page.setPageStart();//从第二页开始
    }

    private void initWidget() {
        progressBar = (ProgressBar) getView().findViewById(R.id.pro_common_content);
        swipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);//下拉组件的事件监听

        // set style for swipeRefreshLayout
        swipeLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright);
        blogListView = (LoadMoreListView) getView().findViewById(R.id.list_view);
        blogListView.setOnLoadMoreListener(this);//为listView添加加载监听
        blogListView.setAdapter(adapter);//设置数据适配器

        blogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == adapter.getCount()) {
                    Toast.makeText(getActivity(), "再点割 jj！！！", Toast.LENGTH_SHORT).show();
                } else {
                    BlogItem blogItem = (BlogItem) adapter.getItem(position);//获取博客对象
                    Intent intent = new Intent(getActivity(), BlogDetailActivity.class);
                    intent.putExtra("blogLink", blogItem.getLink());
                    startActivity(intent);

                    /**
                     * 用于设置当前活动出现或者退出的动画，放在startActivity和finish之后
                     */
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
                }

            }
        });

        noBlogLayout = getView().findViewById(R.id.ll_noBlog);
    }


    @Override
    public void onRefresh() {//刷新监听
        page.setPageStart();//默认从第二页开始
        new RefreshTask(getActivity(), blogService, blogType, adapter, swipeLayout, blogListView, noBlogLayout, progressBar).
                execute(UrlUtil.getRefreshBlogListURL(blogType), RefreshTask.REFRESH);
    }

    @Override
    public void onLoadMore() {//加载监听
        Log.i("refreshitem", blogType + " " + page.getCurrentPage());
        new RefreshTask(getActivity(), blogService, blogType, adapter, swipeLayout, blogListView, noBlogLayout, progressBar).
                execute(UrlUtil.getBlogListURL(blogType, page.getCurrentPage()), RefreshTask.LOAD);
    }
}
