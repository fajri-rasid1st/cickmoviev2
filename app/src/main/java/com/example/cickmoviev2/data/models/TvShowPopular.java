package com.example.cickmoviev2.data.models;

import com.google.gson.annotations.SerializedName;

public class TvShowPopular {
    // attribute
    private String id;

    @SerializedName("name")
    private String title;

    @SerializedName("first_air_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("poster_path")
    private String imgUrl;

    // method
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate != null ? releaseDate.split("-")[0] : "None";
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;

    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
