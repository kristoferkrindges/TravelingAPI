package com.kristofer.traveling.configs.Filter;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsOptionsRequestFilter extends OncePerRequestFilter {

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        // Verifique o tipo de solicitação OPTIONS e ajuste o cabeçalho
        if (request.getRequestURI().startsWith("/api/")) {
            response.setHeader("Access-Control-Allow-Origin", "https://travelingsocial.vercel.app");
        } else {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        }
        response.setStatus(HttpServletResponse.SC_OK);
    } else {
        filterChain.doFilter(request, response);
    }
}
}
