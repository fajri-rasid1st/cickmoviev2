package com.example.cickmoviev2.data.api.repository.callback;

import com.example.cickmoviev2.data.models.TvShow;

public interface OnTvShowCallback {
    void onSuccess(TvShow tvShow, String message);

    void onFailure(String message);
}
