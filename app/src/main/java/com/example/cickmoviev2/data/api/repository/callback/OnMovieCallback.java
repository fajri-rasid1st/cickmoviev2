package com.example.cickmoviev2.data.api.repository.callback;

import com.example.cickmoviev2.data.models.Movie;

public interface OnMovieCallback {
    void onSuccess(Movie movie, String message);

    void onFailure(String message);
}
