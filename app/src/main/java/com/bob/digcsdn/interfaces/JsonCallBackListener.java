package com.bob.digcsdn.interfaces;

import com.bob.digcsdn.models.BlogItem;

import java.util.List;

/**
 * Created by bob on 15-6-14.
 */
public interface JsonCallBackListener {
    void onFinish(List<BlogItem> list);
}
