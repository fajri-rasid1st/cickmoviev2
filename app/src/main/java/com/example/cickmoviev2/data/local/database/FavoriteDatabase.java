package com.example.cickmoviev2.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cickmoviev2.data.local.database.dao.FavoriteDao;
import com.example.cickmoviev2.data.local.database.table.FavoriteMovie;
import com.example.cickmoviev2.data.local.database.table.FavoriteTvShow;

// create database named favorite_database
@Database(entities = {FavoriteMovie.class, FavoriteTvShow.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {
    private static FavoriteDatabase instance;

    public abstract FavoriteDao favoriteDao();

    // create singleton class for this database
    public synchronized static FavoriteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    FavoriteDatabase.class,
                    "favorite_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }
}
