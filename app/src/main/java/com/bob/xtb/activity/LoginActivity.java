package com.bob.xtb.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.xtb.R;
import com.bob.xtb.bean.User;
import com.bob.xtb.interfaces.IVerify;
import com.bob.xtb.util.LogicVerify;

/**
 * Created by bob on 15-4-14.
 */
public class LoginActivity extends Activity implements OnClickListener {

    private EditText et_user, et_psd, et_verify;
    private Button bt_change, bt_login;
    private TextView tv_check;
    private ImageView img_verify;
    private IVerify vCode;
    private User user;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs= getSharedPreferences("login_info", MODE_PRIVATE);

        if (prefs.getBoolean("count_saved", false)){//如果为假则表明账户为新账户或者已经被注销过了
            Intent intent= new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.login);
        initWidget();

        bt_change.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        et_user.addTextChangedListener(textWatcher);
        et_psd.addTextChangedListener(textWatcher);
        et_verify.addTextChangedListener(textWatcher);
    }

    private void initWidget() {//初始化各个控件
        vCode = LogicVerify.getInstance();
        et_user = (EditText) findViewById(R.id.et_user);

        et_psd = (EditText) findViewById(R.id.et_psd);
        et_verify = (EditText) findViewById(R.id.et_verify);

        bt_change = (Button) findViewById(R.id.bt_change);
        bt_login = (Button) findViewById(R.id.bt_login);

        tv_check = (TextView) findViewById(R.id.tv_check_sign);
        img_verify = (ImageView) findViewById(R.id.img_verify);
        img_verify.setImageBitmap(vCode.createBitmap());

        et_user.setText(prefs.getString("username",""));//回填用户名，不论是否存在，这样就少了一层判断
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_change: {
                img_verify.setImageBitmap(vCode.createBitmap());//重新创建一个位图
            }
            break;
            case R.id.bt_login:{
                user= new User(et_user.getText().toString(),et_psd.getText().toString());

                if (vCode.checkCode(et_verify.getText().toString())) {//获取输入验证码并验证
                    if(checkCount(user)){////验证成功,跳转
                        SharedPreferences.Editor editor= getSharedPreferences("login_info",MODE_PRIVATE).edit();
                        editor.putBoolean("count_saved", true);//将登录状态更改为true，也可以在MainActivity里进行更改
                        editor.putString("username", et_user.getText().toString());//只保存用户名，密码拒绝保存

                        editor.commit();

                        Intent intent= new Intent(this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else Toast.makeText(LoginActivity.this, "用户密码或账户错误", Toast.LENGTH_SHORT).show();
                }else tv_check.setVisibility(View.VISIBLE);
            }
        }
    }

    TextWatcher textWatcher= new TextWatcher() {//对EditText进行监听,监听改变tv_check状态
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tv_check.setVisibility(View.GONE);
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    /**
     * 用户验证说明：我们需要先去网络端访问验证，成功后再根据结果返回值进行本地更新
     * @param user
     * @return
     */
    public boolean checkCount(User user){//校验账户密码
        if (TextUtils.equals(user.getUserCount(),"operator") && TextUtils.equals(user.getUserPsd(),"operator"))
        {//模拟查库验证
            return true;
        }else return false;
    }
}
