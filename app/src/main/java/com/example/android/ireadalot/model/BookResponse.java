package com.example.android.ireadalot.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gjezzi on 14/07/16.
 */
public class BookResponse {

    @SerializedName("kind")
    private String mKind;

    @SerializedName("totalItems")
    private int mTotalItems;

    @SerializedName("items")
    private ArrayList<Book> mItems;


    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        this.mKind = kind;
    }

    public ArrayList<Book> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Book> items) {
        this.mItems = items;
    }

    public int getTotalItems() {
        return mTotalItems;
    }

    public void setTotalItems(int totalItems) {
        this.mTotalItems = totalItems;
    }
}
