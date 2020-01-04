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
import com.oong.mymoviecataloguelocalstorage.entity.TvShowItems;
import com.oong.mymoviecataloguelocalstorage.model.TvShowViewModel;

import java.util.ArrayList;

public class DetailTVShowActivity extends AppCompatActivity {

    public static final String TV_EXTRA = "tvshow_extra";
    private ProgressBar progressBar;
    private ImageView imageTV;
    private TextView tvTitle, tvPopularity, tvDesc;
    private TvShowViewModel viewModel;
    private String tvShowId;
    private TvShowItems items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);

        setTitle("Detail TV Show");
        init();

        viewModel = new ViewModelProvider(this).get(TvShowViewModel.class);
        viewModel.setDetailTV(this, tvShowId);
        viewModel.getTVShow().observe(this, new Observer<ArrayList<TvShowItems>>() {
            @Override
            public void onChanged(ArrayList<TvShowItems> tvShowItems) {
                if(tvShowItems != null){
                    ArrayList<TvShowItems> items = new ArrayList<>();
                    items.addAll(tvShowItems);
                    Glide.with(getApplicationContext())
                            .load(items.get(0).getPhoto())
                            .apply(new RequestOptions().override(350,550))
                            .into(imageTV);
                    tvTitle.setText(items.get(0).getTitle());
                    tvPopularity.setText(items.get(0).getPopularity());
                    tvDesc.setText(items.get(0).getDescription());
                    showLoading(false);
                }
            }
        });
    }

    private void init(){
        items = getIntent().getParcelableExtra(TV_EXTRA);
        tvShowId = items.getTvShow_id();
        progressBar = findViewById(R.id.progressBar);
        showLoading(true);
        imageTV = findViewById(R.id.imageView);
        tvTitle = findViewById(R.id.tv_tvShow_title);
        tvPopularity = findViewById(R.id.tv_tvShow_popularity);
        tvDesc = findViewById(R.id.tv_tvShow_desc);
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
