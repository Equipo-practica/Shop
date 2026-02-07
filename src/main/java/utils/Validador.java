package utils;

import java.util.regex.Pattern;

public class Validador {

    //Expresión regular para Email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Expresión regular para Números Enteros Positivos (IDs)
    private static final Pattern ENTERO_PATTERN = Pattern.compile("^\\d+$");

    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean campoNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean esNumeroEntero(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        return ENTERO_PATTERN.matcher(texto).matches();
    }
}