package com.developer.ashwoolford.moviesfreak.second_fragment;

/**
 * Created by ashwoolford on 11/25/2016.
 */
public class SuggestMovieData  {
    String poster;
    Double id;
    Long movieId;
    public SuggestMovieData(Double id,String poster,Long movieId){
        this.id = id;
        this.poster = poster;
        this.movieId = movieId;
    }

    public Double getnTitle() {
        return id;
    }

    public void setnTitle(Double id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
