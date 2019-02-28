/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Workers;

/**
 *
 * @author vinicius meyer
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.print.DocFlavor;


public class insertSql {

    private Connection connect() throws SQLException, ClassNotFoundException {
        String driverName = "com.mysql.jdbc.Driver";

        Class.forName(driverName);
        String serverName = "localhost";    
        String mydatabase = "database";     
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
        String username = "user";        
        String password = "123456";     
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public void insert(String ip,String cpu, String tm, String fm) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO monitor(ip,cpu,tpms,fpms) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ip);
            pstmt.setString(2, cpu);
            pstmt.setString(3, tm);
            pstmt.setString(4, fm);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
