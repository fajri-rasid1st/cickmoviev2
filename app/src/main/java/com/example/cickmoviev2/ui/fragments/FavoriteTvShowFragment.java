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
import com.example.cickmoviev2.data.local.database.table.FavoriteTvShow;
import com.example.cickmoviev2.ui.activities.TvShowDetailActivity;
import com.example.cickmoviev2.ui.adapters.TvShowListAdapter;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnFavoriteTvShowItemClickListener;

import java.util.List;

public class FavoriteTvShowFragment extends Fragment implements OnFavoriteTvShowItemClickListener {
    private ConstraintLayout clFavTvShowEmpty;
    private RecyclerView rvFavTvShow;
    private FavoriteDatabase favoriteDatabase;
    private TvShowListAdapter listAdapter;

    public FavoriteTvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);

        favoriteDatabase = FavoriteDatabase.getInstance(requireActivity().getApplicationContext());
        clFavTvShowEmpty = view.findViewById(R.id.clFavTvShowEmpty);
        rvFavTvShow = view.findViewById(R.id.rvFavTvShow);

        rvFavTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        clFavTvShowEmpty.setVisibility(View.GONE);

        favoriteDatabase.favoriteDao()
                .getAllTvShows()
                .observe(getViewLifecycleOwner(), new Observer<List<FavoriteTvShow>>() {
                    @Override
                    public void onChanged(List<FavoriteTvShow> favoriteTvShows) {

                        listAdapter = new TvShowListAdapter(favoriteTvShows);
                        listAdapter.setClickListener(FavoriteTvShowFragment.this);
                        rvFavTvShow.setAdapter(listAdapter);

                        if (favoriteTvShows.size() == 0) {
                            clFavTvShowEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onClick(FavoriteTvShow favoriteTvShow) {
        Intent tvShowDetailActivity = new Intent(getContext(), TvShowDetailActivity.class);

        tvShowDetailActivity.putExtra("ID", String.valueOf(favoriteTvShow.getId()));
        tvShowDetailActivity.putExtra("TITLE", favoriteTvShow.getTitle());

        startActivity(tvShowDetailActivity);
    }
}