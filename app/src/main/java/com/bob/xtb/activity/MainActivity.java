package com.bob.xtb.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.bob.xtb.R;
import com.bob.xtb.adapter.TabPagerAdapter;
import com.bob.xtb.fragment.LeftMenuFragment;
import com.viewpagerindicator.TabPageIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    //这里不可以直接引用xml里的颜色值，而需要根据RGB或者ARGB多个参数来构造

    private static boolean isExit = false;
    Timer tExit = null;
    private ViewPager pager;//v4包下的一个控件，即就是一个可以左右滑动切换的东东
    private TabPagerAdapter tabAdapter;//和ListView一个道理，集合——>适配器——>控件
    private TabPageIndicator indicator;

    private DrawerLayout drawerLayout;
    private LeftMenuFragment leftMenuFragment;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidget();//集中实例化这几个控件
        initEvent();

        //加载侧滑菜单
        getSupportFragmentManager().beginTransaction().add(R.id.left_menu_container, leftMenuFragment).commit();

        /**
         * 使用ViewPager容器，左右滑动多个页面，默认只能缓存临近操作的两个页面，只有通过
         * 设置setOffscreenPageLimit()来达到多个页面缓存  ps:可以监听Fragment的生命周期来
         * 达到调试目的
         */
        pager.setOffscreenPageLimit(1);//设置最多可以加载的页数== 总页数,这里就缓存1页？？？
        //当然就缓存一页，因为每次的加载都需要保证列表是最新的
        pager.setAdapter(tabAdapter);

        indicator.setViewPager(pager);
    }

    private void initWidget() {
        leftMenuFragment = new LeftMenuFragment();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);//初始化抽屉布局
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);//初始化toolBar
        mToolbar.setTitle(R.string.username);
        setSupportActionBar(mToolbar);
        /**
         * 静态设置属性会先于动态设置属性，因此动态会覆盖静态的设置
         */
        mToggle = new ActionBarDrawerToggle(this,
                drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);


        indicator = (TabPageIndicator) findViewById(R.id.indicator);

        pager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabPagerAdapter(getSupportFragmentManager());

    }

    private void initEvent() {
        mToggle.syncState();//用toggle同步监听drawerLayout
        drawerLayout.setDrawerListener(mToggle);
    }


    @Override
    public void onBackPressed() {
        exitBy2Click();
    }


    private void exitBy2Click() {
        tExit = null;//释放计时器堆内存
        if (!isExit) {//为假，初始化计时器开始计时，为真表示延时任务未被执行，而进入了else
            isExit = true;//开始计时的标识
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();

            tExit.schedule(new TimerTask() {//计划一个延时任务
                @Override
                public void run() {//该任务被执行，则表示两秒之内没有再次触发onBackPressed方法，isExit标记被重置为假。本次为时2s的倒计时结束
                    isExit = false;
                }
            }, 2000);//两秒之后执行run里的代码块，结束本次对第二次连击的监听（异步执行计时任务，也可以理解为对子线程的一个sleep）
        } else {
            finish();
            System.exit(0);//鉴于活动栈里没有活动了，可暂时不使用这段强制退出代码，不过还是建议使用
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应空间的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override//toolBar里的条目监听，里面的条目可以按照menu来处理
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_files:
                Toast.makeText(MainActivity.this, "add_files", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_task:
                Toast.makeText(MainActivity.this, "add_task", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

