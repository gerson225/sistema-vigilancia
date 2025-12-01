package com.proyecto.vigilancia.vigilancia.controller;

import com.proyecto.vigilancia.vigilancia.entity.Usuario;
import com.proyecto.vigilancia.vigilancia.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final UsuarioService usuarioService;

    // 🔥 AGREGAR EL CONSTRUCTOR CON USUARIOSERVICE
    public AuthInterceptor(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

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

        // 🔥 NUEVA VERIFICACIÓN: CONTROL DE ACCESO POR ROLES
        if (!tienePermiso(session, requestURI)) {
            System.out.println("❌ Acceso denegado por permisos insuficientes");
            
            if (requestURI.startsWith("/api/")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"mensaje\":\"Acceso denegado\"}");
            } else {
                response.sendRedirect("/dashboard?error=acceso_denegado");
            }
            return false;
        }

        System.out.println("✅ Sesión activa para: " + session.getAttribute("usuario") + 
                          " - Rol: " + session.getAttribute("rol"));
        return true;
    }

    // 🔥 NUEVO MÉTODO: VERIFICAR PERMISOS POR ROL
    private boolean tienePermiso(HttpSession session, String requestURI) {
        String rol = (String) session.getAttribute("rol");
        
        if (rol == null) {
            return false;
        }

        // 🔐 DEFINIR QUÉ RUTAS PUEDE ACCEDER CADA ROL
        if (requestURI.startsWith("/configuracion") || 
            requestURI.startsWith("/vistas/personas") ||
            requestURI.equals("/personas")) {
            
            // Solo administradores pueden acceder a configuración y personas
            return "ADMINISTRADOR".equals(rol);
        }

        // Los otros módulos son accesibles para ambos roles
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