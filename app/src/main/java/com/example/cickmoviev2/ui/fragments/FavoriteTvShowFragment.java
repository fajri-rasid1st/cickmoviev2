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
import com.example.cickmoviev2.data.local.database.table.FavoriteTvShow;
import com.example.cickmoviev2.ui.activities.TvShowDetailActivity;
import com.example.cickmoviev2.ui.adapters.TvShowListAdapter;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnFavoriteTvShowItemClickListener;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class FavoriteTvShowFragment extends Fragment implements OnFavoriteTvShowItemClickListener {
    private ConstraintLayout clFavTvShowEmpty;
    private RecyclerView rvFavTvShow;
    private TvShowListAdapter listAdapter;
    private FavoriteDatabase favoriteDatabase;
    private FavoriteHelper favoriteHelper;

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

        // Configure custom toast
        Toasty.Config.getInstance().setTextSize(12).apply();

        clFavTvShowEmpty = view.findViewById(R.id.clFavTvShowEmpty);
        rvFavTvShow = view.findViewById(R.id.rvFavTvShow);

        favoriteDatabase = FavoriteDatabase.getInstance(requireActivity().getApplicationContext());
        favoriteHelper = new FavoriteHelper(requireActivity().getApplicationContext());

        rvFavTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();

        return view;
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
                        listAdapter.notifyDataSetChanged();
                        rvFavTvShow.setAdapter(listAdapter);

                        swipeToDelete(listAdapter, rvFavTvShow);

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

    private void swipeToDelete(TvShowListAdapter listAdapter, RecyclerView rvFavTvShow) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (favoriteHelper.deleteFavoriteTvShow(listAdapter.getTvShowAt(position).getId())) {
                    listAdapter.remove(position);
                    rvFavTvShow.removeViewAt(position);
                    listAdapter.notifyItemRemoved(position);
                    listAdapter.notifyItemRangeChanged(position, listAdapter.getItemCount());

                    Toasty.success(requireContext(),
                            "Tv Show Has Been Removed from Favorite.",
                            Toast.LENGTH_SHORT, true)
                            .show();
                } else {
                    Toasty.error(requireContext(),
                            "Unable to Remove Tv Show. Try Again.",
                            Toast.LENGTH_SHORT, true)
                            .show();
                }
            }
        }).attachToRecyclerView(rvFavTvShow);
    }
}