package datos;

import model.Envio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnvioDATOS {

    public boolean insertar(Envio e) {
        String sql = "INSERT INTO envio (id_envio, direccion, id_pedido, fecha_entrega, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, e.getIdEnvio());
            ps.setString(2, e.getDireccion());
            ps.setInt(3, e.getIdPedido());

            if (e.getFechaEntrega() != null) {
                ps.setDate(4, Date.valueOf(e.getFechaEntrega()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setString(5, e.getEstado());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error inserting shipment: " + ex.getMessage());
            return false;
        }
    }

    public List<Envio> listarTodos() {
        List<Envio> lista = new ArrayList<>();
        String sql = "SELECT * FROM envio";
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearEnvio(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public List<Envio> listarPorPedido(int idPedido) {
        List<Envio> lista = new ArrayList<>();
        String sql = "SELECT * FROM envio WHERE id_pedido = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearEnvio(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public Envio buscarPorId(int id) {
        String sql = "SELECT * FROM envio WHERE id_envio = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEnvio(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean actualizar(Envio e) {
        String sql = "UPDATE envio SET direccion=?, id_pedido=?, fecha_entrega=?, estado=? WHERE id_envio=?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getDireccion());
            ps.setInt(2, e.getIdPedido());

            if (e.getFechaEntrega() != null) {
                ps.setDate(3, Date.valueOf(e.getFechaEntrega()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setString(4, e.getEstado());
            ps.setInt(5, e.getIdEnvio());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    public boolean eliminar(int idEnvio) {
        String sql = "DELETE FROM envio WHERE id_envio = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEnvio);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            return false;
        }
    }

    private Envio mapearEnvio(ResultSet rs) throws SQLException {
        Date fechaSQL = rs.getDate("fecha_entrega");
        java.time.LocalDate fechaLocal = (fechaSQL != null) ? fechaSQL.toLocalDate() : null;

        return new Envio(
                rs.getInt("id_envio"),
                rs.getString("direccion"),
                rs.getInt("id_pedido"),
                fechaLocal,
                rs.getString("estado")
        );
    }
}