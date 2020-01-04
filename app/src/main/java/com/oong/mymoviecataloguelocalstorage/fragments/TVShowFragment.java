package com.oong.mymoviecataloguelocalstorage.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.oong.mymoviecataloguelocalstorage.MainActivity;
import com.oong.mymoviecataloguelocalstorage.R;
import com.oong.mymoviecataloguelocalstorage.adapter.MoviesAdapter;
import com.oong.mymoviecataloguelocalstorage.adapter.TVShowAdapter;
import com.oong.mymoviecataloguelocalstorage.entity.MovieItems;
import com.oong.mymoviecataloguelocalstorage.entity.TvShowItems;
import com.oong.mymoviecataloguelocalstorage.model.MoviesViewModel;
import com.oong.mymoviecataloguelocalstorage.model.TvShowViewModel;

import java.util.ArrayList;

import static android.view.View.*;

public class TVShowFragment extends Fragment {

    private ProgressBar progressBar;
    private TvShowViewModel viewModel;
    private TVShowAdapter adapter;
    private RecyclerView rvTVShow;

    public TVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity.tabLayout.setVisibility(GONE);
        MainActivity.viewPager.setVisibility(GONE);
        MainActivity.hostFragment.setVisibility(VISIBLE);

        setRecyclerView(view);
        progressBar = view.findViewById(R.id.progressBar);
        showLoading(true);
        viewModel = ViewModelProviders.of(getActivity()).get(TvShowViewModel.class);
        viewModel.setTVShow(getActivity());

        viewModel.getTVShow().observe(getActivity(), new Observer<ArrayList<TvShowItems>>() {
            @Override
            public void onChanged(ArrayList<TvShowItems> tvShowItems) {
                adapter.setData(tvShowItems);
                showLoading(false);
            }
        });
    }

    private void setRecyclerView(View view){
        rvTVShow = view.findViewById(R.id.listTVShow);
        rvTVShow.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TVShowAdapter();
        adapter.notifyDataSetChanged();
        rvTVShow.setAdapter(adapter);

        adapter.setOnItemClickCallback(new TVShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShowItems data) {
                /*Intent intent = new Intent(getActivity(),DetailMoviesActivity.class);
                intent.putExtra(DetailMoviesActivity.MOVIE_EXTRA, data);
                startActivity(intent);*/
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
}
