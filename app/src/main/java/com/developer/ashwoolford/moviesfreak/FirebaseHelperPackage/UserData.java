package com.developer.ashwoolford.moviesfreak.FirebaseHelperPackage;

/**
 * Created by ashwoolford on 1/28/2017.
 */
public class UserData {
    String displayName,email,uid;

    public UserData(){

    }

    public UserData(String displayName, String email, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
