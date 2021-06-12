package com.example.cickmoviev2.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cickmoviev2.Const;
import com.example.cickmoviev2.R;
import com.example.cickmoviev2.data.api.repository.TvShowRepository;
import com.example.cickmoviev2.data.api.repository.callback.OnCastCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnTvShowCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnVideoCallback;
import com.example.cickmoviev2.data.local.database.FavoriteHelper;
import com.example.cickmoviev2.data.models.Cast;
import com.example.cickmoviev2.data.models.Credit;
import com.example.cickmoviev2.data.models.Genres;
import com.example.cickmoviev2.data.models.TvShow;
import com.example.cickmoviev2.data.models.Video;
import com.example.cickmoviev2.data.models.VideoResponse;
import com.example.cickmoviev2.ui.adapters.CastAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class TvShowDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar tbDetail;
    private LinearProgressIndicator lpiTvShowDetail;
    private MaterialButton btnFavorite, btnTrailer;
    private RecyclerView rvTvShowCast;
    private YouTubePlayerView youTubePlayerView;
    private ScrollView svDetail;
    private ConstraintLayout clDetailBanner, clTvShowDetailVideo, clDetailContainer;
    private TvShowRepository tvShowRepository;
    private TvShow tvShow;
    private List<Video> tvShowVideos;
    private List<Genres> tvShowGenres;
    private List<Cast> tvShowCasts;
    private FavoriteHelper favoriteHelper;
    private ActivityHelper activityHelper;
    private String videoKey;
    private String EXTRAS_ID, EXTRAS_TITLE;
    private String favTitle, favPoster, favVoteAverage, favOverview;
    private boolean isFavorite = false, isVideoOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_detail);

        // Configure custom toast
        Toasty.Config.getInstance().setTextSize(12).apply();

        tbDetail = findViewById(R.id.tbDetail);
        lpiTvShowDetail = findViewById(R.id.lpiTvshowDetail);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnTrailer = findViewById(R.id.btnTrailer);
        rvTvShowCast = findViewById(R.id.rvTvshowCast);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        svDetail = findViewById(R.id.svDetail);
        clDetailBanner = findViewById(R.id.clDetailBanner);
        clTvShowDetailVideo = findViewById(R.id.clDetailVideo);
        clDetailContainer = findViewById(R.id.clDetailContainer);

        tvShowRepository = TvShowRepository.getInstance();
        favoriteHelper = new FavoriteHelper(this);
        activityHelper = new ActivityHelper();

        EXTRAS_ID = getIntent().getStringExtra("ID");
        EXTRAS_TITLE = getIntent().getStringExtra("TITLE");

        btnFavorite.setOnClickListener(this);
        btnTrailer.setOnClickListener(this);

        getLifecycle().addObserver(youTubePlayerView);
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

    private void btnTrailerHandler() {
        if (videoKey != null) {
            Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
            Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);

            if (!isVideoOpen) {
                clTvShowDetailVideo.setVisibility(View.VISIBLE);
                clTvShowDetailVideo.startAnimation(animFadeIn);

                clDetailBanner.setVisibility(View.INVISIBLE);
                clDetailBanner.startAnimation(animFadeOut);

                clDetailContainer.setVisibility(View.INVISIBLE);
                clDetailContainer.startAnimation(animFadeOut);

                btnTrailer.setIconResource(R.drawable.ic_baseline_info_24);
                btnTrailer.setText(getString(R.string.detail));

                activityHelper.scrollToView(svDetail, clTvShowDetailVideo);
            } else {
                clTvShowDetailVideo.setVisibility(View.INVISIBLE);
                clTvShowDetailVideo.startAnimation(animFadeOut);

                clDetailBanner.setVisibility(View.VISIBLE);
                clDetailBanner.startAnimation(animFadeIn);

                clDetailContainer.setVisibility(View.VISIBLE);
                clDetailContainer.startAnimation(animFadeIn);

                btnTrailer.setIconResource(R.drawable.ic_baseline_play_arrow_24);
                btnTrailer.setText(getString(R.string.trailer));

                activityHelper.scrollToView(svDetail, clDetailBanner);
            }

            isVideoOpen = !isVideoOpen;
        } else {
            Toasty.error(this, "This Tv Show Has No Trailer.", Toast.LENGTH_SHORT, false)
                    .show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnFavorite) {
            btnFavoriteHandler();
        } else if (view.getId() == R.id.btnTrailer) {
            btnTrailerHandler();
        }
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
                tvShowGenres = tvShowDetail.getGenres();

                loadTvShowCast(tvId);
                loadTvShowVideo(tvId);
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
            }
        });
    }

    private void loadTvShowVideo(String tvId) {
        tvShowRepository.getTvShowVideo(tvId, new OnVideoCallback() {
            @Override
            public void onSuccess(VideoResponse videoResponse, String message) {
                tvShowVideos = videoResponse.getVideos();

                for (Video video : tvShowVideos) {
                    if (video.getSite().equalsIgnoreCase("YOUTUBE")) {
                        videoKey = video.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(TvShowDetailActivity.this, message, Toast.LENGTH_SHORT).show();
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

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoKey != null ? videoKey : "", 0);
            }
        });
    }
}