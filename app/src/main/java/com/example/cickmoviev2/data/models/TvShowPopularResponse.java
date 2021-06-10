package com.example.cickmoviev2.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowPopularResponse {
    @SerializedName("results")
    @Expose
    private final List<TvShowPopular> populars;

    private final int page;

    public TvShowPopularResponse(List<TvShowPopular> populars, int page) {
        this.populars = populars;
        this.page = page;
    }

    public List<TvShowPopular> getPopulars() {
        return populars;
    }

    public int getPage() {
        return page;
    }
}
