package com.bob.xtb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.xtb.R;

/**
 * Created by bob on 15-4-27.
 */
public class SettingsActivity extends Activity implements View.OnClickListener {
    private TextView tvHead;
    private LinearLayout btBack;
    private EditText etCurrentPsd, etNewPsd, etVerifyPsd;
    private Button btPost;
    private String currentPsd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initWidget();
        initEvent();
    }

    private void initEvent() {
        btBack.setOnClickListener(this);
        btPost.setOnClickListener(this);
        etVerifyPsd.addTextChangedListener(textWatcher);
    }

    public void initWidget() {
        tvHead = (TextView) findViewById(R.id.tv_head);
        tvHead.setText("个人设置");
        btBack = (LinearLayout) findViewById(R.id.bt_back);

        etCurrentPsd = (EditText) findViewById(R.id.current_psd);
        etNewPsd = (EditText) findViewById(R.id.new_psd);
        etVerifyPsd = (EditText) findViewById(R.id.verify_psd);
        btPost = (Button) findViewById(R.id.bt_post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_post:
                postNewPsd();
                break;
        }
    }

    private void postNewPsd() {
        currentPsd= etCurrentPsd.getText().toString();
        if (currentPsd.equals("operator")) {//比对当前密码是否正确
            if (true) {//模拟请求修改密码成功，应该分2步： 比对当前输入密码正确，更新是否成功
                Toast.makeText(SettingsActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
            } else {
            }//密码更新失败
        } else Toast.makeText(SettingsActivity.this, "当前密码错误", Toast.LENGTH_LONG).show();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (etNewPsd.getText().toString().equals(etVerifyPsd.getText().toString())) {//动态判断是两次密码是否相同
                Toast.makeText(SettingsActivity.this, "匹配", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
