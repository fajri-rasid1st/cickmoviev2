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
import com.example.cickmoviev2.data.api.repository.TvShowRepository;
import com.example.cickmoviev2.data.api.repository.callback.OnPopularTvShowsCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnTvShowSearchCallback;
import com.example.cickmoviev2.data.models.TvShowPopular;
import com.example.cickmoviev2.ui.activities.TvShowDetailActivity;
import com.example.cickmoviev2.ui.adapters.TvShowGridAdapter;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnTvShowItemClickListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class TvShowFragment extends Fragment implements OnTvShowItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener {

    private SwipeRefreshLayout srlTvShow;
    private LinearProgressIndicator lpiTvShow;
    private ConstraintLayout clTvShowError;
    private RecyclerView rvTvShow;
    private TvShowGridAdapter gridAdapter;
    private TvShowRepository tvShowRepository;
    private boolean isFetching;
    private boolean responseSuccess = false;
    private int currentPage = 1;

    public TvShowFragment() {
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
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);

        srlTvShow = view.findViewById(R.id.srlTvshow);
        lpiTvShow = view.findViewById(R.id.lpiTvshow);
        clTvShowError = view.findViewById(R.id.clTvshowError);

        rvTvShow = view.findViewById(R.id.rvTvshow);
        tvShowRepository = TvShowRepository.getInstance();

        srlTvShow.setOnRefreshListener(this);
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
        lpiTvShow.show();

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
            lpiTvShow.show();

            gridAdapter = null;
            getRepositoryData(newText, 1);
        } else {
            gridAdapter = null;
            getRepositoryData("", 1);
        }

        return true;
    }

    @Override
    public void onClick(TvShowPopular tvShowPopular) {
        Intent tvShowDetailActivity = new Intent(getContext(), TvShowDetailActivity.class);

        tvShowDetailActivity.putExtra("ID", tvShowPopular.getId());
        tvShowDetailActivity.putExtra("TITLE", tvShowPopular.getTitle());

        startActivity(tvShowDetailActivity);
    }

    private void getRepositoryData(String query, int page) {
        isFetching = true;

        if (query.equals("")) {
            tvShowRepository.getPopularTvShows(new OnPopularTvShowsCallback() {
                @Override
                public void onSuccess(List<TvShowPopular> tvShowList, int page) {
                    responseSuccess = true;
                    clTvShowError.setVisibility(View.GONE);
                    rvTvShow.setVisibility(View.VISIBLE);

                    if (gridAdapter == null) {
                        gridAdapter = new TvShowGridAdapter(tvShowList);
                        gridAdapter.setClickListener(TvShowFragment.this);
                        gridAdapter.notifyDataSetChanged();
                        rvTvShow.setAdapter(gridAdapter);
                    } else {
                        gridAdapter.appendList(tvShowList);
                    }

                    currentPage = page;
                    isFetching = false;

                    srlTvShow.setRefreshing(false);
                    lpiTvShow.hide();
                }

                @Override
                public void onFailure(String message) {
                    if (!responseSuccess) clTvShowError.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(() -> {
                        srlTvShow.setRefreshing(false);
                        lpiTvShow.hide();
                    }, 3000);
                }
            }, page);
        } else {
            tvShowRepository.searchTvShows(new OnTvShowSearchCallback() {
                @Override
                public void onSuccess(List<TvShowPopular> tvShowList, int page, String message) {
                    rvTvShow.setVisibility(View.VISIBLE);

                    if (gridAdapter == null) {
                        gridAdapter = new TvShowGridAdapter(tvShowList);
                        gridAdapter.setClickListener(TvShowFragment.this);
                        gridAdapter.notifyDataSetChanged();
                        rvTvShow.setAdapter(gridAdapter);
                    } else {
                        gridAdapter.appendList(tvShowList);
                    }

                    currentPage = page;
                    isFetching = false;

                    srlTvShow.setRefreshing(false);
                    lpiTvShow.hide();
                }

                @Override
                public void onFailure(String message) {
                    clTvShowError.setVisibility(View.VISIBLE);
                    rvTvShow.setVisibility(View.GONE);

                    new Handler().postDelayed(() -> {
                        srlTvShow.setRefreshing(false);
                        lpiTvShow.hide();
                    }, 3000);
                }
            }, query, page);
        }
    }

    private void onScrollListener() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        rvTvShow.setLayoutManager(layoutManager);
        rvTvShow.addOnScrollListener(new RecyclerView.OnScrollListener() {
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