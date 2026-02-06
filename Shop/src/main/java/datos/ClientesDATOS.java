package datos;

import controller.Launcher;
import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientesDATOS {

    // CREATE
    public static boolean insertar(Cliente c) {
        String sql = "INSERT INTO cliente (nombre, cliente, email, vip, fecha_alta) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Launcher.ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setInt(2, c.getIdCliente());
            ps.setString(3, c.getEmail());
            ps.setBoolean(4, c.isVip());
            ps.setDate(5, Date.valueOf(c.getFechaAlta()));

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding the client: " + e.getMessage());
            return false;
        }
    }

    // READ (Buscar por ID)
    public static Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        Cliente c = null;

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    // READ (Listar todos - Para TableView)
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection con = ConexionBD.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // UPDATE
    public static boolean actualizar(Cliente c) {
        String sql = "UPDATE cliente SET nombre=?, cliente=?, email=?, vip=?, fecha_alta=? WHERE id_cliente=?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(2, c.getNombre());
            ps.setString(3, c.getEmail());
            ps.setBoolean(5, c.isVip());
            ps.setDate(4, Date.valueOf(c.getFechaAlta()));
            ps.setInt(1, c.getIdCliente());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public static boolean eliminar(int id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Impossible to delete (you cannot delete clients with orders): " + e.getMessage());
            return false;
        }
    }
    // Metodo para no repetir codigo al leer ResultSet
        private static Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id_cliente"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getBoolean("vip"),
                rs.getDate("fecha_alta").toLocalDate()
        );
    }
}