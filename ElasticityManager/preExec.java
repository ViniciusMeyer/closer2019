/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElasticityManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 *
 * @author vinicius meyer
 */
public class preExec {

    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException, IOException, ClassNotFoundException, SQLException {

        db database = new db("xxx.xxx.xxx.xxx"); //ip database

        database.cleanMonitor();
        database.organizeIps();
        database.organizeTaskQueue();
        database.cleanLogs();
        database.cleanVmAdd();
        database.cleanVmRem();
        database.cleanWait();
        database.cleanLock();
        database.cleanClock();
        database.cleanResults(); 
        
        
        
    }
}
