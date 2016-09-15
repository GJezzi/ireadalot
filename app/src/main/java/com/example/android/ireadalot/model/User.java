package com.example.android.ireadalot.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by gjezzi on 15/09/16.
 */

public class User implements Serializable {

    @SerializedName("email")
    private String  mUserEmail;
    @SerializedName("name")
    private String mUserName;
    @SerializedName("timestamp")
    private HashMap<String, Object> mTimestamp;

    public User() {}

    public User (String userEmail, String userName, HashMap<String, Object> timestampJoined) {
        this.mUserEmail = userEmail;
        this.mUserName = userName;
        this.mTimestamp = timestampJoined;
    }


    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        this.mUserEmail = userEmail;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public HashMap<String, Object> getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(HashMap<String, Object> timestampJoined) {
        this.mTimestamp = timestampJoined;
    }
}
