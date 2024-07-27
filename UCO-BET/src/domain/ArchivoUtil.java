package domain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoUtil {

    public static List<Usuario> cargarUsuarios(String archivo) throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) { // Asegurarse de que haya 5 campos
                    String nombre = datos[0];
                    String telefono = datos[1];
                    String contraseña = datos[2];
                    double saldo = Double.parseDouble(datos[3]);
                    boolean esAdmin = Boolean.parseBoolean(datos[4]);
                    Usuario usuario = esAdmin ? new Administrador(nombre, telefono, contraseña, saldo) : new Usuario(nombre, telefono, contraseña, saldo);
                    usuarios.add(usuario);
                } else {
                    System.err.println("Formato de línea incorrecto: " + linea);
                }
            }
        }
        return usuarios;
    }

    public static void guardarUsuarios(String archivo, List<Usuario> usuarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Usuario usuario : usuarios) {
                bw.write(usuario.getNombre() + "," + usuario.getTelefono() + "," + usuario.getContraseña() + "," + usuario.getSaldo() + "," + (usuario instanceof Administrador));
                bw.newLine();
            }
        }
    }
}
