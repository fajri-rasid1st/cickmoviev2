package com.example.cickmoviev2.data.api.repository.callback;

import com.example.cickmoviev2.data.models.TvShowPopular;

import java.util.List;

public interface OnTvShowSearchCallback {
    void onSuccess(List<TvShowPopular> tvShowList, int page, String message);

    void onFailure(String message);
}
