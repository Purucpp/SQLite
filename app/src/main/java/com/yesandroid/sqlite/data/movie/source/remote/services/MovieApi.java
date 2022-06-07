package com.yesandroid.sqlite.data.movie.source.remote.services;

import com.yesandroid.sqlite.data.movie.source.remote.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApi {
    @GET("movies.json")
    Call<MovieResponse> getMovies();
}
