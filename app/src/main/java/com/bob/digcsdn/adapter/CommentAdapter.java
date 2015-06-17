package com.bob.digcsdn.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bob.digcsdn.R;
import com.bob.digcsdn.bean.Comment;
import com.bob.digcsdn.util.Constants;
import com.bob.digcsdn.util.ImageLoading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 15-6-16.
 */
public class CommentAdapter extends BaseAdapter {
    private ViewHolder holder;//视图容器
    private LayoutInflater layoutInflater;//布局加载器
    private Context context;
    private List<Comment> list;//资源列表
    private String replyText;
    private ImageLoading imageLoader;

    public CommentAdapter(Context context){
        super();
        layoutInflater= layoutInflater.from(context);
        list= new ArrayList<>();
        imageLoader= ImageLoading.getInstance(context);
    }
    public void setList(List<Comment> list) {
        this.list = list;
    }

    public void addList(List<Comment> list) {
        this.list.addAll(list);
    }

    public void clearList() {
        this.list.clear();
    }

    public List<Comment> getList() {
        return list;
    }

    public void removeItem(int position) {
        if (list.size() > 0) {
            list.remove(position);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {//0~size-1
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment item = list.get(position);
        if (null== convertView) {
            holder = new ViewHolder();
            switch (item.getType()) {
                case Constants.DEF_COMMENT_TYPE.PARENT:
                    convertView = layoutInflater.inflate(R.layout.comment_item, null);
                    holder.name = (TextView) convertView.findViewById(R.id.name);//用户名
                    holder.content = (TextView) convertView.findViewById(R.id.content);//评论内容
                    holder.date = (TextView) convertView.findViewById(R.id.date);
                    holder.replyCount = (TextView) convertView.findViewById(R.id.replyCount);
                    holder.userFace = (ImageView) convertView.findViewById(R.id.userface);
                    break;
                case Constants.DEF_COMMENT_TYPE.CHILD:
                    convertView = layoutInflater.inflate(R.layout.comment_child_item, null);
                    holder.name = (TextView) convertView.findViewById(R.id.name);//用户名
                    holder.content = (TextView) convertView.findViewById(R.id.content);//评论内容
                    holder.date = (TextView) convertView.findViewById(R.id.date);
                    break;
            }
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        if (null != item) {
            switch (item.getType()) {
                case Constants.DEF_COMMENT_TYPE.PARENT:
                    holder.name.setText(item.getUsername());
                    holder.content.setText(Html.fromHtml(item.getContent()));//使用formHtml格式化文本内容
                    holder.date.setText(item.getPostTime());
                    imageLoader.loadImage(item.getUserface(), holder.userFace, R.mipmap.csdn, R.mipmap.ic_default);// 显示头像
                    break;
                case Constants.DEF_COMMENT_TYPE.CHILD://评论不显示头像
                    holder.name.setText(item.getUsername());
                    replyText = item.getContent().replace("[reply]", "【");
                    replyText = replyText.replace("[/reply]", "】");
                    holder.content.setText(Html.fromHtml(replyText));
                    holder.date.setText(item.getPostTime());
                    break;

            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView id;
        TextView date;//评论日期
        TextView name;//用户名称
        TextView content;//评论内容
        ImageView userFace;//用户头像
        TextView replyCount;//评论回复数量，貌似并没有什么卵用
    }
}
