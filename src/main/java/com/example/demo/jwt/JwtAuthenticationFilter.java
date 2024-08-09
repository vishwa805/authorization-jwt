package com.example.demo.jwt;

import com.example.demo.exception.TokenRequiredException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");
            System.out.println("authHeader =======>" + authHeader);

            if (request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/register") || request.getServletPath().equals("api/admin")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Inside bearer token ======================> ");
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("Outside Bearer ========================>");

            final String jwt = authHeader.substring(7);
            final String username = JwtHelper.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (JwtHelper.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);

            } else {
                throw new IllegalArgumentException("Invalid Token");
            }

            filterChain.doFilter(request, response);

        } catch (AuthorizationDeniedException e) {
            throw new AuthorizationDeniedException("You do not have access to the resource", null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
