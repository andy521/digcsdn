package com.bob.xtb.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.*;
import com.android.volley.toolbox.Volley;

/**
 * Created by bob on 15-6-10.
 */
public class ImageLoading {

    private ImageLoader imageLoader;//图片加载器
    private static int maxSize= 10 * 1024 * 1024;//10mb
    private static ImageLoading imageLoading;

    private ImageLoading(ImageLoader imageLoader){
        this.imageLoader= imageLoader;
    }

    public synchronized static ImageLoading getInstance(Context context){
        if (imageLoading == null) {
            imageLoading = new ImageLoading(new ImageLoader(Volley.newRequestQueue(context), new ImageCache() {

                LruCache<String, Bitmap> mCache= new LruCache<String,Bitmap>(maxSize){//缓存总大小
                    @Override//匿名内部类继承LruCache类，即缓存池
                    protected int sizeOf(String key, Bitmap value) {
                        return value.getRowBytes()*value.getHeight();//返回图片大小
                    }
                };

                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
            }));
        }
        return imageLoading;
    }

    public boolean loadImage(String url, ImageView view, int defaultImg, int errorImg){
        try {
            imageLoader.get(url, imageLoader.getImageListener(view, defaultImg,errorImg));
        }catch (Exception e){
            return false;
        }
        return true;//加载成功
    }
}
