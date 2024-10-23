package com.example.ojtbadamockproject.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ojtbadamockproject.R;
import com.example.ojtbadamockproject.adapters.MyPagerAdapter;
import com.example.ojtbadamockproject.database.FavouriteMovieDBHelper;
import com.example.ojtbadamockproject.dto.MovieListResponse;
import com.example.ojtbadamockproject.entities.Movie;
import com.example.ojtbadamockproject.fragments.AboutFragment;
import com.example.ojtbadamockproject.fragments.FavouriteFragment;
import com.example.ojtbadamockproject.fragments.MoviesFragment;
import com.example.ojtbadamockproject.fragments.SettingsFragment;
import com.example.ojtbadamockproject.service.ApiService;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "e7631ffcb8e766993e5ec0c1f4245f93";

    private FavouriteMovieDBHelper dbHelper;


    ViewPager2 viewPager2;
    TabLayout tabLayout;
    MyPagerAdapter pagerAdapter;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;

    ArrayList<Fragment> myFragments;
    ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);






        dbHelper = new FavouriteMovieDBHelper(this);
        getMovieData();





//        setupPages();
//        setupDrawer();    ---> these moved to Retrofit call back because of async

    }

    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.tool_bar);
        navigationView = findViewById(R.id.navigation_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        loadOrUpdateUserInfo(navigationView);
//        Button btnEdit = navigationView.getHeaderView(0).findViewById(R.id.btn_edit);
//        btnEdit.setOnClickListener(view -> changeFragment(mEditProfileFragment));
    }

    private void setupPages() {
        myFragments = new ArrayList<>();
//        Log.d("qz_setup_Page", movieList.toString());
        myFragments.add(MoviesFragment.newInstance(this.movieList));
        myFragments.add(FavouriteFragment.newInstance("", ""));
        myFragments.add(SettingsFragment.newInstance("", ""));
        myFragments.add(AboutFragment.newInstance("", ""));

        viewPager2 = findViewById(R.id.view_pager2);
        tabLayout = findViewById(R.id.tab_layout);
        pagerAdapter =
                new MyPagerAdapter(this, myFragments);
        viewPager2.setAdapter(pagerAdapter);

        //Set up TabLayout
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            String[] tabs = {"Movies", "Favourite", "Settings", "About"};
            int[] icons = {R.drawable.baseline_home_24,
                    R.drawable.baseline_favorite_24,
                    R.drawable.baseline_settings_24,
                    R.drawable.baseline_info_24};

            View tabView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            TextView tabTitle = tabView.findViewById(R.id.tab_title);
            tabTitle.setText(tabs[position]);

            ImageView tabIcon = tabView.findViewById(R.id.tab_icon);
            tabIcon.setImageResource(icons[position]);

            tab.setCustomView(tabView);

        }).attach();

        //Set up tab title on Toolbar
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String[] tabs = {"Movies", "Favourite", "Settings", "About"};
                        String tabTitle = tabs[tab.getPosition()];
                        Objects.requireNonNull(getSupportActionBar()).setTitle(tabTitle);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );

    }

    private void getMovieData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<MovieListResponse> call = apiService.getPopularMovies(API_KEY, 1); //only 1st page
        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                movieList = (ArrayList<Movie>) response.body().getResults();
                setupPages();
                setupDrawer();

                dbHelper.getAllMovies().forEach(movie -> Log.d("qz_movie", movie.toString()));
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_list || itemId == R.id.menu_item_grid) {
            getSupportFragmentManager().setFragmentResult("change_movies_page_mode", null);

            if (itemId == R.id.menu_item_list) {
                item.setVisible(false);
                MenuItem gridItem = toolbar.getMenu().findItem(R.id.menu_item_grid);
                if (gridItem != null) gridItem.setVisible(true);
            } else if (itemId == R.id.menu_item_grid) {
                item.setVisible(false);
                MenuItem listItem = toolbar.getMenu().findItem(R.id.menu_item_list);
                if (listItem != null) listItem.setVisible(true);
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}