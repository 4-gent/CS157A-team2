package com.bookie.bizlogic;

import java.util.List;
import com.bookie.models.Author;

public interface AuthorServiceInterface {
    List<Author> getAuthors();
    Author addAuthor(Author author);
    //boolean changeAuthorName(int authId, String newName);
}