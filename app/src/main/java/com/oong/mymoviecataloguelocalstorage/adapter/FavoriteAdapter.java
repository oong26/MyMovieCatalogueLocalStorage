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
import com.oong.mymoviecataloguelocalstorage.entity.FavoriteItems;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.moviesFavoriteViewHolder> {

    private String jenis;
    private ArrayList<FavoriteItems> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public FavoriteAdapter(String jenis){
        this.jenis = jenis;
    }

    public interface OnItemClickCallback {
        void onItemClicked(FavoriteItems data);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData(ArrayList<FavoriteItems> items){
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public moviesFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView;
        if(jenis.equals("1")){
            //Movies Favorite
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies, parent, false);
        }
        else{
            //TV Show Favorite
            mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_show, parent, false);
        }
        return new moviesFavoriteViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final moviesFavoriteViewHolder holder, int position) {
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

    class moviesFavoriteViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPoster;
        TextView tvTitle;
        TextView tvRelease_date;
        TextView tvPopularity;

        moviesFavoriteViewHolder(@NonNull View itemView){
            super(itemView);
            if(jenis.equals("1")){
                //Movies Favorite
                imgPoster = itemView.findViewById(R.id.img_movie_photo);
                tvTitle = itemView.findViewById(R.id.tv_movie_name);
                tvRelease_date = itemView.findViewById(R.id.tv_movie_release_date);
            }
            else{
                //TV Show Favorite
                imgPoster = itemView.findViewById(R.id.img_tvShow_photo);
                tvTitle = itemView.findViewById(R.id.tv_tvShow_name);
                tvPopularity = itemView.findViewById(R.id.tv_tvShow_popularity);
            }
        }

        void bind(FavoriteItems items){
            String poster_path = "https://image.tmdb.org/t/p/w185" + items.getPoster();
            Glide.with(itemView.getContext())
                    .load(poster_path)
                    .apply(new RequestOptions().override(350,550))
                    .into(imgPoster);
            tvTitle.setText(items.getTitle());
            if(jenis.equals("1")){
                //Movies Favorite
                tvRelease_date.setText(items.getRelease_date());
            }
            else{
                //TV Show Favorite
                tvPopularity.setText(items.getPopularity());
            }
        }
    }
}
