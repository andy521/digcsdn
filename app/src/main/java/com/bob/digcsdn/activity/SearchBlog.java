package com.bob.digcsdn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bob.digcsdn.R;
import com.bob.digcsdn.adapter.BlogListAdapter;
import com.bob.digcsdn.bean.BlogItem;
import com.bob.digcsdn.bean.Page;
import com.bob.digcsdn.interfaces.JsonCallBackListener;
import com.bob.digcsdn.util.Constants;
import com.bob.digcsdn.util.JsoupUtil;
import com.bob.digcsdn.util.LogUtil;
import com.bob.digcsdn.util.UrlUtil;
import com.bob.digcsdn.util.VolleyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 15-6-18.
 */
public class SearchBlog extends Activity {

    private ProgressBar progressBar;//进度条预览
    private View backBtn;

    private ListView blogListView;  //具有上拉加载的ListView
    private BlogListAdapter adapter; //数据适配器
    private List<BlogItem> allList;
    private String blogTitle;//被查询的博客标题
    private Page page;//有甚叼用暂时未知！！！浏览器的博客列表是分页显示的
    private boolean isFinish= false;//是否加载完毕

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_blog);
        init();
        initWidget();
        while (!isFinish) {
            LogUtil.i("execu", "jinlaile");
            executeTask(UrlUtil.getBlogListURL(Constants.DEF_ARTICLE_TYPE.HOME,page.getCurrentPage()));
            page.addPage();
        }
        adapter.setList(query(blogTitle));
        adapter.notifyDataSetChanged();
    }

    private void init() {
        adapter = new BlogListAdapter(this);
        allList = new ArrayList<>();
        blogTitle = getIntent().getExtras().getString("query");
        page = new Page();
        LogUtil.i("init", "coming");
    }

    private void initWidget() {
        progressBar = (ProgressBar) findViewById(R.id.pro_common_content);
        backBtn = findViewById(R.id.bt_back);
        blogListView = (ListView) findViewById(R.id.list_search_view);
        blogListView.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        blogListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BlogItem blogItem = (BlogItem) adapter.getItem(position);//获取博客对象
                Intent intent = new Intent(SearchBlog.this, BlogDetailActivity.class);
                intent.putExtra("blogLink", blogItem.getLink());
                startActivity(intent);

                /**
                 * 用于设置当前活动出现或者退出的动画，放在startActivity和finish之后
                 */
                overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
            }
        });
    }

    public List<BlogItem> query(String title){//查询就先这么写了
        List<BlogItem> result= new ArrayList<>();
        for (BlogItem item: allList){
            if (item.getTitle().contains("title"))
                result.add(item);
        }
        return result;
    }


    public void executeTask(String url) {

        StringRequest htmlRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String html) {//主线程里
                progressBar.setVisibility(View.INVISIBLE);

                JsoupUtil.getBlogItemList(Constants.DEF_ARTICLE_TYPE.HOME, html, new JsonCallBackListener() {
                    @Override
                    public void onFinish(final List<BlogItem> list) {//子线程里

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {//主线程里
                                if (list.size() == 0 || list.size() > 20) {//重复或者空列表，则停止加载
                                    isFinish= true;
                                }
                                /**
                                 * 在主页中加载超页之后，不再分页显示,分页的时候按照每页15条显示
                                 */
                                LogUtil.i("加载的数量", list.size() + "");
                                if (list.size() <= 20) {
                                    allList.addAll(list);
                                }
                            }
                        });
                    }
                });//Jsoup解析html
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SearchBlog.this, "网络信号不佳", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        VolleyUtil.getQueue().add(htmlRequest);//队列里肯定是按顺序执行的，也不用担心多线程访问的线程安全问题
    }
}
