package com.bob.digcsdn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bob.digcsdn.adapters.BlogDetailAdapter;
import com.bob.digcsdn.models.Blog;
import com.bob.digcsdn.utils.Constants;
import com.bob.digcsdn.utils.JsoupUtil;
import com.bob.digcsdn.utils.LogUtil;
import com.bob.digcsdn.utils.VolleyUtil;
import com.bob.digcsdn.view.LoadMoreListView;

import java.util.List;

/**
 * Created by bob on 15-6-8.
 */
public class BlogDetailActivity extends BaseActivity implements View.OnClickListener, LoadMoreListView.OnLoadMoreListener {
    private LoadMoreListView listView;
    private BlogDetailAdapter blogDetailAdapter;

    private ProgressBar progressBar;
    private View reloadView;
    private Button reloadBtn;

    private View backBtn, commentBtn;//评论和回退按钮
    private TextView titleTv;
    private String blogTitle;

    public String url;
    private String fileName;

    public static final int FIRST = 0;
    public static final int NOT_FIRST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        removeDefaultToolbar();
        init();
        initWidget();
        initEvent();
        executeRefresh(FIRST);
    }

    // 初始化成员变量
    private void init() {
        blogDetailAdapter = new BlogDetailAdapter(this);
        url = getIntent().getExtras().getString("blogLink");
        fileName = url.substring(url.lastIndexOf("/") + 1);//fileName表示的是博客链接中最后一个节点，即博客代号
        blogTitle= getIntent().getExtras().getString("blogTitle");
        if (blogTitle.length()> 7)
            blogTitle= blogTitle.substring(0,7)+"...";
    }

    //初始化控件
    private void initWidget() {
        progressBar = (ProgressBar) findViewById(R.id.pro_common_content);
        reloadBtn = (Button) findViewById(R.id.bt_article_reLoad);
        reloadView = findViewById(R.id.ll_article_reLoad);
        backBtn =  findViewById(R.id.img_article_detail_back);
        commentBtn = findViewById(R.id.img_comment);
        titleTv= (TextView) findViewById(R.id.article_title);
        titleTv.setVisibility(View.VISIBLE);
        titleTv.setText(blogTitle);
        LogUtil.i("title", titleTv.getText().toString());

        listView = (LoadMoreListView) findViewById(R.id.list_article_view);
        listView.setAdapter(blogDetailAdapter);
    }

    private void initEvent() {
        progressBar.setOnClickListener(this);
        reloadBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        commentBtn.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击列表项的状态
                int state = blogDetailAdapter.getList().get(position)
                        .getState();
                switch (state) {
                    case Constants.DEF_BLOG_ITEM_TYPE.IMG: // 点击的是图片
                        String url = blogDetailAdapter.getList().get(position)
                                .getImgLink();

                        Intent intent = new Intent(BlogDetailActivity.this, ImageActivity.class);
                        intent.putExtra("url", url);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_article_reLoad:
                executeRefresh(FIRST);
                break;
            case R.id.img_article_detail_back:
                finish();
                break;
            case R.id.img_comment:{
                Intent intent= new Intent(BlogDetailActivity.this, CommentsActivity.class);
                intent.putExtra("fileName", fileName);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_no);
            }
        }
    }

    @Override
    public void onLoadMore() {
        if (!JsoupUtil.contentLastPage) {//不是最后一页，才进行加载
            executeRefresh(NOT_FIRST);
        } else listView.setCanLoadMore(false);
    }

    private void executeRefresh(final int refreshType){
        StringRequest htmlRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String html) {
                List<Blog> blogs= JsoupUtil.getContent(html);
                if (blogs.size()== 0){
                    if (refreshType== FIRST){
                        Toast.makeText(getApplicationContext(), "网络信号不佳",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        reloadView.setVisibility(View.VISIBLE);
                    }
                    else listView.setCanLoadMore(false);
                }

                blogDetailAdapter.addList(blogs);
                blogDetailAdapter.notifyDataSetChanged();
                if (refreshType== NOT_FIRST)
                    listView.setCanLoadMore(false);

                progressBar.setVisibility(View.INVISIBLE);
                reloadView.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.INVISIBLE);
                reloadView.setVisibility(View.VISIBLE);
            }
        });
        VolleyUtil.getQueue().add(htmlRequest);
    }
}
