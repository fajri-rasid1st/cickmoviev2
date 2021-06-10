package com.example.cickmoviev2.data.api.repository;

import androidx.annotation.NonNull;

import com.example.cickmoviev2.Const;
import com.example.cickmoviev2.data.api.TvShowApiService;
import com.example.cickmoviev2.data.api.repository.callback.OnCastCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnPopularTvShowsCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnTvShowCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnTvShowSearchCallback;
import com.example.cickmoviev2.data.models.Credit;
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

    // create singleton instance for this class
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

    // method to get popular tv shows
    public void getPopularTvShows(final OnPopularTvShowsCallback callback, int page) {
        tvShowService.getPopularTvShows(Const.API_KEY, page).enqueue(new Callback<TvShowPopularResponse>() {
            @Override
            // this method called when response on progress
            public void onResponse(@NonNull Call<TvShowPopularResponse> call, @NonNull Response<TvShowPopularResponse> response) {
                // response is successful if code() is in the range [200..300)
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

            // this method called when response on failure
            @Override
            public void onFailure(@NonNull Call<TvShowPopularResponse> call, @NonNull Throwable t) {
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    // method to get detail tv show
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
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    // method to get tv show casts
    public void getTvShowCast(String tvId, final OnCastCallback callback) {
        tvShowService.getTvShowCast(tvId, Const.API_KEY).enqueue(new Callback<Credit>() {
            @Override
            public void onResponse(@NonNull Call<Credit> call, @NonNull Response<Credit> response) {
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
            public void onFailure(@NonNull Call<Credit> call, @NonNull Throwable t) {
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }

    // method to get tv show list when searching
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
                callback.onFailure(t.getLocalizedMessage());
            }
        });
    }
}
