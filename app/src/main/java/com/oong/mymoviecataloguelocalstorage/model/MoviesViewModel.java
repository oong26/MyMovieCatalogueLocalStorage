package com.oong.mymoviecataloguelocalstorage.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oong.mymoviecataloguelocalstorage.MainActivity;
import com.oong.mymoviecataloguelocalstorage.entity.MovieItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MoviesViewModel extends ViewModel {

    private static final String API_KEY = "8a9136100788b5ba869e016a4625a9bc";
    private MutableLiveData<ArrayList<MovieItems>> listMovies = new MutableLiveData<>();

    public void setMovies(final Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieItems> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY;
        final String image_url = "https://image.tmdb.org/t/p/w185";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");
                    for(int i = 0; i < results.length(); i++){
                        JSONObject movies = results.getJSONObject(i);
                        MovieItems movieItems = new MovieItems();
                        movieItems.setMovie_id(movies.getString("id"));
                        movieItems.setTitle(movies.getString("title"));
                        movieItems.setDescription(movies.getString("overview"));
                        movieItems.setPoster_path(movies.getString("poster_path"));
                        movieItems.setPhoto(image_url + movieItems.getPoster_path());
                        movieItems.setRelease_date(movies.getString("release_date"));
                        listItems.add(movieItems);
                    }
                    listMovies.postValue(listItems);
                }
                catch (Exception e){
                    Log.d("Exception", e.getMessage());
                    Toast.makeText(context, MainActivity.ERROR_MSG , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Exception", error.getMessage());
                Toast.makeText(context, MainActivity.SERVER_ERROR , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDetailMovie(final Activity activity, final Context context, final String movie_id){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieItems> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/movie/" + movie_id + "?api_key=" + API_KEY + "&language=en-US";
        final String image_url = "https://image.tmdb.org/t/p/w185";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    String movie_id = responseObject.getString("id");
                    String title = responseObject.getString("original_title");
                    String poster_path = responseObject.getString("poster_path");
                    String desc = responseObject.getString("overview");
                    String release_date = responseObject.getString("release_date");

                    MovieItems movieItems = new MovieItems();
                    movieItems.setMovie_id(movie_id);
                    movieItems.setTitle(title);
                    movieItems.setPoster_path(poster_path);
                    movieItems.setPhoto(image_url + poster_path);
                    movieItems.setDescription(desc);
                    movieItems.setRelease_date(release_date);

                    listItems.add(movieItems);
                    listMovies.postValue(listItems);
                }
                catch (Exception e){
                    Log.d("Exception", e.getMessage());
                    Toast.makeText(context, MainActivity.ERROR_MSG , Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Exception", error.getMessage());
                Toast.makeText(context, MainActivity.SERVER_ERROR , Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
    }

    public LiveData<ArrayList<MovieItems>> getMovie(){
        return listMovies;
    }

}
