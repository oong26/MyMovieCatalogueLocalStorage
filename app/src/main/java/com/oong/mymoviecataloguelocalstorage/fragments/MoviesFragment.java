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

import com.oong.mymoviecataloguelocalstorage.DetailMovieActivity;
import com.oong.mymoviecataloguelocalstorage.MainActivity;
import com.oong.mymoviecataloguelocalstorage.R;
import com.oong.mymoviecataloguelocalstorage.adapter.MoviesAdapter;
import com.oong.mymoviecataloguelocalstorage.entity.MovieItems;
import com.oong.mymoviecataloguelocalstorage.model.MoviesViewModel;

import java.util.ArrayList;

import static android.view.View.*;

public class MoviesFragment extends Fragment {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    private MoviesAdapter adapter;
    private MoviesViewModel viewModel;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
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
        viewModel = ViewModelProviders.of(getActivity()).get(MoviesViewModel.class);
        viewModel.setMovies(getActivity());
        viewModel.getMovie().observe(getActivity(), new Observer<ArrayList<MovieItems>>() {
            @Override
            public void onChanged(ArrayList<MovieItems> movieItems) {
                adapter.setData(movieItems);
                showLoading(false);
            }
        });
    }

    private void setRecyclerView(View view){
        rvMovies = view.findViewById(R.id.listMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MoviesAdapter();
        adapter.notifyDataSetChanged();
        rvMovies.setAdapter(adapter);

        adapter.setOnItemClickCallback(new MoviesAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(MovieItems data) {
                Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.MOVIE_EXTRA, data);
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
}
