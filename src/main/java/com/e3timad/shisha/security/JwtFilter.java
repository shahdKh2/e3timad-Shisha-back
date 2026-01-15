package com.e3timad.shisha.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String auth = req.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            try {
                Claims claims = JwtUtil.parseToken(token);
                req.setAttribute("username", claims.getSubject());
                req.setAttribute("role", claims.get("role"));
            } catch (Exception e) {
                throw new RuntimeException("Invalid token");
            }
        }

        chain.doFilter(request, response);
    }
}
