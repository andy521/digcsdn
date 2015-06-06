package com.bob.xtb.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.bob.xtb.bean.Resource;
import com.bob.xtb.bean.Task;
import com.bob.xtb.interfaces.IAdapter;
import com.bob.xtb.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 15-5-10.
 */
public class RefreshTask extends AsyncTask<String, Void, Integer> {//分别是传入参数类型，显示进度类型，返回结果码

    private IAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private LoadMoreListView listView;
    private Context context;
    private String type;
    private List temp ;

    public RefreshTask(Context context, String type, IAdapter adapter, SwipeRefreshLayout swipeLayout, LoadMoreListView listView) {
        this.context = context;
        this.type = type;
        this.adapter = adapter;
        this.swipeLayout = swipeLayout;
        this.listView = listView;

        temp= new ArrayList();
        if (type.equals("task")) {//添加任务
            for (int i = 0; i < 12; i++) {//模拟为从服务器获取到的数据
                Task task = new Task();
                task.setTaskId(i+"");
                task.setTaskName("Task" + i);
                task.setTaskDesc("Desc"+i);
                temp.add(task);
            }
        }else{//添加资源
            for (int i = 0; i < 12; i++) {
                Resource resource = new Resource();
                resource.setResId(i+"");
                resource.setResName("Res"+i);
                resource.setResType("Type"+i);
                temp.add(resource);
            }
        }
    }

    @Override
    protected Integer doInBackground(String... params) {//子线程中的耗时任务
        //我们给params[0]存放访问的URL,params[1]存放任务类型
        try {//这是模拟获取数据的耗时任务
            Thread.sleep(1 * 1300);    //sleep 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Constants.TASK_RESULT_CODE.ERROR;
        }

        if (params[1].equals("refresh")) {
            adapter.setList(temp);//模拟重新刷新数据
            return Constants.TASK_RESULT_CODE.REFRESH;
        } else {
            adapter.addList(temp);//模拟添加数据，不属于耗时任务，可以交给主线程完成
            return Constants.TASK_RESULT_CODE.LOAD;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {//UI主线程回调方法
        adapter.notifyDataSetChanged();
        switch (result) {
            case Constants.TASK_RESULT_CODE.ERROR:
                Toast.makeText(context, "网络信号不佳", Toast.LENGTH_SHORT);
                swipeLayout.setRefreshing(false);
                listView.setCanLoadMore(false);//设置为不可刷新状态
                break;
            case Constants.TASK_RESULT_CODE.REFRESH:
                listView.setCanLoadMore(adapter.getCount() < 45);
                swipeLayout.setRefreshing(false);//刷新完毕
                break;
            case Constants.TASK_RESULT_CODE.LOAD:
                listView.setCanLoadMore(adapter.getCount() < 45);//大于45设置为不可加载状态
                listView.onLoadMoreComplete();//加载完毕
        }
        super.onPostExecute(result);//还得照顾父类默认的回调方法
    }
}