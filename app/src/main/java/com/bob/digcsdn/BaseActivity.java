package com.bob.digcsdn;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bob.digcsdn.R;


/**
 * Created by Bob on 15/10/12.
 */
public class BaseActivity extends AppCompatActivity {
    private LinearLayout rootLayout;
    private boolean useCustomToolbar;
    private int toolbarId;
    public Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base);
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void initToolbar(int toolbarId) {
        mToolbar = (Toolbar) findViewById(toolbarId);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    /**
     * 当自带的toolbar的布局(LinearLayout vertical)结构或有特殊样式达不到activity要求时使用。
     *
     * @param layoutId
     * @param useCustomToolbar
     * @param toolbarId
     */
    public void setContentView(int layoutId, boolean useCustomToolbar, int toolbarId) {
        this.useCustomToolbar = useCustomToolbar;
        this.toolbarId = toolbarId;
        setContentView(layoutId);
    }

    /**
     * 如果直接使用这个方法，说明useCustomToolbar为false，
     *
     * @param layoutId
     */
    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootLayout == null) return;

        if (useCustomToolbar) {
            rootLayout.removeAllViews();//remove the default toolbar in rootLayout
            rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            initToolbar(toolbarId);
        } else {
            rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            initToolbar();
        }
    }

    /**
     * 移除默认的toolbar，这里适合在不使用toolbar的环境下使用（或者有其他的替代品）
     */
    protected void removeDefaultToolbar() {
        rootLayout.removeViewAt(0);
    }

    protected void hideSoftInputForce(View v) {
//        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//隐藏软键盘
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    protected void showSoftInputForce() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);//弹出软键盘
    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int stringId) {
        showToast(getResources().getString(stringId));
    }


    protected void skipActivity(Class<?> dest) {
        Intent intent = new Intent(this, dest);
        startActivity(intent);
    }
}
