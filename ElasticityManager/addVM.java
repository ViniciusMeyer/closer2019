/*   
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElasticityManager;

import Class.disk_size;
import Class.history;
import Class.history_records;
import Class.host;
import Class.host_pool;
import Class.monitoring;
import Class.pci;
import Class.pci_devices;
import Class.security_group_rule;
import Class.template;
import Class.vm;
import Class.vm_pool;
import com.thoughtworks.xstream.XStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import org.opennebula.client.OneResponse;
import org.opennebula.client.host.HostPool;
import org.opennebula.client.vm.VirtualMachine;
import org.opennebula.client.vm.VirtualMachinePool;

/**
 *
 * @author vinicius meyer
 */
public class addVM {

    public static void main(String args[]) throws FileNotFoundException, IOException, ClientConfigurationException, SQLException, ClassNotFoundException {
        String one_auth = "user:123456", one_xmlrpc = "http://192.168.1.85:2633/RPC2", vmWaiting = ""; // user, password and address OpenNebula
        Client oneClient = new Client(one_auth, one_xmlrpc);
        int stage = 0, hostFree = 0;

        stage = Integer.parseInt(args[0]);
       

        db database = new db("localhost");

        String linhaAtual = database.checkStageStatus(stage);
        if (!linhaAtual.equals("x")) {
            database.blockAddVM(stage); //lockState (x)

            vmWaiting = database.checkWaiting();
            if (!vmWaiting.equals("null")) {
                if (database.removeWaiting(vmWaiting)) {
                    database.vmAdd(vmWaiting, stage);
                    database.saveLogs(98, "add vm (" + vmWaiting + ") in stage " + stage);
		    }
            } else {
                database.saveLogs(98, "Trying to create vm in stage " + stage);
                try {

                    OneResponse ihp = HostPool.info(oneClient);
                    if (ihp.isError()) {
                        System.out.println("HostPool(s):" + "\n" + ihp.getErrorMessage());
                    } else {
                        XStream xstream = new XStream();
                        xstream.alias("HOST_POOL", host_pool.class);
                        xstream.addImplicitCollection(host_pool.class, "HOST", host.class);

                        xstream.alias("pci_devices", pci_devices.class);
                        xstream.addImplicitCollection(pci_devices.class, "PCI", pci.class);

                        host_pool a = (host_pool) xstream.fromXML(ihp.getMessage());

                        int cont = 0;
                        while (cont < a.getIdHosts().length) {
                            System.out.println("Host ID: " + a.getIdHosts()[cont]);

                            System.out.println("VMs Amount: " + a.getMaxCpuHosts()[cont] / 100);
                            if ((a.getMaxCpuHosts()[cont] / 100) - (a.getCpuUsageHosts()[cont] / 100) >= 1) {
                                hostFree = a.getIdHosts()[cont];

                                cont = a.getIdHosts().length;
                            }

                            System.out.println("Avaiable VMs amount: " + ((a.getMaxCpuHosts()[cont] / 100) - (a.getCpuUsageHosts()[cont] / 100)));
                            System.out.println("");
                            cont++;

                        }

                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());

                }

                if (hostFree != 0) {

                    try {
                        oneClient = new Client(one_auth, one_xmlrpc);

                        String vmTemplate = "CONTEXT = [\n"
                                + "  NETWORK = \"YES\",\n"
                                + "  SSH_PUBLIC_KEY = \"$USER[SSH_PUBLIC_KEY]\" ]\n"
                                + "CPU = \"1\"\n"
                                + "DISK = [\n"
                                + "  IMAGE = \"Worker\",\n"
                                + "  IMAGE_UNAME = \"user\" ]\n"
                                + "GRAPHICS = [\n"
                                + "  LISTEN = \"0.0.0.0\",\n"
                                + "  TYPE = \"VNC\" ]\n"
                                + "HYPERVISOR = \"kvm\"\n"
                                + "INPUTS_ORDER = \"\"\n"
                                + "LOGO = \"images/logos/ubuntu.png\"\n"
                                + "MEMORY = \"1024\"\n"
                                + "MEMORY_UNIT_COST = \"MB\"\n"
                                + "NIC = [\n"
                                + "  NETWORK = \"vnet\",\n"
                                + "  NETWORK_UNAME = \"user\" ]\n"
                                + "OS = [\n"
                                + "  BOOT = \"\" ]\n"
                                + "VCPU = \"1\"";

        
                        OneResponse rc = VirtualMachine.allocate(oneClient, vmTemplate);
                        if (rc.isError()) {
                            System.out.println("failed!");
                            throw new Exception(rc.getErrorMessage());
                        }
                        int newVMID = Integer.parseInt(rc.getMessage());

                        VirtualMachine vm = new VirtualMachine(newVMID, oneClient);

                        rc = vm.deploy(hostFree);

                        XStream xstreamVm = new XStream();
                        xstreamVm.alias("HISTORY_RECORDS", history_records.class);
                        xstreamVm.addImplicitCollection(history_records.class, "HISTORY", history.class);
                        xstreamVm.alias("TEMPLATE", template.class);
                        xstreamVm.addImplicitCollection(template.class, "SECURITY_GROUP_RULE", security_group_rule.class);
                        xstreamVm.alias("MONITORING", monitoring.class);
                        xstreamVm.addImplicitCollection(monitoring.class, "DISK_SIZE", disk_size.class);
                        xstreamVm.alias("VM_POOL", vm_pool.class);
                        xstreamVm.addImplicitCollection(vm_pool.class, "VM", vm.class);

                        OneResponse vm_pool = VirtualMachinePool.infoAll(oneClient);
                        vm_pool teste1 = (vm_pool) xstreamVm.fromXML(vm_pool.getMessage());
                        InetAddress inet;
                        boolean ping = false;
                        while (!ping) {

                            try {
                                inet = InetAddress.getByName(teste1.getIPById(newVMID));
                                if (inet.isReachable(3000)) {
                                    ping = true;
                                }
                            } catch (IOException e) {
                                System.err.println(e.getMessage());
                            }

                        }
                        Thread.sleep(1000);
                        if (database.vmAdd(teste1.getIPById(newVMID), stage)) {
                            System.out.println(teste1.getIPById(newVMID));
                            database.saveLogs(98, "add vm (" + teste1.getIPById(newVMID) + ") in stage " + stage);
                        }

                    } catch (Exception e) {

                        System.out.println(e.getMessage());

                    }
                } else {
                    database.saveLogs(98, "host full on trying add vm in stage " + stage);
                    System.out.println("no host available");
                }
            }

        } else {
            System.out.println("VM being created/removed");

        }

    }
}
