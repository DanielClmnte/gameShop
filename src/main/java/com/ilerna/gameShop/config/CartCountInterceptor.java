package com.ilerna.gameShop.config;

import com.ilerna.gameShop.service.CarritoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Interceptor que inyecta el conteo de productos del carrito
 * en el modelo de TODAS las peticiones, para que el badge
 * del header siempre muestre la cantidad correcta.
 */
public class CartCountInterceptor implements HandlerInterceptor {

    private CarritoService carritoService = new CarritoService();

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) return;

        HttpSession session = request.getSession(false);
        if (session == null) return;

        int usuarioId = obtenerUsuarioId(session);
        int count = carritoService.obtenerCantidadTotal(usuarioId);
        modelAndView.addObject("carritoCount", count);
    }

    private int obtenerUsuarioId(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("usuarioId");
        if (uid != null) return uid;

        Integer anonId = (Integer) session.getAttribute("anonCarritoId");
        if (anonId == null) {
            anonId = -(Math.abs(session.getId().hashCode() % 100_000) + 1);
            session.setAttribute("anonCarritoId", anonId);
        }
        return anonId;
    }
}

