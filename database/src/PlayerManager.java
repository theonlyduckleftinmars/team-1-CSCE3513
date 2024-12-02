import java.sql.*;

//Need postgresql-42.7.4.jar in library folder
public class PlayerManager {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/photon";
        String username = "student";
        String password = "student";
        
        Connection con = DriverManager.getConnection(url, username, password);
        Statement stmt = con.createStatement();

        String sql = "INSERT INTO players (id, codename) VALUES (1, 'p1')";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO players (id, codename) VALUES (2, 'p2')";
        stmt.executeUpdate(sql);


        con.close();
    }
}
