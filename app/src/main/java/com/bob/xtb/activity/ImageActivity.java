package com.bob.xtb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bob.xtb.R;
import com.bob.xtb.util.FileUtil;
import com.bob.xtb.util.ImageLoading;
import com.polites.android.GestureImageView;

/**
 * 一个单纯的用于显示图片的活动，可以下载，当然啦，是黑色背景
 * Created by bob on 15-6-10.
 */
public class ImageActivity extends AppCompatActivity implements View.OnClickListener {
    private String url;//图片地址
    private GestureImageView imageView;//一个可以支持手势的ImageView的库,支持双手，单手的操作
    private ProgressBar progressBar;

    private ImageView btBack,btDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Bundle bundle= getIntent().getExtras();
        url= bundle.getString("url", "");

        imageView= (GestureImageView) findViewById(R.id.img_pic);
        progressBar= (ProgressBar) findViewById(R.id.pro_loading);

        btBack= (ImageView) findViewById(R.id.img_back);
        btDownload= (ImageView) findViewById(R.id.img_download);

        //listener需要通过imageLoader获取
        ImageLoading imageLoading= ImageLoading.getInstance(ImageActivity.this);
        boolean result= imageLoading.loadImage(url, imageView, R.mipmap.ic_default, R.mipmap.ic_default);
        if (result)
            progressBar.setVisibility(View.GONE);
        else Toast.makeText(ImageActivity.this, "网络信号不佳", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_download: {
                imageView.setDrawingCacheEnabled(true);//使用缓存技术
                if (FileUtil.write2SdCard(imageView.getDrawingCache(), url)){//这个是保存到本地的方法
                    Toast.makeText(ImageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ImageActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
                imageView.setDrawingCacheEnabled(false);//关闭缓存
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_no, R.anim.push_right_out);//设置退出动画
    }
}
