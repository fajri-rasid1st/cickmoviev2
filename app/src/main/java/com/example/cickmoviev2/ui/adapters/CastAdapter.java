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
import com.example.cickmoviev2.data.models.Cast;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {
    private final List<Cast> casts;

    public CastAdapter(List<Cast> casts) {
        this.casts = casts;
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cast_layout, parent, false);

        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(Const.IMG_URL_200 + casts.get(position).getProfileImgUrl())
                .into(holder.ivCastPoster);

        holder.tvCastName.setText(casts.get(position).getName());
        holder.tvCastCharacter.setText(casts.get(position).getCharacter());
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public static class CastViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCastPoster;
        TextView tvCastName;
        TextView tvCastCharacter;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCastPoster = itemView.findViewById(R.id.ivCastPosterGrid);
            tvCastName = itemView.findViewById(R.id.tvCastNameGrid);
            tvCastCharacter = itemView.findViewById(R.id.tvCastCharacterGrid);
        }
    }
}
