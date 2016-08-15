package com.example.android.ireadalot.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gjezzi on 21/07/16.
 */
public class ImageLinks implements Serializable{

    @SerializedName("smallThumbnail")
    private String  mSmallThumb;
    @SerializedName("thumbnail")
    private String mThumbnail;

    public ImageLinks() { }

    public ImageLinks (String smallThumb, String thumbnail) {
        this.mSmallThumb = smallThumb;
        this.mThumbnail = thumbnail;
    }

    public String getSmallThumb() {
        return mSmallThumb;
    }

    public void setSmallThumb(String smallThumb) {
        this.mSmallThumb = smallThumb;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }
}
