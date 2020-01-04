package com.oong.mymoviecataloguelocalstorage.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oong.mymoviecataloguelocalstorage.MainActivity;
import com.oong.mymoviecataloguelocalstorage.entity.TvShowItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TvShowModel extends ViewModel {

    private static final String API_KEY = "8a9136100788b5ba869e016a4625a9bc";
    private MutableLiveData<ArrayList<TvShowItems>> listTV = new MutableLiveData<>();
    private String language = "en-US";

    public void setTVShow(final Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShowItems> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=" + language;
        final String image_url = "https://image.tmdb.org/t/p/w185";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray results = responseObject.getJSONArray("results");
                    for(int i = 0; i < results.length(); i++){
                        JSONObject jsonObject = results.getJSONObject(i);
                        TvShowItems items = new TvShowItems();
                        items.setTvShow_id(jsonObject.getString("id"));
                        items.setTitle(jsonObject.getString("original_name"));
                        items.setDescription(jsonObject.getString("overview"));
                        items.setPoster_path(jsonObject.getString("poster_path"));
                        items.setPhoto(image_url + items.getPoster_path());
                        items.setPopularity(jsonObject.getString("popularity"));
                        listItems.add(items);
                    }
                    listTV.postValue(listItems);
                }
                catch (Exception e){
                    Log.d("Exception", e.getMessage());
                    Toast.makeText(context, MainActivity.ERROR_MSG , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Exception", error.getMessage());
                Toast.makeText(context, MainActivity.ERROR_MSG , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDetailTV(final Context context, final String tv_id){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShowItems> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/tv/" + tv_id + "?api_key=" + API_KEY + "&language=" + language;
        final String image_url = "https://image.tmdb.org/t/p/w185";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    String movie_id = responseObject.getString("id");
                    String title = responseObject.getString("name");
                    String poster_path = responseObject.getString("poster_path");
                    String desc = responseObject.getString("overview");
                    String popularity = responseObject.getString("popularity");

                    TvShowItems items = new TvShowItems();
                    items.setTvShow_id(movie_id);
                    items.setTitle(title);
                    items.setPoster_path(poster_path);
                    items.setPhoto(image_url + poster_path);
                    items.setDescription(desc);
                    items.setPopularity(popularity);

                    listItems.add(items);
                    listTV.postValue(listItems);
                }
                catch (Exception e){
                    Log.d("Exception", e.getMessage());
                    Toast.makeText(context, MainActivity.ERROR_MSG , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Exception", error.getMessage());
                Toast.makeText(context, MainActivity.ERROR_MSG , Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LiveData<ArrayList<TvShowItems>> getTVShow(){
        return listTV;
    }

}
