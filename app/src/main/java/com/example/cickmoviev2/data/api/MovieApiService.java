package com.example.cickmoviev2.data.api;

import com.example.cickmoviev2.data.models.Credit;
import com.example.cickmoviev2.data.models.Movie;
import com.example.cickmoviev2.data.models.MoviePopularResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {
    @GET("movie/popular")
    Call<MoviePopularResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/credits")
    Call<Credit> getMovieCast(
            @Path("movie_id") String id,
            @Query("api_key") String apiKey
    );

    @GET("search/movie")
    Call<MoviePopularResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int page
    );
}
