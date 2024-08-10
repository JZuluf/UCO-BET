package app;

import domain.*;
import util.ArchivoUtil;
import util.ExcepcionesUcoBet;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private static final String ARCHIVO_SORTEOS = "sorteos.txt";

    public static void main(String[] args) {
        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorSorteos gestorSorteos = new GestorSorteos();
        try {
            gestorUsuarios.setUsuarios(ArchivoUtil.cargarUsuarios(ARCHIVO_USUARIOS));
            gestorSorteos.setSorteos(ArchivoUtil.cargarSorteos(ARCHIVO_SORTEOS));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        }

        // Verificación periódica de sorteos
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                verificarSorteos(gestorSorteos);
                try {
                    ArchivoUtil.guardarSorteos(ARCHIVO_SORTEOS, gestorSorteos.obtenerSorteos());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 60000); // Verificar cada minuto

        while (true) {
            String[] opciones = {"Iniciar sesión como usuario", "Iniciar sesión como administrador", "Crear cuenta de usuario", "Salir"};
            String opcion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción:", "UcoBet", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (opcion == null || opcion.equals("Salir")) {
                break;
            }

            switch (opcion) {
                case "Iniciar sesión como usuario":
                    iniciarSesionUsuario(gestorUsuarios, gestorSorteos);
                    break;
                case "Iniciar sesión como administrador":
                    iniciarSesionAdministrador(gestorUsuarios, gestorSorteos);
                    break;
                case "Crear cuenta de usuario":
                    crearCuentaUsuario(gestorUsuarios);
                    break;
            }
        }
    }

    private static void iniciarSesionUsuario(GestorUsuarios gestorUsuarios, GestorSorteos gestorSorteos) {
        String nombre = JOptionPane.showInputDialog("Nombre de usuario:");
        String contraseña = JOptionPane.showInputDialog("Contraseña:");
        Usuario usuario = gestorUsuarios.autenticarUsuario(nombre, contraseña);
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
            return;
        }

        while (true) {
            String[] opciones = {"Ver sorteos activos", "Ver historial de sorteos", "Hacer apuesta", "Cerrar sesión"};
            String opcion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción:", "Usuario: " + usuario.getNombre(), JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (opcion == null || opcion.equals("Cerrar sesión")) {
                break;
            }

            switch (opcion) {
                case "Ver sorteos activos":
                    verSorteosActivos(gestorSorteos);
                    break;
                case "Ver historial de sorteos":
                    verHistorialSorteos(gestorSorteos);
                    break;
                case "Hacer apuesta":
                    hacerApuesta(usuario, gestorSorteos);
                    try {
                        ArchivoUtil.guardarUsuarios(ARCHIVO_USUARIOS, gestorUsuarios.obtenerUsuarios());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error al guardar usuarios: " + e.getMessage());
                    }
                    break;
            }
        }
    }

    private static void iniciarSesionAdministrador(GestorUsuarios gestorUsuarios, GestorSorteos gestorSorteos) {
        String nombre = JOptionPane.showInputDialog("Nombre de administrador:");
        String contraseña = JOptionPane.showInputDialog("Contraseña:");
        Usuario administrador = gestorUsuarios.autenticarUsuario(nombre, contraseña);
        if (administrador == null || !administrador.isEsAdmin()) {
            JOptionPane.showMessageDialog(null, "Administrador o contraseña incorrectos.");
            return;
        }

        while (true) {
            String[] opciones = {"Crear sorteo", "Ver sorteos activos", "Ver historial de sorteos", "Crear nuevo administrador", "Recargar saldo a usuario", "Cerrar sesión"};
            String opcion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción:", "Administrador: " + administrador.getNombre(), JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

            if (opcion == null || opcion.equals("Cerrar sesión")) {
                break;
            }

            switch (opcion) {
                case "Crear sorteo":
                    crearSorteo(gestorSorteos);
                    try {
                        ArchivoUtil.guardarSorteos(ARCHIVO_SORTEOS, gestorSorteos.obtenerSorteos());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error al guardar sorteos: " + e.getMessage());
                    }
                    break;
                case "Ver sorteos activos":
                    verSorteosActivos(gestorSorteos);
                    break;
                case "Ver historial de sorteos":
                    verHistorialSorteos(gestorSorteos);
                    break;
                case "Crear nuevo administrador":
                    crearNuevoAdministrador(gestorUsuarios);
                    try {
                        ArchivoUtil.guardarUsuarios(ARCHIVO_USUARIOS, gestorUsuarios.obtenerUsuarios());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error al guardar usuarios: " + e.getMessage());
                    }
                    break;
                case "Recargar saldo a usuario":
                    recargarSaldoUsuario(gestorUsuarios);
                    try {
                        ArchivoUtil.guardarUsuarios(ARCHIVO_USUARIOS, gestorUsuarios.obtenerUsuarios());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error al guardar usuarios: " + e.getMessage());
                    }
                    break;
            }
        }
    }

    private static void crearCuentaUsuario(GestorUsuarios gestorUsuarios) {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");
        String contraseña = JOptionPane.showInputDialog("Contraseña:");
        String saldoStr = JOptionPane.showInputDialog("Monto inicial:");
        double saldo;
        try {
            saldo = Double.parseDouble(saldoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Monto inválido.");
            return;
        }

        try {
            gestorUsuarios.crearUsuario(nombre, telefono, contraseña, saldo);
            ArchivoUtil.guardarUsuarios(ARCHIVO_USUARIOS, gestorUsuarios.obtenerUsuarios());
            JOptionPane.showMessageDialog(null, "Usuario creado exitosamente.");
        } catch (ExcepcionesUcoBet | IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void hacerApuesta(Usuario usuario, GestorSorteos gestorSorteos) {
        List<Sorteo> sorteosActivos = gestorSorteos.obtenerSorteosActivos();
        if (sorteosActivos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sorteos activos.");
            return;
        }

        StringBuilder sb = new StringBuilder("Sorteos activos:\n");
        for (int i = 0; i < sorteosActivos.size(); i++) {
            Sorteo sorteo = sorteosActivos.get(i);
            sb.append(i).append(". ").append(sorteo.toStringSinNumeroGanador()).append("\n");
        }
        String seleccionStr = JOptionPane.showInputDialog(sb.toString() + "Seleccione un sorteo:");
        int seleccion;
        try {
            seleccion = Integer.parseInt(seleccionStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Selección inválida.");
            return;
        }

        if (seleccion < 0 || seleccion >= sorteosActivos.size()) {
            JOptionPane.showMessageDialog(null, "Selección inválida.");
            return;
        }

        Sorteo sorteoSeleccionado = sorteosActivos.get(seleccion);
        String numeroStr = JOptionPane.showInputDialog("Ingrese el número para apostar (1-9999):");
        int numero;
        try {
            numero = Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Número inválido.");
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

        if (usuario.getSaldo() < monto) {
            JOptionPane.showMessageDialog(null, "Saldo insuficiente.");
            return;
        }

        try {
            gestorSorteos.agregarApuesta(sorteoSeleccionado, usuario, numero, monto);
            usuario.setSaldo(usuario.getSaldo() - monto);
            JOptionPane.showMessageDialog(null, "Apuesta realizada exitosamente.");
        } catch (ExcepcionesUcoBet e) {
            JOptionPane.showMessageDialog(null, "Error al realizar la apuesta: " + e.getMessage());
        }
    }

    private static void crearSorteo(GestorSorteos gestorSorteos) {
        String fechaHoraStr = JOptionPane.showInputDialog("Ingrese la fecha y hora del sorteo (yyyy-MM-dd HH:mm):");
        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(fechaHoraStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fecha y hora inválidas.");
            return;
        }

        String numeroGanadorStr = JOptionPane.showInputDialog("Ingrese el número ganador:");
        int numeroGanador;
        try {
            numeroGanador = Integer.parseInt(numeroGanadorStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Número ganador inválido.");
            return;
        }

        gestorSorteos.crearSorteo(fechaHora, numeroGanador);
        JOptionPane.showMessageDialog(null, "Sorteo creado exitosamente.");
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
        List<Usuario> usuarios = gestorUsuarios.obtenerUsuarios();
        StringBuilder sb = new StringBuilder("Usuarios:\n");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            sb.append(i).append(". ").append(usuario.getNombre()).append(" - Saldo: ").append(usuario.getSaldo()).append("\n");
        }
        String seleccionStr = JOptionPane.showInputDialog(sb.toString() + "Seleccione un usuario:");
        int seleccion;
        try {
            seleccion = Integer.parseInt(seleccionStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Selección inválida.");
            return;
        }

        if (seleccion < 0 || seleccion >= usuarios.size()) {
            JOptionPane.showMessageDialog(null, "Selección inválida.");
            return;
        }

        Usuario usuarioSeleccionado = usuarios.get(seleccion);
        String montoStr = JOptionPane.showInputDialog("Ingrese el monto a recargar:");
        double monto;
        try {
            monto = Double.parseDouble(montoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Monto inválido.");
            return;
        }

        usuarioSeleccionado.setSaldo(usuarioSeleccionado.getSaldo() + monto);
        JOptionPane.showMessageDialog(null, "Saldo recargado exitosamente.");
    }

    private static void verSorteosActivos(GestorSorteos gestorSorteos) {
        List<Sorteo> sorteosActivos = gestorSorteos.obtenerSorteosActivos();
        if (sorteosActivos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sorteos activos.");
            return;
        }

        StringBuilder sb = new StringBuilder("Sorteos activos:\n");
        for (Sorteo sorteo : sorteosActivos) {
            sb.append(sorteo.toStringSinNumeroGanador()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void verHistorialSorteos(GestorSorteos gestorSorteos) {
        List<Sorteo> sorteos = gestorSorteos.obtenerSorteos();
        if (sorteos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sorteos en el historial.");
            return;
        }

        StringBuilder sb = new StringBuilder("Historial de sorteos:\n");
        for (Sorteo sorteo : sorteos) {
            if (!sorteo.estaActivo()) {
                sb.append(sorteo).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void verificarSorteos(GestorSorteos gestorSorteos) {
        List<Sorteo> sorteosActivos = gestorSorteos.obtenerSorteosActivos();
        LocalDateTime ahora = LocalDateTime.now();

        for (Sorteo sorteo : sorteosActivos) {
            if (sorteo.getFechaHora().isBefore(ahora)) {
                sorteo.setActivo(false);
                // Aquí podrías añadir la lógica para determinar los ganadores y mostrar un mensaje
                List<Apuesta> apuestas = sorteo.getApuestas();
                StringBuilder mensaje = new StringBuilder("Sorteo finalizado!\nNúmero ganador: " + sorteo.getNumeroGanador() + "\n");
                for (Apuesta apuesta : apuestas) {
                    int diferencia = Math.abs(sorteo.getNumeroGanador() - apuesta.getNumeroApostado());
                    int digitosGanadores = 0;
                    if (diferencia == 0) {
                        digitosGanadores = 4;
                    } else if (diferencia < 10) {
                        digitosGanadores = 1;
                    } else if (diferencia < 100) {
                        digitosGanadores = 2;
                    } else if (diferencia < 1000) {
                        digitosGanadores = 3;
                    }

                    double ganancia = 0;
                    switch (digitosGanadores) {
                        case 1:
                            ganancia = apuesta.getMonto() * 10;
                            break;
                        case 2:
                            ganancia = apuesta.getMonto() * 15;
                            break;
                        case 3:
                            ganancia = apuesta.getMonto() * 50;
                            break;
                        case 4:
                            ganancia = apuesta.getMonto() * 100;
                            break;
                    }

                    if (ganancia > 0) {
                        apuesta.getUsuario().setSaldo(apuesta.getUsuario().getSaldo() + ganancia);
                        mensaje.append("Ganador: ").append(apuesta.getUsuario().getNombre())
                                .append(" - Apuesta: ").append(apuesta.getNumeroApostado())
                                .append(" - Ganancia: ").append(ganancia).append("\n");
                    } else {
                        mensaje.append("Perdedor: ").append(apuesta.getUsuario().getNombre())
                                .append(" - Apuesta: ").append(apuesta.getNumeroApostado()).append("\n");
                    }
                }
                JOptionPane.showMessageDialog(null, mensaje.toString());
            }
        }
    }
}
