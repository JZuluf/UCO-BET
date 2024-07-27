package app;

import domain.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Sorteo> sorteos = new ArrayList<>();
    private static Historial historial = new Historial();
    private static Administrador admin = new Administrador("Admin", 30, "123456789", "admin123", 0);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        usuarios.add(admin);

        while (true) {
            verificarSorteos(); // Verifica si hay sorteos que deben realizarse
            mostrarHoraActual();

            System.out.println("1. Iniciar sesión como usuario");
            System.out.println("2. Iniciar sesión como administrador");
            System.out.println("3. Crear cuenta de usuario");
            System.out.println("4. Salir");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el newline

            switch (opcion) {
                case 1:
                    mostrarHoraActual();
                    iniciarSesionUsuario(scanner);
                    break;
                case 2:
                    mostrarHoraActual();
                    iniciarSesionAdministrador(scanner);
                    break;
                case 3:
                    mostrarHoraActual();
                    crearCuentaUsuario(scanner);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }

    private static void verificarSorteos() {
        for (Sorteo sorteo : sorteos) {
            if (sorteo.getFechaHora().isBefore(LocalDateTime.now()) && sorteo.getNumeroGanador() == null) {
                sorteo.realizarSorteo();
                mostrarResultadosSorteo(sorteo);
            }
        }
    }

    private static void mostrarResultadosSorteo(Sorteo sorteo) {
        System.out.println("Sorteo realizado el " + sorteo.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("Número ganador: " + sorteo.getNumeroGanador());

        for (Apuesta apuesta : sorteo.obtenerApuestas()) {
            if (apuesta.getNumero() == sorteo.getNumeroGanador()) {
                double ganancia = apuesta.calcularGanancia();
                System.out.println("Usuario " + apuesta.getUsuario().getNombre() + " apostó " + apuesta.getNumero() + " y ganó " + ganancia);
            }
        }

        double totalApuestas = sorteo.obtenerApuestas().stream().mapToDouble(Apuesta::getMonto).sum();
        double totalGanancias = sorteo.obtenerApuestas().stream().filter(a -> a.getNumero() == sorteo.getNumeroGanador()).mapToDouble(Apuesta::calcularGanancia).sum();
        double diferencia = totalGanancias - totalApuestas;
        System.out.println("Diferencia del bote: " + (diferencia > 0 ? "Positiva" : "Negativa") + " (" + diferencia + ")");
    }

    private static void iniciarSesionUsuario(Scanner scanner) {
        System.out.println("Ingrese su nombre:");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese su contraseña:");
        String contraseña = scanner.nextLine();
        Usuario usuario = autenticarUsuario(nombre, contraseña);
        if (usuario != null) {
            menuUsuario(scanner, usuario);
        } else {
            System.out.println("Credenciales incorrectas.");
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

    private static void crearCuentaUsuario(Scanner scanner) {
        System.out.println("Ingrese su nombre:");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese su edad:");
        int años = scanner.nextInt();
        scanner.nextLine(); // Consumir el newline
        System.out.println("Ingrese su teléfono:");
        String telefono = scanner.nextLine();
        System.out.println("Ingrese su contraseña:");
        String contraseña = scanner.nextLine();
        System.out.println("Ingrese su saldo inicial:");
        double saldo = scanner.nextDouble();
        scanner.nextLine(); // Consumir el newline

        Usuario nuevoUsuario = new Usuario(nombre, años, telefono, contraseña, saldo);
        usuarios.add(nuevoUsuario);
        System.out.println("Usuario creado: " + nuevoUsuario.getNombre());
    }

    private static void menuUsuario(Scanner scanner, Usuario usuario) {
        while (true) {
            System.out.println("1. Crear apuesta");
            System.out.println("2. Ver historial de sorteos");
            System.out.println("3. Ver saldo");
            System.out.println("4. Cerrar sesión");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el newline

            switch (opcion) {
                case 1:
                    mostrarHoraActual();
                    crearApuesta(scanner, usuario);
                    break;
                case 2:
                    mostrarHoraActual();
                    verHistorial();
                    break;
                case 3:
                    mostrarHoraActual();
                    System.out.println("Su saldo es: " + usuario.getSaldo());
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }

    private static void crearApuesta(Scanner scanner, Usuario usuario) {
        mostrarHoraActual(); // Muestra la hora actual

        System.out.println("Ingrese el número a apostar (1-4 dígitos):");
        int numero = scanner.nextInt();
        System.out.println("Ingrese el monto a apostar:");
        double monto = scanner.nextDouble();
        scanner.nextLine(); // Consumir el newline

        // Mostrar sorteos activos
        System.out.println("Sorteos activos:");
        List<Sorteo> sorteosActivos = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        for (int i = 0; i < sorteos.size(); i++) {
            Sorteo sorteo = sorteos.get(i);
            if (sorteo.getFechaHora().isAfter(ahora) && sorteo.getFechaHora().isBefore(ahora.plusMinutes(5))) {
                sorteosActivos.add(sorteo);
                System.out.println(i + ": Sorteo en " + sorteo.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        }

        if (sorteosActivos.isEmpty()) {
            System.out.println("No hay sorteos activos para esta apuesta.");
            return;
        }

        System.out.println("Seleccione el número del sorteo para apostar (ingrese el índice del sorteo):");
        int indice = scanner.nextInt();
        scanner.nextLine(); // Consumir el newline

        if (indice < 0 || indice >= sorteosActivos.size()) {
            System.out.println("Índice de sorteo no válido.");
            return;
        }

        Sorteo sorteoSeleccionado = sorteosActivos.get(indice);

        if (usuario.getSaldo() >= monto) {
            usuario.setSaldo(usuario.getSaldo() - monto);
            Apuesta apuesta = new Apuesta(usuario, numero, monto, ahora);
            sorteoSeleccionado.agregarApuesta(apuesta);
            System.out.println("Apuesta creada para " + usuario.getNombre());
        } else {
            System.out.println("Saldo insuficiente.");
        }
    }
    private static void mostrarHoraActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ahora = LocalDateTime.now();
        System.out.println("Hora actual: " + ahora.format(formatter));
    }



    private static void verHistorial() {
        System.out.println("Historial de sorteos:");
        for (Sorteo sorteo : historial.obtenerSorteos()) {
            System.out.println("Sorteo en: " + sorteo.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println("Número ganador: " + sorteo.getNumeroGanador());
            System.out.println("Apuestas:");
            for (Apuesta apuesta : sorteo.obtenerApuestas()) {
                System.out.println("Usuario: " + apuesta.getUsuario().getNombre() + ", Número: " + apuesta.getNumero() + ", Monto: " + apuesta.getMonto() + ", Ganancia: " + apuesta.calcularGanancia());
            }
        }
    }

    private static void iniciarSesionAdministrador(Scanner scanner) {
        System.out.println("Ingrese su nombre:");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese su contraseña:");
        String contraseña = scanner.nextLine();
        Administrador administrador = autenticarAdministrador(nombre, contraseña);
        if (administrador != null) {
            menuAdministrador(scanner, administrador);
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    private static Administrador autenticarAdministrador(String nombre, String contraseña) {
        if (admin.getNombre().equals(nombre) && admin.getContraseña().equals(contraseña)) {
            return admin;
        }
        return null;
    }

    private static void menuAdministrador(Scanner scanner, Administrador administrador) {
        while (true) {
            System.out.println("1. Programar sorteo");
            System.out.println("2. Ver historial de sorteos");
            System.out.println("3. Crear nuevo administrador");
            System.out.println("4. Cerrar sesión");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el newline

            switch (opcion) {
                case 1:
                    mostrarHoraActual();
                    programarSorteo(scanner, administrador);
                    break;
                case 2:
                    mostrarHoraActual();
                    verHistorialCompleto();
                    break;
                case 3:
                    mostrarHoraActual();
                    crearAdministrador(scanner, administrador);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }
    }

    private static void programarSorteo(Scanner scanner, Administrador administrador) {
        System.out.println("Ingrese la fecha y hora del sorteo (yyyy-MM-dd HH:mm):");
        String fechaHoraSorteo = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraSorteo, formatter);
        Sorteo sorteo = new Sorteo(fechaHora);
        sorteos.add(sorteo);
        historial.agregarSorteo(sorteo);
        System.out.println("Sorteo programado para " + fechaHora.format(formatter));
    }

    private static void verHistorialCompleto() {
        System.out.println("Historial completo de sorteos:");
        for (Sorteo sorteo : historial.obtenerSorteos()) {
            System.out.println("Sorteo en: " + sorteo.getFechaHora().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            System.out.println("Número ganador: " + sorteo.getNumeroGanador());
            System.out.println("Apuestas:");
            double totalApuestas = 0;
            double totalGanancias = 0;
            for (Apuesta apuesta : sorteo.obtenerApuestas()) {
                double ganancia = apuesta.calcularGanancia();
                totalApuestas += apuesta.getMonto();
                totalGanancias += ganancia;
                System.out.println("Usuario: " + apuesta.getUsuario().getNombre() + ", Número: " + apuesta.getNumero() + ", Monto: " + apuesta.getMonto() + ", Ganancia: " + ganancia);
            }
            double diferencia = totalGanancias - totalApuestas;
            System.out.println("Diferencia del bote: " + (diferencia > 0 ? "Positiva" : "Negativa") + " (" + diferencia + ")");
        }
    }

    private static void crearAdministrador(Scanner scanner, Administrador administrador) {
        System.out.println("Ingrese nombre del nuevo administrador:");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese años:");
        int años = scanner.nextInt();
        scanner.nextLine(); // Consumir el newline
        System.out.println("Ingrese teléfono:");
        String telefono = scanner.nextLine();
        System.out.println("Ingrese contraseña:");
        String contraseña = scanner.nextLine();
        Administrador nuevoAdmin = new Administrador(nombre, años, telefono, contraseña, 0);
        usuarios.add(nuevoAdmin);
        System.out.println("Nuevo administrador creado: " + nuevoAdmin.getNombre());
    }

}

