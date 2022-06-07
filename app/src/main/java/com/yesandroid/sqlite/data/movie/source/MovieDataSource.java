package com.yesandroid.sqlite.data.movie.source;

import com.yesandroid.sqlite.data.movie.Movie;

import java.util.List;

/**
 * Created by Ali Asadi on 30/01/2019.
 */
public interface MovieDataSource {

    interface LoadMoviesCallback {
        void onMoviesLoaded(List<Movie> movies);
        void onDataNotAvailable();
        void onError();
    }

    void getMovies(LoadMoviesCallback callback);
    void saveMovies(List<Movie> movies);
}
