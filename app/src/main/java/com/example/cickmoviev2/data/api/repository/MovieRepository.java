package com.example.cickmoviev2.data.api.repository;

import androidx.annotation.NonNull;

import com.example.cickmoviev2.Const;
import com.example.cickmoviev2.data.api.MovieApiService;
import com.example.cickmoviev2.data.api.repository.callback.OnMovieCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnMovieSearchCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnPopularMoviesCallback;
import com.example.cickmoviev2.data.models.Movie;
import com.example.cickmoviev2.data.models.MoviePopularResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {
    private static MovieRepository movieRepository;
    private final MovieApiService movieService;

    private MovieRepository(MovieApiService movieService) {
        this.movieService = movieService;
    }

    public static MovieRepository getInstance() {
        if (movieRepository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            movieRepository = new MovieRepository(retrofit.create(MovieApiService.class));
        }

        return movieRepository;
    }

    public void getPopularMovies(final OnPopularMoviesCallback callback, int page) {
        movieService.getPopularMovies(Const.API_KEY, page).enqueue(new Callback<MoviePopularResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviePopularResponse> call, @NonNull Response<MoviePopularResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getPopulars() != null) {
                            callback.onSuccess(response.body().getPopulars(), response.body().getPage());
                        } else {
                            callback.onFailure(response.message());
                        }
                    } else {
                        callback.onFailure(response.message());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviePopularResponse> call, @NonNull Throwable t) {
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    public void getMovie(String movieId, final OnMovieCallback callback) {
        movieService.getMovie(movieId, Const.API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        callback.onSuccess(response.body(), response.message());
                    } else {
                        callback.onFailure(response.message());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void searchMovies(final OnMovieSearchCallback callback, String query, int page) {
        movieService.searchMovies(Const.API_KEY, query, page).enqueue(new Callback<MoviePopularResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviePopularResponse> call, @NonNull Response<MoviePopularResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getPopulars() != null) {
                            callback.onSuccess(response.body().getPopulars(), response.body().getPage(), response.message());
                        } else {
                            callback.onFailure(response.message());
                        }
                    } else {
                        callback.onFailure(response.message());
                    }
                } else {
                    callback.onFailure(response.message() + " : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviePopularResponse> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
