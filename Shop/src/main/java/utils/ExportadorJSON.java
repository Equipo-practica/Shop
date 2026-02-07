package utils;

import model.Cliente;
import model.Envio;
import model.Pedido;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportadorJSON {
    // Exportar JSON Clientes
    public static boolean exportarClientes(List<Cliente> clientes, String ruta) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            String jsonObject = String.format(
                    "  {\n    \"idCliente\": %d,\n    \"nombre\": \"%s\",\n    \"email\": \"%s\",\n    \"vip\": %b,\n    \"fechaAlta\": \"%s\"\n  }",
                    c.getIdCliente(), escaparTexto(c.getNombre()), escaparTexto(c.getEmail()), c.isVip(), c.getFechaAlta());
            jsonBuilder.append(jsonObject);
            if (i < clientes.size() - 1) jsonBuilder.append(",\n");
        }
        jsonBuilder.append("\n]");
        return escribirArchivo(ruta, jsonBuilder.toString());
    }
    // Exportar JSON Pedidos
    public static boolean exportarPedidos(List<Pedido> pedidos, String ruta) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            String jsonObject = String.format(
                    "  {\n    \"idPedido\": %d,\n    \"fecha\": \"%s\",\n    \"importe\": %.2f,\n    \"pagado\": %b,\n    \"idCliente\": %d\n  }",
                    p.getIdPedido(), p.getFecha(), p.getImporte(), p.isPagado(), p.getIdCliente());
            jsonBuilder.append(jsonObject);
            if (i < pedidos.size() - 1) jsonBuilder.append(",\n");
        }
        jsonBuilder.append("\n]");
        return escribirArchivo(ruta, jsonBuilder.toString());
    }
    // Exportar JSON Envios
    public static boolean exportarEnvios(List<Envio> envios, String ruta) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");

        for (int i = 0; i < envios.size(); i++) {
            Envio e = envios.get(i);

            // Protección contra Fecha Nula en el JSON
            String fechaStr = (e.getFechaEntrega() != null) ? e.getFechaEntrega().toString() : "null";

            String jsonObject = String.format(
                    "  {\n    \"idEnvio\": %d,\n    \"direccion\": \"%s\",\n    \"idPedido\": %d,\n    \"fechaEntrega\": \"%s\",\n    \"estado\": \"%s\"\n  }",
                    e.getIdEnvio(), escaparTexto(e.getDireccion()), e.getIdPedido(), fechaStr, escaparTexto(e.getEstado()));

            jsonBuilder.append(jsonObject);
            if (i < envios.size() - 1) jsonBuilder.append(",\n");
        }
        jsonBuilder.append("\n]");
        return escribirArchivo(ruta, jsonBuilder.toString());
    }

    // Métodos auxiliares
    private static boolean escribirArchivo(String ruta, String contenido) {
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write(contenido);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String escaparTexto(String texto) {
        if (texto == null) return "";
        return texto.replace("\"", "\\\"");
    }
}