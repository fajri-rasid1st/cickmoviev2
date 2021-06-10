package com.example.cickmoviev2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cickmoviev2.Const;
import com.example.cickmoviev2.R;
import com.example.cickmoviev2.data.local.database.table.FavoriteMovie;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnFavoriteMovieItemClickListener;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private final List<FavoriteMovie> favoriteMovieList;
    public OnFavoriteMovieItemClickListener onFavoriteMovieItemClickListener;

    public MovieListAdapter(List<FavoriteMovie> favoriteMovieList) {
        this.favoriteMovieList = favoriteMovieList;
    }

    public void setClickListener(OnFavoriteMovieItemClickListener onFavoriteMovieItemClickListener) {
        this.onFavoriteMovieItemClickListener = onFavoriteMovieItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindItemView(favoriteMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteMovieList.size();
    }

    public FavoriteMovie getMovieAt(int position) {
        return favoriteMovieList.get(position);
    }

    public void remove(int position) {
        favoriteMovieList.remove(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FavoriteMovie favoriteMovie;
        ImageView ivPoster;
        TextView tvTitle;
        TextView tvOverview;
        RatingBar rbVoteAverage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPosterList);
            tvTitle = itemView.findViewById(R.id.tvTitleList);
            tvOverview = itemView.findViewById(R.id.tvOverviewList);
            rbVoteAverage = itemView.findViewById(R.id.rbVoteAverageList);

            itemView.setOnClickListener(this);
        }

        public void onBindItemView(FavoriteMovie favoriteMovie) {
            this.favoriteMovie = favoriteMovie;

            Glide.with(itemView.getContext())
                    .load(Const.IMG_URL_300 + favoriteMovie.getPosterPath())
                    .into(ivPoster);

            tvTitle.setText(favoriteMovie.getTitle());
            tvOverview.setText(favoriteMovie.getOverview());
            rbVoteAverage.setRating(Float.parseFloat(favoriteMovie.getVoteAverage()) / 2);
        }

        @Override
        public void onClick(View view) {
            onFavoriteMovieItemClickListener.onClick(favoriteMovie);
        }
    }
}
