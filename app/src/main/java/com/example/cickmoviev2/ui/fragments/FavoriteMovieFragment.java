package com.example.cickmoviev2.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cickmoviev2.R;
import com.example.cickmoviev2.data.local.database.FavoriteDatabase;
import com.example.cickmoviev2.data.local.database.FavoriteHelper;
import com.example.cickmoviev2.data.local.database.table.FavoriteMovie;
import com.example.cickmoviev2.ui.activities.MovieDetailActivity;
import com.example.cickmoviev2.ui.adapters.MovieListAdapter;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnFavoriteMovieItemClickListener;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class FavoriteMovieFragment extends Fragment implements OnFavoriteMovieItemClickListener {
    private ConstraintLayout clFavMovieEmpty;
    private RecyclerView rvFavMovie;
    private MovieListAdapter listAdapter;
    private FavoriteDatabase favoriteDatabase;
    private FavoriteHelper favoriteHelper;

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

        // Configure custom toast
        Toasty.Config.getInstance().setTextSize(12).apply();

        clFavMovieEmpty = view.findViewById(R.id.clFavMovieEmpty);
        rvFavMovie = view.findViewById(R.id.rvFavMovie);

        favoriteDatabase = FavoriteDatabase.getInstance(requireActivity().getApplicationContext());
        favoriteHelper = new FavoriteHelper(requireActivity().getApplicationContext());

        rvFavMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();

        return view;
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
                        listAdapter.notifyDataSetChanged();
                        rvFavMovie.setAdapter(listAdapter);

                        swipeToDelete(listAdapter, rvFavMovie);

                        if (favoriteMovies.size() == 0) {
                            clFavMovieEmpty.setVisibility(View.VISIBLE);
                        }
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

    // this method is used for delete item in recycler view when swiped left or right
    private void swipeToDelete(MovieListAdapter listAdapter, RecyclerView rvFavMovie) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // position of item
                int position = viewHolder.getAdapterPosition();

                // when item is successfully removed
                if (favoriteHelper.deleteFavoriteMovie(listAdapter.getMovieAt(position).getId())) {
                    listAdapter.remove(position);
                    rvFavMovie.removeViewAt(position);
                    listAdapter.notifyItemRemoved(position);
                    listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount());

                    Toasty.success(requireContext(),
                            "Movie Has Been Removed from Favorite.",
                            Toast.LENGTH_SHORT, true)
                            .show();
                }
                // when item is failed to remove
                else {
                    Toasty.error(requireContext(),
                            "Unable to Remove Movie. Try Again.",
                            Toast.LENGTH_SHORT, true)
                            .show();
                }
            }
        }).attachToRecyclerView(rvFavMovie);
    }
}