package com.bob.xtb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bob.xtb.R;
import com.bob.xtb.bean.Resource;
import com.bob.xtb.interfaces.IAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 15-5-10.
 */
public class ResourceAdapter extends IAdapter {
    private ViewHolder holder ;//视图容器
    private LayoutInflater layoutInflater;//布局加载器
    private Context context;
    private List list;//资源列表

    public ResourceAdapter(Context context){
        super();
        this.context= context;
        list= new ArrayList();
        layoutInflater= LayoutInflater.from(context);
    }

    @Override
    public void setList(List list) {
        this.list= list;
    }

    @Override
    public void addList(List list) {
        this.list.addAll(list);
    }

    @Override
    public void clearList() {
        this.list.clear();
    }

    @Override
    public List<?> getList() {
        return null;
    }

    @Override
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
        Resource task= (Resource)list.get(position);
        if (convertView == null) {//空说明第一次加载当前条目
            holder = new ViewHolder();//构建布局容器
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder.resourceId = (TextView) convertView.findViewById(R.id.tv_id);
            holder.resourceName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.resourceType = (TextView) convertView.findViewById(R.id.tv_desc_type);
            convertView.setTag(holder);//将布局容器存入当前布局对象中
        } else {
            holder = (ViewHolder) convertView.getTag();//下次就可以直接获取容器了
            //避免反复去资源文件中加载widget
        }
        holder.resourceId.setText(task.getResId());
        holder.resourceName.setText(task.getResName());
        holder.resourceType.setText(task.getResType());
        return convertView;
    }

    private class ViewHolder {
        TextView resourceId,resourceName,resourceType;
    }
}
