/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElasticityManager;

/**
 *
 * @author vinicius meyer
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public String getCurrentTask(int state) throws SQLException, ClassNotFoundException {
        String sql = "select task from atual WHERE stage=" + state + "";
        
        String task = "";
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                task = rs.getString("task");
            }
            pstmt.close();
            conn.close();
            return task;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return task;
        }
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

    public boolean cleanVmAdd() throws SQLException, ClassNotFoundException {
        String sql = "UPDATE vmadd SET vm='x'";

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

    public boolean cleanVmRem() throws SQLException, ClassNotFoundException {
        String sql = "UPDATE vmrem SET vm='x'";

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
                //System.out.println(rs.getString("vm"));
                if (rs.getString("vm").equals(ip)) {
                    result = true;
                    System.out.println("igual");
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

    public String checkWaiting() throws SQLException, ClassNotFoundException {
        String sql = "SELECT vm from wait ORDER BY RAND()";
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

    public boolean cleanWait() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM wait";

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

    public boolean cleanLock() throws SQLException, ClassNotFoundException {
        String sql = "UPDATE lockStatus SET lockState='y'";

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

    public boolean cleanClock() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM clock";

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

    public List<String> getClock(int stage) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM clock WHERE stage=" + stage + " ORDER BY id DESC LIMIT 1";
        List<String> result = new ArrayList<String>();
        long time;
        int qnt = 0, cont = 0;
        //System.out.println(sql);
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                //System.out.println(rs.getString("ip"));
                result.add(Long.toString(rs.getLong("time")));
                result.add(Integer.toString(rs.getInt("qnt")));
                result.add(Integer.toString(rs.getInt("cont")));

            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }

    }

    public int getCpuFromVm(String informIp) throws ClassNotFoundException {
        int cpu = -1;
        //String id = "";
        String sql = "SELECT * FROM monitor WHERE ip='" + informIp + "' ORDER BY ID DESC LIMIT 1";

        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                //System.out.println(rs.getString("ip"));
                //id = rs.getString("id");
                cpu = rs.getInt("cpu");

            }
            pstmt.close();
            conn.close();
            return cpu;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }

    }

    public double getMCpuFromStage(int stage) throws ClassNotFoundException {
        double av = 0;
        List<Double> average = new ArrayList<>();
        List<String> ip = new ArrayList<String>();
        //String id = "";
        String sql = "SELECT * FROM ipstage WHERE stage=" + stage;
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                //System.out.println(rs.getString("ip"));
                ip.add(rs.getString("ip"));

            }
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }

        for (int i = 0; i < ip.size(); i++) {

            sql = "SELECT cpu FROM monitor WHERE ip='" + ip.get(i) + "' ORDER BY ID DESC LIMIT 1";
            try (Connection conn = this.connect();
                    Statement pstmt = conn.createStatement()) {

                ResultSet rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    //System.out.println(rs.getString("cpu"));
                    average.add(Double.parseDouble(rs.getString("cpu")));
                }
                pstmt.close();
                conn.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return -1;
            }

        }

        for (int f = 0; f < average.size(); f++) {
            av = av + average.get(f);
        }

        return av / average.size();

    }

    public boolean blockAddVM(int stage) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE lockStatus SET lockState='x' WHERE stage=" + stage + "";

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

    public boolean blockRemVM(int stage) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE lockStatus SET lockState='x' WHERE stage=" + stage + "";

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

    public boolean vmAdd(String ip, int stage) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE vmadd SET vm='" + ip + "' WHERE stage=" + stage + "";

        // System.out.println(sql);
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

    public boolean vmRem(String ip, int stage) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE vmrem SET vm='" + ip + "' WHERE stage=" + stage + "";

        // System.out.println(sql);
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

    public String getLastVMFromStage(int stage) throws ClassNotFoundException {
        String sql = "SELECT ip FROM ipstage WHERE stage=" + stage + "";
        String result = "";
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                result = rs.getString("ip");

            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "NULL";
        }

    }

    public String checkStageStatus(int stage) throws ClassNotFoundException {
        String sql = "SELECT * FROM lockStatus WHERE stage=" + stage + "";
        String result = "";
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                //System.out.println(rs.getString("ip"));
                //id = rs.getString("id");
                result = rs.getString("lockState");

            }
            pstmt.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "NULL";
        }

    }

    public boolean cleanMonitor() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM monitor";
        String sql2 = "ALTER TABLE monitor AUTO_INCREMENT = 1 ";
//System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();

            try (Connection conn2 = this.connect();
                    PreparedStatement pstmt2 = conn2.prepareStatement(sql2)) {
                pstmt2.execute();
                pstmt2.close();
                conn2.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean organizeIps() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM ipstage";
        String sql2 = "ALTER TABLE ipstage AUTO_INCREMENT = 1 ";
        String sql3 = "INSERT INTO ipstage (ip, stage) VALUES (?,?)";
//System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();

            try (Connection conn2 = this.connect();
                    PreparedStatement pstmt2 = conn2.prepareStatement(sql2)) {
                pstmt2.execute();

                for (int i = 1; i <= 3; i++) {
                    String ip = "192.168.80.5" + i;
                    try (Connection conn3 = this.connect();
                            PreparedStatement pstmt3 = conn3.prepareStatement(sql3)) {

                        //System.out.println(sql3);
                        pstmt3.setString(1, ip);
                        pstmt3.setInt(2, i);

                        pstmt3.execute();
                        pstmt3.close();
                        conn3.close();

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        return false;
                    }
                }

                pstmt2.close();
                conn2.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean organizeTaskQueue() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM taskQueue";
        String sql2 = "ALTER TABLE taskQueue AUTO_INCREMENT = 1 ";
        String sql3 = "INSERT INTO taskQueue (task, stage) VALUES (?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();

            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        try (Connection conn2 = this.connect();
                PreparedStatement pstmt2 = conn2.prepareStatement(sql2)) {
            pstmt2.execute();
            pstmt2.close();
            conn2.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

        for (int i = 1; i <= 40; i++) {
            String task = "imagem" + i + ".jpg";
            try (Connection conn3 = this.connect();
                    PreparedStatement pstmt3 = conn3.prepareStatement(sql3)) {

                //System.out.println(sql3);
                pstmt3.setString(1, task);
                pstmt3.setInt(2, 1);

                pstmt3.execute();
                pstmt3.close();
                conn3.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }

        }
        return true;
    }

    public boolean cleanLogs() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM logs";
        String sql2 = "ALTER TABLE logs AUTO_INCREMENT = 1 ";
//System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();

            try (Connection conn2 = this.connect();
                    PreparedStatement pstmt2 = conn2.prepareStatement(sql2)) {
                pstmt2.execute();
                pstmt2.close();
                conn2.close();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;
            }

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean checkOneVM(int stage) throws ClassNotFoundException {
        String sql = "SELECT count(ip) as count FROM ipstage WHERE stage=" + stage + "";
        int cont = 0;
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {
                //String ips=rs.getString("ip");
                //System.out.println(ips);
                cont = rs.getInt("count");

            }
            pstmt.close();
            conn.close();
            if (cont == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return true;
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

    public boolean cleanResults() throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM result";

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

    public boolean saveResult(double mtotal, String clock, int vms, int lastClockTime, int stage) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO result (mtotal,clock,vms,lastclocktime,stage) VALUES (?,?,?,?,?)";

        //System.out.println(sql);
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, mtotal);
            pstmt.setString(2, clock);
            pstmt.setInt(3, vms);
            pstmt.setInt(4, lastClockTime);
            pstmt.setInt(5, stage);
            pstmt.execute();

            pstmt.close();
            conn.close();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean saveResultsIntoFile(String dir, String name) throws SQLException, ClassNotFoundException, IOException {
        String sql = "Select * from result";
        BufferedWriter bw = null, bw1 = null;
        FileWriter fw = null, fw1 = null;
        String fullname = dir + name + ".csv";
        String fullnamelog = dir + name + ".log";
        fw = new FileWriter(fullname);
        bw = new BufferedWriter(fw);
        fw1 = new FileWriter(fullnamelog);
        bw1 = new BufferedWriter(fw1);

        int cont = 0;
        try (Connection conn = this.connect();
                Statement pstmt = conn.createStatement()) {

            ResultSet rs = pstmt.executeQuery(sql);
            while (rs.next()) {

                Double mtotal = rs.getDouble("mtotal");
                String clock = rs.getString("clock");
                int vms = rs.getInt("vms");
                int lastclocktime = rs.getInt("lastclocktime");
                int stage = rs.getInt("stage");

                if (cont == 0) {
                    bw.write("id;mtotal;vms;clock;lastclocktime;stage\n");
                } else {
                    bw.write(cont + ";" + mtotal + ";" + vms + ";" + clock + ";" + lastclocktime + ";" + stage + "\n");

                }
                cont++;
            }

            bw.close();
            fw.close();
            pstmt.close();
            conn.close();

        }

        sql = "Select * from logs";
        try (Connection conn1 = this.connect();
                Statement pstmt1 = conn1.createStatement()) {

            ResultSet rs1 = pstmt1.executeQuery(sql);
            cont = 0;
            while (rs1.next()) {

                String log = rs1.getString("log");
                int stage = rs1.getInt("stage");
                String time = rs1.getString("reg_date");

                if (cont == 0) {
                    bw1.write("id*log*stage*time\n");
                } else {
                    bw1.write(cont + "*" + log + "*" + stage + "*" + time + "\n");
                }
                cont++;
            }

            bw1.close();
            fw1.close();
            pstmt1.close();
            conn1.close();

        }

        return true;
    }

    public boolean saveStartTime(long starttime) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE general set starttime=" + starttime + "";

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
}
