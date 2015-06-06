package com.bob.xtb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bob.xtb.R;

/**
 * Created by bob on 15-4-27.
 * 侧滑退出会有白屏延迟，如果做成fragment，侧边栏菜单只是一个辅助类菜单，不应该
 */
public class AboutActivity extends Activity {
    private TextView tv_head;
    private LinearLayout bt_back;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        setContentView(R.layout.about);

        tv_head= (TextView) findViewById(R.id.tv_head);
        tv_head.setText("关于");
        bt_back= (LinearLayout) findViewById(R.id.bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
