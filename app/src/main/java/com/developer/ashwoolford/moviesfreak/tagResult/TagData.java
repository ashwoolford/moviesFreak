package com.developer.ashwoolford.moviesfreak.tagResult;

/**
 * Created by ashwoolford on 1/18/2017.
 */
public class TagData {
    String poster_path,title,releaseDate;
    Long id;
    Double ratting;


    public TagData(String poster_path, String title, String releaseDate, Long id, Double ratting) {
        this.poster_path = poster_path;
        this.title = title;
        this.releaseDate = releaseDate;
        this.id = id;
        this.ratting = ratting;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitleT() {
        return title;
    }

    public void setTitleT(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getIdT() {
        return id;
    }

    public void setIdT(Long id) {
        this.id = id;
    }

    public Double getRatting() {
        return ratting;
    }

    public void setRatting(Double ratting) {
        this.ratting = ratting;
    }
}
