package com.bookie.bizlogic;

import java.util.List;

import com.bookie.auth.AuthorizationProxy;
import com.bookie.auth.IsAdmin;
import com.bookie.bizlogic.interfaces.AuthorServiceInterface;
import com.bookie.dao.AuthorDAO;
import com.bookie.models.Author;

public class AuthorService implements AuthorServiceInterface {
    private AuthorDAO authorDAO;

    private AuthorService() {
        authorDAO = new AuthorDAO();
    }
    
    public static AuthorServiceInterface getServiceInstance() {
    	return AuthorizationProxy.createProxy(new AuthorService());
    }

    @Override
    public List<Author> getAuthors() {
        return authorDAO.getAllAuthors();
    }

    @Override
    @IsAdmin
    public Author addAuthor(Author author) {
        return authorDAO.add(author);
    }

//    @Override
//    @IsAdmin
//    public boolean changeAuthorName(int authId, String newName) {
//        Author a = authorDAO.getById(authId);
//        a.setName(newName);
//        return authorDAO.update(a);
//    }
}