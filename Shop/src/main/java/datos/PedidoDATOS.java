package datos;

import model.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDATOS {

    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido";
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing orders: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public List<Pedido> listarPorCliente(int idCliente) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido WHERE id_cliente = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insertar(Pedido p) {
        String sql = "INSERT INTO pedido (id_pedido, fecha, importe, pagado, id_cliente) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getIdPedido());
            ps.setDate(2, Date.valueOf(p.getFecha()));
            ps.setFloat(3, p.getImporte());
            ps.setString(4, p.isPagado());
            ps.setInt(5, p.getIdCliente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting order: " + e.getMessage());
            return false;
        }
    }

    public Pedido buscarPorId(int id) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPedido(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error searching order: " + e.getMessage());
        }
        return null;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting order: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Pedido p) {
        String sql = "UPDATE pedido SET fecha=?, importe=?, pagado=?, id_cliente=? WHERE id_pedido=?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(p.getFecha()));
            ps.setFloat(2, p.getImporte());
            ps.setString(3, p.isPagado());
            ps.setInt(4, p.getIdCliente());
            ps.setInt(5, p.getIdPedido());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            return false;
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        return new Pedido(
                rs.getInt("id_pedido"),
                rs.getDate("fecha").toLocalDate(),
                rs.getFloat("importe"),
                rs.getBoolean("pagado"),
                rs.getInt("id_cliente")
        );
    }
}