package com.developer.ashwoolford.moviesfreak.details_activity.FragmentForDetailsActivity;

/**
 * Created by ashwoolford on 1/14/2017.
 */
public class ImagesData {

    String path;
    String castNames;


    public ImagesData(String path,String castNames)
    {
        this.path = path;
        this.castNames = castNames;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCastNames() {
        return castNames;
    }

    public void setCastNames(String castNames) {
        this.castNames = castNames;
    }
}
