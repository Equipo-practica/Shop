package datos;

import model.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDATOS {

    public boolean insertar(Pedido p) {
        String sql = "INSERT INTO pedido (fecha, importe, pagado, id_cliente) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(p.getFecha()));
            ps.setFloat(2, p.getImporte());
            ps.setBoolean(3, p.isPagado());
            ps.setInt(4, p.getIdCliente());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
            return false;
        }
    }

    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getFloat("importe"),
                        rs.getBoolean("pagado"),
                        rs.getInt("id_cliente")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean eliminar(int idPedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método UPDATE opcional (según necesites)
    public boolean actualizar(Pedido p) {
        String sql = "UPDATE pedido SET fecha=?, importe=?, pagado=?, id_cliente=? WHERE id_pedido=?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(p.getFecha()));
            ps.setFloat(2, p.getImporte());
            ps.setBoolean(3, p.isPagado());
            ps.setInt(4, p.getIdCliente());
            ps.setInt(5, p.getIdPedido());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}