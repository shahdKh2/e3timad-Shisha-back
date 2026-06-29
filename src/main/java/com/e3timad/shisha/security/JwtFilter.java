package com.e3timad.shisha.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        String path = req.getRequestURI();

        if (path.startsWith("/api/auth") || path.startsWith("/api/products") || path.startsWith("/api/offers")) {            chain.doFilter(request, response);
            return;

        }
        if (path.startsWith("/api/invoices") && req.getMethod().equals("GET")) {
            chain.doFilter(request, response);
            return;
        }

//        --------------------------------------------
        System.out.println("==============");
        System.out.println("METHOD = " + req.getMethod());
        System.out.println("PATH = " + req.getRequestURI());
        System.out.println("AUTH = " + req.getHeader("Authorization"));
//        --------------------------------------------
        String auth = req.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String token = auth.substring(7);

            Claims claims = JwtUtil.parseToken(token);
            req.setAttribute("username", claims.getSubject());
            req.setAttribute("role", claims.get("role"));

            chain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }}

//package com.e3timad.shisha.security;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
//
//public class JwtFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        System.out.println("Request: " + req.getMethod() + " " + req.getRequestURI());
//
//
//        if (req.getMethod().equals("OPTIONS")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String path = req.getRequestURI();
//
//        if (path.startsWith("/api/auth") || path.startsWith("/api/products")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String auth = req.getHeader("Authorization");
//
//        if (auth == null || !auth.startsWith("Bearer ")) {
//            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//        System.out.println("Authorization: " + auth);
//
//        try {
//            String token = auth.substring(7);
//
//            Claims claims = JwtUtil.parseToken(token);
//            req.setAttribute("username", claims.getSubject());
//            req.setAttribute("role", claims.get("role"));
//
//            chain.doFilter(request, response);
//
//        } catch (Exception e) {
//            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        }
//    }
//}