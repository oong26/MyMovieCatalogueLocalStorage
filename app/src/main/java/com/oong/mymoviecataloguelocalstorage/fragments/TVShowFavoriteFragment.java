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
import com.oong.mymoviecataloguelocalstorage.DetailTVShowActivity;
import com.oong.mymoviecataloguelocalstorage.MainActivity;
import com.oong.mymoviecataloguelocalstorage.R;
import com.oong.mymoviecataloguelocalstorage.adapter.FavoriteAdapter;
import com.oong.mymoviecataloguelocalstorage.database.DatabaseHelper;
import com.oong.mymoviecataloguelocalstorage.database.FavoriteHelper;
import com.oong.mymoviecataloguelocalstorage.entity.FavoriteItems;
import com.oong.mymoviecataloguelocalstorage.entity.MovieItems;
import com.oong.mymoviecataloguelocalstorage.entity.TvShowItems;
import com.oong.mymoviecataloguelocalstorage.model.MoviesViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static android.view.View.*;

public class TVShowFavoriteFragment extends Fragment implements LoadTVFavoriteCallback {

    private RecyclerView rvTVShow;
    private ProgressBar progressBar;
    private FavoriteAdapter adapter;
    private MoviesViewModel viewModel;
    private FavoriteHelper favoriteHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static ArrayList<FavoriteItems> favoriteList;
    private Bundle bundle;
    private View view;

    public TVShowFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity.tabLayout.setVisibility(VISIBLE);
        MainActivity.viewPager.setVisibility(VISIBLE);
        MainActivity.hostFragment.setVisibility(GONE);

        this.view = view;
        this.bundle = savedInstanceState;
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

    @Override
    public void onResume() {
        new LoadNotesAsync(favoriteHelper, this).execute();
        if (bundle == null) {
            // proses ambil data
            new LoadNotesAsync(favoriteHelper, this).execute();
        } else {
            ArrayList<FavoriteItems> list = bundle.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setData(list);
            }
        }
        super.onResume();
    }

    public static ArrayList<FavoriteItems> mapCursorToArrayList(Cursor cursor) {
        favoriteList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TITLE));
            String popularity = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POPULARITY));
            String poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POSTER));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DESC));
            favoriteList.add(new FavoriteItems(id, title, null, popularity, poster, description, 2));
        }
        return favoriteList;
    }

    private void setRecyclerView(View view){
        rvTVShow = view.findViewById(R.id.listFavTVShow);
        rvTVShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FavoriteAdapter("2");
        adapter.notifyDataSetChanged();
        rvTVShow.setAdapter(adapter);

        adapter.setOnItemClickCallback(new FavoriteAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(FavoriteItems data) {
                Intent intent = new Intent(getActivity(), DetailTVShowActivity.class);
                TvShowItems tvShowItems = new TvShowItems();
                tvShowItems.setTvShow_id(String.valueOf(data.getId()));
                tvShowItems.setTitle(data.getTitle());
                tvShowItems.setPopularity(data.getPopularity());
                tvShowItems.setDescription(data.getDesc());
                tvShowItems.setPoster_path(data.getPoster());
                intent.putExtra(DetailTVShowActivity.TV_EXTRA, tvShowItems);
                startActivity(intent);
            }
        });
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
        }
        showLoading(false);
    }

    private static class LoadNotesAsync extends AsyncTask<Void, Void, ArrayList<FavoriteItems>> {
        private final WeakReference<FavoriteHelper> weakFavoriteHelper;
        private final WeakReference<LoadTVFavoriteCallback> weakCallback;
        private LoadNotesAsync(FavoriteHelper favoriteHelper, LoadTVFavoriteCallback callback) {
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
            Cursor dataCursor = weakFavoriteHelper.get().queryByJenis("2");
            return mapCursorToArrayList(dataCursor);
        }
        @Override
        protected void onPostExecute(ArrayList<FavoriteItems> items) {
            super.onPostExecute(items);
            weakCallback.get().postExecute(items);
        }
    }
}

interface LoadTVFavoriteCallback{
    void preExecute();
    void postExecute(ArrayList<FavoriteItems> notes);
}