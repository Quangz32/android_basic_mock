package com.example.ojtbadamockproject.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.ojtbadamockproject.fragments.AboutFragment;
import com.example.ojtbadamockproject.fragments.FavouriteFragment;
import com.example.ojtbadamockproject.fragments.MoviesFragment;
import com.example.ojtbadamockproject.fragments.SettingsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    MyPagerAdapter pagerAdapter;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ArrayList<Fragment> myFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        setupPages();
        setupDrawer();

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
        myFragments.add(MoviesFragment.newInstance());
        myFragments.add(FavouriteFragment.newInstance());
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

                        toolbar.getMenu().findItem(R.id.menu_item_search).setVisible(tab.getPosition() == 1);
                        toolbar.getMenu().findItem(R.id.menu_item_list).setVisible(tab.getPosition() == 0);
                        toolbar.getMenu().findItem(R.id.menu_item_grid).setVisible(false);

                        //REFRESH FRAGMENTS WHEN CHANGING TABS

                        switch (tab.getPosition()) {
                            case 0:
                                MoviesFragment fragment0 = (MoviesFragment) myFragments.get(0);
                                fragment0.refresh();
                                break;
                            case 1:
                                FavouriteFragment fragment1 = (FavouriteFragment) myFragments.get(1);
                                fragment1.refresh();
                                break;
                            case 2:
                                myFragments.set(2, SettingsFragment.newInstance("", ""));
                                break;
                            case 3:
                                myFragments.set(3, AboutFragment.newInstance("", ""));
                                break;
                            default:
                                break;
                        }

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