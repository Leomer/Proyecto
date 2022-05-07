package programas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class modelo {

    private Connection connect() {
        String url = "jdbc:sqlite:C:/sqlite/db/DBmrnuez.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void selectallclientes(){
        String sql = "SELECT id, nombres, apellidos, sexo, telefono, fechacreacion FROM Clientes";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("nombres") + "\t" +
                        rs.getString("apellidos") + "\t" +
                        rs.getBoolean("sexo") + "\t" +
                        rs.getString("telefono") + "\t" +
                        rs.getString("fechacreacion"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectmovimientos(){
        String sql = "SELECT pro.nombre , his.tipo, his.stock, pro.precio, his.stock * pro.precio as montototal FROM Historialmovimientos his LEFT JOIN Productos pro ON his.productoid = pro.id;";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            System.out.println("\n|    Nombre    ||  Tipo  || Stock ||  Total  |");
            System.out.println("--------------------------------------------------");
            while (rs.next()) {
                String tpmov = "-";
                String nombreproducto = rs.getString("nombre");
                int tipomovimiento = rs.getInt("tipo");
                int stockproducto = rs.getInt("stock");
                int precioproducto = rs.getInt("precio");
                String montototal = rs.getString("montototal");

                if (tipomovimiento == 1){
                    tpmov = "compra";
                    montototal = "-";
                }else if (tipomovimiento == 2){
                    tpmov = "venta";
                    montototal = "S/" + montototal;
                }

                System.out.println("| " + String.format("%-13s", nombreproducto) +
                        "|| " + String.format("%-7s", tpmov) +
                        "|| " + String.format("%-6s", stockproducto) +
                        "|| " + String.format("%-8s", montototal) +
                        "|");
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectproveedores(){
        String sql = "SELECT empresa, nombres, apellidos, sexo, telefono FROM Proveedores";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            System.out.println("\n|    Empresa    ||  Nombre  ||  Apellido  || Sexo ||  Telefono  |");
            System.out.println("---------------------------------------------------------------------");
            while (rs.next()) {
                String sexo = "-";
                String empresa = rs.getString("empresa");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                Boolean tpsexo = rs.getBoolean("sexo");
                String telefono = rs.getString("telefono");

                if (tpsexo){
                    sexo = "M";
                }else{
                    sexo = "F";
                }

                System.out.println("| " + String.format("%-14s", empresa) +
                        "|| " + String.format("%-9s", nombres) +
                        "|| " + String.format("%-11s", apellidos) +
                        "|| " + String.format("%-5s", sexo) +
                        "|| " + String.format("%-11s", telefono) +
                        "|");
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectclientes(){
        String sql = "SELECT nombres, apellidos, sexo, telefono FROM Clientes";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            System.out.println("\n||  Nombre  ||  Apellido  || Sexo ||  Telefono  |");
            System.out.println("-----------------------------------------------------");
            while (rs.next()) {
                String sexo = "-";
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                Boolean tpsexo = rs.getBoolean("sexo");
                String telefono = rs.getString("telefono");

                if (tpsexo){
                    sexo = "M";
                }else{
                    sexo = "F";
                }

                System.out.println("| " + String.format("%-10s", nombres) +
                        "|| " + String.format("%-11s", apellidos) +
                        "|| " + String.format("%-5s", sexo) +
                        "|| " + String.format("%-11s", telefono) +
                        "|");
            }
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<ArrayList<Integer>> selectallproductos(){
        List<ArrayList<Integer>> arrayprods = new ArrayList<>();

        ArrayList<Integer> arrayidpro = new ArrayList<Integer>();
        ArrayList<Integer> arraycntpro = new ArrayList<Integer>();

        String sql = "SELECT id, nombre, stock, precio, fechaingreso FROM productos";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            System.out.println("\n| ID ||    Nombre    || Precio c/u || Stock |");
            System.out.println("---------------------------------------------");
            int count = 1;
            while (rs.next()) {
                int idproducto = rs.getInt("id");
                String nombreproducto = rs.getString("nombre");
                int stockproducto = rs.getInt("stock");
                int precioproducto = rs.getInt("precio");

                arrayidpro.add(idproducto);
                arraycntpro.add(stockproducto);

                System.out.println("| " + String.format("%-3s", idproducto) +
                        "|| " + String.format("%-13s", nombreproducto) +
                        "|| S/" + String.format("%-9s", precioproducto) +
                        "|| " + String.format("%-6s", stockproducto) +
                        "|");
                count ++;
            }
            arrayprods.add(arrayidpro);
            arrayprods.add(arraycntpro);
            return arrayprods;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean selectusuario(String username) {
        String sql = "SELECT username FROM Usuarios WHERE username = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,username);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean selectcontrasena(String password) {
        String sql = "SELECT username FROM Usuarios WHERE password = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,password);
            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void insertclientes(String nombre, String apellido, boolean sexo, String telefono) {
        String sql = "INSERT INTO Clientes(nombres,apellidos,sexo,telefono,fechacreacion) VALUES(?,?,?,?,datetime(CURRENT_TIMESTAMP, 'localtime'))";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setBoolean(3, sexo);
            pstmt.setString(4, telefono);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void insertlogmov(int tpmove, int idproducto, int cantidadproducto) {
        String sql = "INSERT INTO Historialmovimientos(tipo, productoid, stock, fechamodificacion) VALUES(?,?,?,datetime(CURRENT_TIMESTAMP, 'localtime'))";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tpmove);
            pstmt.setInt(2, idproducto);
            pstmt.setInt(3, cantidadproducto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateclientes(int id, String nombre, String apellido, boolean sexo, String telefono) {
        String sql = "UPDATE Clientes SET nombres = ?, apellidos = ?, sexo = ?, telefono = ? WHERE id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setBoolean(3, sexo);
            pstmt.setString(4, telefono);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateproductos(int idproducto, int newcantidad) {
        String sql = "UPDATE Productos SET stock = ? WHERE id = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newcantidad);
            pstmt.setInt(2, idproducto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteclientes(int id) {
        String sql = "DELETE FROM Clientes WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}