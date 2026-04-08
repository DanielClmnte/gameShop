package com.ilerna.gameShop.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Interceptor para proteger rutas del admin
 * Solo usuarios admin pueden acceder a /admin/**
 */
public class AdminSecurityInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        String requestURI = request.getRequestURI();
        
        // Si la ruta es /admin/**, verificar autenticación
        if (requestURI.startsWith("/admin")) {
            HttpSession session = request.getSession(false);
            
            // Verificar si hay sesión y si el usuario es admin
            if (session == null || session.getAttribute("usuarioId") == null) {
                // No hay sesión - redirigir a login
                response.sendRedirect("/login?error=no-autenticado");
                return false;
            }
            
            // Aquí se podría verificar el rol (admin/cliente)
            // Por ahora aceptamos cualquier usuario autenticado
        }
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) throws Exception {
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) throws Exception {
    }
}

