package com.example.cickmoviev2.data.local.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.cickmoviev2.data.local.database.table.FavoriteMovie;
import com.example.cickmoviev2.data.local.database.table.FavoriteTvShow;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface FavoriteDao {
    // queries for favorite_movie_table
    @Query("SELECT * FROM favorite_movie_table")
    LiveData<List<FavoriteMovie>> getAllMovies();

    @Query("SELECT * FROM favorite_movie_table WHERE id=:id LIMIT 1")
    FavoriteMovie findByMovieId(int id);

    @Query("SELECT EXISTS (SELECT * FROM favorite_movie_table WHERE id=:id)")
    boolean isMovieExist(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable addFavoriteMovie(FavoriteMovie favoriteMovie);

    @Delete
    Completable deleteFavoriteMovie(FavoriteMovie favoriteMovie);

    // queries for favorite_tv_show_table
    @Query("SELECT * FROM favorite_tv_show_table")
    LiveData<List<FavoriteTvShow>> getAllTvShows();

    @Query("SELECT * FROM favorite_tv_show_table WHERE id=:id LIMIT 1")
    FavoriteTvShow findByTvShowId(int id);

    @Query("SELECT EXISTS (SELECT * FROM favorite_tv_show_table WHERE id=:id)")
    boolean isTvShowExist(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable addFavoriteTvShow(FavoriteTvShow favoriteTvShow);

    @Delete()
    Completable deleteFavoriteTvShow(FavoriteTvShow favoriteTvShow);
}