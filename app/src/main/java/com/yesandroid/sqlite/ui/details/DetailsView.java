package com.yesandroid.sqlite.ui.details;

import com.yesandroid.sqlite.data.movie.Movie;
import com.yesandroid.sqlite.ui.base.BaseView;

/**
 * Created by Ali Asadi on 12/03/2018.
 */
public interface DetailsView extends BaseView {

    void showMovieData(Movie movie);

    void showDataUnavailableMessage();

}
