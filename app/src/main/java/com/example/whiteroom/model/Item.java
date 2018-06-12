package com.example.whiteroom.model;

public class Item {


    String name;

    int price;

    int picture;

    int itemId;

    public Item( String name, int picture, int price, int i ) {
        this.picture = picture;
        this.name = name;
        this.price = price;
        this.itemId = i;
    }


    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture( int picture ) {
        this.picture = picture;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice( int price ) {
        this.price = price;
    }
    public int getItemId() {
        return itemId;
    }

}
