package com.example.whiteroom.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whiteroom.R;
import com.example.whiteroom.activity.LoginActivity;
import com.example.whiteroom.activity.SelfCenter;
import com.example.whiteroom.activity.ShopCart;
import com.example.whiteroom.application.myApplication;
import com.example.whiteroom.model.ShopCartBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

public class SelectedItemFragment extends Fragment {

    public static final String ARGUMENT_ITEMID = "ARGUMENT_ITEMID"; // ditto



    private static final String LOG_TAG = SelectedItemFragment.class.getSimpleName();


    private myApplication mShopCartList;
    int priseRes;
    String nameRes;
    int imageRes;
    int itemId;
    int flavor;
    FloatingActionButton mButton;
    FloatingActionButton mButtonShopCart;
    RadioButton radioButton5, radioButton6, radioButton7;
    private Button mButtonAdd;
    private myApplication application;


    @BindView( R.id.selected_good_iv_picture )
    public ImageView mPictureImageView;
    @BindView( R.id.good_name)
    public TextView mNameTextView;
    @BindView(R.id.good_price)
    public TextView mPriceTextView;

    public SelectedItemFragment() {
    }

    public static SelectedItemFragment newInstance( int itemId) {

        SelectedItemFragment selectedItemFragment = new SelectedItemFragment();

        Bundle bundle = new Bundle();

        bundle.putInt( ARGUMENT_ITEMID, itemId );

        selectedItemFragment.setArguments( bundle );


        return selectedItemFragment;

    }
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        View rootView = inflater.inflate( R.layout.fragment_selected_item, container, false );


        ButterKnife.bind( this, rootView );
        mButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        radioButton5 = (RadioButton) rootView.findViewById(R.id.radioButton5);
        radioButton6 = (RadioButton) rootView.findViewById(R.id.radioButton6);
        radioButton7 = (RadioButton) rootView.findViewById(R.id.radioButton7);

        mButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent startA;
                application = (myApplication) getActivity().getApplication();
                if (application.getlogined()) {
                    startA = new Intent(getActivity(), SelfCenter.class);
                }
                else {
                    startA = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(startA);
            }
        });

        mButtonShopCart = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton4);
        mButtonShopCart.setOnClickListener(new View.OnClickListener(){
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v){
                Intent intent = new Intent(getActivity(),ShopCart.class);
                startActivity(intent);  }
        });
        radioButton5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                flavor = 0;
            }
        });
        radioButton6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                flavor = 1;
            }
        });
        radioButton7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                flavor = 2;
            }
        });

        Bundle bundle = getArguments();

        // begin if there is a bundle and it has the arguments we need
        if ( bundle != null && bundle.containsKey( ARGUMENT_ITEMID )) {
            application = (myApplication) getActivity().getApplication();
            itemId = bundle.getInt( ARGUMENT_ITEMID );
            imageRes = application.getPicture(itemId);

            mPictureImageView.setImageResource( imageRes );

            nameRes = application.getName(itemId);

            mNameTextView.setText(nameRes);

            priseRes = application.getPrice(itemId);

            mPriceTextView.setText( String.valueOf(priseRes) + " RMB" );

        }
        mButtonAdd = (Button) rootView.findViewById((R.id.button3));
        mButtonAdd.setOnClickListener(new View.OnClickListener(){
            //为找到的button设置监听
            @Override
            //重写onClick函数，加入购入车
            public void onClick(View v){
                BmobUser bmobUser = BmobUser.getCurrentUser();
                if(bmobUser == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(getActivity(), "加入购物车成功", Toast.LENGTH_SHORT).show();
                mShopCartList = (myApplication) getActivity().getApplication();
                ShopCartBean.CartlistBean sb = new ShopCartBean.CartlistBean();
                sb.setShopId(1);
                sb.setProductId(itemId);
                sb.setPrice(String.valueOf(priseRes));
                sb.setDefaultPic(imageRes);
                sb.setProductName(mNameTextView.getText().toString());
                sb.setShopName("福味轩");
                sb.setFlavor(flavor);
                sb.setCount(1);
                mShopCartList.addCartlistBean(sb);
            }
        });

        return rootView;

    }

}
