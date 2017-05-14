package com.developer.ashwoolford.moviesfreak.search_activity;

/**
 * Created by ashwoolford on 11/25/2016.
 */
public class SearchData {
    String title,poster,releseDate;
    Long id;
    Double ratting;

    public SearchData(String title,String poster,String releseDate,Long id,Double ratting){
        this.title = title;
        this.poster = poster;
        this.releseDate = releseDate;
        this.id = id;
        this.ratting = ratting;
    }

    public String getNtitle() {
        return title;
    }

    public void setNtitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleseDate() {
        return releseDate;
    }

    public void setReleseDate(String releseDate) {
        this.releseDate = releseDate;
    }

    public Long getNid() {
        return id;
    }

    public void setNid(Long id) {
        this.id = id;
    }

    public Double getRatting() {
        return ratting;
    }

    public void setRatting(Double ratting) {
        this.ratting = ratting;
    }
}
