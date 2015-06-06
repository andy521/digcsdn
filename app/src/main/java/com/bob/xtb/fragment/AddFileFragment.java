package com.bob.xtb.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bob.xtb.R;
import com.bob.xtb.adapter.ResourceAdapter;
import com.bob.xtb.util.RefreshTask;
import com.bob.xtb.view.LoadMoreListView;

/**
 * Created by bob on 15-4-21.
 */
public class AddFileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener {
    private SwipeRefreshLayout swipeLayout;  //系统带的下拉刷新控件
    private ResourceAdapter adapter; //数据适配器
    private LoadMoreListView listView;  //具有上拉加载的ListView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_add_resource, container, false);//最后一项为是否有父布局，设为false
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);//下拉组件的事件监听

        // set style for swipeRefreshLayout
        swipeLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright);
        listView = (LoadMoreListView) getView().findViewById(R.id.list_view);
        listView.setOnLoadMoreListener(this);//为listView添加加载监听
        adapter = new ResourceAdapter(getActivity());
        listView.setAdapter(adapter);//设置数据适配器

        registerForContextMenu(listView);//注册上下文菜单
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);//上下文菜单的呼出每一次都会被调用
       /* menu.setHeaderTitle("文件操作");
        menu.add(0, 1, Menu.NONE, "发送");//第一个是组别，第二个是数字是该组里的条目序号
        menu.add(0, 2,Menu.NONE, "删除");*///静态添加菜单项
        getActivity().getMenuInflater().inflate(R.menu.menu_context_addfile, menu);//资源id易于管理，google推荐
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //获取到的是listView里的条目信息
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_item_create_task:
                Toast.makeText(getActivity(), item.getTitle().toString()+info.id, Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_item_alter_file:
                Toast.makeText(getActivity(), item.getTitle().toString()+info.id, Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_item_delete:
                adapter.removeItem((int)info.id);
                adapter.notifyDataSetChanged();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }


    @Override
    public void onRefresh() {//刷新监听
        new RefreshTask(getActivity(),"res",adapter,swipeLayout,listView).
                execute("www.baidu.com", "refresh");
    }

    @Override
    public void onLoadMore() {//加载监听
        new RefreshTask(getActivity(),"res",adapter,swipeLayout,listView).
                execute("www.udiab.com", "load");
    }
}
