package com.bob.xtb.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bob.xtb.R;
import com.bob.xtb.activity.AboutActivity;
import com.bob.xtb.activity.ChangeUserActivity;
import com.bob.xtb.activity.LoginActivity;
import com.bob.xtb.activity.SettingsActivity;
import com.bob.xtb.adapter.MenuListAdapter;

/**
 * Created by bob on 15-5-17.
 */
public class LeftMenuFragment extends Fragment implements View.OnClickListener {

    private LinearLayout llLogout;
    private ListView menuListView;
    private MenuListAdapter menuAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_menu, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        initEvent();
        menuListView.setAdapter(menuAdapter);
    }

    private void initWidget() {
        context = getActivity();
        llLogout = (LinearLayout) getView().findViewById(R.id.ll_logout);
        menuListView = (ListView) getView().findViewById(R.id.menu_list);
        menuAdapter = new MenuListAdapter(context);
    }

    private void initEvent() {
        llLogout.setOnClickListener(this);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        skipActivity(SettingsActivity.class);
                        break;
                    case 1:
                        skipActivity(ChangeUserActivity.class);
                        break;
                    case 2:
                        Toast.makeText(context, "正在检查中...", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        skipActivity(AboutActivity.class);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_logout:
                logout();
        }
    }

    public void logout() {//在主界面点击退出提示退出应用对话框
        AlertDialog.Builder dialog = new AlertDialog.Builder(context).setTitle("注销账户")
                .setMessage("注销将会删除已保存的密码，您确定要注销吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // 退出程序
                        SharedPreferences.Editor editor = context.getSharedPreferences("login_info", context.MODE_PRIVATE).edit();
                        editor.putBoolean("count_saved", false);
                        editor.commit();

                        skipActivity(LoginActivity.class);
                        getActivity().finish();//这里返回的是FragmentActivity，而context里是没有finish
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        dialog.show();
    }

    private void skipActivity(Class<?> aimClass) {
        Intent intent = new Intent(context, aimClass);
        startActivity(intent);
    }

}
