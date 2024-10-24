package com.example.ojtbadamockproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojtbadamockproject.R;
import com.example.ojtbadamockproject.adapters.MovieListAdapter;
import com.example.ojtbadamockproject.database.FavouriteMovieDBHelper;
import com.example.ojtbadamockproject.entities.Movie;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment {

    private ArrayList<Movie> favouriteMovies;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DB handle
        try (FavouriteMovieDBHelper dbHelper = new FavouriteMovieDBHelper(getContext())) {
            favouriteMovies = (ArrayList<Movie>) dbHelper.getAllMovies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewAdapter = new MovieListAdapter(getContext(), favouriteMovies, null, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }

    public void refresh() {
        try (FavouriteMovieDBHelper dbHelper = new FavouriteMovieDBHelper(getContext())) {

            if (recyclerViewAdapter != null) {
                recyclerViewAdapter.notifyItemRangeRemoved(0, favouriteMovies.size()); // Xóa các item cũ
                favouriteMovies.clear(); // Xóa dữ liệu hiện tại
                favouriteMovies.addAll(dbHelper.getAllMovies()); // Cập nhật dữ liệu mới từ cơ sở dữ liệu
                recyclerViewAdapter.notifyItemRangeInserted(0, favouriteMovies.size()); // Thông báo cho adapter về sự thay đổi dữ liệu
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doSearch(String text) {
        try (FavouriteMovieDBHelper dbHelper = new FavouriteMovieDBHelper(getContext())) {
            if (recyclerViewAdapter != null) {
                recyclerViewAdapter.notifyItemRangeRemoved(0, favouriteMovies.size());
                favouriteMovies.clear();
                favouriteMovies.addAll(dbHelper.searchByKeyword(text));
                recyclerViewAdapter.notifyItemRangeInserted(0, favouriteMovies.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}