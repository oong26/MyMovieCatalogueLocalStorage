package com.oong.mymoviecataloguelocalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oong.mymoviecataloguelocalstorage.entity.MovieItems;
import com.oong.mymoviecataloguelocalstorage.model.MoviesViewModel;

import java.util.ArrayList;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA = "movie_extra";
    private ProgressBar progressBar;
    private ImageView imageMovie;
    private TextView tvTitle, tvReleaseDate, tvDesc;
    private MoviesViewModel viewModel;
    private String movieId;
    private MovieItems items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        init();

        viewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        viewModel.setDetailMovie(this, movieId);
        viewModel.getMovie().observe(this, new Observer<ArrayList<MovieItems>>() {
            @Override
            public void onChanged(ArrayList<MovieItems> movieItems) {
                if(movieItems != null){
                    ArrayList<MovieItems> items = new ArrayList<>();
                    items.addAll(movieItems);
                    Glide.with(getApplicationContext())
                            .load(items.get(0).getPhoto())
                            .apply(new RequestOptions().override(350, 550))
                            .into(imageMovie);
                    tvTitle.setText(items.get(0).getTitle());
                    tvReleaseDate.setText(items.get(0).getRelease_date());
                    tvDesc.setText(items.get(0).getDescription());
                    showLoading(false);
                }
            }
        });
    }

    private void init(){
        items = getIntent().getParcelableExtra(MOVIE_EXTRA);
        movieId = items.getMovie_id();
        progressBar = findViewById(R.id.progressBar);
        showLoading(true);
        imageMovie = findViewById(R.id.imageView);
        tvTitle = findViewById(R.id.tv_movie_title);
        tvReleaseDate = findViewById(R.id.tv_movie_release_date);
        tvDesc = findViewById(R.id.tv_movie_desc);
    }

    void showLoading(boolean state){
        if(state){
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
