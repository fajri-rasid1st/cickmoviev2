package com.example.cickmoviev2.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cickmoviev2.R;
import com.example.cickmoviev2.ui.fragments.FavoriteFragment;
import com.example.cickmoviev2.ui.fragments.MovieFragment;
import com.example.cickmoviev2.ui.fragments.TvShowFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);

        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();

            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources()
                        .getColor(R.color.textOnPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }

    private void setSelectedItem(BottomNavigationView bnvMain) {
        if (getIntent().getStringExtra("SELECTED_FRAGMENT") != null) {
            switch (getIntent().getStringExtra("SELECTED_FRAGMENT")) {
                case "movie":
                    bnvMain.setSelectedItemId(R.id.menu_item_movie);
                    break;
                case "tv_show":
                    bnvMain.setSelectedItemId(R.id.menu_item_tv_show);
                    break;
                case "favorite":
                    bnvMain.setSelectedItemId(R.id.menu_item_favorite);
                    break;
            }
        } else {
            bnvMain.setSelectedItemId(R.id.menu_item_movie);
        }
    }

    private void setActionBar(String title) {
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void startFragment(Fragment selectedFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flMain, selectedFragment)
                .commit();
    }
}