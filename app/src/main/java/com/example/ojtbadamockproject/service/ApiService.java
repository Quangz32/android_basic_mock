package com.example.ojtbadamockproject.service;

import com.example.ojtbadamockproject.dto.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie/popular")
    Call<MovieListResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int pageNumber);

    @GET("movie/top_rated")
    Call<MovieListResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int pageNumber);

    @GET("movie/upcoming")
    Call<MovieListResponse> getUpcomingMovies(@Query("api_key") String apiKey, @Query("page") int pageNumber);

    @GET("movie/now_playing")
    Call<MovieListResponse> getNowPlayingMovies(@Query("api_key") String apiKey, @Query("page") int pageNumber);


}
