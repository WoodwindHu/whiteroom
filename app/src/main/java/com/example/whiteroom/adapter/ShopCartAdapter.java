package com.example.whiteroom.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whiteroom.R;
import com.example.whiteroom.model.ShopCartBean;
import com.example.whiteroom.model.MyInterface.*;

import java.util.List;

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.MyViewHolder>{
    private Context context;
    private List<ShopCartBean.CartlistBean> data;
    private OnDeleteClickListener mOnDeleteClickListener;
    private OnEditClickListener mOnEditClickListener;
    private OnResfreshListener mOnResfreshListener;
    private OnItemClickListener mOnItemClickListener;
    //构造方法
    public ShopCartAdapter(Context context, List<ShopCartBean.CartlistBean> data){
        this.context = context;
        this.data = data;
    }
    //布局绑定
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shopcart, parent, false);
        return new MyViewHolder(view);
    }
    //    数据绑定
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(context).load(data.get(position).getDefaultPic()).into(holder.iv_goodsImage);
        if (position > 0) {
            if (data.get(position).getShopId() == data.get(position - 1).getShopId()) {
                //不是第一条数据 && ShopId与上一条数据不一致  ->   GONE
                holder.ll_shopcart_header.setVisibility(View.GONE);
            } else {            
                holder.ll_shopcart_header.setVisibility(View.VISIBLE);
            }
        }else {
            holder.ll_shopcart_header.setVisibility(View.VISIBLE);
        }
        //数据填充
        String flavor;
        switch (data.get(position).getFlavor()){
            case 0: flavor = "特辣";
            break;
            case 1: flavor = "微辣";
            break;
            case 2: flavor = "不辣";
            break;
            default: flavor = "特辣";
        }
        holder.tv_goodsDescription.setText("口味：" + flavor +"  "+"尺寸：" + data.get(position).getSize());
        holder.tv_goodsName.setText(data.get(position).getProductName());
        holder.tv_shopName.setText(data.get(position).getShopName());
        holder.tv_goodsPrice.setText("¥" + data.get(position).getPrice());
        holder.tv_goodsNum.setText(data.get(position).getCount() + "");

        if(mOnResfreshListener != null){
            boolean isSelect = false;
            for(int i = 0;i < data.size(); i++){
                if(!data.get(i).getIsSelect()){
                    isSelect = false;
                    break;//终止当前循环，没有全选
                }else{
                    isSelect = true;
                }
            }
            mOnResfreshListener.onResfresh(isSelect);
        }
        //给每个item->GoodsMin绑定点击事件，数量的改变
        holder.iv_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).getCount() > 1) {
                    int count = data.get(position).getCount() - 1;
                    if (mOnEditClickListener != null) {
                        mOnEditClickListener.onEditClick(position, data.get(position).getId(), count);
                    }
                    data.get(position).setCount(count);
                    notifyDataSetChanged();
                }
            }
        });
        //给每个item->GoodsAdd绑定点击事件，数量的改变
        holder.iv_goodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = data.get(position).getCount() + 1;
                if(mOnEditClickListener != null){
                    mOnEditClickListener.onEditClick(position,data.get(position).getId(),count);
                }
                data.get(position).setCount(count);
                notifyDataSetChanged();
            }
        });
        //        商品是否勾选
        if(data.get(position).getIsSelect()){
            holder.iv_goodsSelect.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_selected));
        }else {
            holder.iv_goodsSelect.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_unselected));
        }
        //        店铺是否勾选
        if(data.get(position).getIsShopSelect()){
            holder.iv_shopSelect.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_selected));
        }else {
            holder.iv_shopSelect.setImageDrawable(context.getResources().getDrawable(R.drawable.shopcart_unselected));
        }
        //每个item删除的点击事件
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v,position);
            }
        });
        //商品选中的点击事件
        holder.iv_goodsSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //商铺的选中信息取反
                data.get(position).setSelect(!data.get(position).getIsSelect());
                //通过循环找出不同商铺的第一个商品的位置
                for(int i = 0;i < data.size(); i++){
                    //  IsFirst字段，是否是第一个的标志
                    if(data.get(i).getIsFirst() == 1) {
                        //遍历去找出同一家商铺的所有商品的勾选情况
                        for(int j = 0;j < data.size();j++){
                            //如果是同一家商铺的商品，并且其中一个商品是未选中，那么商铺的全选勾选取消
                            if(data.get(j).getShopId() == data.get(i).getShopId() && !data.get(j).getIsSelect()){
                                data.get(i).setShopSelect(false);
                                break;
                            }else{
                                //如果是同一家商铺的商品，并且所有商品是选中，那么商铺的选中全选勾选
                                data.get(i).setShopSelect(true);
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });
        //shop选中的点击事件
        holder.iv_shopSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).getIsFirst() == 1) {
                    //  当前商铺勾选状态取反
                    data.get(position).setShopSelect(!data.get(position).getIsShopSelect());
                    for(int i = 0;i < data.size();i++){
                        //   该商铺下所有商品勾选状态同商铺一样
                        if(data.get(i).getShopId() == data.get(position).getShopId()){
                            data.get(i).setSelect(data.get(position).getIsShopSelect());
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }
    private void showDialog(final View view, final int position){
        //调用删除某个规格商品的接口
        if(mOnDeleteClickListener != null){
            mOnDeleteClickListener.onDeleteClick(view,position,data.get(position).getId());
        }
        data.remove(position);
        //重新排序，标记所有商品不同商铺第一个的商品位置
        isSelectFirst(data);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        return count;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_shopSelect,iv_shopImag,iv_goodsSelect,iv_goodsImage,iv_mine,iv_goodsAdd;
        private TextView tv_shopName,tv_goodsName,tv_goodsDescription,tv_goodsNum,tv_goodsPrice,tv_delete;
        private LinearLayout ll_shopcart_header;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll_shopcart_header=(LinearLayout) itemView.findViewById(R.id.ll_shopcart_header);
            iv_shopSelect=(ImageView) itemView.findViewById(R.id.iv_shopSelect);
            iv_shopImag=(ImageView)itemView.findViewById(R.id.iv_shopImag);
            iv_goodsSelect=(ImageView)itemView.findViewById(R.id.iv_goodsSelect);
            iv_goodsImage=(ImageView)itemView.findViewById(R.id.iv_goodsImage);
            iv_mine=(ImageView)itemView.findViewById(R.id.iv_mine);
            iv_goodsAdd=(ImageView)itemView.findViewById(R.id.iv_goodsAdd);
            tv_shopName=(TextView) itemView.findViewById(R.id.tv_shopName);
            tv_goodsName=(TextView)itemView.findViewById(R.id.tv_goodsName);
            tv_goodsDescription=(TextView)itemView.findViewById(R.id.tv_goodsDescription);
            tv_goodsNum=(TextView)itemView.findViewById(R.id.tv_goodsNum);
            tv_goodsPrice=(TextView)itemView.findViewById(R.id.tv_goodsPrice);
            tv_delete=(TextView)itemView.findViewById(R.id.tv_delete);
        }
    }
    public void isSelectFirst(List<ShopCartBean.CartlistBean> list){
        if(list.size() > 0) {
            list.get(0).setIsFirst(1);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getShopId() == list.get(i - 1).getShopId()) {
                    list.get(i).setIsFirst(2);
                } else {
                    list.get(i).setIsFirst(1);
                }
            }
        }

    }
    /*********************************事件接口*******************************************************/
    //    删除事件初始化
    public void setOnDeleteClickListener(OnDeleteClickListener mOnDeleteClickListener){
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }
    //    刷新事件初始化
    public void setResfreshListener(OnResfreshListener mOnResfreshListener){
        this.mOnResfreshListener = mOnResfreshListener;
    }
    //    数量改变监听事件初始化
    public void setOnEditClickListener(OnEditClickListener mOnEditClickListener){
        this.mOnEditClickListener = mOnEditClickListener;
    }
    //    item的点击事件初始化
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}


