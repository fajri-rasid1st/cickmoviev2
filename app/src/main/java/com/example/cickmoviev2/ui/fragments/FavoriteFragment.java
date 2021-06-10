package com.example.cickmoviev2.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cickmoviev2.R;
import com.example.cickmoviev2.ui.adapters.FavoritePagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class FavoriteFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        viewPager = view.findViewById(R.id.favoriteViewPager);
        tabLayout = view.findViewById(R.id.favoriteTabLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        FavoritePagerAdapter favoritePagerAdapter = new FavoritePagerAdapter(getChildFragmentManager());

        favoritePagerAdapter.addFragment(new FavoriteMovieFragment(),
                getString(R.string.label_movie));
        favoritePagerAdapter.addFragment(new FavoriteTvShowFragment(),
                getString(R.string.label_tv));

        viewPager.setAdapter(favoritePagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
    }
}