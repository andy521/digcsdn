package com.bob.digcsdn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bob.digcsdn.R;
import com.bob.digcsdn.bean.BlogItem;
import com.bob.digcsdn.util.ImageLoading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 15-5-10.
 */
public class BlogListAdapter extends BaseAdapter {
    private ViewHolder holder ;//视图容器
    private LayoutInflater layoutInflater;//布局加载器
    private Context context;
    private List<BlogItem> list;//资源列表

    public BlogListAdapter(Context context){
        super();
        this.context= context;
        list= new ArrayList();
        layoutInflater= LayoutInflater.from(context);
    }

    public void setList(List list) {
        this.list= list;
    }

    public void addList(List list) {
        this.list.addAll(list);
    }

    public void clearList() {
        this.list.clear();
    }

    public List<BlogItem> getList() {
        return list;
    }

    public void removeItem(int position) {//position只能在0～size-1范围上
        if (this.list.size()-1 > position) {
            this.list.remove(position);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //灵感来自《第一行代码》
        if (convertView == null) {//空说明第一次加载当前条目
            holder = new ViewHolder();//构建布局容器
            convertView = layoutInflater.inflate(R.layout.blog_list_item, null);
            holder.title= (TextView) convertView.findViewById(R.id.title);
            holder.content= (TextView) convertView.findViewById(R.id.content);
            holder.date= (TextView) convertView.findViewById(R.id.date);
            holder.blogImg= (ImageView) convertView.findViewById(R.id.blogImg);
            convertView.setTag(holder);//将布局容器存入当前布局对象中
        } else {
            holder = (ViewHolder) convertView.getTag();//下次就可以直接获取容器了
            //避免反复去资源文件中加载widget
        }

        BlogItem item= list.get(position);
        if (item!= null){
            holder.title.setText(item.getTitle());
            holder.content.setText(item.getContent());
            holder.date.setText(item.getDate());
            if (item.getImgLink()!= null){
                ImageLoading.getInstance(context).loadImage(item.getImgLink(),
                        holder.blogImg, R.mipmap.csdn, R.mipmap.csdn);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView id;
        TextView date;
        TextView title;
        ImageView blogImg;
        TextView content;
    }
}
