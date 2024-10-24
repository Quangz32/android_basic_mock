package com.example.ojtbadamockproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojtbadamockproject.R;
import com.example.ojtbadamockproject.adapters.MovieGridAdapter;
import com.example.ojtbadamockproject.adapters.MovieListAdapter;
import com.example.ojtbadamockproject.database.FavouriteMovieDBHelper;
import com.example.ojtbadamockproject.dto.MovieListResponse;
import com.example.ojtbadamockproject.entities.Movie;
import com.example.ojtbadamockproject.service.ApiService;

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

        getMovieData();
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
        recyclerView = view.findViewById(R.id.recycler_view);
//        isListNow = false;
//        toggleRecyclerViewMode();
        return view;
    }

    public void toggleRecyclerViewMode() {
        //NEED TO IMPROVE PERFORMANCE
        if (isListNow) {
            isListNow = false;
            recyclerViewAdapter = new MovieGridAdapter(getContext(), movieList);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(recyclerViewAdapter);
        } else {
            isListNow = true;
            recyclerViewAdapter = new MovieListAdapter(getContext(), movieList, favouriteMovieIds, false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(recyclerViewAdapter);
        }
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
                isListNow = false;
                toggleRecyclerViewMode();
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFavouriteMovie() {
        try (FavouriteMovieDBHelper dbHelper = new FavouriteMovieDBHelper(getContext())) {
            favouriteMovieIds = dbHelper.getMovieIdsSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        getMovieData();
        getFavouriteMovie();

        recyclerViewAdapter = new MovieListAdapter(getContext(), movieList, favouriteMovieIds, false);
        recyclerView.setAdapter(recyclerViewAdapter);

    }


}