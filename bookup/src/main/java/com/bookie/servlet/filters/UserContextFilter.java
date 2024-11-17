package com.bookie.servlet.filters;

import java.io.IOException;

import com.bookie.auth.UserContext;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

public class UserContextFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userId = httpRequest.getHeader("X-User-ID");

        // Set UserContext
        if (userId != null) {
            UserContext.setUserId(userId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            // Clear the context after request processing
            UserContext.clear();
        }
    }

    @Override
    public void destroy() {}
}