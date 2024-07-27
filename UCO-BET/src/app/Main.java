package app;

import domain.*;
import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Sorteo> sorteos = new ArrayList<>();
    private static Historial historial = new Historial();
    private static Administrador admin;

    public static void main(String[] args) {
        // Cargar usuarios desde archivo
        try {
            usuarios = ArchivoUtil.cargarUsuarios("usuarios.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crear administrador inicial si no existe
        if (usuarios.stream().noneMatch(u -> u instanceof Administrador)) {
            admin = new Administrador("admin", "0000000000", "admin123", 0);
            usuarios.add(admin);
        }

        while (true) {
            mostrarHoraActual();
            String[] opciones = {"Iniciar Sesión como Usuario", "Iniciar Sesión como Administrador", "Crear Cuenta de Usuario", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Uco-main",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

            switch (opcion) {
                case 0:
                    iniciarSesionUsuario();
                    break;
                case 1:
                    iniciarSesionAdministrador();
                    break;
                case 2:
                    crearUsuario();
                    break;
                case 3:
                    guardarUsuariosYSalir();
                    break;
                default:
                    break;
            }
        }
    }

    private static void iniciarSesionUsuario() {
        String nombre = JOptionPane.showInputDialog("Ingrese su nombre:");
        String contraseña = JOptionPane.showInputDialog("Ingrese su contraseña:");

        Usuario usuario = autenticarUsuario(nombre, contraseña);
        if (usuario != null && !(usuario instanceof Administrador)) {
            menuUsuario(usuario);
        } else {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas o el usuario es un administrador.");
        }
    }

    private static void iniciarSesionAdministrador() {
        String nombre = JOptionPane.showInputDialog("Ingrese su nombre:");
        String contraseña = JOptionPane.showInputDialog("Ingrese su contraseña:");

        Usuario usuario = autenticarUsuario(nombre, contraseña);
        if (usuario != null && usuario instanceof Administrador) {
            menuAdministrador((Administrador) usuario);
        } else {
            JOptionPane.showMessageDialog(null, "Credenciales incorrectas o el usuario no es un administrador.");
        }
    }

    private static Usuario autenticarUsuario(String nombre, String contraseña) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombre) && usuario.getContraseña().equals(contraseña)) {
                return usuario;
            }
        }
        return null;
    }

    private static void menuAdministrador(Administrador administrador) {
        while (true) {
            mostrarHoraActual();
            String[] opciones = {"Programar Sorteo", "Ver Historial de Sorteos", "Crear Administrador", "Cerrar Sesión"};
            int opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Administrador",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

            switch (opcion) {
                case 0:
                    programarSorteo();
                    break;
                case 1:
                    verHistorialCompleto();
                    break;
                case 2:
                    crearAdministrador();
                    break;
                case 3:
                    return;
                default:
                    break;
            }
        }
    }

    private static void menuUsuario(Usuario usuario) {
        while (true) {
            mostrarHoraActual();
            String[] opciones = {"Realizar Apuesta", "Ver Historial de Sorteos", "Cerrar Sesión"};
            int opcion = JOptionPane.showOptionDialog(null, "Seleccione una opción", "Usuario",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opciones, opciones[0]);

            switch (opcion) {
                case 0:
                    crearApuesta(usuario);
                    break;
                case 1:
                    verHistorialUsuario();
                    break;
                case 2:
                    return;
                default:
                    break;
            }
        }
    }

    private static void programarSorteo() {
        String fecha = JOptionPane.showInputDialog("Ingrese la fecha del sorteo (yyyy-MM-dd HH:mm):");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fechaHora = LocalDateTime.parse(fecha, formatter);
        Sorteo sorteo = new Sorteo(fechaHora);
        sorteos.add(sorteo);
        JOptionPane.showMessageDialog(null, "Sorteo programado para " + fechaHora);
    }

    private static void crearApuesta(Usuario usuario) {
        if (sorteos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sorteos disponibles.");
            return;
        }

        String mensajeSorteos = "Sorteos disponibles:\n";
        for (int i = 0; i < sorteos.size(); i++) {
            Sorteo sorteo = sorteos.get(i);
            if (!sorteo.isFinalizado() && LocalDateTime.now().isBefore(sorteo.getFechaHora().minusMinutes(5))) {
                mensajeSorteos += (i + 1) + ". Sorteo en " + sorteo.getFechaHora() + "\n";
            }
        }

        int opcionSorteo = Integer.parseInt(JOptionPane.showInputDialog(mensajeSorteos + "Seleccione un sorteo (número):"));
        Sorteo sorteoSeleccionado = sorteos.get(opcionSorteo - 1);

        int numero = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el número (1-9999):"));
        double monto = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el monto a apostar:"));

        Apuesta apuesta = new Apuesta(usuario, numero, monto);
        sorteoSeleccionado.agregarApuesta(apuesta);
        usuario.setSaldo(usuario.getSaldo() - monto);
        JOptionPane.showMessageDialog(null, "Apuesta realizada.");
    }

    private static void crearAdministrador() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del administrador:");
        String telefono = JOptionPane.showInputDialog("Ingrese el teléfono del administrador:");
        String contraseña = JOptionPane.showInputDialog("Ingrese la contraseña del administrador:");

        Administrador nuevoAdmin = new Administrador(nombre, telefono, contraseña, 0);
        usuarios.add(nuevoAdmin);
        JOptionPane.showMessageDialog(null, "Administrador creado.");
    }

    private static void crearUsuario() {
        String nombre = JOptionPane.showInputDialog("Ingrese su nombre:");
        String telefono = JOptionPane.showInputDialog("Ingrese su teléfono:");
        String contraseña = JOptionPane.showInputDialog("Ingrese su contraseña:");

        Usuario nuevoUsuario = new Usuario(nombre, telefono, contraseña, 1000); // Saldo inicial de 1000
        usuarios.add(nuevoUsuario);
        JOptionPane.showMessageDialog(null, "Usuario creado.");
    }

    private static void guardarUsuariosYSalir() {
        try {
            ArchivoUtil.guardarUsuarios("usuarios.txt", usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void mostrarHoraActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ahora = LocalDateTime.now();
        JOptionPane.showMessageDialog(null, "Hora actual: " + ahora.format(formatter));
    }

    private static void verHistorialCompleto() {
        StringBuilder historialTexto = new StringBuilder();
        for (Sorteo sorteo : historial.obtenerSorteos()) {
            historialTexto.append("Sorteo: ").append(sorteo.getFechaHora())
                    .append(", Número ganador: ").append(sorteo.getNumeroGanador())
                    .append("\n");
            for (Apuesta apuesta : sorteo.getApuestas()) {
                historialTexto.append("Usuario: ").append(apuesta.getUsuario().getNombre())
                        .append(", Número: ").append(apuesta.getNumero())
                        .append(", Monto: ").append(apuesta.getMonto())
                        .append(", Ganancia: ").append(apuesta.calcularGanancia())
                        .append("\n");
            }
            historialTexto.append("\n");
        }
        JOptionPane.showMessageDialog(null, historialTexto.toString());
    }

    private static void verHistorialUsuario() {
        StringBuilder historialTexto = new StringBuilder();
        for (Sorteo sorteo : historial.obtenerSorteos()) {
            historialTexto.append("Sorteo: ").append(sorteo.getFechaHora())
                    .append(", Número ganador: ").append(sorteo.getNumeroGanador())
                    .append("\n");
            for (Apuesta apuesta : sorteo.getApuestas()) {
                historialTexto.append("Usuario: ").append(apuesta.getUsuario().getNombre())
                        .append(", Número: ").append(apuesta.getNumero())
                        .append(", Monto: ").append(apuesta.getMonto())
                        .append(", Ganancia: ").append(apuesta.calcularGanancia())
                        .append("\n");
            }
            historialTexto.append("\n");
        }
        JOptionPane.showMessageDialog(null, historialTexto.toString());
    }
}
