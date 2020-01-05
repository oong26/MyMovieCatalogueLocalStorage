package com.oong.mymoviecataloguelocalstorage.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.oong.mymoviecataloguelocalstorage.DetailMovieActivity;
import com.oong.mymoviecataloguelocalstorage.MainActivity;
import com.oong.mymoviecataloguelocalstorage.R;
import com.oong.mymoviecataloguelocalstorage.adapter.FavoriteAdapter;
import com.oong.mymoviecataloguelocalstorage.database.DatabaseHelper;
import com.oong.mymoviecataloguelocalstorage.database.FavoriteHelper;
import com.oong.mymoviecataloguelocalstorage.entity.FavoriteItems;
import com.oong.mymoviecataloguelocalstorage.entity.MovieItems;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static android.view.View.*;

public class MoviesFavoriteFragment extends Fragment implements LoadFavoriteCallback {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    private FavoriteAdapter adapter;
    private FavoriteHelper favoriteHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static ArrayList<FavoriteItems> favoriteList;

    public MoviesFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity.tabLayout.setVisibility(VISIBLE);
        MainActivity.viewPager.setVisibility(VISIBLE);
        MainActivity.hostFragment.setVisibility(GONE);

        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
        favoriteHelper = FavoriteHelper.getInstance(getActivity());
        favoriteHelper.open();
        setRecyclerView(view);
        new LoadNotesAsync(favoriteHelper, this).execute();
        if (savedInstanceState == null) {
            // proses ambil data
            new LoadNotesAsync(favoriteHelper, this).execute();
        } else {
            ArrayList<FavoriteItems> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setData(list);
            }
        }
    }

    public static ArrayList<FavoriteItems> mapCursorToArrayList(Cursor cursor) {
        favoriteList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TITLE));
            String release_date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RELEASE_DATE));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POSTER));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DESC));
            favoriteList.add(new FavoriteItems(id, title, release_date, null, poster, description, 1));
        }
        return favoriteList;
    }

    private void setRecyclerView(View view){
        rvMovies = view.findViewById(R.id.listFavMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavoriteAdapter("1");
        adapter.notifyDataSetChanged();
        rvMovies.setAdapter(adapter);

        adapter.setOnItemClickCallback(new FavoriteAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(FavoriteItems data) {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                MovieItems movieItems = new MovieItems();
                movieItems.setMovie_id(String.valueOf(data.getId()));
                movieItems.setTitle(data.getTitle());
                movieItems.setPoster_path(data.getPoster());
                movieItems.setDescription(data.getDesc());
                movieItems.setRelease_date(data.getRelease_date());
                intent.putExtra(DetailMovieActivity.MOVIE_EXTRA, movieItems);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(true);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<FavoriteItems> favoriteItems) {
        if(favoriteItems.size() > 0){
            adapter.setData(favoriteItems);
        }
        else{
            adapter.setData(new ArrayList<FavoriteItems>());
            Toast.makeText(getActivity(), MainActivity.DATA_NOT_FOUND, Toast.LENGTH_SHORT).show();
        }
        showLoading(false);
    }

    private static class LoadNotesAsync extends AsyncTask<Void, Void, ArrayList<FavoriteItems>> {
        private final WeakReference<FavoriteHelper> weakFavoriteHelper;
        private final WeakReference<LoadFavoriteCallback> weakCallback;
        private LoadNotesAsync(FavoriteHelper favoriteHelper, LoadFavoriteCallback callback) {
            weakFavoriteHelper = new WeakReference<>(favoriteHelper);
            weakCallback = new WeakReference<>(callback);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }
        @Override
        protected ArrayList<FavoriteItems> doInBackground(Void... voids) {
            Cursor dataCursor = weakFavoriteHelper.get().queryByJenis("1");
            return mapCursorToArrayList(dataCursor);
        }
        @Override
        protected void onPostExecute(ArrayList<FavoriteItems> items) {
            super.onPostExecute(items);
            weakCallback.get().postExecute(items);
        }
    }

}

interface LoadFavoriteCallback{
    void preExecute();
    void postExecute(ArrayList<FavoriteItems> notes);
}
