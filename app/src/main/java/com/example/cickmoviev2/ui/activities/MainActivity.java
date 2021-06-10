package com.example.cickmoviev2.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.cickmoviev2.R;
import com.example.cickmoviev2.ui.fragments.FavoriteFragment;
import com.example.cickmoviev2.ui.fragments.MovieFragment;
import com.example.cickmoviev2.ui.fragments.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Toast backToast;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tbMain = findViewById(R.id.tbMain);
        BottomNavigationView bnvMain = findViewById(R.id.bnvMain);

        setSupportActionBar(tbMain);
        tbMain.setTitleTextAppearance(this, R.style.PoppinsBoldTextAppearance);

        bnvMain.setOnNavigationItemSelectedListener(this);
        setSelectedItem(bnvMain);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        assert getSupportActionBar() != null;
        switch (item.getItemId()) {
            case R.id.menu_item_movie:
                setActionBar(getString(R.string.label_movie));
                selectedFragment = new MovieFragment();
                break;
            case R.id.menu_item_tv_show:
                setActionBar(getString(R.string.label_tv));
                selectedFragment = new TvShowFragment();
                break;
            case R.id.menu_item_favorite:
                setActionBar(getString(R.string.label_favorite));
                selectedFragment = new FavoriteFragment();
                break;
        }

        assert selectedFragment != null;
        startFragment(selectedFragment);

        return true;
    }

    private void setSelectedItem(BottomNavigationView bnvMain) {
        if (getIntent().getStringExtra("SELECTED_FRAGMENT") != null) {
            switch (getIntent().getStringExtra("SELECTED_FRAGMENT")) {
                case "MOVIE":
                    bnvMain.setSelectedItemId(R.id.menu_item_movie);
                    break;
                case "TV_SHOW":
                    bnvMain.setSelectedItemId(R.id.menu_item_tv_show);
                    break;
                case "FAVORITE":
                    bnvMain.setSelectedItemId(R.id.menu_item_favorite);
                    break;
            }
        } else {
            bnvMain.setSelectedItemId(R.id.menu_item_movie);
        }
    }

    private void setActionBar(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    private void startFragment(Fragment selectedFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flMain, selectedFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(this, "Press Back Again to Exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}