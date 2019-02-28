/*   
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stageController;

/**
 *
 * @author vinicius meyer
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.print.DocFlavor;

public class db {

    String address = "";

    public db(String address1) {
        address = address1;
    }

    private Connection connect() throws SQLException, ClassNotFoundException {
        String driverName = "com.mysql.jdbc.Driver";

        Class.forName(driverName);
        String serverName = this.address;    
        String mydatabase = "database";      
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
        String username = "root";        
        String password = "123456";   
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public boolean currentTask(int state, String task) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE atual SET task = '" + task + "' WHERE stage=" + state + "";
        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public int ntasks() throws SQLException, ClassNotFoundException {
        String sql = "SELECT ntasks FROM general";
        int id = -1;
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                id = rs.getInt("ntasks");
            }
            pstmt.close();
            conn.close();
            return id;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return id;
        }

    }

    public String getFirstTask(int state) throws SQLException, ClassNotFoundException {
        String sql = "SELECT task FROM taskQueue WHERE stage=" + state + " ORDER BY ID LIMIT 1";
        String result = null;
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                result = rs.getString("task");
            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }

    }

    public String checkVmToAdd(int state) throws SQLException, ClassNotFoundException {
        String sql = "SELECT vm FROM vmadd WHERE stage='" + state + "' LIMIT 1";
        String result = null;
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                result = rs.getString("vm");
            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }

    }

    public boolean freeStateLock(int state) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE lockStatus set lockState='y' WHERE stage=" + state + "";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean cleanVmAddList(int state) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE vmadd SET vm='x' WHERE stage=" + state + "";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean cleanVmRemList(int state) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE vmrem SET vm='x' WHERE stage=" + state + "";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean remVmFromStage(int stage, String ip) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ipstage WHERE ip='" + ip + "' AND stage='" + stage + "'";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean remVmFromStage(String ip) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ipstage WHERE ip='" + ip + "'";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean saveStartTime(long starttime) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE general set starttime="+starttime+"";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }
   public long getStartTime() throws SQLException, ClassNotFoundException {
        String sql = "SELECT starttime FROM general";
        long result = 0;
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                result = rs.getLong("starttime");
            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }

    }

    
    public boolean addVmToStage(int stage, String ip) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO ipstage (ip,stage) VALUES (?,?)";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ip);
            pstmt.setInt(2, stage);

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public String checkVmToRem(int state) throws SQLException, ClassNotFoundException {
        String sql = "SELECT vm FROM vmrem WHERE stage='" + state + "' LIMIT 1";
        String result = null;
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                result = rs.getString("vm");
            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }

    }

    public List<String> getVmListFromStage(int stage) throws SQLException, ClassNotFoundException {
        String sql = "SELECT ip FROM ipstage WHERE stage='" + stage + "'";
        List<String> result = new ArrayList<String>();
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                //System.out.println(rs.getString("ip"));
                result.add(rs.getString("ip"));
            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }

    }

    public boolean saveLogs(int stage, String log) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO logs (log,stage) VALUES (?,?)";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, log);
            pstmt.setInt(2, stage);

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean remFromTaskList(int stage, String task) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM taskQueue WHERE task='" + task + "' AND stage='" + stage + "'";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean sendToNextStage(int stage, String task) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO taskQueue (task,stage) VALUES (?,?)";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, task);
            pstmt.setInt(2, stage + 1);

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean checkWaiting(String ip) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM wait WHERE vm='" + ip + "'";
        boolean result = false;
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                //System.out.println(rs.getString("ip"));
                if (rs.getString("vm").equals(ip)) {
                    result = true;
                } else {
                    result = false;
                }
            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }

    }

    public boolean removeWaiting(String ip) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM wait WHERE vm='" + ip + "'";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean setVmWainting(String ip) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO wait (vm) VALUES (?)";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ip);
            pstmt.execute();
            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean setClock(long time, int qnt, int cont, int stage) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO clock (time,qnt, cont, stage) VALUES (?,?,?,?)";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, time);
            pstmt.setInt(2, qnt);
            pstmt.setInt(3, cont);
            pstmt.setInt(4, stage);

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean cleanClock(int stage) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM clock WHERE stage='" + stage + "'";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public String checkWaiting() throws SQLException, ClassNotFoundException {
        String sql = "SELECT vm from wait";
        String result = "null";
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            if (rs.next()) {
                //System.out.println(rs.getString("ip"));
                result = rs.getString("vm");
            }
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean removeFromWaiting(String vm) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM wint WHERE vm='" + vm + "'";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

}
