package com.ilerna.gameShop.repository;

import com.ilerna.gameShop.model.CarritoItem;
import com.ilerna.gameShop.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar pedidos.
 * Consultas SQL contra las tablas 'pedidos' y 'detalle_pedidos' de gameshop_db.
 */
public class PedidoRepository implements IPedidoRepository {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    /**
     * Guarda un nuevo pedido y sus detalles (items).
     * Usa transacción para garantizar que ambos se guardan o ninguno.
     */
    public Pedido guardar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (numero_pedido, usuario_id, nombre_completo, email, "
                         + "direccion, ciudad, codigo_postal, subtotal, impuestos, total_con_impuestos, "
                         + "estado, fecha_entrega_estimada) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlDetalle = "INSERT INTO detalle_pedidos (pedido_id, videojuego_id, titulo_videojuego, "
                          + "imagen_videojuego, cantidad, precio_unitario) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);

            // 1. Insertar pedido
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, pedido.getNumeroPedido());
                pstmt.setInt(2, pedido.getUsuarioId());
                pstmt.setString(3, pedido.getNombreCompleto());
                pstmt.setString(4, pedido.getEmail());
                pstmt.setString(5, pedido.getDireccion());
                pstmt.setString(6, pedido.getCiudad());
                pstmt.setString(7, pedido.getCodigoPostal());
                pstmt.setDouble(8, pedido.getSubtotal());
                pstmt.setDouble(9, pedido.getImpuestos());
                pstmt.setDouble(10, pedido.getTotalConImpuestos());
                pstmt.setString(11, pedido.getEstado() != null ? pedido.getEstado() : "COMPLETADO");
                pstmt.setDate(12, pedido.getFechaEntregaEstimada() != null
                        ? Date.valueOf(pedido.getFechaEntregaEstimada()) : null);
                pstmt.executeUpdate();

                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        pedido.setId(keys.getInt(1));
                    }
                }
            }

            // 2. Insertar detalles del pedido (items)
            if (pedido.getItems() != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlDetalle)) {
                    for (CarritoItem item : pedido.getItems()) {
                        pstmt.setInt(1, pedido.getId());
                        pstmt.setInt(2, item.getVideojuegoId());
                        pstmt.setString(3, item.getTituloVideojuego());
                        pstmt.setString(4, item.getImagenVideojuego());
                        pstmt.setInt(5, item.getCantidad());
                        pstmt.setDouble(6, item.getPrecioUnitario());
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                }
            }

            conn.commit();
            System.out.println("✅ Pedido #" + pedido.getNumeroPedido() + " guardado correctamente.");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("❌ Error al guardar pedido.");
            e.printStackTrace();
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
        return pedido;
    }

    /**
     * Obtiene todos los pedidos de un usuario (con sus items).
     */
    public List<Pedido> obtenerPorUsuario(int usuarioId) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE usuario_id = ? ORDER BY fecha_pedido DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = mapearPedido(rs);
                    pedido.setItems(obtenerDetallesPedido(pedido.getId()));
                    pedidos.add(pedido);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener pedidos del usuario.");
            e.printStackTrace();
        }
        return pedidos;
    }

    /**
     * Obtiene un pedido por su número de pedido.
     */
    public Optional<Pedido> obtenerPorNumeroPedido(int numeroPedido) {
        String sql = "SELECT * FROM pedidos WHERE numero_pedido = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, numeroPedido);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = mapearPedido(rs);
                    pedido.setItems(obtenerDetallesPedido(pedido.getId()));
                    return Optional.of(pedido);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener pedido por número.");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Obtiene todos los pedidos (para el panel admin).
     */
    public List<Pedido> obtenerTodos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos ORDER BY fecha_pedido DESC";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = mapearPedido(rs);
                pedido.setItems(obtenerDetallesPedido(pedido.getId()));
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar todos los pedidos.");
            e.printStackTrace();
        }
        return pedidos;
    }

    // ── Obtener los items (detalles) de un pedido ──
    private List<CarritoItem> obtenerDetallesPedido(int pedidoId) {
        List<CarritoItem> items = new ArrayList<>();
        String sql = "SELECT * FROM detalle_pedidos WHERE pedido_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, pedidoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CarritoItem item = new CarritoItem();
                    item.setVideojuegoId(rs.getInt("videojuego_id"));
                    item.setTituloVideojuego(rs.getString("titulo_videojuego"));
                    item.setImagenVideojuego(rs.getString("imagen_videojuego"));
                    item.setCantidad(rs.getInt("cantidad"));
                    item.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    items.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener detalles del pedido.");
            e.printStackTrace();
        }
        return items;
    }

    // ── Mapear ResultSet a Pedido ──
    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setNumeroPedido(rs.getInt("numero_pedido"));
        p.setUsuarioId(rs.getInt("usuario_id"));
        p.setNombreCompleto(rs.getString("nombre_completo"));
        p.setEmail(rs.getString("email"));
        p.setDireccion(rs.getString("direccion"));
        p.setCiudad(rs.getString("ciudad"));
        p.setCodigoPostal(rs.getString("codigo_postal"));
        p.setSubtotal(rs.getDouble("subtotal"));
        p.setImpuestos(rs.getDouble("impuestos"));
        p.setTotalConImpuestos(rs.getDouble("total_con_impuestos"));
        p.setEstado(rs.getString("estado"));
        Timestamp fechaPedido = rs.getTimestamp("fecha_pedido");
        p.setFechaPedido(fechaPedido != null ? fechaPedido.toLocalDateTime().toLocalDate() : null);
        Date fechaEntrega = rs.getDate("fecha_entrega_estimada");
        p.setFechaEntregaEstimada(fechaEntrega != null ? fechaEntrega.toLocalDate() : null);
        return p;
    }
}
