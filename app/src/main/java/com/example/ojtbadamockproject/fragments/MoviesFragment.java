package com.example.ojtbadamockproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ojtbadamockproject.R;
import com.example.ojtbadamockproject.adapters.MovieGridAdapter;
import com.example.ojtbadamockproject.adapters.MovieListAdapter;
import com.example.ojtbadamockproject.database.FavouriteMovieDBHelper;
import com.example.ojtbadamockproject.dto.MovieListResponse;
import com.example.ojtbadamockproject.entities.Movie;
import com.example.ojtbadamockproject.service.ApiService;
import com.example.ojtbadamockproject.utils.MyConstants;

import java.util.ArrayList;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MoviesFragment extends Fragment {
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "e7631ffcb8e766993e5ec0c1f4245f93";

//    private static final String MOVIE_LIST = "movie_list";

    private ArrayList<Movie> movieList;
    private Set<Integer> favouriteMovieIds;
    private int pageNumber;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private boolean isListNow;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
//        args.putSerializable(MOVIE_LIST, movieList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            movieList = (ArrayList<Movie>) getArguments().getSerializable(MOVIE_LIST);
//            if (movieList == null) {
//                movieList = new ArrayList<>();
//            }
//        }

        movieList = new ArrayList<>();
        getFavouriteMovie();

        getParentFragmentManager().setFragmentResultListener(
                "change_movies_page_mode",
                getActivity(),
                (requestKey, result) -> {
                    toggleRecyclerViewMode();
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewAdapter = new MovieListAdapter(getContext(), movieList, favouriteMovieIds, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        //PULL UP TO LOAD MORE
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //PULL UP TO LOAD MORE
                if (!recyclerView.canScrollVertically(1)) {
                    if (MyConstants.SHOW_ACTION_TOAST) {
                        Toast.makeText(getContext(), "Page " + (pageNumber + 1) +" loaded", Toast.LENGTH_SHORT).show();
                    }
                    pageNumber++;
                    addMovie(pageNumber);
                }

            }
        });

        //PULL DOWN TO REFRESH
        swipeRefreshLayout.setOnRefreshListener(() -> {
            int oldSize = movieList.size();
            movieList.clear();
            pageNumber = 1;
            addMovie(1);
            refresh();
            swipeRefreshLayout.setRefreshing(false);
        });


        //INIT VIEW
        pageNumber = 1;
        addMovie(1);        // -> load UI
        return view;
    }


    private void addMovie(int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<MovieListResponse> call = apiService.getPopularMovies(API_KEY, page); //only 1st page

        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                int oldSize = movieList.size();
                movieList.addAll(response.body().getResults());
                recyclerViewAdapter.notifyItemRangeInserted(oldSize, movieList.size() - oldSize);
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toggleRecyclerViewMode() {
        //NEED TO IMPROVE PERFORMANCE
        if (isListNow) {
            recyclerViewAdapter = new MovieGridAdapter(getContext(), movieList);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(recyclerViewAdapter);
        } else {
            recyclerViewAdapter = new MovieListAdapter(getContext(), movieList, favouriteMovieIds, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(recyclerViewAdapter);
        }

        isListNow = !isListNow;
    }


    private void getFavouriteMovie() {
        try (FavouriteMovieDBHelper dbHelper = new FavouriteMovieDBHelper(getContext())) {
            favouriteMovieIds = dbHelper.getMovieIdsSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
//        addMovie(pageNumber); //MAYBE NOT NEED TO RELOAD Movies data
        getFavouriteMovie();

        recyclerViewAdapter = new MovieListAdapter(getContext(), movieList, favouriteMovieIds, false);
        recyclerView.setAdapter(recyclerViewAdapter);

    }


}