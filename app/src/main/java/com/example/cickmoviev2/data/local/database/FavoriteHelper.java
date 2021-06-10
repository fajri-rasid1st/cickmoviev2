package com.example.cickmoviev2.data.local.database;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.cickmoviev2.data.local.database.table.FavoriteMovie;
import com.example.cickmoviev2.data.local.database.table.FavoriteTvShow;

public class FavoriteHelper {
    private final FavoriteDatabase roomDatabase;
    private boolean status;

    public FavoriteHelper(Context context) {
        roomDatabase = FavoriteDatabase.getInstance(context);
        status = false;
    }

    public boolean checkFavoriteMovies(int id) {
        return roomDatabase.favoriteDao().isMovieExist(id);
    }

    public boolean checkFavoriteTvShow(int id) {
        return roomDatabase.favoriteDao().isTvShowExist(id);
    }

    @SuppressLint("CheckResult")
    public boolean insertFavoriteMovie(int id, String title, String posterPath, String voteAverage, String overview) {
        FavoriteMovie movie = new FavoriteMovie(id, title, posterPath, voteAverage, overview);

        roomDatabase.favoriteDao().addFavoriteMovie(movie).subscribe(() -> {
            status = true;
        }, throwable -> {
            status = false;
        });

        return status;
    }

    @SuppressLint("CheckResult")
    public boolean insertFavoriteTvShow(int id, String title, String posterPath, String voteAverage, String overview) {
        FavoriteTvShow tvShow = new FavoriteTvShow(id, title, posterPath, voteAverage, overview);

        roomDatabase.favoriteDao().addFavoriteTvShow(tvShow).subscribe(() -> {
            status = true;
        }, throwable -> {
            status = false;
        });

        return status;
    }

    @SuppressLint("CheckResult")
    public boolean deleteFavoriteMovie(int id) {
        FavoriteMovie movie = roomDatabase.favoriteDao().findByMovieId(id);

        roomDatabase.favoriteDao().deleteFavoriteMovie(movie).subscribe(() -> {
            status = true;
        }, throwable -> {
            status = false;
        });

        return status;
    }

    @SuppressLint("CheckResult")
    public boolean deleteFavoriteTvShow(int id) {
        FavoriteTvShow tvShow = roomDatabase.favoriteDao().findByTvShowId(id);

        roomDatabase.favoriteDao().deleteFavoriteTvShow(tvShow).subscribe(() -> {
            status = true;
        }, throwable -> {
            status = false;
        });

        return status;
    }
}
