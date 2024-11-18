package com.bookie.bizlogic;

import java.util.List;
import com.bookie.auth.IsAdmin;
import com.bookie.dao.GenreDAO;
import com.bookie.models.Genre;

public class GenreService implements GenreServiceInterface {
    private GenreDAO genreDAO;

    public GenreService() {
        genreDAO = new GenreDAO();
    }

    @Override
    public List<Genre> getGenres() {
        return genreDAO.getAllGenres();
    }

    @Override
    @IsAdmin
    public Genre addGenre(Genre genre) {
        return genreDAO.add(genre);
    }

    @Override
    @IsAdmin
    public boolean changeGenreName(int genreID, String newName) {
        Genre a = genreDAO.getById(genreID);
        a.setName(newName);
        return genreDAO.update(a);
    }
}