package com.example.cickmoviev2.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.cickmoviev2.R;
import com.example.cickmoviev2.data.api.repository.MovieRepository;
import com.example.cickmoviev2.data.api.repository.callback.OnMovieSearchCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnPopularMoviesCallback;
import com.example.cickmoviev2.data.models.MoviePopular;
import com.example.cickmoviev2.ui.activities.MovieDetailActivity;
import com.example.cickmoviev2.ui.adapters.MovieGridAdapter;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnMovieItemClickListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class MovieFragment extends Fragment implements OnMovieItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener {

    private SwipeRefreshLayout srlMovie;
    private LinearProgressIndicator lpiMovie;
    private ConstraintLayout clMovieError;
    private RecyclerView rvMovie;
    private MovieGridAdapter gridAdapter;
    private MovieRepository movieRepository;
    private boolean isFetching;
    private boolean responseSuccess = false;
    private int currentPage = 1;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        srlMovie = view.findViewById(R.id.srlMovie);
        lpiMovie = view.findViewById(R.id.lpiMovie);
        clMovieError = view.findViewById(R.id.clMovieError);

        rvMovie = view.findViewById(R.id.rvMovie);
        movieRepository = MovieRepository.getInstance();

        srlMovie.setOnRefreshListener(this);
        getRepositoryData("", currentPage);
        onScrollListener();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        lpiMovie.show();

        gridAdapter = null;
        currentPage = 1;

        getRepositoryData("", currentPage);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        setMenuVisibility(false);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 0) {
            lpiMovie.show();

            gridAdapter = null;
            getRepositoryData(newText, 1);
        } else {
            gridAdapter = null;
            getRepositoryData("", 1);
        }

        return true;
    }

    @Override
    public void onClick(MoviePopular moviePopular) {
        Intent movieDetailActivity = new Intent(getContext(), MovieDetailActivity.class);

        movieDetailActivity.putExtra("ID", moviePopular.getId());
        movieDetailActivity.putExtra("TITLE", moviePopular.getTitle());

        startActivity(movieDetailActivity);
    }

    private void getRepositoryData(String query, int page) {
        isFetching = true;

        if (query.equals("")) {
            movieRepository.getPopularMovies(new OnPopularMoviesCallback() {
                @Override
                public void onSuccess(List<MoviePopular> movieList, int page) {
                    responseSuccess = true;
                    clMovieError.setVisibility(View.GONE);
                    rvMovie.setVisibility(View.VISIBLE);

                    if (gridAdapter == null) {
                        gridAdapter = new MovieGridAdapter(movieList);
                        gridAdapter.setClickListener(MovieFragment.this);
                        gridAdapter.notifyDataSetChanged();
                        rvMovie.setAdapter(gridAdapter);
                    } else {
                        gridAdapter.appendList(movieList);
                    }

                    currentPage = page;
                    isFetching = false;

                    srlMovie.setRefreshing(false);
                    lpiMovie.hide();
                }

                @Override
                public void onFailure(String message) {
                    if (!responseSuccess) clMovieError.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(() -> {
                        srlMovie.setRefreshing(false);
                        lpiMovie.hide();
                    }, 3000);
                }
            }, page);
        } else {
            movieRepository.searchMovies(new OnMovieSearchCallback() {
                @Override
                public void onSuccess(List<MoviePopular> movieList, int page, String message) {
                    rvMovie.setVisibility(View.VISIBLE);

                    if (gridAdapter == null) {
                        gridAdapter = new MovieGridAdapter(movieList);
                        gridAdapter.setClickListener(MovieFragment.this);
                        gridAdapter.notifyDataSetChanged();
                        rvMovie.setAdapter(gridAdapter);
                    } else {
                        gridAdapter.appendList(movieList);
                    }

                    currentPage = page;
                    isFetching = false;

                    srlMovie.setRefreshing(false);
                    lpiMovie.hide();
                }

                @Override
                public void onFailure(String message) {
                    clMovieError.setVisibility(View.VISIBLE);
                    rvMovie.setVisibility(View.GONE);

                    new Handler().postDelayed(() -> {
                        srlMovie.setRefreshing(false);
                        lpiMovie.hide();
                    }, 3000);
                }
            }, query, page);
        }
    }

    private void onScrollListener() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        rvMovie.setLayoutManager(layoutManager);
        rvMovie.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalItem = layoutManager.getItemCount();
                int visibleItem = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItem >= totalItem / 2) {
                    if (!isFetching) {
                        isFetching = true;

                        currentPage++;
                        getRepositoryData("", currentPage);

                        isFetching = false;
                    }
                }
            }
        });
    }
}