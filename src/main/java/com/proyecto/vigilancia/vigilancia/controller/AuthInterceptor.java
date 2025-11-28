package com.proyecto.vigilancia.vigilancia.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();
        
        System.out.println("🔐 Interceptor verificando: " + requestURI);

        // ✅ URLs que NO requieren autenticación
        if (isPublicResource(requestURI)) {
            return true;
        }

        // ✅ Verificar sesión para todas las demás URLs
        if (session == null || session.getAttribute("usuario") == null) {
            System.out.println("❌ No hay sesión activa, redirigiendo a login");
            
            // Si es una API, devolver error JSON
            if (requestURI.startsWith("/api/")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"mensaje\":\"No autorizado\"}");
            } else {
                // Si es una página, redirigir al login
                response.sendRedirect("/login");
            }
            return false;
        }

        System.out.println("✅ Sesión activa para: " + session.getAttribute("usuario"));
        return true;
    }

    private boolean isPublicResource(String uri) {
        return uri.equals("/login") || 
               uri.equals("/api/login") || 
               uri.equals("/api/check-session") ||
               uri.equals("/api/logout") ||
               uri.startsWith("/css/") || 
               uri.startsWith("/js/") ||
               uri.startsWith("/images/") ||
               uri.startsWith("/webjars/") ||
               uri.equals("/favicon.ico");
    }
}