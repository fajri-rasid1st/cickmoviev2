package com.example.cickmoviev2.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cickmoviev2.Const;
import com.example.cickmoviev2.R;
import com.example.cickmoviev2.data.api.repository.TvShowRepository;
import com.example.cickmoviev2.data.api.repository.callback.OnCastCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnTvShowCallback;
import com.example.cickmoviev2.data.local.database.FavoriteHelper;
import com.example.cickmoviev2.data.models.Cast;
import com.example.cickmoviev2.data.models.Credit;
import com.example.cickmoviev2.data.models.Genres;
import com.example.cickmoviev2.data.models.TvShow;
import com.example.cickmoviev2.ui.adapters.CastAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class TvShowDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar tbDetail;
    private LinearProgressIndicator lpiTvShowDetail;
    private MaterialButton btnFavorite;
    private RecyclerView rvTvShowCast;
    private TvShowRepository tvShowRepository;
    private TvShow tvShow;
    private List<Genres> tvShowGenres;
    private List<Cast> tvShowCasts;
    private FavoriteHelper favoriteHelper;
    private String EXTRAS_ID, EXTRAS_TITLE;
    private String favTitle, favPoster, favVoteAverage, favOverview;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);

        // Configure custom toast
        Toasty.Config.getInstance().setTextSize(12).apply();

        tbDetail = findViewById(R.id.tbDetail);
        lpiTvShowDetail = findViewById(R.id.lpiTvshowDetail);
        btnFavorite = findViewById(R.id.btnFavorite);
        rvTvShowCast = findViewById(R.id.rvTvshowCast);

        tvShowRepository = TvShowRepository.getInstance();

        favoriteHelper = new FavoriteHelper(this);

        EXTRAS_ID = getIntent().getStringExtra("ID");
        EXTRAS_TITLE = getIntent().getStringExtra("TITLE");

        btnFavorite.setOnClickListener(this);

        setActionBar(EXTRAS_TITLE);
        updateFavoriteButton(EXTRAS_ID);
        loadTvShow(EXTRAS_ID);
    }

    private void setActionBar(String title) {
        setSupportActionBar(tbDetail);
        tbDetail.setTitleTextAppearance(this, R.style.WhiteTextAppearance);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateFavoriteButton(String tvId) {
        isFavorite = favoriteHelper.checkFavoriteTvShow(Integer.parseInt(tvId));

        if (!isFavorite) {
            btnFavorite.setIconResource(R.drawable.ic_outline_favorite_24);
        } else {
            btnFavorite.setIconResource(R.drawable.ic_baseline_favorite_24);
        }
    }

    private void btnFavoriteHandler() {
        String textStatus;

        if (!isFavorite) {
            if (favoriteHelper.insertFavoriteTvShow(Integer.parseInt(EXTRAS_ID), favTitle, favPoster, favVoteAverage, favOverview)) {
                textStatus = "Tv Show Has Been Added to Favorite.";
            } else {
                textStatus = "Unable to Add Tv Show. Try Again.";
            }
        } else {
            if (favoriteHelper.deleteFavoriteTvShow(Integer.parseInt(EXTRAS_ID))) {
                textStatus = "Tv Show Has Been Removed from Favorite";
            } else {
                textStatus = "Unable to Remove Tv Show. Try Again.";
            }
        }
        // make toast status
        if (textStatus.split(" ")[0].equalsIgnoreCase("UNABLE")) {
            Toasty.error(this, textStatus, Toast.LENGTH_SHORT, true)
                    .show();
        } else {
            Toasty.success(this, textStatus, Toast.LENGTH_SHORT, true)
                    .show();
        }
        // update favorite button
        updateFavoriteButton(EXTRAS_ID);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnFavorite) btnFavoriteHandler();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_toolbar_menu, menu);

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

    private void loadTvShow(String tvId) {
        tvShowRepository.getTvShow(tvId, new OnTvShowCallback() {
            @Override
            public void onSuccess(TvShow tvShowDetail, String message) {
                tvShow = tvShowDetail;
                tvShowGenres = tvShow.getGenres();

                loadTvShowCast(tvId);
                setDetailActivityContent();

                lpiTvShowDetail.hide();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(TvShowDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> lpiTvShowDetail.hide(), 3000);
            }
        });
    }

    private void loadTvShowCast(String tvId) {
        tvShowRepository.getTvShowCast(tvId, new OnCastCallback() {
            @Override
            public void onSuccess(Credit credit, String message) {
                tvShowCasts = credit.getCast();

                rvTvShowCast.setAdapter(new CastAdapter(tvShowCasts));
                rvTvShowCast.setLayoutManager(new LinearLayoutManager(TvShowDetailActivity.this, RecyclerView.HORIZONTAL, false));
                rvTvShowCast.setHasFixedSize(true);

                lpiTvShowDetail.hide();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(TvShowDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> lpiTvShowDetail.hide(), 3000);
            }
        });
    }

    private void setDetailActivityContent() {
        TextView tvTitle = findViewById(R.id.tvTvshowTitleDetail);
        TextView tvRuntimeDetail = findViewById(R.id.tvTvshowRuntimeDetail);
        TextView tvVoteCount = findViewById(R.id.tvTvshowVoteCountDetail);
        TextView tvFullTitle = findViewById(R.id.tvTvshowFullTitleDetail);
        TextView tvReleased = findViewById(R.id.tvTvshowReleaseDetail);
        TextView tvVoteAverage = findViewById(R.id.tvTvshowVoteAverageDetail);
        TextView tvTotalEpisode = findViewById(R.id.tvTotalEpisode);
        TextView tvTotalSeason = findViewById(R.id.tvTotalSeason);
        TextView tvGenres = findViewById(R.id.tvTvshowGenresDetail);
        ExpandableTextView etvOverview = findViewById(R.id.etvTvshowOverviewDetail);
        RatingBar rbVoteAverage = findViewById(R.id.rbTvshowVoteAverageDetail);
        ImageView ivPoster = findViewById(R.id.ivTvshowPosterDetail);
        ImageView ivBanner = findViewById(R.id.ivTvshowBannerDetail);

        tvTitle.setText(tvShow.getTitle());
        tvRuntimeDetail.setText(tvShow.getRuntime());
        tvVoteCount.setText(tvShow.getVoteCount());
        tvFullTitle.setText(tvShow.getTitle());
        tvReleased.setText(tvShow.getFirstAndLastAirDate());
        tvVoteAverage.setText(tvShow.getVoteAverage());
        tvTotalEpisode.setText(tvShow.getTotalEpisode());
        tvTotalSeason.setText(tvShow.getTotalSeason());
        etvOverview.setText(tvShow.getOverview());
        rbVoteAverage.setRating(Float.parseFloat(tvShow.getVoteAverage()) / 2);

        favTitle = tvShow.getTitle();
        favPoster = tvShow.getPosterUrl();
        favVoteAverage = tvShow.getVoteAverage();
        favOverview = tvShow.getOverview();

        StringBuilder genresText = new StringBuilder();

        for (Genres genres : tvShowGenres) {
            genresText.append(genres.getGenre()).append(", ");
        }

        tvGenres.setText(genresText.substring(0, genresText.length() - 2));

        Glide.with(TvShowDetailActivity.this)
                .load(Const.IMG_URL_300 + tvShow.getPosterUrl())
                .into(ivPoster);

        Glide.with(TvShowDetailActivity.this)
                .load(Const.IMG_URL_500 + tvShow.getBackdropUrl())
                .into(ivBanner);
    }
}