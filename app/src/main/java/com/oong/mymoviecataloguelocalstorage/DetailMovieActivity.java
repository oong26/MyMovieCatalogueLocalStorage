package com.oong.mymoviecataloguelocalstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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
import com.oong.mymoviecataloguelocalstorage.entity.MovieItems;
import com.oong.mymoviecataloguelocalstorage.model.MoviesViewModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String MOVIE_EXTRA = "movie_extra";
    private ProgressBar progressBar;
    private ImageView imageMovie;
    private TextView tvTitle, tvReleaseDate, tvDesc;
    private MoviesViewModel viewModel;
    private String movieId;
    private MovieItems items;
    private FavoriteHelper favoriteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        setTitle("Detail Movie");
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
        favoriteHelper = FavoriteHelper.getInstance(this);
        favoriteHelper.open();
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
        Cursor cursor = favoriteHelper.queryById(items.getMovie_id());

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
        ContentValues values = new ContentValues();
        values.put(_ID, items.getMovie_id());
        values.put(DatabaseHelper.TITLE, items.getTitle());
        values.put(DatabaseHelper.RELEASE_DATE, items.getRelease_date());
        values.put(DatabaseHelper.POSTER, items.getPoster_path());
        values.put(DatabaseHelper.DESC, items.getDescription());
        values.put(DatabaseHelper.JENIS, 1);
        try{
            favoriteHelper.insert(values);
            Toast.makeText(this, MainActivity.MOVIE_SUCCESS_MSG, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, MainActivity.ERROR_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFavorite(){
        try{
            favoriteHelper.deleteById(items.getMovie_id());
            Toast.makeText(this, MainActivity.REMOVED_FAV_MSG, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex){
            Toast.makeText(this, MainActivity.ERROR_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DETAIL", "Detail : onDestroy");
        favoriteHelper.close();
    }
}
