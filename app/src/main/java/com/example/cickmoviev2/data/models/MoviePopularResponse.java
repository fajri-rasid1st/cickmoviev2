package com.example.cickmoviev2.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviePopularResponse {
    @SerializedName("results")
    @Expose
    private final List<MoviePopular> populars;

    private final int page;

    public MoviePopularResponse(List<MoviePopular> populars, int page) {
        this.populars = populars;
        this.page = page;
    }

    public List<MoviePopular> getPopulars() {
        return populars;
    }

    public int getPage() {
        return page;
    }
}
