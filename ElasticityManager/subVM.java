/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElasticityManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 *
 * @author vinicius meyer
 */
public class subVM {

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException, IOException, ClassNotFoundException, SQLException {
        int stage = Integer.parseInt(args[0]);
        db database = new db("xxx.xxx.xxx.xxx"); //ip database
		String linhaAtual = database.checkStageStatus(stage); //lockstatus

        if (!linhaAtual.equals("x")) {
            database.blockRemVM(stage);  //lockstatus (x)

            String ultimoip = database.getLastVMFromStage(stage); //ipstage
            database.vmRem(ultimoip, stage);
            database.saveLogs(97, "rem vm (" + ultimoip + ") in stage " + stage);
            
        } else {
            System.out.println("exists");
        }
    }
}
