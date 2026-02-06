package datos;

import model.Envio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnvioDAO {

    public boolean insertar(Envio e) {
        String sql = "INSERT INTO envio (direccion, id_pedido, fecha_entrega, estado) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getDireccion());
            ps.setInt(2, e.getIdPedido());
            ps.setDate(3, java.sql.Date.valueOf(e.getFechaEntrega()));
            ps.setString(4, e.getEstado());

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Error al crear envío: " + ex.getMessage());
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
                lista.add(new Envio(
                        rs.getInt("id_envio"),
                        rs.getString("direccion"),
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_entrega").toLocalDate(),
                        rs.getString("estado")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public boolean eliminar(int idEnvio) {
        String sql = "DELETE FROM envio WHERE id_envio = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEnvio);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Método para buscar por ID
    public Envio buscarPorId(int id) {
        String sql = "SELECT * FROM envio WHERE id_envio = ?";
        Envio env = null;
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    env = new Envio(
                            rs.getInt("id_envio"),
                            rs.getString("direccion"),
                            rs.getInt("id_pedido"),
                            rs.getDate("fecha_entrega").toLocalDate(),
                            rs.getString("estado")
                    );
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return env;
    }

    public boolean actualizar(Envio e) {
        String sql = "UPDATE envio SET direccion=?, id_pedido=?, fecha_entrega=?, estado=? WHERE id_envio=?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, e.getDireccion());
            ps.setInt(2, e.getIdPedido());
            ps.setDate(3, java.sql.Date.valueOf(e.getFechaEntrega()));
            ps.setString(4, e.getEstado());
            ps.setInt(5, e.getIdEnvio());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}