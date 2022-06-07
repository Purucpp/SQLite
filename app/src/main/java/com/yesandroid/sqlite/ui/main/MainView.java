package com.yesandroid.sqlite.ui.main;

import com.yesandroid.sqlite.data.movie.Movie;
import com.yesandroid.sqlite.ui.base.BaseView;

import java.util.List;

/**
 * Created by Ali Asadi on 12/03/2018.
 */
public interface MainView extends BaseView {
    void showMovies(List<Movie> movies);

    void showErrorMessage();

    void showThereIsNoMovies();
}
