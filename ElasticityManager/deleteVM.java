/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElasticityManager;

import Class.disk_size;
import Class.history;
import Class.history_records;
import Class.monitoring;
import Class.security_group_rule;
import Class.template;
import Class.vm;
import Class.vm_pool;
import com.thoughtworks.xstream.XStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

/**
 *
 * @author vinicius meyer
 */
public class deleteVM {

    public static void main(String args[]) throws FileNotFoundException, IOException, InterruptedException, ClientConfigurationException {
        String one_auth = "oneadmin:oneadmin", one_xmlrpc = "http://localhost:2633/RPC2";
        Client oneClient = new Client(one_auth, one_xmlrpc);
        String ip = args[0];
        boolean x = false;

        int cont = 999, vmDestroy = 0;

        FileWriter writer1 = new FileWriter(wait, true);
        PrintWriter writer = new PrintWriter(writer1);
        writer.println(ip);
        writer.close();

        Thread.sleep(15000);

        FileReader subb1 = new FileReader(wait);
        BufferedReader lersubb1 = new BufferedReader(subb1);
        ArrayList<String> ips = new ArrayList<String>();
        String linhasubb1 = lersubb1.readLine();
        while (linhasubb1 != null) {
            ips.add(linhasubb1);
            if (linhasubb1.equals(ip)) {
                x = true;
            }
            linhasubb1 = lersubb1.readLine();
        }
        lersubb1.close();
        subb1.close();

        if (x) {
            
            FileWriter manvms = new FileWriter(wait, false);
            PrintWriter adicionavms = new PrintWriter(manvms);
            for (int hh = 0; hh < (ips.size() - 1); hh++) {
                if (!ips.get(hh).equals(ip)) {
                    adicionavms.println(ips.get(hh));
                }
            }
            adicionavms.close();
            manvms.close();

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
                vm.finalizeVM();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
