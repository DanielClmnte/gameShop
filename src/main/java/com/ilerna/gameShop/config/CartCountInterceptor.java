package com.ilerna.gameShop.config;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.service.CarritoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Interceptor que inyecta el conteo de productos del carrito
 * en el modelo de TODAS las peticiones, para que el badge
 * del header siempre muestre la cantidad correcta.
 * - Logueados: cuenta desde BD
 * - Anónimos: cuenta desde carritoSesion en HttpSession
 */
public class CartCountInterceptor implements HandlerInterceptor {

    private CarritoService carritoService = new CarritoService();

    @Override
    @SuppressWarnings("unchecked")
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null) return;

        HttpSession session = request.getSession(false);
        if (session == null) return;

        int count = 0;
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");

        if (usuarioId != null) {
            // Usuario logueado → contar desde BD
            count = carritoService.obtenerCantidadTotal(usuarioId);
        } else {
            // Anónimo → contar desde sesión
            List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carritoSesion");
            if (carrito != null) {
                for (CarritoItem item : carrito) {
                    count += item.getCantidad();
                }
            }
        }

        modelAndView.addObject("carritoCount", count);
    }
}
