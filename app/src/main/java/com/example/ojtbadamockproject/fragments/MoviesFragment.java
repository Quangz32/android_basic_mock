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
import com.example.ojtbadamockproject.entities.Movie;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {
    private static final String MOVIE_LIST = "movie_list";

    private ArrayList<Movie> mMovieList;

    private RecyclerView recyclerView;
//    private MovieListAdapter movieListAdapter;
//    private MovieGridAdapter movieGridAdapter;
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
            mMovieList = (ArrayList<Movie>) getArguments().getSerializable(MOVIE_LIST);
            if (mMovieList == null) {
                mMovieList = new ArrayList<>();
            }
        }

//        setupRecyclerView();

        getParentFragmentManager().setFragmentResultListener(
                "change_movies_page_mode",
                getActivity(),
                (requestKey, result) ->{
            toggleRecyclerViewMode();
        });

    }

//    private void setupRecyclerView() {
//        recyclerView = getView().findViewById(R.id.recycler_view);
//
//        if (isListNow) {
//            movieListAdapter = new MovieListAdapter(getContext(), mMovieList);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            recyclerView.setAdapter(movieListAdapter);
//        }
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

//        if (isListNow) {
//            Log.d("qz", mMovieList.toString());
//            movieListAdapter = new MovieListAdapter(getContext(), mMovieList);
//            movieGridAdapter = new MovieGridAdapter(getContext(), mMovieList);
////            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            recyclerView.setAdapter(movieGridAdapter);
//
//            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
//            recyclerView.setAdapter(movieGridAdapter);
//        }
//
        isListNow = false;
        toggleRecyclerViewMode();

        return view;

        // Inflate the layout for this fragment
    }

    public void toggleRecyclerViewMode() {
        if (isListNow) {
            isListNow = false;
            recyclerViewAdapter = new MovieGridAdapter(getContext(), mMovieList);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(recyclerViewAdapter);
        }
        else {
            isListNow = true;
            recyclerViewAdapter = new MovieListAdapter(getContext(), mMovieList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

}