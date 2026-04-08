package com.ilerna.gameShop.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Interceptor que protege /admin/**: solo usuarios con rol ADMIN pueden acceder.
 * Redirige a /login si no hay sesión activa, o a / si el rol no es ADMIN.
 */
public class AdminSecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (request.getRequestURI().startsWith("/admin")) {
            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("usuarioId") == null) {
                response.sendRedirect("/login?redirect=" + request.getRequestURI());
                return false;
            }

            // Solo rol ADMIN puede acceder
            String rol = (String) session.getAttribute("usuarioRol");
            if (!"ADMIN".equals(rol)) {
                response.sendRedirect("/?error=acceso-denegado");
                return false;
            }
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
