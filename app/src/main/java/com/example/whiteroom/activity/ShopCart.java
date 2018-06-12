package com.example.whiteroom.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whiteroom.R;
import com.example.whiteroom.adapter.ShopCartAdapter;
import com.example.whiteroom.application.myApplication;
import com.example.whiteroom.model.MyInterface.*;
import com.example.whiteroom.model.ShopCartBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ShopCart extends AppCompatActivity {
    private LinearLayout ll_selectAll;
    private Button btn_commit;
    private TextView tv_allPrice,tv_allGoodsNum,tv_selectAll;
    private RecyclerView recycleview;
    private ImageView iv_selectAll;
    private List<ShopCartBean.CartlistBean> mAllOrderList = new ArrayList<>();
    private ArrayList<ShopCartBean.CartlistBean> mGoPayList = new ArrayList<>();
    private ShopCartAdapter mShopCartAdapter;
    private int mCount,mPosition;
    private boolean mSelect;
    private float mTotalPrice1;
    private myApplication mShopCartList;
    private class MyThread extends Thread {
        @Override
        public void run() {
            try {
                Socket socket = new Socket("10.128.202.176",10087);
                Log.i("socket", String.valueOf(socket.isConnected()));
                //2、获取输出流，向服务器端发送信息
                OutputStream os = socket.getOutputStream();//字节输出流
                PrintWriter pw = new PrintWriter(os);//将输出流包装成打印流
                pw.println(mAllOrderList.size());
                for (int i = 0; i < mAllOrderList.size(); i++) {
                    pw.println(mAllOrderList.get(i).getProductId());
                    pw.println(mAllOrderList.get(i).getFlavor());

                }
                pw.flush();
                socket.shutdownOutput();
                //3、获取输入流，并读取服务器端的响应信息
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String info = null;
                String rt = null;
                while((info=br.readLine()) != null){
                    Looper.prepare();
                    Toast.makeText(ShopCart.this, info, Toast.LENGTH_LONG).show();
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    // 通知渠道的id
                    String id = "my_channel_01";
                    // 用户可以看到的通知渠道的名字.
                    CharSequence name = "小白房订餐app";
                    // 用户可以看到的通知渠道的描述
                    String description = "订餐号";
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                    // 配置通知渠道的属性
                    mChannel.setDescription(description);
                    // 设置通知出现时的闪灯（如果 android 设备支持的话）
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    // 设置通知出现时的震动（如果 android 设备支持的话）
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    //最后在notificationmanager中创建该通知渠道
                    mNotificationManager.createNotificationChannel(mChannel);

                    Notification.Builder mBuilder = new Notification.Builder(ShopCart.this)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle("My notification")
                            .setChannelId(id)
                            .setContentText(info);
                    // Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(ShopCart.this, MainActivity.class);

                    mNotificationManager.notify(1, mBuilder.build());

                    Looper.loop();
                }
                br.close();
                is.close();
                pw.close();
                os.close();
                socket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ShopCart.this, "hello world", Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);

        ll_selectAll= (LinearLayout) findViewById(R.id.ll_selectAll);
        btn_commit= (Button) findViewById(R.id.btn_commit);
        tv_allPrice= (TextView) findViewById(R.id.tv_allPrice);
        tv_allGoodsNum= (TextView) findViewById(R.id.tv_allGoodsNum);
        tv_selectAll= (TextView) findViewById(R.id.tv_selectAll);
        iv_selectAll= (ImageView) findViewById(R.id.iv_selectAll);
        recycleview= (RecyclerView) findViewById(R.id.recycleview);

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        mShopCartAdapter = new ShopCartAdapter(this,mAllOrderList);
        recycleview.setAdapter(mShopCartAdapter);
        //删除商品接口
        mShopCartAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position, int cartid) {
                mShopCartAdapter.notifyDataSetChanged();
            }
        });
        //修改数量接口
        mShopCartAdapter.setOnEditClickListener(new OnEditClickListener() {
            @Override
            public void onEditClick(int position, int cartid, int count) {
                mCount = count;
                mPosition = position;
            }
        });
        //实时监控全选按钮
        mShopCartAdapter.setResfreshListener(new OnResfreshListener() {
            @Override
            public void onResfresh( boolean isSelect) {
                mSelect = isSelect;
                if(isSelect){
                    iv_selectAll.setImageDrawable(getResources().getDrawable(R.drawable.shopcart_selected));
                }else {
                    iv_selectAll.setImageDrawable(getResources().getDrawable(R.drawable.shopcart_unselected));
                }
                float mTotalPrice = 0;
                int mTotalNum = 0;
                mTotalPrice1 = 0;
                mGoPayList.clear();
                for(int i = 0;i < mAllOrderList.size(); i++)
                    if(mAllOrderList.get(i).getIsSelect()) {
                        mTotalPrice += Float.parseFloat(mAllOrderList.get(i).getPrice()) * mAllOrderList.get(i).getCount();
                        mTotalNum += mAllOrderList.get(i).getCount();
                        mGoPayList.add(mAllOrderList.get(i));
                    }
                mTotalPrice1 = mTotalPrice;
                tv_allPrice.setText("总价：" + mTotalPrice);
                tv_allGoodsNum.setText("共" + mTotalNum + "件商品");
            }
        });
        //全选
        ll_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelect = !mSelect;
                if(mSelect){
                    iv_selectAll.setImageDrawable(getResources().getDrawable(R.drawable.shopcart_selected));
                    for(int i = 0;i < mAllOrderList.size();i++){
                        mAllOrderList.get(i).setSelect(true);
                        mAllOrderList.get(i).setShopSelect(true);
                    }
                }else{
                    iv_selectAll.setImageDrawable(getResources().getDrawable(R.drawable.shopcart_unselected));
                    for(int i = 0;i < mAllOrderList.size();i++){
                        mAllOrderList.get(i).setSelect(false);
                        mAllOrderList.get(i).setShopSelect(false);
                    }
                }
                mShopCartAdapter.notifyDataSetChanged();

            }
        });
        initData();
        mShopCartAdapter.notifyDataSetChanged();
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new MyThread();
                t.start();

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mShopCartList = (myApplication) getApplication();
        mShopCartList.setOrderList(mAllOrderList);
    }
    private void initData(){

        mShopCartList = (myApplication) getApplication();
        for (int i = 0; i < mShopCartList.getOrderList().size(); i++) {
            mAllOrderList.add(mShopCartList.getOrderList().get(i));
        }
        mShopCartAdapter.isSelectFirst(mAllOrderList);
    }
}

