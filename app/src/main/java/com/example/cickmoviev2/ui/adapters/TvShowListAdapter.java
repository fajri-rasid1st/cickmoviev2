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
import com.example.cickmoviev2.data.local.database.table.FavoriteTvShow;
import com.example.cickmoviev2.ui.adapters.clicklisteners.OnFavoriteTvShowItemClickListener;

import java.util.List;

public class TvShowListAdapter extends RecyclerView.Adapter<TvShowListAdapter.ViewHolder> {
    private final List<FavoriteTvShow> favoriteTvShowList;
    public OnFavoriteTvShowItemClickListener onFavoriteTvShowItemClickListener;

    public TvShowListAdapter(List<FavoriteTvShow> favoriteTvShowList) {
        this.favoriteTvShowList = favoriteTvShowList;
    }

    public void setClickListener(OnFavoriteTvShowItemClickListener onFavoriteTvShowItemClickListener) {
        this.onFavoriteTvShowItemClickListener = onFavoriteTvShowItemClickListener;
    }

    @NonNull
    @Override
    public TvShowListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_layout, parent, false);

        return new TvShowListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowListAdapter.ViewHolder holder, int position) {
        holder.onBindItemView(favoriteTvShowList.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteTvShowList.size();
    }

    public FavoriteTvShow getTvShowAt(int position) {
        return favoriteTvShowList.get(position);
    }

    public void remove(int position) {
        favoriteTvShowList.remove(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FavoriteTvShow favoriteTvShow;
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

        public void onBindItemView(FavoriteTvShow favoriteTvShow) {
            this.favoriteTvShow = favoriteTvShow;

            Glide.with(itemView.getContext())
                    .load(Const.IMG_URL_300 + favoriteTvShow.getPosterPath())
                    .into(ivPoster);

            tvTitle.setText(favoriteTvShow.getTitle());
            tvOverview.setText(favoriteTvShow.getOverview());
            rbVoteAverage.setRating(Float.parseFloat(favoriteTvShow.getVoteAverage()) / 2);
        }

        @Override
        public void onClick(View view) {
            onFavoriteTvShowItemClickListener.onClick(favoriteTvShow);
        }
    }
}
