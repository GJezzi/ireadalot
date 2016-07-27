package com.example.android.ireadalot.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gjezzi on 21/07/16.
 */
public class VolumeInfo implements Serializable{

    @SerializedName("title")
    private String mTitle;
    @SerializedName("authors")
    private ArrayList<String> mAuthors = new ArrayList<>();
    @SerializedName("publishedDate")
    private String mBookYear;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("pageCount")
    private String mPages;

    @SerializedName("imageLinks")
    private ImageLinks mImageLinks;


    public VolumeInfo (String title, ArrayList<String> authors, String description, ImageLinks imageLinks ) {
        this.mTitle = title;
        this.mAuthors = authors;
        this.mDescription = description;
        this.mImageLinks = imageLinks;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.mAuthors = authors;
    }

    public String getBookYear() {
        return mBookYear;
    }

    public void setBookYear(String bookYear) {
        this.mBookYear = bookYear;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getPages() {
        return mPages;
    }

    public void setPages(String pages) {
        this.mPages = pages;
    }

    public ImageLinks getImageLinks() {
        return mImageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.mImageLinks = imageLinks;
    }
}
