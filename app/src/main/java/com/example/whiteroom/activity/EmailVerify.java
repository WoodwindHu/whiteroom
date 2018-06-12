package com.example.whiteroom.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whiteroom.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EmailVerify extends AppCompatActivity {
    private EditText email_et;
    private Button email_verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);
        email_et = (EditText) findViewById(R.id.email);

        email_verify = (Button) findViewById(R.id.emailverify);
        email_verify.setOnClickListener(new View.OnClickListener() {
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v) {
                final String email = email_et.getText().toString();
                BmobUser bmobUser = BmobUser.getCurrentUser();
                bmobUser.setEmail(email);
                bmobUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(EmailVerify.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活。", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmailVerify.this, SelfCenter.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(EmailVerify.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
