package com.example.cickmoviev2.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cickmoviev2.Const;
import com.example.cickmoviev2.R;
import com.example.cickmoviev2.data.models.MoviePopular;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnMovieItemClickListener;

import java.util.List;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private final List<MoviePopular> moviePopulars;
    private OnMovieItemClickListener onMovieItemClickListener;

    public MovieGridAdapter(List<MoviePopular> moviePopulars) {
        this.moviePopulars = moviePopulars;
    }

    public void setClickListener(OnMovieItemClickListener onMovieItemClickListener) {
        this.onMovieItemClickListener = onMovieItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_grid_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.onBindItemView(moviePopulars.get(i));
    }

    public void appendList(List<MoviePopular> listToAppend) {
        moviePopulars.addAll(listToAppend);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return moviePopulars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MoviePopular moviePopular;
        TextView tvTitle;
        TextView tvRelease;
        TextView tvVoteAverage;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitleGrid);
            tvRelease = itemView.findViewById(R.id.tvReleaseGrid);
            tvVoteAverage = itemView.findViewById(R.id.tvVoteAverageGrid);
            ivPoster = itemView.findViewById(R.id.ivPosterGrid);

            itemView.setOnClickListener(this);
        }

        public void onBindItemView(MoviePopular moviePopular) {
            this.moviePopular = moviePopular;

            Glide.with(itemView.getContext())
                    .load(Const.IMG_URL_300 + moviePopular.getImgUrl())
                    .into(ivPoster);

            tvTitle.setText(moviePopular.getTitle());
            tvRelease.setText(moviePopular.getReleaseDate());
            tvVoteAverage.setText(moviePopular.getVoteAverage());
        }

        @Override
        public void onClick(View view) {
            onMovieItemClickListener.onClick(moviePopular);
        }
    }
}
