package com.yesandroid.sqlite.ui.details;

import com.yesandroid.sqlite.data.movie.Movie;
import com.yesandroid.sqlite.ui.base.BasePresenter;


/**
 * Created by Ali Asadi on 12/03/2018.
 */
public class DetailsPresenter extends BasePresenter<DetailsView> {

    private final Movie movie;

    DetailsPresenter(DetailsView view, Movie movie) {
        super(view);
        this.movie = movie;
    }

    public void onAttach() {
        showMovieData();
    }

    private void showMovieData() {
        if (movie != null) {
            view.showMovieData(movie);
        } else {
            view.showDataUnavailableMessage();
        }
    }
}
