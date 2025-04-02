package com.zestindiait.security;

import com.zestindiait.dto.User;
import com.zestindiait.service.UserServiceFeignClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class JwtAuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    @Lazy
    private UserServiceFeignClient userServiceFeignClient;

    private static final List<String> ADMIN_ONLY_ENDPOINTS = List.of(
            "POST /product",
            "PUT /product",
            "DELETE /product"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Bypass interceptor for internal service-to-service calls
        String internalAuth = request.getHeader("Internal-Auth");
        if ("SECRET_INTERNAL_KEY".equals(internalAuth)) {
            return true; 
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
            return false;
        }

        try {
            User user = userServiceFeignClient.getUserDetails(authHeader);
            request.setAttribute("user", user);

            String requestPath = request.getRequestURI();
            String method = request.getMethod();
            String endpoint = method + " " + requestPath;

            if (ADMIN_ONLY_ENDPOINTS.stream().anyMatch(endpoint::startsWith) && !"ADMIN".equals(user.getRole())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admins only");
                return false;
            }

            return true;
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
    }

}
