package com.yesandroid.sqlite.db;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Ali Asadi on 30/01/2019.
 */
@Dao
public interface MovieDao {

    /**
     * Select all movies from the movies table.
     *
     * @return all movies.
     */
    @Query("SELECT * FROM movies")
    List<Movie> getMovies();

    /**
     * Insert all movies.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveMovies(List<Movie> movies);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void singleMovie(Movie movie);

    /**
     * Delete all movies.
     */
    @Query("DELETE FROM movies")
    void deleteMovies();
}
