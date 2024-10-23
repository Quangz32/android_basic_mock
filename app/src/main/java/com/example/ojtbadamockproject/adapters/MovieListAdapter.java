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

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private static final String PICASSO_URL = "https://image.tmdb.org/t/p/original";

    private Context context;
    private ArrayList<Movie> movieList;

    public MovieListAdapter(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent, false);
        return new MovieListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, int position) {
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
        TextView tvReleaseDate;
        TextView tvRating;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.movie_name);
            ivPoster = itemView.findViewById(R.id.movie_poster);
            tvReleaseDate = itemView.findViewById(R.id.movie_release_date);
            tvRating = itemView.findViewById(R.id.movie_rating);
            tvOverview = itemView.findViewById(R.id.movie_overview);
        }

        public void bind(Movie movie) {
            tvMovieName.setText(movie.getTitle());
            tvReleaseDate.setText(movie.getReleaseDate());
            tvRating.setText(String.valueOf(movie.getRating()));
            tvOverview.setText(movie.getOverview());

            Picasso.get().load(PICASSO_URL + movie.getPosterPath()).into(ivPoster);

//            Picass.get()
//                    .load(PICASSO_URL)
//                    .placeholder(R.drawable.placeholder_img) // Ảnh placeholder khi đang load
//                    .error(R.drawable.error_img) // Ảnh mặc định khi load lỗi
//                    .into(ivPoster);
        }
    }
}
