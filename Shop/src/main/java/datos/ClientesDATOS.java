package datos;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientesDATOS {

    public boolean insertar(Cliente c) {
        String sql = "INSERT INTO cliente (id_cliente, nombre, email, vip, fecha_alta) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getIdCliente());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getEmail());
            ps.setBoolean(4, c.isVip());
            ps.setDate(5, Date.valueOf(c.getFechaAlta()));

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting client: " + e.getMessage());
            return false;
        }
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearCliente(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapearCliente(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean actualizar(Cliente c) {
        String sql = "UPDATE cliente SET nombre=?, email=?, vip=?, fecha_alta=? WHERE id_cliente=?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getEmail());
            ps.setBoolean(3, c.isVip());
            ps.setDate(4, Date.valueOf(c.getFechaAlta()));
            ps.setInt(5, c.getIdCliente());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getBoolean("vip"),
                rs.getDate("fecha_alta").toLocalDate()
        );
    }
}