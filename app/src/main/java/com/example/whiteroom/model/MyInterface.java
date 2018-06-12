package com.example.whiteroom.model;

import android.view.View;

public class MyInterface {
    public interface OnDeleteClickListener{
        void onDeleteClick(View view, int position, int cartid);
    }
    public interface OnEditClickListener{
        void onEditClick(int position, int cartid, int count);
    }
    public interface OnResfreshListener{
        void onResfresh(boolean isSelect);
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
