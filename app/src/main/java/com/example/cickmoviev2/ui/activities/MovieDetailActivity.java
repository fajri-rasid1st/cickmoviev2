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
import com.example.cickmoviev2.data.api.repository.MovieRepository;
import com.example.cickmoviev2.data.api.repository.callback.OnCastCallback;
import com.example.cickmoviev2.data.api.repository.callback.OnMovieCallback;
import com.example.cickmoviev2.data.local.database.FavoriteDatabase;
import com.example.cickmoviev2.data.models.Cast;
import com.example.cickmoviev2.data.models.Credit;
import com.example.cickmoviev2.data.models.Genres;
import com.example.cickmoviev2.data.models.Movie;
import com.example.cickmoviev2.ui.adapters.CastAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private MovieRepository movieRepository;
    private LinearProgressIndicator lpiMovieDetail;
    private RecyclerView rvMovieCast;
    private MaterialButton btnFavorite;
    private Movie movie;
    private FavoriteDatabase roomDatabase;
    private List<Genres> movieGenres;
    private List<Cast> movieCasts;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieRepository = MovieRepository.getInstance();
        roomDatabase = FavoriteDatabase.getInstance(getApplicationContext());

        lpiMovieDetail = findViewById(R.id.lpiMovieDetail);
        rvMovieCast = findViewById(R.id.rvMovieCast);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(this);

        Toolbar tbDetail = findViewById(R.id.tbDetail);
        setSupportActionBar(tbDetail);
        tbDetail.setTitleTextAppearance(this, R.style.WhiteTextAppearance);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));

        checkIsFavorite(getIntent().getStringExtra("ID"));
        loadMovie(getIntent().getStringExtra("ID"));
    }

    private void checkIsFavorite(String movieId) {
        isFavorite = roomDatabase.favoriteDao().isMovieExist(Integer.parseInt(movieId));

        if (!isFavorite) {
            btnFavorite.setIconResource(R.drawable.ic_baseline_favorite_24);
        } else {
            btnFavorite.setIconResource(R.drawable.ic_outline_favorite_24);
        }
    }

    @Override
    public void onClick(View view) {
        // if (view.getId() == R.id.btnFavorite) btnFavoriteHandler();
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
                movieGenres = movie.getGenres();

                loadMovieCast(movieId);
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
                lpiMovieDetail.hide();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(MovieDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> lpiMovieDetail.hide(), 3000);
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
    }
}