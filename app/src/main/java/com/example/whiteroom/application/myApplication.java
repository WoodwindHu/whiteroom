package com.example.whiteroom.application;

import android.app.Application;

import com.example.whiteroom.R;
import com.example.whiteroom.model.ShopCartBean;

import java.util.ArrayList;
import java.util.List;

public class myApplication extends Application {
    private String[] names = {"黄焖鸡米饭", "宫保鸡丁", "麻辣香锅", "鸡腿饭"};
    private List<ShopCartBean.CartlistBean> mAllOrderList = new ArrayList<>();
    private boolean logined = false;
    public List<ShopCartBean.CartlistBean> getOrderList() {
        return mAllOrderList;
    }
    public void addCartlistBean(ShopCartBean.CartlistBean sb) {
        mAllOrderList.add(sb);
    }
    public boolean getlogined() {
        return logined;
    }
    public void login() {
        logined = true;
    }
    public void logout() {
        logined = false;
    }
    public void setOrderList(List<ShopCartBean.CartlistBean> list) {
        mAllOrderList = list;
    }
    public String getName(int itemId) {
        String name;
        switch ( itemId % 4 ) {

            case 0:
                name = getString( R.string.item_0 );
                break;

            case 1:
                name = getString( R.string.item_1 );
                break;

            case 2:
                name = getString( R.string.item_2 );
                break;

            case 3: default:
                name = getString( R.string.item_3 );
                break;
        }
        return name;
    }
    public int getPicture(int itemId) {
        int picture;
        switch ( itemId % 4 ) {

            case 0:
                picture = R.drawable.item_0;
                break;

            case 1:
                picture = R.drawable.item_1;
                break;

            case 2:
                picture = R.drawable.item_2;
                break;

            case 3: default:
                picture = R.drawable.item_3;
                break;
        }
        return picture;
    }
    public int getPrice(int itemId) {
        int value;
        switch ( itemId % 4 ) {

            case 0:
                value = 12;
                break;

            case 1:
                value = 12;
                break;

            case 2:
                value = 14;
                break;

            case 3: default:
                value = 7;
                break;
        }
        return value;
    }
}
