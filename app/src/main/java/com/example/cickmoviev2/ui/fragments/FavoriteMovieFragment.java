package com.example.cickmoviev2.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cickmoviev2.R;
import com.example.cickmoviev2.data.local.database.FavoriteDatabase;
import com.example.cickmoviev2.data.local.database.table.FavoriteMovie;
import com.example.cickmoviev2.ui.activities.MovieDetailActivity;
import com.example.cickmoviev2.ui.adapters.MovieListAdapter;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnFavoriteMovieItemClickListener;

import java.util.List;

public class FavoriteMovieFragment extends Fragment implements OnFavoriteMovieItemClickListener {
    private ConstraintLayout clFavMovieEmpty;
    private RecyclerView rvFavMovie;
    private FavoriteDatabase favoriteDatabase;
    private MovieListAdapter listAdapter;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);

        favoriteDatabase = FavoriteDatabase.getInstance(requireActivity().getApplicationContext());
        clFavMovieEmpty = view.findViewById(R.id.clFavMovieEmpty);
        rvFavMovie = view.findViewById(R.id.rvFavMovie);

        rvFavMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        clFavMovieEmpty.setVisibility(View.GONE);

        favoriteDatabase.favoriteDao()
                .getAllMovies()
                .observe(getViewLifecycleOwner(), new Observer<List<FavoriteMovie>>() {
                    @Override
                    public void onChanged(List<FavoriteMovie> favoriteMovies) {

                        listAdapter = new MovieListAdapter(favoriteMovies);
                        listAdapter.setClickListener(FavoriteMovieFragment.this);
                        rvFavMovie.setAdapter(listAdapter);

                        if (favoriteMovies.size() == 0) clFavMovieEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onClick(FavoriteMovie favoriteMovie) {
        Intent movieDetailActivity = new Intent(getContext(), MovieDetailActivity.class);

        movieDetailActivity.putExtra("ID", String.valueOf(favoriteMovie.getId()));
        movieDetailActivity.putExtra("TITLE", favoriteMovie.getTitle());

        startActivity(movieDetailActivity);
    }
}