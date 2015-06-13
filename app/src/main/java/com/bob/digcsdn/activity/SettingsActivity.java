package com.bob.digcsdn.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bob.digcsdn.R;

/**
 * Created by bob on 15-4-27.
 */
public class SettingsActivity extends Activity implements View.OnClickListener {
    private TextView tvHead;
    private LinearLayout btBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initWidget();
        initEvent();
    }

    private void initEvent() {
        btBack.setOnClickListener(this);
    }

    public void initWidget() {
        tvHead = (TextView) findViewById(R.id.tv_head);
        tvHead.setText("个人设置");
        btBack = (LinearLayout) findViewById(R.id.bt_back);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                finish();
                break;
        }
    }

}
