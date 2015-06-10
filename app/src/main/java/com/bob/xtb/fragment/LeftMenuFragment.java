package com.bob.xtb.fragment;

import android.content.Context;
import android.content.Intent;
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
import com.bob.xtb.activity.SettingsActivity;
import com.bob.xtb.adapter.MenuListAdapter;

/**
 * Created by bob on 15-5-17.
 */
public class LeftMenuFragment extends Fragment {

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
        menuListView = (ListView) getView().findViewById(R.id.menu_list);
        menuAdapter = new MenuListAdapter(context);
    }

    private void initEvent() {
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        skipActivity(SettingsActivity.class);
                        break;
                    case 1:
                        Toast.makeText(context, "正在检查中...", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        skipActivity(AboutActivity.class);
                }
            }
        });
    }

    private void skipActivity(Class<?> aimClass) {
        Intent intent = new Intent(context, aimClass);
        startActivity(intent);
    }
}
