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

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class PhoneVerify extends AppCompatActivity {
    private EditText phone_et;
    private Button phone_verify;
    private EditText SMS;
    private Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);
        phone_et = (EditText) findViewById(R.id.phone);

        phone_verify = (Button) findViewById(R.id.phoneverify);
        SMS = (EditText) findViewById(R.id.SMS);
        verify = (Button) findViewById(R.id.SMSverify);
        phone_verify.setOnClickListener(new View.OnClickListener() {
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v) {
                final String phone = phone_et.getText().toString();
                BmobSMS.requestSMSCode(phone, "默认模板", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if(e == null)
                            Toast.makeText(PhoneVerify.this, "验证码发送成功", Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(PhoneVerify.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v) {
                final String phone = phone_et.getText().toString();
                final String smsCode = SMS.getText().toString();
                BmobUser user =new BmobUser();
                user.setMobilePhoneNumber(phone);
                user.setMobilePhoneNumberVerified(true);
                BmobUser cur = BmobUser.getCurrentUser();
                user.update(cur.getObjectId(),new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
//                            toast("手机号码绑定成功");
                        }else{
                            Toast.makeText(PhoneVerify.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                BmobSMS.verifySmsCode(phone, smsCode, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(PhoneVerify.this, "手机号验证成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhoneVerify.this, SelfCenter.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(PhoneVerify.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
