package com.bob.xtb.interfaces;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bob.xtb.bean.BlogItem;

import java.util.List;

/**
 * Created by bob on 15-5-10.
 */
public abstract class IAdapter extends BaseAdapter{//我们只能通过处理抽象类BaseAdapter来解决适配问题，因此接口解决不了问题

    public abstract void setList(List<?> list);//重置列表，刷新用
    public abstract void addList(List<?> list);//添加列表内容
    public abstract void clearList();//清除列表
    public abstract List<BlogItem> getList();//获取列表
    public abstract void removeItem(int position);

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
