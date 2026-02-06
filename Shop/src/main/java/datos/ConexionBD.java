package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // AJUSTAR ESTOS VALORES A LA CONFIGURACIÓN DE DBEAVER/MARIADB
    private static final String URL = "jdbc:mariadb://localhost:3306/SHOP";
    private static final String USER = "equipo";
    private static final String PASSWORD = "equipo";

    private static Connection conexion = null;

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                // Cargar driver (opcional en versiones nuevas, pero recomendado por seguridad)
                Class.forName("org.mariadb.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(">>> Connected to MariaDB.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Critical error connecting to the database.");
            e.printStackTrace();
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println(">>> Disconnected.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}