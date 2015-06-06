package com.bob.xtb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bob.xtb.R;
import com.bob.xtb.bean.Task;
import com.bob.xtb.interfaces.IAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 15-5-10.
 */
public class TaskAdapter extends IAdapter{
    private ViewHolder holder ;//视图容器
    private LayoutInflater layoutInflater;//布局加载器
    private Context context;
    private List list;//任务列表

    public TaskAdapter(Context context){
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
        Task task= (Task)list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder.taskId = (TextView) convertView.findViewById(R.id.tv_id);
            holder.taskName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.taskDesc = (TextView) convertView.findViewById(R.id.tv_desc_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.taskId.setText(task.getTaskId());
        holder.taskName.setText(task.getTaskName());
        holder.taskDesc.setText(task.getTaskDesc());
        return convertView;
    }

    private class ViewHolder {
        TextView taskId,taskName,taskDesc;
    }
}