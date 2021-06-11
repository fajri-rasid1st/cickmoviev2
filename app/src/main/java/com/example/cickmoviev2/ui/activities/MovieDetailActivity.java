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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cickmoviev2.Const;
import com.example.cickmoviev2.R;
import com.example.cickmoviev2.data.api.repository.MovieRepository;
import com.example.cickmoviev2.data.api.repository.callback.OnCastCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnMovieCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnVideoCallback;
import com.example.cickmoviev2.data.local.database.FavoriteHelper;
import com.example.cickmoviev2.data.models.Cast;
import com.example.cickmoviev2.data.models.Credit;
import com.example.cickmoviev2.data.models.Genres;
import com.example.cickmoviev2.data.models.Movie;
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

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar tbDetail;
    private LinearProgressIndicator lpiMovieDetail;
    private MaterialButton btnFavorite;
    private MaterialButton btnTrailer;
    private RecyclerView rvMovieCast;
    private YouTubePlayerView youTubePlayerView;
    private ConstraintLayout clDetailBanner;
    private ConstraintLayout clMovieDetailVideo;
    private ConstraintLayout clDetailContainer;
    private MovieRepository movieRepository;
    private Movie movie;
    private List<Video> movieVideos;
    private List<Genres> movieGenres;
    private List<Cast> movieCasts;
    private FavoriteHelper favoriteHelper;
    private String videoKey;
    private String EXTRAS_ID, EXTRAS_TITLE;
    private String favTitle, favPoster, favVoteAverage, favOverview;
    private boolean isFavorite = false;
    private boolean isVideoOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Configure custom toast
        Toasty.Config.getInstance().setTextSize(12).apply();

        tbDetail = findViewById(R.id.tbDetail);
        lpiMovieDetail = findViewById(R.id.lpiMovieDetail);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnTrailer = findViewById(R.id.btnTrailer);
        rvMovieCast = findViewById(R.id.rvMovieCast);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        clDetailBanner = findViewById(R.id.clDetailBanner);
        clMovieDetailVideo = findViewById(R.id.clDetailVideo);
        clDetailContainer = findViewById(R.id.clDetailContainer);

        movieRepository = MovieRepository.getInstance();
        favoriteHelper = new FavoriteHelper(this);

        EXTRAS_ID = getIntent().getStringExtra("ID");
        EXTRAS_TITLE = getIntent().getStringExtra("TITLE");

        btnFavorite.setOnClickListener(this);
        btnTrailer.setOnClickListener(this);

        getLifecycle().addObserver(youTubePlayerView);
        setActionBar(EXTRAS_TITLE);
        updateFavoriteButton(EXTRAS_ID);
        loadMovie(EXTRAS_ID);
    }

    private void setActionBar(String title) {
        setSupportActionBar(tbDetail);
        tbDetail.setTitleTextAppearance(this, R.style.WhiteTextAppearance);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateFavoriteButton(String movieId) {
        isFavorite = favoriteHelper.checkFavoriteMovies(Integer.parseInt(movieId));

        if (!isFavorite) {
            btnFavorite.setIconResource(R.drawable.ic_outline_favorite_24);
        } else {
            btnFavorite.setIconResource(R.drawable.ic_baseline_favorite_24);
        }
    }

    private void btnFavoriteHandler() {
        String textStatus;

        if (!isFavorite) {
            if (favoriteHelper.insertFavoriteMovie(Integer.parseInt(EXTRAS_ID), favTitle, favPoster, favVoteAverage, favOverview)) {
                textStatus = "Movie Has Been Added to Favorite.";
            } else {
                textStatus = "Unable to Add Movie. Try Again.";
            }
        } else {
            if (favoriteHelper.deleteFavoriteMovie(Integer.parseInt(EXTRAS_ID))) {
                textStatus = "Movie Has Been Removed from Favorite.";
            } else {
                textStatus = "Unable to Remove Movie. Try Again.";
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
                clMovieDetailVideo.setVisibility(View.VISIBLE);
                clMovieDetailVideo.startAnimation(animFadeIn);

                clDetailBanner.setVisibility(View.INVISIBLE);
                clDetailBanner.startAnimation(animFadeOut);

                clDetailContainer.setVisibility(View.INVISIBLE);
                clDetailContainer.startAnimation(animFadeOut);

                btnTrailer.setIconResource(R.drawable.ic_baseline_info_24);
                btnTrailer.setText(getString(R.string.detail));
            } else {
                clMovieDetailVideo.setVisibility(View.INVISIBLE);
                clMovieDetailVideo.startAnimation(animFadeOut);

                clDetailBanner.setVisibility(View.VISIBLE);
                clDetailBanner.startAnimation(animFadeIn);

                clDetailContainer.setVisibility(View.VISIBLE);
                clDetailContainer.startAnimation(animFadeIn);

                btnTrailer.setIconResource(R.drawable.ic_baseline_play_arrow_24);
                btnTrailer.setText(getString(R.string.trailer));
            }

            isVideoOpen = !isVideoOpen;
        } else {
            Toasty.error(this, "This Movie Has No Trailer.", Toast.LENGTH_SHORT, false)
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

    private void loadMovie(String movieId) {
        movieRepository.getMovie(movieId, new OnMovieCallback() {
            @Override
            public void onSuccess(Movie movieDetail, String message) {
                movie = movieDetail;
                movieGenres = movieDetail.getGenres();

                loadMovieCast(movieId);
                loadMovieVideo(movieId);
                setDetailActivityContent();

                lpiMovieDetail.hide();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> lpiMovieDetail.hide(), 3000);
            }
        });
    }

    private void loadMovieCast(String movieId) {
        movieRepository.getMovieCast(movieId, new OnCastCallback() {
            @Override
            public void onSuccess(Credit credit, String message) {
                movieCasts = credit.getCast();

                rvMovieCast.setAdapter(new CastAdapter(movieCasts));
                rvMovieCast.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, RecyclerView.HORIZONTAL, false));
                rvMovieCast.setHasFixedSize(true);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMovieVideo(String movieId) {
        movieRepository.getMovieVideo(movieId, new OnVideoCallback() {
            @Override
            public void onSuccess(VideoResponse videoResponse, String message) {
                movieVideos = videoResponse.getVideos();

                for (Video video : movieVideos) {
                    if (video.getSite().equalsIgnoreCase("YOUTUBE")) {
                        videoKey = video.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDetailActivityContent() {
        TextView tvTitle = findViewById(R.id.tvMovieTitleDetail);
        TextView tvRuntimeDetail = findViewById(R.id.tvMovieRuntimeDetail);
        TextView tvVoteCount = findViewById(R.id.tvMovieVoteCountDetail);
        TextView tvFullTitle = findViewById(R.id.tvMovieFullTitleDetail);
        TextView tvReleased = findViewById(R.id.tvMovieReleaseDetail);
        TextView tvVoteAverage = findViewById(R.id.tvMovieVoteAverageDetail);
        TextView tvGenres = findViewById(R.id.tvMovieGenresDetail);
        ExpandableTextView etvOverview = findViewById(R.id.etvMovieOverviewTitle);
        RatingBar rbVoteAverage = findViewById(R.id.rbMovieVoteAverageDetail);
        ImageView ivPoster = findViewById(R.id.ivMoviePosterDetail);
        ImageView ivBanner = findViewById(R.id.ivMovieBannerDetail);

        favTitle = movie.getTitle();
        favPoster = movie.getPosterUrl();
        favVoteAverage = movie.getVoteAverage();
        favOverview = movie.getOverview();

        tvTitle.setText(movie.getTitle());
        tvRuntimeDetail.setText(movie.getRuntime());
        tvVoteCount.setText(movie.getVoteCount());
        tvFullTitle.setText(movie.getTitle());
        tvReleased.setText(movie.getReleaseDate());
        tvVoteAverage.setText(movie.getVoteAverage());
        etvOverview.setText(movie.getOverview());
        rbVoteAverage.setRating(Float.parseFloat(movie.getVoteAverage()) / 2);

        StringBuilder genresText = new StringBuilder();

        for (Genres genres : movieGenres) {
            genresText.append(genres.getGenre()).append(", ");
        }

        tvGenres.setText(genresText.substring(0, genresText.length() - 2));

        Glide.with(MovieDetailActivity.this)
                .load(Const.IMG_URL_300 + movie.getPosterUrl())
                .into(ivPoster);

        Glide.with(MovieDetailActivity.this)
                .load(Const.IMG_URL_500 + movie.getBackdropUrl())
                .into(ivBanner);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(videoKey != null ? videoKey : "", 0);
            }
        });
    }
}