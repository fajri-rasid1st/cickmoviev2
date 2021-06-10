package com.example.cickmoviev2.data.api;

import com.example.cickmoviev2.data.models.Credit;
import com.example.cickmoviev2.data.models.TvShow;
import com.example.cickmoviev2.data.models.TvShowPopularResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TvShowApiService {
    // method to get popular tv show from API
    @GET("tv/popular")
    Call<TvShowPopularResponse> getPopularTvShows(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    // method to get tv show detail based on tvId from API
    @GET("tv/{tv_id}")
    Call<TvShow> getTvShow(
            @Path("tv_id") String id,
            @Query("api_key") String apiKey
    );

    // method to get tv show casts based on tvId from API
    @GET("tv/{tv_id}/credits")
    Call<Credit> getTvShowCast(
            @Path("tv_id") String id,
            @Query("api_key") String apiKey
    );

    // method to get tv show based on query search from API
    @GET("search/tv")
    Call<TvShowPopularResponse> searchTvShows(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int page
    );
}
