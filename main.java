package programas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class main {
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args){
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Ingrese credenciales validas para acceder al sistema:\nUsuario: ");
            String username = sc.next();
            System.out.print("Contrasena: ");
            String password = sc.next();
            System.out.println();
            if (iniciarsesionsistema(username, password)){
                break;
            }
        }

        while (true) {
            int opcionescogida = menunavegacion();
            if(opcionescogida == 1){
                ingresarventa();
            }else if(opcionescogida == 2){
                registroproductos();
            }else if(opcionescogida == 3){
                aumentarstockproductos();
            }else if(opcionescogida == 4){
                mostrarmovimientos();
            }else if(opcionescogida == 5){
                mostrarproveedores();
            }else if(opcionescogida == 6){
                mostrarclientes();
            }else if(opcionescogida == 7){
                mostrarcierrecaja();
            }else if(opcionescogida == 8){
                break;
            }
        }
    }

    public static boolean aumentarstockproductos(){
        ArrayList<Integer> productoid = new ArrayList<Integer>();
        ArrayList<Integer> productocantidad = new ArrayList<Integer>();

        Scanner sc = new Scanner(System.in);
        modelo app = new modelo();

        boolean confirmarsalida = false;

        while (true) {
            List<ArrayList<Integer>> arrayprods = app.selectallproductos();

            System.out.println("\nCual producto de la lista vas a agregar stock?, escribe el ID\n");

            int idproducto = 0;

            boolean exitidproducto = false;
            while (true) {
                System.out.print("Id producto: ");
                idproducto = sc.nextInt();

                int count = 1;
                for(ArrayList obj:arrayprods){
                    ArrayList<Integer> temp = obj;
                    if (count == 1){
                        for(Integer job : temp){
                            if (job == idproducto){
                                exitidproducto = true;
                                break;
                            }
                        }
                    }
                    count++;
                }
                if (exitidproducto){
                    break;
                }else{
                    System.out.println("Producto no encontrado, intente nuevamente");
                }
            }

            Integer stockactual = arrayprods.get(1).get(idproducto-1);

            System.out.print("Cantidad producto: ");
            int cantidadproducto = sc.nextInt();

            productoid.add(idproducto);
            productocantidad.add(cantidadproducto);

            while (true) {
                System.out.println("\nDeseas agregar otro producto?, escribe el ID\n1) Si\n2) No\n");
                System.out.print("Opcion: ");
                int agregarproducto = sc.nextInt();

                if (agregarproducto == 1){
                    break;
                }else if(agregarproducto == 2){
                    confirmarsalida = true;
                    break;
                }else{
                    System.out.println("Opcion incorrecta, escriba la opcion correcta");
                }
            }

            if (confirmarsalida == true){
                for (int i = 0; i < productoid.size(); i++) {
                    idproducto = productoid.get(i);
                    cantidadproducto = productocantidad.get(i);
                    int tpmove = 1;

                    int newcantidad = stockactual + cantidadproducto;
                    nuevodatoslogmov(tpmove, idproducto, cantidadproducto);
                    actualizardatosproductos(idproducto, newcantidad);
                }

                break;
            }
        }

        return true;
    }

    public static boolean registroproductos(){
        System.out.println("\n\nPor favor escriba correctamente el nombre del producto:");
        Scanner sc = new Scanner(System.in);

        System.out.print("Nombre de producto: ");

        String nameproduct = sc.nextLine();

        System.out.println("Por favor escriba correctamente la categoria del producto:");
        System.out.print("Categoria de producto: ");

        String cateproduct = sc.nextLine();

        System.out.println("Ingrese el precio de la unidad del producto:");
        System.out.print("Precio: ");
        int precioproduct = sc.nextInt();

        System.out.println("Ingrese la cantidad de Stock inicial:");
        System.out.print("Cantidad: ");

        int cantiniproduct = sc.nextInt();

        modelo app = new modelo();
        app.insertnuevoproducto(nameproduct, cateproduct, precioproduct, cantiniproduct);

        int lastrow = app.getlastrowid();

        int tpmove = 1;

        int newlastrow = lastrow + 1;

        app.insertlogmov(tpmove, newlastrow, cantiniproduct);

        return true;
    }

    public static boolean mostrarcierrecaja(){
        modelo app = new modelo();
        app.selectcierrecaja();

        System.out.println("Presionar dos veces enter para salir");
        try{System.in.read();}
        catch(Exception e){}

        return true;
    }

    public static boolean mostrarmovimientos(){
        modelo app = new modelo();
        app.selectmovimientos();

        System.out.println("Presionar dos veces enter para salir");
        try{System.in.read();}
        catch(Exception e){}

        return true;
    }

    public static boolean mostrarproveedores(){
        modelo app = new modelo();
        app.selectproveedores();

        System.out.println("Presionar dos veces enter para salir");
        try{System.in.read();}
        catch(Exception e){}

        return true;
    }

    public static boolean mostrarclientes(){
        modelo app = new modelo();
        app.selectclientes();

        System.out.println("Presionar dos veces enter para salir");
        try{System.in.read();}
        catch(Exception e){}

        return true;
    }

    public static boolean iniciarsesionsistema(String username, String password){
        if (comprobarcredencialusuario(username)){
            if (comprobarcredencialcontrasena(password)){
                System.out.println("Bienvenido de vuelta Admin.");
                return true;
            }else{
                System.out.println("\n" + ANSI_YELLOW_BACKGROUND + "Usuario correcto pero la contrasena no es valida." + ANSI_RESET + "\n");
            }
        }else{
            System.out.println("\n" + ANSI_YELLOW_BACKGROUND + "Credenciales incorrectas, intente nuevamente." + ANSI_RESET + "\n");
        }
        return false;
    }

    public static boolean ingresarventa(){
        ArrayList<Integer> productoid = new ArrayList<Integer>();
        ArrayList<Integer> productocantidad = new ArrayList<Integer>();

        Scanner sc = new Scanner(System.in);
        modelo app = new modelo();

        boolean confirmarsalida = false;

        while (true) {
            List<ArrayList<Integer>> arrayprods = app.selectallproductos();

            System.out.println("\nCual producto de la lista vas a vender?, escribe el ID\n");

            int idproducto = 0;

            boolean exitidproducto = false;
            while (true) {
                System.out.print("Id producto: ");
                idproducto = sc.nextInt();

                int count = 1;
                for(ArrayList obj:arrayprods){
                    ArrayList<Integer> temp = obj;
                    if (count == 1){
                        for(Integer job : temp){
                            if (job == idproducto){
                                exitidproducto = true;
                                break;
                            }
                        }
                    }
                    count++;
                }
                if (exitidproducto){
                    break;
                }else{
                    System.out.println("Producto no encontrado, intente nuevamente");
                }
            }

            Integer stockactual = arrayprods.get(1).get(idproducto-1);

            System.out.print("Cantidad producto: ");
            int cantidadproducto = sc.nextInt();

            productoid.add(idproducto);
            productocantidad.add(cantidadproducto);

            while (true) {
                System.out.println("\nDeseas agregar otro producto?, escribe el ID\n1) Si\n2) No\n");
                System.out.print("Opcion: ");
                int agregarproducto = sc.nextInt();

                if (agregarproducto == 1){
                    break;
                }else if(agregarproducto == 2){
                    confirmarsalida = true;
                    break;
                }else{
                    System.out.println("Opcion incorrecta, escriba la opcion correcta");
                }
            }

            if (confirmarsalida == true){
                for (int i = 0; i < productoid.size(); i++) {
                    idproducto = productoid.get(i);
                    cantidadproducto = productocantidad.get(i);
                    int tpmove = 2;

                    int newcantidad = stockactual - cantidadproducto;
                    nuevodatoslogmov(tpmove, idproducto, cantidadproducto);
                    actualizardatosproductos(idproducto, newcantidad);
                }

                break;
            }
        }

        return false;
    }

    public static boolean nuevodatoslogmov(int tpmove, int idproducto, int cantidadproducto){
        modelo app = new modelo();
        app.insertlogmov(tpmove, idproducto, cantidadproducto);
        return true;
    }

    public static boolean actualizardatosproductos(int idproducto, int newcantidad){
        modelo app = new modelo();
        app.updateproductos(idproducto, newcantidad);
        return true;
    }

    public static int menunavegacion(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Por favor escriba la opcion a la cual desees ingresar");
        System.out.println("1) Ingresar una venta");
        System.out.println("2) Registrar nuevo producto");
        System.out.println("3) Aumentar stock de productos actuales");
        System.out.println("4) Registro de movimientos");
        System.out.println("5) Registro de proveedores");
        System.out.println("6) Registro de clientes");
        System.out.println("7) Cierre de caja");
        System.out.println("8) Cerrar sesion\n");

        System.out.print("Opcion: ");

        int idmenu = sc.nextInt();

        return idmenu;
    }

    public static boolean mostrardatostodoclientes(){
        modelo app = new modelo();
        app.selectallclientes();
        return true;
    }

    public static boolean nuevodatosclientes(){
        modelo app = new modelo();
        app.insertclientes("nom PRUEBANUEVA", "ape PRUEBANUEVA", false, "8888888888");
        return true;
    }

    public static boolean actualizardatosclientes(){
        modelo app = new modelo();
        app.updateclientes(4,"Jenessis", "Delgado", false, "55555555");
        return true;
    }

    public static boolean eliminardatos(){
        modelo app = new modelo();
        app.deleteclientes(5);
        return true;
    }

    public static boolean comprobarcredencialusuario(String username){
        modelo app = new modelo();
        return app.selectusuario(username);
    }

    public static boolean comprobarcredencialcontrasena(String password){
        modelo app = new modelo();
        return app.selectcontrasena(password);
    }

}

