package com.developer.ashwoolford.moviesfreak.FirebaseHelperPackage;

/**
 * Created by ashwoolford on 1/28/2017.
 */
public class SavedListData {
    String movieName,movieId,movieGenre,status,posterUri;

    public SavedListData(String movieName, String movieId, String movieGenre, String status,String posterUri) {
        this.movieName = movieName;
        this.movieId = movieId;
        this.movieGenre = movieGenre;
        this.status = status;
        this.posterUri = posterUri;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }
}
