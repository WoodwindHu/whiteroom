package com.example.whiteroom.fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whiteroom.R;
import com.example.whiteroom.activity.LoginActivity;
import com.example.whiteroom.activity.SelectedItemActivity;
import com.example.whiteroom.activity.SelfCenter;
import com.example.whiteroom.activity.ShopCart;
import com.example.whiteroom.adapter.ItemsAdapter;
import com.example.whiteroom.adapter.ItemsAdapterOnClickHandler;
import com.example.whiteroom.application.myApplication;
import com.example.whiteroom.model.Item;
import com.example.whiteroom.viewholder.ItemViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsFragment extends Fragment implements ItemsAdapterOnClickHandler {

    public static final int GOODS_NUMBER = 20;

    private static final String LOG_TAG = ItemsFragment.class.getSimpleName();

    private myApplication application;


    private ItemsAdapter mItemsAdapter;

    private List<Item> mItems;

    public FloatingActionButton mButton;
    public FloatingActionButton shopCart;


    @BindView( R.id.goods_rv_list )
    public RecyclerView mGoodsRecyclerView;


    public ItemsFragment() {
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {

        View rootView = inflater.inflate( R.layout.fragment_items, container, false );

        ButterKnife.bind( this, rootView );

        mItems = generateBakedGoods();

        mGoodsRecyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) );

        mGoodsRecyclerView.setHasFixedSize( true );

        mItemsAdapter = new ItemsAdapter( getActivity(), mItems, this );

        mGoodsRecyclerView.setAdapter( mItemsAdapter );

        mButton = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton2);
        mButton.setOnClickListener(new View.OnClickListener(){
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v){
                Intent startA;
                application = (myApplication) getActivity().getApplication();
                if (application.getlogined()) {
                    startA = new Intent(getActivity(), SelfCenter.class);
                }
                else {
                    startA = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(startA);  }
        });
        shopCart = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton5);
        shopCart.setOnClickListener(new View.OnClickListener(){
            //为找到的button设置监听
            @Override
            //重写onClick函数
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), ShopCart.class);
                startActivity(intent);
            }
        });


        return rootView;

    }
    @Override
    public void onClick( ItemViewHolder clickedHolder ) {

        int itemId = mItems.get( clickedHolder.getAdapterPosition() ).getItemId();

        Intent selectedGoodIntent = new Intent( getActivity(), SelectedItemActivity.class );
        selectedGoodIntent.putExtra( SelectedItemFragment.ARGUMENT_ITEMID, itemId );
        startActivity( selectedGoodIntent );

    }
    private ArrayList< Item > generateBakedGoods() {

        ArrayList< Item > goods = new ArrayList<>( GOODS_NUMBER );

        for ( int i = 0; i < GOODS_NUMBER; i++ ) {

            int picture, value;

            String name;

            switch ( i % 4 ) {

                case 0:
                    picture = R.drawable.item_0;
                    value = 12;
                    name = getString( R.string.item_0 );
                    break;

                case 1:
                    picture = R.drawable.item_1;
                    value = 12;
                    name = getString( R.string.item_1 );
                    break;

                case 2:
                    picture = R.drawable.item_2;
                    value = 14;
                    name = getString( R.string.item_2 );
                    break;

                case 3: default:
                    picture = R.drawable.item_3;
                    value = 7;
                    name = getString( R.string.item_3 );
                    break;
            }

            Item good = new Item( name, picture, value, i );

            goods.add( good );

        }

        return goods;

    }
}