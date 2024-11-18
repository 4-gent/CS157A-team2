package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bookie.auth.UserContext;
import com.bookie.auth.AuthorizationProxy;
import com.bookie.models.Author;
import java.util.List;

public class AuthorServiceTest {

    private AuthorServiceInterface authorService;

    @BeforeEach
    public void setUp() {
        // Create a proxied instance of AuthorService
        authorService = AuthorizationProxy.createProxy(new AuthorService());
    }

    @Test
    public void testAddAuthor_AccessDenied() {
        UserContext.setUserId("nonAdminUser");
        Exception exception = assertThrows(SecurityException.class, () -> {
            authorService.addAuthor(new Author("New Author"));
        });
        assertEquals("Access denied: Admin privileges required", exception.getMessage());
    }

    @Test
    public void testGetAuthors() {
        List<Author> authors = authorService.getAuthors();
        assertNotNull(authors);
    }
}