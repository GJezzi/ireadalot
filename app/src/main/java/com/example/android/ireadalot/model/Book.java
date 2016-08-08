package com.example.android.ireadalot.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gjezzi on 22/06/16.
 */

public class Book implements Serializable{

    @SerializedName("kind")
    private String mKind;
    @SerializedName("id")
    private String mId;
    @SerializedName("etag")
    private String mEtag;
    @SerializedName("selfLink")
    private String mSelfLink;
    @SerializedName("volumeInfo")
    private VolumeInfo mVolumeInfo;

    public Book(String kind, String id, String etag, String selfLink, VolumeInfo volumeInfo){
        this.mKind = kind;
        this.mId = id;
        this.mEtag = etag;
        this.mSelfLink = selfLink;
        this.mVolumeInfo = volumeInfo;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        this.mKind = kind;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getEtag() {
        return mEtag;
    }

    public void setEtag(String etag) {
        this.mEtag = etag;
    }

    public String getSelfLink() {
        return mSelfLink;
    }

    public void setSelfLink(String selfLink) {
        this.mSelfLink = selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return mVolumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.mVolumeInfo = volumeInfo;
    }

}
