package com.bob.digcsdn.fragments;


import android.annotation.SuppressLint;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bob.digcsdn.R;
import com.bob.digcsdn.BlogDetailActivity;
import com.bob.digcsdn.adapters.BlogListAdapter;
import com.bob.digcsdn.adapters.TabPagerAdapter;
import com.bob.digcsdn.models.BlogItem;
import com.bob.digcsdn.models.Page;
import com.bob.digcsdn.db.BlogService;
import com.bob.digcsdn.interfaces.JsonCallBackListener;
import com.bob.digcsdn.utils.Constants;
import com.bob.digcsdn.utils.JsoupUtil;
import com.bob.digcsdn.utils.LogUtil;
import com.bob.digcsdn.utils.UrlUtil;
import com.bob.digcsdn.utils.VolleyUtil;
import com.bob.digcsdn.view.LoadMoreListView;

import java.util.List;

/**
 * Created by bob on 15-4-21.
 */
@SuppressLint("ValidFragment")
public class BlogFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {
    private int blogType = 0;//默认的是首页
    private boolean isLoad = false;//是否已经被加载过了
    private Boolean canLoadMore = true;

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
            LogUtil.i("flag", "into " + TabPagerAdapter.TITLE[blogType]);
            isLoad = true;
            /**
             * 这里只需要在第一次进入的时候访问网络加载数据即可，二次回来的时候，成员变量不会被销毁
             * 因此adapter只要保存完好，数据就不会丢失
             */
            adapter.setList(blogService.loadBlog(blogType));
            onRefresh();
        } else {
            LogUtil.i("back", "into " + blogType);
            blogListView.setCanLoadMore(canLoadMore);
            progressBar.setVisibility(View.INVISIBLE);
            if (blogService.loadBlog(blogType).size() == 0) {
                noBlogLayout.setVisibility(View.VISIBLE);//恢复noBlogLayout应有的状态
            }
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
                    intent.putExtra("blogTitle", blogItem.getTitle());
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


    public void executeTask(String url, final int taskType) {

        StringRequest htmlRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String html) {//主线程里
                progressBar.setVisibility(View.INVISIBLE);
                noBlogLayout.setVisibility(View.INVISIBLE);

                JsoupUtil.getBlogItemList(blogType, html, new JsonCallBackListener() {
                    @Override
                    public void onFinish(final List<BlogItem> list) {//子线程里，就这样把部分操作放到子线程里？？？
                        if (taskType == Constants.DEF_TASK_TYPE.REFRESH) {
                            /**
                             * 既然放到子线程里了，就应该注意线程同步安全的问题
                             */
                            adapter.setList(list);//将listView里的数据重置为解析得到的list
                            blogService.delete(blogType);//先删除库中已有的博客内容
                            blogService.insert(adapter.getList());//存库
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {//主线程里，也可以使用handler的post方法来切换到主线程中作业
                                if (list.size() == 0 || list.size() > 20) {//重复或者空列表，则停止加载
                                    blogListView.setCanLoadMore(false);//停止加载
                                    list.clear();
                                    canLoadMore= false;
                                }
                                if (taskType == Constants.DEF_TASK_TYPE.REFRESH) {//刷新,这里只会存上第一页的博客，因为在加载的时候，并没有存库
                                    blogListView.setCanLoadMore(true);
                                    adapter.notifyDataSetChanged();
                                    swipeLayout.setRefreshing(false);//刷新完毕，停止刷新动画

                                    if (adapter.getCount() == 0)
                                        noBlogLayout.setVisibility(View.VISIBLE);
                                } else {//加载
                                    /**
                                     * 在主页中加载超页之后，不再分页显示,分页的时候按照每页15条显示
                                     */
                                    LogUtil.i("加载的数量", list.size() + "");
                                    if (list.size() <= 20) {
                                        adapter.addList(list);
                                        adapter.notifyDataSetChanged();
                                    }
                                    blogListView.onLoadMoreComplete();//本次加载完毕
                                    page.addPage();//加载完毕后指向下一页
                                }
                            }
                        });
                    }
                });//Jsoup解析html
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "网络信号不佳", Toast.LENGTH_SHORT).show();
                blogListView.onLoadMoreComplete();
                swipeLayout.setRefreshing(false);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        VolleyUtil.getQueue().add(htmlRequest);//队列里肯定是按顺序执行的，也不用担心多线程访问的线程安全问题
    }

    @Override
    public void onRefresh() {//刷新监听
        page.setPageStart();//默认从第二页开始
        executeTask(UrlUtil.getRefreshBlogListURL(blogType), Constants.DEF_TASK_TYPE.REFRESH);
    }

    @Override
    public void onLoadMore() {//加载监听
        Log.i("refreshitem", blogType + " " + page.getCurrentPage());
        executeTask(UrlUtil.getBlogListURL(blogType, page.getCurrentPage()), Constants.DEF_TASK_TYPE.LOAD);
    }
}
