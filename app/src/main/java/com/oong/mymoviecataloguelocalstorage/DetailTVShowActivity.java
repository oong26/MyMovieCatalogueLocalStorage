package com.oong.mymoviecataloguelocalstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.oong.mymoviecataloguelocalstorage.database.DatabaseHelper;
import com.oong.mymoviecataloguelocalstorage.database.FavoriteHelper;
import com.oong.mymoviecataloguelocalstorage.entity.TvShowItems;
import com.oong.mymoviecataloguelocalstorage.model.TvShowViewModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class DetailTVShowActivity extends AppCompatActivity {

    public static final String TV_EXTRA = "tvshow_extra";
    private ProgressBar progressBar;
    private ImageView imageTV;
    private TextView tvTitle, tvPopularity, tvDesc;
    private TvShowViewModel viewModel;
    private String tvShowId;
    private TvShowItems items;
    private FavoriteHelper favoriteHelper;


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
        favoriteHelper = FavoriteHelper.getInstance(this);
        favoriteHelper.open();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem menuItem = menu.getItem(0);
        if(isFavorited()){
            menuItem.setIcon(R.drawable.ic_favorite_white_24dp);
        }
        else{
            menuItem.setIcon(R.drawable.ic_favorite_border_white_24dp);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_favorite){
            if(isFavorited()){
                //Menghapus item dari favorite
                removeFavorite();
                item.setIcon(R.drawable.ic_favorite_border_white_24dp);
            }
            else{
                //Menambah item ke favorite
                addFavorite();
                item.setIcon(R.drawable.ic_favorite_white_24dp);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFavorited(){
        boolean isFavorite;
        Cursor cursor = favoriteHelper.queryById(items.getTvShow_id());

        if(cursor.getCount() > 0){
            //Favorit
            isFavorite = true;
        }
        else{
            //bukan favorite
            isFavorite = false;
        }
        return isFavorite;
    }

    private void addFavorite(){
        try{
            ContentValues values = new ContentValues();
            values.put(_ID, items.getTvShow_id());
            values.put(DatabaseHelper.TITLE, items.getTitle());
            values.put(DatabaseHelper.POPULARITY, items.getPopularity());
            values.put(DatabaseHelper.POSTER, items.getPoster_path());
            values.put(DatabaseHelper.DESC, items.getDescription());
            values.put(DatabaseHelper.JENIS, 2);
            favoriteHelper.insert(values);
            Toast.makeText(this, MainActivity.TV_SUCCESS_MSG, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, MainActivity.ERROR_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFavorite(){
        try {
            favoriteHelper.deleteById(items.getTvShow_id());
            Toast.makeText(this, MainActivity.REMOVED_FAV_MSG, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, MainActivity.ERROR_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
