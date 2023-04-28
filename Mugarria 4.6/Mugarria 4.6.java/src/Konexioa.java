import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Konexioa {
    public Connection konexioa;
    public Statement sententzia;

    private String erabiltzailea="root";
    private String pasahitza="zubiri";


    public void konexioEgin(){

        try {

            final String url="jdbc:mysql://localhost:3306/mugarria6";
            konexioa= DriverManager.getConnection(url,erabiltzailea,pasahitza);
            sententzia=konexioa.createStatement();
            System.out.println("Konektatuta");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
