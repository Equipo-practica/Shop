package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mariadb://localhost:3306/SHOP";
    private static final String USER = "equipo";
    private static final String PASS = "equipo";

    private static Connection conexion = null;

    public static Connection getConexion() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASS);
            return conexion;

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: MariaDB Driver not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("ERROR: Could not connect to DB 'SHOP'.");
            e.printStackTrace();
            return null;
        }
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("DB Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}