package com.example.cickmoviev2.data.api.repository;

import androidx.annotation.NonNull;

import com.example.cickmoviev2.Const;
import com.example.cickmoviev2.data.api.TvShowApiService;
import com.example.cickmoviev2.data.api.repository.callback.OnPopularTvShowsCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnTvShowCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnTvShowSearchCallback;
import com.example.cickmoviev2.data.models.TvShow;
import com.example.cickmoviev2.data.models.TvShowPopularResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TvShowRepository {
    private static TvShowRepository tvShowRepository;
    private final TvShowApiService tvShowService;

    private TvShowRepository(TvShowApiService tvShowService) {
        this.tvShowService = tvShowService;
    }

    public static TvShowRepository getInstance() {
        if (tvShowRepository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            tvShowRepository = new TvShowRepository(retrofit.create(TvShowApiService.class));
        }

        return tvShowRepository;
    }

    public void getPopularTvShows(final OnPopularTvShowsCallback callback, int page) {
        tvShowService.getPopularTvShows(Const.API_KEY, page).enqueue(new Callback<TvShowPopularResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowPopularResponse> call, @NonNull Response<TvShowPopularResponse> response) {
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
            public void onFailure(@NonNull Call<TvShowPopularResponse> call, @NonNull Throwable t) {
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    public void getTvShow(String tvId, final OnTvShowCallback callback) {
        tvShowService.getTvShow(tvId, Const.API_KEY).enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(@NonNull Call<TvShow> call, @NonNull Response<TvShow> response) {
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
            public void onFailure(@NonNull Call<TvShow> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void searchTvShows(final OnTvShowSearchCallback callback, String query, int page) {
        tvShowService.searchTvShows(Const.API_KEY, query, page).enqueue(new Callback<TvShowPopularResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowPopularResponse> call, @NonNull Response<TvShowPopularResponse> response) {
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
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowPopularResponse> call, @NonNull Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
