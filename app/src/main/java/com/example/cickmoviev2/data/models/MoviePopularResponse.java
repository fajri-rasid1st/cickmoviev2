package com.example.cickmoviev2.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviePopularResponse {
    private int page;

    @SerializedName("results")
    @Expose
    private List<MoviePopular> populars;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    public MoviePopularResponse() {
    }

    public int getPage() {
        return page;
    }

    public List<MoviePopular> getPopulars() {
        return populars;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
