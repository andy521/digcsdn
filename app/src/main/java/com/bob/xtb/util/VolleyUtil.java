package com.bob.xtb.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by bob on 15-6-11.
 * 提供请求队列queue的同时兼任了获取全局context的责任
 * 一般情况下，Activity的context适用于UI相关的操作，而Application则是适用于非UI的操作
 */
public class VolleyUtil extends Activity{
    private static RequestQueue queue;
    private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        context= getApplicationContext();
        queue= Volley.newRequestQueue(context);
    }

    public static RequestQueue getQueue() {
        return queue;
    }

    public static Context getContext() {
        return context;
    }
}
