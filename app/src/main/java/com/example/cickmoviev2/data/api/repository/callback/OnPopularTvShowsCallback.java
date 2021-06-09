package com.example.cickmoviev2.data.api.repository.callback;

import com.example.cickmoviev2.data.models.TvShowPopular;

import java.util.List;

public interface OnPopularTvShowsCallback {
    void onSuccess(List<TvShowPopular> tvShowList, int page);

    void onFailure(String message);
}
