package com.bob.digcsdn.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bob.digcsdn.R;
import com.bob.digcsdn.util.FileUtil;
import com.bob.digcsdn.util.ImageLoading;
import com.polites.android.GestureImageView;

/**
 * 一个单纯的用于显示图片的活动，可以下载，当然啦，是黑色背景
 * Created by bob on 15-6-10.
 */
public class ImageActivity extends AppCompatActivity {
    private String url;//图片地址
    private ImageView imageView;//一个可以支持手势的ImageView的库,支持双手，单手的操作(GestureImageView不支持长按事件，放弃)
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        init();

        //listener需要通过imageLoader获取
        ImageLoading imageLoading = ImageLoading.getInstance(ImageActivity.this);
        boolean result = imageLoading.loadImage(url, imageView, R.mipmap.ic_default, R.mipmap.ic_default);
        if (result)
            progressBar.setVisibility(View.GONE);
        else Toast.makeText(ImageActivity.this, "网络信号不佳", Toast.LENGTH_SHORT).show();
    }

    private void init(){
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url", "");

        imageView = (ImageView) findViewById(R.id.img_pic);
        this.registerForContextMenu(imageView);
        progressBar = (ProgressBar) findViewById(R.id.pro_loading);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_no, R.anim.push_right_out);//设置退出动画
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_image_download, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi1:
                finish();
                break;
            case R.id.mi2: {
                imageView.setDrawingCacheEnabled(true);//使用缓存技术
                if (FileUtil.write2SdCard(imageView.getDrawingCache(), url)) {//这个是保存到本地的方法
                    Toast.makeText(ImageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImageActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
                imageView.setDrawingCacheEnabled(false);//关闭缓存
            }break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
}
