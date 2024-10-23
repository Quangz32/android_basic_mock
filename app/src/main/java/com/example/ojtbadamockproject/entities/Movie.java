package com.example.ojtbadamockproject.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {
//     "adult": false,
//             "backdrop_path": "/417tYZ4XUyJrtyZXj7HpvWf1E8f.jpg",
//             "genre_ids": [16, 878, 10751],
//             "id": 1184918,
//             "original_language": "en",
//             "original_title": "The Wild Robot",
//             "overview": "After a shipwreck, an intelligent robot called Roz is stranded on an uninhabited island. To survive the harsh environment, Roz bonds with the island's animals and cares for an orphaned baby goose.",
//             "popularity": 7456.608,
//             "poster_path": "/wTnV3PCVW5O92JMrFvvrRcV39RU.jpg",
//             "release_date": "2024-09-12",
//             "title": "The Wild Robot",
//             "video": false,
//             "vote_average": 8.7,
//             "vote_count": 1015

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private float rating;


    public Movie() {
    }

    public Movie(int id, String title, String releaseDate, float rating, String overview, String posterPath, boolean adult) {
        this.id = id;
        this.rating = rating;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.adult = adult;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", adult=" + adult +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", overview='" + overview + '\'' +
                ", rating=" + rating +
                '}';
    }
}
