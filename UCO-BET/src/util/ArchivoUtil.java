package util;

import domain.Sorteo;
import domain.Usuario;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArchivoUtil {
    public static List<Usuario> cargarUsuarios(String rutaArchivo) throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return usuarios;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String nombre = partes[0];
                String telefono = partes[1];
                String contraseña = partes[2];
                double saldo = Double.parseDouble(partes[3]);
                boolean esAdmin = Boolean.parseBoolean(partes[4]);
                usuarios.add(new Usuario(nombre, telefono, contraseña, saldo, esAdmin));
            }
        }
        return usuarios;
    }

    public static void guardarUsuarios(String rutaArchivo, List<Usuario> usuarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Usuario usuario : usuarios) {
                bw.write(usuario.getNombre() + "," + usuario.getTelefono() + "," + usuario.getContraseña() + "," + usuario.getSaldo() + "," + usuario.isEsAdmin() + "\n");
            }
        }
    }

    public static List<Sorteo> cargarSorteos(String rutaArchivo) throws IOException {
        List<Sorteo> sorteos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return sorteos;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                LocalDateTime fechaHora = LocalDateTime.parse(partes[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                int numeroGanador = Integer.parseInt(partes[1]);
                boolean activo = Boolean.parseBoolean(partes[2]);
                sorteos.add(new Sorteo(fechaHora, numeroGanador, activo));
            }
        }
        return sorteos;
    }

    public static void guardarSorteos(String rutaArchivo, List<Sorteo> sorteos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Sorteo sorteo : sorteos) {
                bw.write(sorteo.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "," + sorteo.getNumeroGanador() + "," + sorteo.estaActivo() + "\n");
            }
        }
    }
}
