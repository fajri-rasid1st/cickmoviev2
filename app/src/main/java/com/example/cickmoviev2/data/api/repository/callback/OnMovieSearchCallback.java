package com.example.cickmoviev2.data.api.repository.callback;

import com.example.cickmoviev2.data.models.MoviePopular;

import java.util.List;

public interface OnMovieSearchCallback {
    void onSuccess(List<MoviePopular> movieList, int page, String message);

    void onFailure(String message);
}
