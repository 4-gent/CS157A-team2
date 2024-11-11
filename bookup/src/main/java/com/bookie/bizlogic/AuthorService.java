package com.bookie.bizlogic;

import java.util.List;

import com.bookie.dao.AuthorDAO;
import com.bookie.models.Author;

public class AuthorService {
	
	private AuthorDAO authorDAO;
	
	public AuthorService() {
		authorDAO = new AuthorDAO();
	}
	
	public List<Author> getAuthors() {

		return authorDAO.getAllAuthors();
	}
	
	//TODO Add Admin check, only admin can use this method
	public Author addAuthor(Author author) {
		
		return authorDAO.add(author);
	}
	
	//TODO Add Admin check, only admin can use this method
	public boolean changeAuthorName(int authId, String newName) {
		Author a = authorDAO.getById(authId);
		a.setName(newName);
		return authorDAO.update(a);
	}

}
