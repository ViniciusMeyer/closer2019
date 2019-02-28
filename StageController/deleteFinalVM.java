
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stageController;

import Class.*;
import stageController_inside.*;
import com.thoughtworks.xstream.XStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

/**
 *
 * @author vinicius meyer
 */
public class deleteFinalVM {

    public static void main(String args[]) throws FileNotFoundException, IOException, InterruptedException, ClientConfigurationException, SQLException, ClassNotFoundException {
        String one_auth = "user:123456", one_xmlrpc = "http://192.168.80.200:2633/RPC2"; // OpenNebula user, password and address
        Client oneClient = new Client(one_auth, one_xmlrpc);
        String ip = args[0]; //vm ip
        String ipDB = args[1]; //db ip
        boolean x = false;

        int cont = 999, vmDestroy = 0;

        db database = new db(ipDB);
         database.remVmFromStage(ip);
        database.setVmWainting(ip);

        Thread.sleep(10000);

        
        if (database.checkWaiting(ip)) {
            database.removeWaiting(ip);
   
            //OpenNebula 5.4.13
            try {
                OneResponse ihp = VirtualMachinePool.infoAll(oneClient);
                XStream xstream = new XStream();
                xstream.alias("HISTORY_RECORDS", history_records.class);
                xstream.addImplicitCollection(history_records.class, "HISTORY", history.class);
                xstream.alias("TEMPLATE", template.class);
                xstream.addImplicitCollection(template.class, "SECURITY_GROUP_RULE", security_group_rule.class);
                xstream.alias("MONITORING", monitoring.class);
                xstream.addImplicitCollection(monitoring.class, "DISK_SIZE", disk_size.class);
                xstream.alias("VM_POOL", vm_pool.class);
                xstream.addImplicitCollection(vm_pool.class, "VM", vm.class);

                vm_pool a = (vm_pool) xstream.fromXML(ihp.getMessage());
                vmDestroy = a.getIdByIP(ip);
                VirtualMachine vm = new VirtualMachine(vmDestroy, oneClient);
                vm.delete();

                database.remVmFromStage(ip);

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }

        } else {
            System.out.println("vm does not exists anymore");
        }
    }
}
