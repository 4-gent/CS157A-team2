package com.bookie.bizlogic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.bookie.auth.UserContext;
import com.bookie.auth.AuthorizationProxy;
import com.bookie.models.Genre;
import java.util.List;

public class GenreServiceTest {

    private GenreServiceInterface genreService;

    @BeforeEach
    public void setUp() {
        // Create a proxied instance of GenreService
        genreService = AuthorizationProxy.createProxy(new GenreService());
    }

    @Test
    public void testAddGenre_AccessDenied() {
        UserContext.setUserId("nonAdminUser");
        Exception exception = assertThrows(SecurityException.class, () -> {
            genreService.addGenre(new Genre("New Genre"));
        });
        assertEquals("Access denied: Admin privileges required", exception.getMessage());
    }

    @Test
    public void testGetGenres() {
        List<Genre> genres = genreService.getGenres();
        assertNotNull(genres);
    }
}