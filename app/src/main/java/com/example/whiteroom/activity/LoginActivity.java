package com.example.whiteroom.activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import com.example.whiteroom.application.myApplication;
import com.example.whiteroom.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends AppCompatActivity implements OnClickListener {

    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button forgive_pwd;
    private Button bt_pwd_eye;
    private Button login;
    private Button register;
    private boolean isOpen = false;
    private  myApplication application;
    private String username_s, password_s;
    private String ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            Intent intent = new Intent(this, SelfCenter.class);
            startActivity(intent);
        }
        else {
            initView();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(LoginActivity.this, MainMenu.class);
            startActivity(intent);
            return true;//return true;拦截事件传递,从而屏蔽back键。
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {

        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 获得文本框中的用户
                String user = username.getText().toString().trim();
                if ("".equals(user)) {
                    // 用户名为空,设置按钮不可见
                    bt_username_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 获得文本框中的密码
                String pwd = password.getText().toString().trim();
                if ("".equals(pwd)) {
                    // 密码为空,设置按钮不可见
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 密码不为空，设置按钮可见
                    bt_pwd_clear.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);

        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);

        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        forgive_pwd = (Button) findViewById(R.id.forgive_pwd);
        forgive_pwd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_username_clear:
                // 清除登录名
                username.setText("");
                break;
            case R.id.bt_pwd_clear:
                // 清除密码
                password.setText("");
                break;
            case R.id.bt_pwd_eye:
                // 密码可见与不可见的切换
                if (isOpen) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }

                // 默认isOpen是false,密码不可见
                changePwdOpenOrClose(isOpen);

                break;
            case R.id.login:
                // TODO 登录按钮

                username_s = username.getText().toString().trim();

                password_s = password.getText().toString().trim();
                BmobUser.loginByAccount(username_s, password_s, new LogInListener<BmobUser>() {

                    @Override
                    public void done(BmobUser user, BmobException e) {
                        if(e ==null){
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, SelfCenter.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                break;
            case R.id.register:
                // 注册按钮
                username_s = username.getText().toString().trim();
                password_s = password.getText().toString().trim();
                BmobUser bu = new BmobUser();
                bu.setUsername(username_s);
                bu.setPassword(password_s);
                //注意：不能用save方法进行注册
                bu.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser s, BmobException e) {
                        if(e==null){
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                break;
            case R.id.forgive_pwd:
                // 忘记密码按钮
                Toast.makeText(LoginActivity.this, "功能暂不可用", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void changePwdOpenOrClose(boolean flag) {
        // 第一次过来是false，密码不可见
        if (flag) {
            // 密码可见
            bt_pwd_eye.setBackgroundResource(R.drawable.eye);
            // 设置EditText的密码可见
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            // 密码不接见
            bt_pwd_eye.setBackgroundResource(R.drawable.eyeoff);
            // 设置EditText的密码隐藏
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

}