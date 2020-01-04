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
import com.oong.mymoviecataloguelocalstorage.entity.MovieItems;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.moviesViewHolder> {

    private ArrayList<MovieItems> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public interface OnItemClickCallback {
        void onItemClicked(MovieItems data);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<MovieItems> items){
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public moviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies, parent, false);
        return new moviesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapter.moviesViewHolder holder, int position) {
        holder.bind(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked((mData.get(holder.getAdapterPosition())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class moviesViewHolder extends RecyclerView.ViewHolder{

        ImageView moviePoster;
        TextView tvTitle;
        TextView tvRelease_date;

        moviesViewHolder(@NonNull View itemView){
            super(itemView);
            moviePoster = itemView.findViewById(R.id.img_movie_photo);
            tvTitle = itemView.findViewById(R.id.tv_movie_name);
            tvRelease_date = itemView.findViewById(R.id.tv_movie_release_date);
        }

        void bind(MovieItems items){
            Glide.with(itemView.getContext())
                    .load(items.getPhoto())
                    .apply(new RequestOptions().override(350,550))
                    .into(moviePoster);
            tvTitle.setText(items.getTitle());
            tvRelease_date.setText(items.getRelease_date());
        }
    }
}
