package com.bob.xtb.interfaces;

import android.graphics.Bitmap;

/**
 * Created by bob on 15-4-18.
 */
public interface IVerify {
     boolean checkCode(String input);//接口中默认的方法属性是public abstract
     Bitmap createBitmap();
    }
