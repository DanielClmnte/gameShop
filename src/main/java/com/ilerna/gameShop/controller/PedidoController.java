package com.ilerna.gameShop.controller;

import com.ilerna.gameShop.model.Pedido;
import com.ilerna.gameShop.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
 * Controlador para el historial de pedidos del usuario.
 * Requiere sesión activa (login).
 * - GET /pedidos : Historial completo del usuario logueado
 */
@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private PedidoService pedidoService;

    public PedidoController() {
        this.pedidoService = new PedidoService();
    }

    @GetMapping
    public String mostrarHistorial(HttpSession session, Model model) {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/login?redirect=/pedidos";
        }

        List<Pedido> pedidos = pedidoService.obtenerPedidosDeUsuario(usuarioId);

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("titulo", "Mis Pedidos - GameShop");
        return "pedidos/historial";
    }
}

