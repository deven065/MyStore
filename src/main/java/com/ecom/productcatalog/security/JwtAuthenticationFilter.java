package com.ecom.productcatalog.security;

import com.ecom.productcatalog.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /*
    *   JWT is responsible for JWT related operations
    *   The filter will use it to:
    *   1. Extract the email from the Jwt
    *   2. Validate the Jwt
     */
    private final JwtService jwtService;

    /*
    *   Constructor Injection
    * Spring automatically gives this filter the JwtService object.
     */
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /*
    * This method runs once for every Http request.
    * Example:
    * GET /api/products
    * GET /api/profile
    * POST /api/orders
    * The filter gets a chance to inspect the request before the request reaches our controller.
     */

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        /*
        * Get the Authorization Header from the Http request.
        * A JWT request should contain:
        * Authorization: Bearer <token>
         */
        String authHeader = request.getHeader("Authorization");

        /*
        * Check whether the authorization header exists and whether it starts with "Bearer".
        * "Bearer" is the standard formar used when sending a JWT through the Authorization header.
         */
        if (authHeader == null || authHeader.startsWith("Bearer ")) {
            /*
            * There is no JWT in the request
            * We dont immediately reject the request here.
            * We allow Spring Security to decide later whether this particular endpoint requires authentication.
             */
            filterChain.doFilter(request, response);
            return;
        }

        //  Remove "Bearer " from the beginning of the header.
        String token = authHeader.substring(7);

        String email = jwtService.extractEmail(token);

        /*
        * If we successfully extracted an email and
        * Spring Security has not already authenticated
        * this request, we can create an authentication object.
         */
        if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            /*
            * Create an authentication object.
            * This tells Spring Security:
            * "This request belongs to this user"
            * 1st Parameter is User's Identity
            * 2nd Parameter contains credentials.
            * we dont need to store the password here, so we use null.
            * 3rd Parameter contains authorities/roles.
            * I will add proper roles later
             */
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            java.util.Collections.emptyList()
                    );
            /*
            * Attach additional information about the HTTP request to the Authentication object.
             */
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            /*
            * Put the Authentication object into Spring Security's SecurityContext.
            * From this point onward, Spring Security considers this request authenticated
             */
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        /*
        * Continue the request.
        * The request now moves to the next filter or eventually to the controller.
         */
        filterChain.doFilter(request, response);
    }
}
