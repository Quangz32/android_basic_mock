package com.example.ojtbadamockproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojtbadamockproject.R;
import com.example.ojtbadamockproject.adapters.MovieGridAdapter;
import com.example.ojtbadamockproject.adapters.MovieListAdapter;
import com.example.ojtbadamockproject.database.FavouriteMovieDBHelper;
import com.example.ojtbadamockproject.entities.Movie;

import java.util.ArrayList;
import java.util.Set;


public class MoviesFragment extends Fragment {
    private static final String MOVIE_LIST = "movie_list";

    private ArrayList<Movie> movieList;
    private Set<Integer> favouriteMovieIds;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private boolean isListNow;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance(ArrayList<Movie> movieList) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putSerializable(MOVIE_LIST, movieList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieList = (ArrayList<Movie>) getArguments().getSerializable(MOVIE_LIST);
            if (movieList == null) {
                movieList = new ArrayList<>();
            }
        }

        //DB handle
        try (FavouriteMovieDBHelper dbHelper = new FavouriteMovieDBHelper(getContext())){
            favouriteMovieIds = dbHelper.getMovieIdsSet();
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        isListNow = false;
        toggleRecyclerViewMode();
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
            recyclerViewAdapter = new MovieListAdapter(getContext(), movieList, favouriteMovieIds);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

}