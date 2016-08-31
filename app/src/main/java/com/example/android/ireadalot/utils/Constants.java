package com.example.android.ireadalot.utils;

import com.example.android.ireadalot.BuildConfig;

/**
 * Created by gjezzi on 29/07/16.
 */
public class Constants {
    /**
    *Constants related to locations in Firebase, such as the name of the node
    * where active lists are stored (ie "activeLists")
    */
    public static final String FIREBASE_MY_SHELF_BOOKS = "myShelfBooks";

    /**
     * Constants for Firebase URL
     */
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_MY_SHELF_LIST = FIREBASE_URL + "/" + FIREBASE_MY_SHELF_BOOKS;

    /**
     * Constants for bundles, extras and shared preferences keys
     */
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";
    public static final String KEY_BOOK_ID = "BOOK_ID";
}
