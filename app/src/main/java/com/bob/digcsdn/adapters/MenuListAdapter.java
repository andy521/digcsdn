package com.bob.digcsdn.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bob.digcsdn.R;

/**
 * Created by bob on 15-5-10.
 */
public class MenuListAdapter extends BaseAdapter {
    private String[] titles = {"个人设置", "切换用户", "检查更新", "关于"};
    private int[] icons = {R.drawable.settings, R.drawable.changeuser,
            R.drawable.update, R.drawable.about};
    private Context context;

    public MenuListAdapter(Context context){
        this.context= context;
    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {//返回当前实例，没有就返回null
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;//灵感来自《第一行代码》,这里其实并不需要使用到holder缓存，因为它知识一个菜单
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.menulist_item, null);
            holder.title = (TextView) view.findViewById(R.id.tv_menu);
            holder.image= (ImageView) view.findViewById(R.id.img_menu);
            view.setTag(holder);//将holder装入view中
        } else {
            holder = (ViewHolder) view.getTag();//获取holder缓存
        }

        holder.title.setText(titles[i]);
        holder.image.setImageResource(icons[i]);
        return view;
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
    }
}
