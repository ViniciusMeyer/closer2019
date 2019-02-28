/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElasticityManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author vinicius meyer
 */
public class VmMonitor {

    public VmMonitor() {

    }

    public static String[][] getVmStage() {

        try {

            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost/pipel";
            String user = "root";
            String passwd = "123456";
            int cont = 0;

            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, user, passwd);

            String query = "SELECT count(*) as cont FROM ipstage";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                cont = rs.getInt("cont");
            }
            String vet[][] = new String[cont][3];

            query = "SELECT * FROM ipstage";
            st = conn.createStatement();
            rs = st.executeQuery(query);

            cont = 0;
            while (rs.next()) {
                vet[cont][0] = rs.getString("stage");
                vet[cont][1] = rs.getString("ip");
                vet[cont][2] = rs.getString("id");

                cont++;
            }

            rs.close();
            st.close();
            conn.close();

            return vet;

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());

            return new String[1][1];
        }

    }

    public static String getCpuFromVm(String informIp) {

        try {

            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost/pipel";
            String user = "root";
            String passwd = "123456";
            String cpu = "";
            String id = "";

            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, user, passwd);

            String query = "SELECT * FROM monitor WHERE ip='" + informIp + "' ORDER BY ID DESC LIMIT 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                id = rs.getString("id");
                cpu = rs.getString("cpu");
            }

            rs.close();
            st.close();
            conn.close();
            return cpu+";"+id;

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return "-1";
        }

    }

    public static void monitor() {
        try {

            String myDriver = "com.mysql.jdbc.Driver";
            String myUrl = "jdbc:mysql://localhost/pipel";
            String user = "root";
            String passwd = "123456";

            // create our mysql database connection
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, user, passwd);

            // our SQL SELECT query. 
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM monitor ORDER BY ID DESC LIMIT 1";

            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next()) {
                int id = rs.getInt("id");
                String ip = rs.getString("ip");
                String cpu = rs.getString("cpu");
                String tpms = rs.getString("tpms");
                String fpms = rs.getString("fpms");
                Date dateCreated = rs.getDate("reg_date");

                // print the results
                System.out.format("%s, %s, %s, %s, %s, %s\n", id, ip, cpu, tpms, fpms, dateCreated);
            }

            rs.close();

            st.close();

            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

    }

}
