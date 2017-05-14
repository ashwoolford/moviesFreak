package com.developer.ashwoolford.moviesfreak.Fragment3;

/**
 * Created by ashwoolford on 2/3/2017.
 */
public class TopMoviesData {

    private String title,poster,releaseYear;
    private Long id;
    private Double vote_average;


    public TopMoviesData(String title, String poster, String releaseYear, Long id, Double vote_average) {
        this.title = title;
        this.poster = poster;
        this.releaseYear = releaseYear;
        this.id = id;
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Long getFId() {
        return id;
    }

    public void setFId(Long id) {
        this.id = id;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }
}
