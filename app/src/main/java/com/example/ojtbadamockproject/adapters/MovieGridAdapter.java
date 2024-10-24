package com.example.ojtbadamockproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojtbadamockproject.R;
import com.example.ojtbadamockproject.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private static final String PICASSO_URL = "https://image.tmdb.org/t/p/original";

    private Context context;
    private ArrayList<Movie> movieList;


    public MovieGridAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieGridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
        return new MovieGridAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieGridAdapter.ViewHolder holder, int position) {
        holder.bind(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Views...
        TextView tvMovieName;
        ImageView ivPoster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.movie_name);
            ivPoster = itemView.findViewById(R.id.movie_poster);
        }

        public void bind(Movie movie) {
            tvMovieName.setText(movie.getTitle());

            Picasso.get().load(PICASSO_URL + movie.getPosterPath()).into(ivPoster);

        }
    }
}
