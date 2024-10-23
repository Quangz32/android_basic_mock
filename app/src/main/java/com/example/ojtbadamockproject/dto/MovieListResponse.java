package com.example.ojtbadamockproject.dto;

import com.example.ojtbadamockproject.entities.Movie;

import java.util.List;

public class MovieListResponse {
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }
}
