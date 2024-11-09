package com.todolist.es.security;

import com.todolist.es.services.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtilService jwtUtil;

    @Autowired
    public JwtAuthFilter(@Lazy JwtUtilService jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * This method is called for each request that comes to the server
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the token from the header
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        System.out.println("JWT Filter inicializado..........");

        // Check if the token is not null and starts with Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            System.out.println("Token recebido: " + token);
            System.out.println("Cabeçalho Authorization: " + authorizationHeader);


            try {
                    String subID = jwtUtil.extractSubId(token);
                    System.out.println("Sub ID extraído do token: " + subID);
                    request.setAttribute("subID", subID);

                    UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(subID) // ""
                        .authorities(Collections.emptyList())
                        .password("")
                        .build();

                    // Create UsernamePasswordAuthenticationToken with authorities
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, // principal
                            null, // credentials
                            null // authorities Collections.emptyList()
                    );

                    // Set the authentication details
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    System.out.println("Authentication: " + authentication);
                    System.out.println("Autenticação configurada no SecurityContext com subID: " + subID);

                    // Set the authenticated user in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // If the token is invalid or an exception occurs, clear the security context
                SecurityContextHolder.clearContext();
                e.printStackTrace();
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
        System.out.println("JWT Finalizado ................");
    }
}
