package com.developer.ashwoolford.moviesfreak.AccountPackage;

/**
 * Created by ashwoolford on 1/28/2017.
 */
public class ListData {
    String movieGenre,movieId,movieName,posterUri,status;

    public ListData(){

    }

    public ListData(String movieGenre, String movieId, String movieName, String posterUri, String status) {
        this.movieGenre = movieGenre;
        this.movieId = movieId;
        this.movieName = movieName;
        this.posterUri = posterUri;
        this.status = status;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
