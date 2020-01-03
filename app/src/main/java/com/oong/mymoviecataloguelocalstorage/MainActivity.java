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
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.oong.mymoviecataloguelocalstorage.adapter.SectionsPagerAdapter;
import com.oong.mymoviecataloguelocalstorage.database.FavoriteHelper;

public class MainActivity extends AppCompatActivity {

    FavoriteHelper favoriteHelper;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    private BottomNavigationView bottomView;
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;
    private SectionsPagerAdapter sectionsPagerAdapter;
    public static FrameLayout hostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inisialisasi Komponen
        init();
    }

    private void init(){
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
}
