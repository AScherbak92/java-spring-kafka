package by.gsu.scherbak.orderService.security;

import by.gsu.scherbak.orderService.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

/*
 * Authentication filter
 *
 * @version 1.0 21.09.2025
 * @author Scherbak Andrey
 * */
public class AuthenticationFilter implements Filter {
    private final ApiKeyService apiKeyService;

    public AuthenticationFilter(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    /*Filter method*/
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authorization");
        String apiKey;


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(httpResponse,"Missing or invalid authorization header");
            return;
        }

        apiKey = authHeader.substring(7);

        if (!apiKeyService.validateKey(apiKey)) {
            sendError(httpResponse,"Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /*Method for printing errors during authentication*/
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
