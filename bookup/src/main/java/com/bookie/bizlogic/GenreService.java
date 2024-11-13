package com.bookie.bizlogic;

import java.util.List;

import com.bookie.dao.GenreDAO;
import com.bookie.models.Genre;

public class GenreService {

private GenreDAO genreDAO;
	
	public GenreService() {
		genreDAO = new GenreDAO();
	}
	
	public List<Genre> getGenres() {

		return genreDAO.getAllGenres();
	}
	
	//TODO Add Admin check, only admin can use this method
	public Genre addGenre(Genre genre) {
		
		return genreDAO.add(genre);
	}
	
	//TODO Add Admin check, only admin can use this method
	public boolean changeGenreName(int genreID, String newName) {
		Genre a = genreDAO.getById(genreID);
		a.setName(newName);
		return genreDAO.update(a);
	}
	
}
