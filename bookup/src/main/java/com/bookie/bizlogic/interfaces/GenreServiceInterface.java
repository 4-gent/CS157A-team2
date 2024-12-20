package com.bookie.bizlogic.interfaces;

import java.util.List;
import com.bookie.models.Genre;

public interface GenreServiceInterface {
    List<Genre> getGenres();
    Genre addGenre(Genre genre);
    boolean changeGenreName(int genreID, String newName);
}