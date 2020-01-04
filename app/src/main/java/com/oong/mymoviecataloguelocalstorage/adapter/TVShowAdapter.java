package com.oong.mymoviecataloguelocalstorage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oong.mymoviecataloguelocalstorage.R;
import com.oong.mymoviecataloguelocalstorage.entity.TvShowItems;

import java.util.ArrayList;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.tvShowViewHolder> {

    private ArrayList<TvShowItems> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    @NonNull
    @Override
    public TVShowAdapter.tvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_show, parent, false);
        return new tvShowViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TVShowAdapter.tvShowViewHolder holder, int position) {
        holder.bind(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mData.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShowItems data);
    }

    public void setOnItemClickCallback(TVShowAdapter.OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<TvShowItems> items){
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    class tvShowViewHolder extends RecyclerView.ViewHolder{

        ImageView imgTVShow;
        TextView tvTitle,tvPopularity;

        tvShowViewHolder(@NonNull View itemView){
            super(itemView);
            imgTVShow = itemView.findViewById(R.id.img_tvShow_photo);
            tvTitle = itemView.findViewById(R.id.tv_tvShow_name);
            tvPopularity = itemView.findViewById(R.id.tv_tvShow_popularity);
        }

        void bind(TvShowItems items){
            Glide.with(itemView.getContext())
                    .load(items.getPhoto())
                    .apply(new RequestOptions().override(350,550))
                    .into(imgTVShow);
            tvTitle.setText(items.getTitle());
            tvPopularity.setText(items.getPopularity());
        }
    }
}
