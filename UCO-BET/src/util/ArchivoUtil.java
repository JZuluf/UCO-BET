package util;

import domain.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArchivoUtil {
    public static List<Usuario> cargarUsuarios(String archivo) throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    boolean esAdmin = Boolean.parseBoolean(datos[4]);
                    usuarios.add(new Usuario(datos[0], datos[1], datos[2], Double.parseDouble(datos[3]), esAdmin));
                }
            }
        }
        return usuarios;
    }

    public static void guardarUsuarios(String archivo, List<Usuario> usuarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Usuario usuario : usuarios) {
                bw.write(String.join(",", usuario.getNombre(), usuario.getTelefono(), usuario.getContraseña(), String.valueOf(usuario.getSaldo()), String.valueOf(usuario.isEsAdmin())));
                bw.newLine();
            }
        }
    }

    public static List<Sorteo> cargarSorteos(String archivo) throws IOException {
        List<Sorteo> sorteos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                LocalDateTime fechaHora = LocalDateTime.parse(datos[0]);
                int numeroGanador = Integer.parseInt(datos[1]);
                boolean activo = Boolean.parseBoolean(datos[2]);
                sorteos.add(new Sorteo(fechaHora, numeroGanador, activo));
            }
        }
        return sorteos;
    }

    public static void guardarSorteos(String archivo, List<Sorteo> sorteos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            for (Sorteo sorteo : sorteos) {
                bw.write(String.join(",", sorteo.getFechaHora().toString(), String.valueOf(sorteo.getNumeroGanador()), String.valueOf(sorteo.estaActivo())));
                bw.newLine();
            }
        }
    }
}
