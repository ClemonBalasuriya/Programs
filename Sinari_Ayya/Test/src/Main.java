import com.sun.jdi.connect.spi.Connection;

import java.sql.DriverManager;
import java.sql.Connection;




public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String url = "jdbc:mysql://localhost:1401/Sinari_Ayya-postgres";
        String username = "postgres";
        String password = "Sinari2000";
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
        }catch (Exception e){
            System.out.println("Error.");
        }


    }
}