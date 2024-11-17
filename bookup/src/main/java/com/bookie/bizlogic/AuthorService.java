package com.bookie.bizlogic;

import java.util.List;

import com.bookie.auth.IsAdmin;
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
	
	@IsAdmin
	public Author addAuthor(Author author) {
		
		return authorDAO.add(author);
	}
	
	@IsAdmin
	public boolean changeAuthorName(int authId, String newName) {
		Author a = authorDAO.getById(authId);
		a.setName(newName);
		return authorDAO.update(a);
	}

}
