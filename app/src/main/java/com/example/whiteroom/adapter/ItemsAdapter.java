package com.example.whiteroom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whiteroom.R;
import com.example.whiteroom.model.Item;
import com.example.whiteroom.viewholder.ItemViewHolder;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    public ItemsAdapterOnClickHandler mItemsAdapterOnClickHandler; // ditto

    private List<Item> mGoods; // ditto

    public ItemsAdapter(Context context, List< Item > goods,
                        ItemsAdapterOnClickHandler clickHandler ) {

        mGoods = goods;
        mItemsAdapterOnClickHandler = clickHandler;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType ) {


        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.item, parent, false );


        return new ItemViewHolder( view, this );

    }
    @Override
    public void onBindViewHolder( ItemViewHolder itemViewHolder, int position ) {


        Item currentGood = mGoods.get( position );


        itemViewHolder.mPictureImageView.setImageResource( currentGood.getPicture() );


        itemViewHolder.mNameTextView.setText( currentGood.getName() );

        itemViewHolder.mValueTextView.setText( String.valueOf( currentGood.getPrice() ) + " RMB" );

    }

    @Override
    public int getItemCount() {
        return mGoods.size();
    }


}

