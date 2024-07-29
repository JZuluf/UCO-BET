package app;

import domain.*;
import util.ArchivoUtil;
import util.ExcepcionesUcoBet;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static final String ARCHIVO_SORTEOS = "sorteos.txt";

    public static void main(String[] args) {
        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorSorteos gestorSorteos = new GestorSorteos();

        try {
            List<Usuario> usuarios = ArchivoUtil.cargarUsuarios(ARCHIVO_USUARIOS);
            if (usuarios.isEmpty()) {
                gestorUsuarios.crearAdministrador("admin", "admin123", "admin123");
                ArchivoUtil.guardarUsuarios(ARCHIVO_USUARIOS, gestorUsuarios.obtenerUsuarios());
            } else {
                gestorUsuarios.setUsuarios(usuarios);
            }
        } catch (IOException | ExcepcionesUcoBet e) {
            e.printStackTrace();
        }

        try {
            gestorSorteos.setSorteos(ArchivoUtil.cargarSorteos(ARCHIVO_SORTEOS));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mostrarMenuPrincipal(gestorUsuarios, gestorSorteos);
    }

    private static void mostrarMenuPrincipal(GestorUsuarios gestorUsuarios, GestorSorteos gestorSorteos) {
        while (true) {
            String opcion = JOptionPane.showInputDialog(null, "Hora actual: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n1. Iniciar sesión como usuario\n2. Iniciar sesión como administrador\n3. Crear cuenta de usuario\n4. Salir", "Menú principal", JOptionPane.PLAIN_MESSAGE);

            if (opcion == null || opcion.equals("4")) {
                break;
            }

            switch (opcion) {
                case "1":
                    iniciarSesionUsuario(gestorUsuarios, gestorSorteos);
                    break;
                case "2":
                    iniciarSesionAdministrador(gestorUsuarios, gestorSorteos);
                    break;
                case "3":
                    crearCuentaUsuario(gestorUsuarios);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida. Inténtelo de nuevo.");
            }
        }
    }

    private static void iniciarSesionUsuario(GestorUsuarios gestorUsuarios, GestorSorteos gestorSorteos) {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String contraseña = JOptionPane.showInputDialog("Contraseña:");

        Usuario usuario = gestorUsuarios.autenticarUsuario(nombre, contraseña);
        if (usuario == null || usuario.isEsAdmin()) {
            JOptionPane.showMessageDialog(null, "Credenciales inválidas o no tiene permisos de usuario.");
            return;
        }

        mostrarMenuUsuario(usuario, gestorSorteos);
    }

    private static void iniciarSesionAdministrador(GestorUsuarios gestorUsuarios, GestorSorteos gestorSorteos) {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String contraseña = JOptionPane.showInputDialog("Contraseña:");

        Usuario administrador = gestorUsuarios.autenticarUsuario(nombre, contraseña);
        if (administrador == null || !administrador.isEsAdmin()) {
            JOptionPane.showMessageDialog(null, "Credenciales inválidas o no tiene permisos de administrador.");
            return;
        }

        mostrarMenuAdministrador(gestorUsuarios, gestorSorteos, administrador);
    }

    private static void crearCuentaUsuario(GestorUsuarios gestorUsuarios) {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");
        String contraseña = JOptionPane.showInputDialog("Contraseña:");
        String saldoStr = JOptionPane.showInputDialog("Saldo inicial:");

        try {
            double saldoInicial = Double.parseDouble(saldoStr);
            gestorUsuarios.crearUsuario(nombre, telefono, contraseña, saldoInicial);
            ArchivoUtil.guardarUsuarios(ARCHIVO_USUARIOS, gestorUsuarios.obtenerUsuarios());
            JOptionPane.showMessageDialog(null, "Cuenta creada exitosamente.");
        } catch (ExcepcionesUcoBet | IOException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void mostrarMenuUsuario(Usuario usuario, GestorSorteos gestorSorteos) {
        while (true) {
            String opcion = JOptionPane.showInputDialog(null, "1. Ver sorteos activos\n2. Realizar apuesta\n3. Ver historial de sorteos\n4. Salir", "Menú de Usuario", JOptionPane.PLAIN_MESSAGE);

            if (opcion == null || opcion.equals("4")) {
                break;
            }

            switch (opcion) {
                case "1":
                    verSorteosActivos(gestorSorteos);
                    break;
                case "2":
                    realizarApuesta(usuario, gestorSorteos);
                    break;
                case "3":
                    verHistorialSorteos(gestorSorteos);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida. Inténtelo de nuevo.");
            }
        }
    }

    private static void mostrarMenuAdministrador(GestorUsuarios gestorUsuarios, GestorSorteos gestorSorteos, Usuario administrador) {
        while (true) {
            String opcion = JOptionPane.showInputDialog(null, "1. Crear sorteo\n2. Ver historial de sorteos\n3. Crear nuevo administrador\n4. Recargar saldo a usuario\n5. Salir", "Menú de Administrador", JOptionPane.PLAIN_MESSAGE);

            if (opcion == null || opcion.equals("5")) {
                break;
            }

            switch (opcion) {
                case "1":
                    crearSorteo(gestorSorteos);
                    break;
                case "2":
                    verHistorialSorteos(gestorSorteos);
                    break;
                case "3":
                    crearNuevoAdministrador(gestorUsuarios);
                    break;
                case "4":
                    recargarSaldoUsuario(gestorUsuarios);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida. Inténtelo de nuevo.");
            }
        }
    }

    private static void verSorteosActivos(GestorSorteos gestorSorteos) {
        List<Sorteo> sorteosActivos = gestorSorteos.obtenerSorteosActivos();
        if (sorteosActivos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sorteos activos.");
        } else {
            StringBuilder sb = new StringBuilder("Sorteos Activos:\n");
            for (Sorteo sorteo : sorteosActivos) {
                sb.append(sorteo.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    private static void realizarApuesta(Usuario usuario, GestorSorteos gestorSorteos) {
        List<Sorteo> sorteosActivos = gestorSorteos.obtenerSorteosActivos();
        if (sorteosActivos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sorteos activos.");
            return;
        }

        StringBuilder sb = new StringBuilder("Sorteos Activos:\n");
        for (int i = 0; i < sorteosActivos.size(); i++) {
            sb.append(i).append(". ").append(sorteosActivos.get(i).toString()).append("\n");
        }
        String sorteoSeleccionado = JOptionPane.showInputDialog(sb.toString() + "Seleccione un sorteo:");
        int indiceSorteo;
        try {
            indiceSorteo = Integer.parseInt(sorteoSeleccionado);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Selección inválida.");
            return;
        }

        if (indiceSorteo < 0 || indiceSorteo >= sorteosActivos.size()) {
            JOptionPane.showMessageDialog(null, "Selección inválida.");
            return;
        }

        Sorteo sorteo = sorteosActivos.get(indiceSorteo);
        String numeroStr = JOptionPane.showInputDialog("Ingrese el número a apostar (1-9999):");
        int numero;
        try {
            numero = Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Número inválido.");
            return;
        }

        if (numero < 1 || numero > 9999) {
            JOptionPane.showMessageDialog(null, "Número fuera de rango.");
            return;
        }

        String montoStr = JOptionPane.showInputDialog("Ingrese el monto a apostar:");
        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Monto inválido.");
            return;
        }

        try {
            gestorSorteos.realizarApuesta(usuario, sorteo, numero, monto);
            JOptionPane.showMessageDialog(null, "Apuesta realizada exitosamente.");
        } catch (ExcepcionesUcoBet e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void crearSorteo(GestorSorteos gestorSorteos) {
        String fechaHoraStr = JOptionPane.showInputDialog("Ingrese la fecha y hora del sorteo (yyyy-MM-dd HH:mm):");
        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(fechaHoraStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha y hora inválido.");
            return;
        }

        String numeroGanadorStr = JOptionPane.showInputDialog("Ingrese el número ganador (1-9999):");
        int numeroGanador;
        try {
            numeroGanador = Integer.parseInt(numeroGanadorStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Número ganador inválido.");
            return;
        }

        if (numeroGanador < 1 || numeroGanador > 9999) {
            JOptionPane.showMessageDialog(null, "Número ganador fuera de rango.");
            return;
        }

        try {
            gestorSorteos.crearSorteo(fechaHora, numeroGanador);
            ArchivoUtil.guardarSorteos(ARCHIVO_SORTEOS, gestorSorteos.obtenerSorteos());
            JOptionPane.showMessageDialog(null, "Sorteo creado exitosamente.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el sorteo.");
        } catch (ExcepcionesUcoBet e) {
            throw new RuntimeException(e);
        }
    }

    private static void crearNuevoAdministrador(GestorUsuarios gestorUsuarios) {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");
        String contraseña = JOptionPane.showInputDialog("Contraseña:");

        try {
            gestorUsuarios.crearAdministrador(nombre, telefono, contraseña);
            ArchivoUtil.guardarUsuarios(ARCHIVO_USUARIOS, gestorUsuarios.obtenerUsuarios());
            JOptionPane.showMessageDialog(null, "Administrador creado exitosamente.");
        } catch (ExcepcionesUcoBet | IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void recargarSaldoUsuario(GestorUsuarios gestorUsuarios) {
        String listaUsuarios = gestorUsuarios.obtenerListaUsuarios();
        String seleccion = JOptionPane.showInputDialog(listaUsuarios + "Seleccione el índice del usuario a recargar saldo:");
        int indice;
        try {
            indice = Integer.parseInt(seleccion);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Selección inválida.");
            return;
        }

        String montoStr = JOptionPane.showInputDialog("Ingrese el monto a recargar:");
        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Monto inválido.");
            return;
        }

        try {
            gestorUsuarios.recargarSaldo(indice, monto);
            ArchivoUtil.guardarUsuarios(ARCHIVO_USUARIOS, gestorUsuarios.obtenerUsuarios());
            JOptionPane.showMessageDialog(null, "Saldo recargado exitosamente.");
        } catch (ExcepcionesUcoBet | IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void verHistorialSorteos(GestorSorteos gestorSorteos) {
        List<Sorteo> historial = gestorSorteos.obtenerHistorialSorteos();
        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sorteos en el historial.");
        } else {
            StringBuilder sb = new StringBuilder("Historial de Sorteos:\n");
            for (Sorteo sorteo : historial) {
                sb.append(sorteo.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }
}
