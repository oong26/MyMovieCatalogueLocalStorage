package com.oong.mymoviecataloguelocalstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.oong.mymoviecataloguelocalstorage.adapter.SectionsPagerAdapter;
import com.oong.mymoviecataloguelocalstorage.database.FavoriteHelper;

public class MainActivity extends AppCompatActivity {

    public static String ERROR_MSG;
    public static String SERVER_ERROR;
    public static String MOVIE_SUCCESS_MSG;
    public static String TV_SUCCESS_MSG;
    public static String REMOVED_FAV_MSG;
    public static String DATA_NOT_FOUND;
    private FavoriteHelper favoriteHelper;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private BottomNavigationView bottomView;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private SectionsPagerAdapter sectionsPagerAdapter;
    public static FrameLayout hostFragment;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        ERROR_MSG = getResources().getString(R.string.error_msg);
        SERVER_ERROR = getResources().getString(R.string.server_error);
        MOVIE_SUCCESS_MSG = getResources().getString(R.string.movie_fav_msg);
        TV_SUCCESS_MSG = getResources().getString(R.string.tv_fav_msg);
        REMOVED_FAV_MSG = getResources().getString(R.string.remove_favorite);
        DATA_NOT_FOUND = getResources().getString(R.string.data_not_found);
        favoriteHelper = FavoriteHelper.getInstance(this);
        favoriteHelper.open();
        hostFragment = findViewById(R.id.nav_host_fragment);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        bottomView = findViewById(R.id.bottom_nav_view);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_movies, R.id.navigation_tv_show, R.id.navigation_favorite).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomView, navController);
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.back_text), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        },2000);
    }
}
