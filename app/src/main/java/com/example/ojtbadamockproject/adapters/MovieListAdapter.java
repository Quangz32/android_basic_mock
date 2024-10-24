package com.example.ojtbadamockproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ojtbadamockproject.R;
import com.example.ojtbadamockproject.database.FavouriteMovieDBHelper;
import com.example.ojtbadamockproject.entities.Movie;
import com.example.ojtbadamockproject.utils.MyConstants;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private static final String PICASSO_URL = "https://image.tmdb.org/t/p/original";

    private Context context;
    private ArrayList<Movie> movieList;
    private Set<Integer> favouriteMovieIds;
    private boolean isAllFavourite;

    public MovieListAdapter(Context context, ArrayList<Movie> movieList, Set<Integer> favouriteMovieIds, boolean isAllFavourite) {
        this.context = context;
        this.movieList = movieList;
        this.favouriteMovieIds = favouriteMovieIds;
        this.isAllFavourite = isAllFavourite;

//        favouriteMovieIds.forEach(id -> Log.d("qz_id", String.valueOf(id)));
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
        ImageView iv18plus;

        ImageView ivStarFilled, ivStarBorder;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.movie_name);
            ivPoster = itemView.findViewById(R.id.movie_poster);
            tvReleaseDate = itemView.findViewById(R.id.movie_release_date);
            tvRating = itemView.findViewById(R.id.movie_rating);
            tvOverview = itemView.findViewById(R.id.movie_overview);
            iv18plus = itemView.findViewById(R.id.movie_18_plus);

            ivStarFilled = itemView.findViewById(R.id.favourite_star_filled);
            ivStarBorder = itemView.findViewById(R.id.favourite_star_border);
        }

        public void bind(Movie movie) {

            tvMovieName.setText(movie.getTitle());
            tvReleaseDate.setText(movie.getReleaseDate());
            tvRating.setText(new DecimalFormat("#.0").format(movie.getRating()).concat("/10"));
            tvOverview.setText(movie.getOverview());

            iv18plus.setVisibility(movie.isAdult() ? View.VISIBLE : View.GONE);

            Picasso.get().load(PICASSO_URL + movie.getPosterPath()).into(ivPoster);

            if (isAllFavourite || (favouriteMovieIds != null && favouriteMovieIds.contains(movie.getId()))) {
                ivStarFilled.setVisibility(View.VISIBLE);
                ivStarBorder.setVisibility(View.GONE);
            } else {
                ivStarFilled.setVisibility(View.GONE);
                ivStarBorder.setVisibility(View.VISIBLE);
            }

            ivStarFilled.setOnClickListener(v -> {
                ivStarFilled.setVisibility(View.GONE);
                ivStarBorder.setVisibility(View.VISIBLE);

                //DB handle
                try (FavouriteMovieDBHelper dbHelper = new FavouriteMovieDBHelper(context)) {
                    dbHelper.deleteMovie(movie.getId());
                    if (MyConstants.SHOW_ACTION_TOAST) {
                        Toast.makeText(context, "Movie " + movie.getId() + " deleted", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            ivStarBorder.setOnClickListener(v -> {
                ivStarFilled.setVisibility(View.VISIBLE);
                ivStarBorder.setVisibility(View.GONE);

                //DB handle
                try (FavouriteMovieDBHelper dbHelper = new FavouriteMovieDBHelper(context)) {
                    dbHelper.addMovie(movie);
                    if (MyConstants.SHOW_ACTION_TOAST) {
                        Toast.makeText(context, "Movie " + movie.getId() + " added", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
