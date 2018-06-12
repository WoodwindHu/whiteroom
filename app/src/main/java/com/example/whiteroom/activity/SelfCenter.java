package com.example.whiteroom.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whiteroom.R;
import com.example.whiteroom.application.myApplication;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobUser;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class SelfCenter extends AppCompatActivity {
    private TextView tv, emailVerify, phoneVerify;
    private TextView userid, email;
    private myApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_center);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        ImageView image2 = (ImageView) findViewById(R.id.imageView1);
        Glide.with(this).load(R.drawable.patient)
                .apply(bitmapTransform(new RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(image2);
        userid = (TextView) findViewById(R.id.userid);
        userid.setText(bmobUser.getUsername());
        email = (TextView) findViewById(R.id.username);
        email.setText(bmobUser.getEmail());
        emailVerify = (TextView) findViewById(R.id.textView9);
        // 验证邮箱
        emailVerify.setOnClickListener(new View.OnClickListener(){
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v){
                Intent intent = new Intent(SelfCenter.this, EmailVerify.class);
                startActivity(intent);
            }});
        phoneVerify = (TextView) findViewById(R.id.textView10);
        // 验证邮箱
        phoneVerify.setOnClickListener(new View.OnClickListener(){
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v){
                Intent intent = new Intent(SelfCenter.this, PhoneVerify.class);
                startActivity(intent);
            }});
        tv = (TextView) findViewById(R.id.logout);
        tv.setOnClickListener(new View.OnClickListener(){
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v){
                Intent startA;
                application = (myApplication) getApplication();
                application.logout();
                BmobUser.logOut();   //清除缓存用户对象
                Toast.makeText(SelfCenter.this, "退出登录成功", Toast.LENGTH_SHORT).show();
                startA = new Intent(SelfCenter.this, MainMenu.class);
                startActivity(startA);  }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SelfCenter.this, MainMenu.class);
            startActivity(intent);
            return true;//return true;拦截事件传递,从而屏蔽back键。
        }
        return super.onKeyDown(keyCode, event);
    }
}
