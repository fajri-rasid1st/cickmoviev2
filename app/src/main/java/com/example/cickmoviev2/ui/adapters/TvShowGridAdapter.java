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
import com.example.cickmoviev2.data.models.TvShowPopular;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnTvShowItemClickListener;

import java.util.List;

public class TvShowGridAdapter extends RecyclerView.Adapter<TvShowGridAdapter.ViewHolder> {
    private final List<TvShowPopular> tvShowPopulars;
    private OnTvShowItemClickListener onTvShowItemClickListener;

    public TvShowGridAdapter(List<TvShowPopular> tvShowPopulars) {
        this.tvShowPopulars = tvShowPopulars;
    }

    public void setClickListener(OnTvShowItemClickListener onTvShowItemClickListener) {
        this.onTvShowItemClickListener = onTvShowItemClickListener;
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
        viewHolder.onBindItemView(tvShowPopulars.get(i));
    }

    public void appendList(List<TvShowPopular> listToAppend) {
        tvShowPopulars.addAll(listToAppend);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShowPopulars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TvShowPopular tvShowPopular;
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

        public void onBindItemView(TvShowPopular tvShowPopular) {
            this.tvShowPopular = tvShowPopular;

            Glide.with(itemView.getContext())
                    .load(Const.IMG_URL_300 + tvShowPopular.getImgUrl())
                    .into(ivPoster);

            tvTitle.setText(tvShowPopular.getTitle());
            tvRelease.setText(tvShowPopular.getReleaseDate());
            tvVoteAverage.setText(tvShowPopular.getVoteAverage());
        }

        @Override
        public void onClick(View view) {
            onTvShowItemClickListener.onClick(tvShowPopular);
        }
    }
}
