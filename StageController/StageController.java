/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stageController;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author vinicius meyer
 *
 * to execute > StageController NetworkInterface Stage ipDatabase
 */
public class StageController {

    static BufferedImage imagemMaster = null;

    public static void setImage(BufferedImage image) {
        imagemMaster = image;
    }

    public static BufferedImage getImage() {
        return imagemMaster;
    }

    public static void main(String[] args) throws Exception {

        int x = 0, y = 0, div = 0, stagesq = 0, datacont = 0;
        File dir = new File(".//");
        long milisinicial = 0, milisfinal = 0, diftempo = 0, milisinicial1 = 0, milisfinal1 = 0;
        String nextStateIp = "";

        Socket clientSocket = null, cs = null;
        OutputStream outputStream = null;
        DataOutputStream dataOutputStream = null;

        int state = Integer.parseInt(args[1]);
        String interfaceName =  args[0];
        String ipDB = args[2]

        int sPort = 6000 + state;
        ServerSocket serv = new ServerSocket(sPort);
        db database = new db(ipDB);

        if (state == 1) {
            Calendar cal1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH;mm;ss");
            System.out.println("INICIO APP: " + sdf1.format(cal1.getTime()));
            milisinicial1 = cal1.getTimeInMillis();
            database.saveStartTime(milisinicial1);
        }

        database.freeStateLock(state); //y

        database.cleanVmAddList(state); //x  (vmadd)

        database.freeStateLock(state); //y (lockStatus)

        database.cleanVmRemList(state); //x  (vmrem)

        String iplocal = "ip error", currentTask = "", vmToAdd = "", vmToRem = "";
        NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
        Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();
        InetAddress currentAddress;
        currentAddress = inetAddress.nextElement();
        while (inetAddress.hasMoreElements()) {
            currentAddress = inetAddress.nextElement();
            if (currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()) {
                iplocal = currentAddress.toString();

            }
        }
        
        iplocal = iplocal.replaceAll("/", "");

        if (database.currentTask(state, "x")) {
            System.out.println(state + " :processing begins");
        } else {
            System.out.println(state + " :error processing begins");
        }

        database.cleanClock(state);

        stagesq = database.ntasks(); 
        
        while (datacont < stagesq) {
            currentTask = database.getFirstTask(state); // (queue)
            if (currentTask == null) {
                System.out.println(state + " :no tasks");
                Thread.sleep(1000);
            } else {

                if (database.currentTask(state, "" + (datacont + 1))) {
                    System.out.println(state + " :Task changed to - " + (datacont + 1));
                } else {
                    System.out.println(state + " :Error while processing");
                }

                System.out.println(state + " :task - " + currentTask);

                vmToAdd = database.checkVmToAdd(state);

                if (!vmToAdd.equals("x")) {

                    database.freeStateLock(state); //y

                    database.cleanVmAddList(state); //x  (vmadd)

                    database.addVmToStage(state, vmToAdd);

                }

                vmToRem = database.checkVmToRem(state);

                if (!vmToRem.equals("x")) {
                    database.freeStateLock(state); //y (lockStatus)

                    database.cleanVmRemList(state); //x  (vmrem)

                    database.remVmFromStage(state, vmToRem); //(ipstage)

                    String command = "java -cp .:./com.mysql.jdbc_5.1.5.jar:./org.opennebula.client.jar:./xmlrpc-client-3.1.2.jar:./xmlrpc-common-3.1.2.jar:./xstream-1.4.8.jar:./xmlpull-1.1.3.1.jar:./ws-commons-util-1.0.2.jar:./xpp3_min-1.1.4c.jar:./  deleteVM " + vmToRem + " " + ipDB + "";

                    try {
                        Runtime run = Runtime.getRuntime();
                        run.exec(command, null, dir);

                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.err.println("ERROR REMOVING VM");
                    }

                }

                System.out.println("\n - - - - - - - - - - - - - - - - - - -\n");
                System.out.println("Task: " + currentTask + " - Stage: " + state);

                List<String> workers = new ArrayList<String>();
                workers = database.getVmListFromStage(state); //(ipstage)

                div = workers.size();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH;mm;ss");
                System.out.println("Inicio: " + sdf.format(cal.getTime()));
                milisinicial = cal.getTimeInMillis();

                try {

                    div = workers.size();
                    System.out.println("Sending to" + div + " Workers");
                    for (int ggg = 0; ggg < div; ggg++) {

                        clientSocket = new Socket("localhost", 6000); 
                        outputStream = clientSocket.getOutputStream();
                        dataOutputStream = new DataOutputStream(outputStream);
                        if (state == 1) {
                            dataOutputStream.writeUTF("volta" + ggg + ";" + ggg + ";" + iplocal + ";1");
                        }
                        if (state == 2) {
                            dataOutputStream.writeUTF("volta" + ggg + ";" + ggg + ";" + iplocal + ";2");
                        }
                        if (state == 3) {
                            dataOutputStream.writeUTF("volta" + ggg + ";" + ggg + ";" + iplocal + ";3");
                        }
                        imagemMaster = ImageIO.read(new File("//commom_file//" + state + "//" + currentTask));  
                        x = imagemMaster.getWidth();
                        y = imagemMaster.getHeight();

                        ImageIO.write(imagemMaster.getSubimage(0, (y / div) * ggg, x, (y / div)), "JPG", clientSocket.getOutputStream());
                        outputStream.flush();
                        clientSocket.close();

                    }

                    System.out.println("Waiting return ...");
                    for (int yyy = 0; yyy < div; yyy++) {
                        Socket clie = serv.accept();
                        new stageControllerThread(clie).start();    //run();
                    }

                    ImageIO.write(imagemMaster, "jpg", new File("//commom_file//" + (state + 1) + "//" + currentTask));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Calendar cal1 = Calendar.getInstance();
                milisfinal = cal1.getTimeInMillis();

                database.saveLogs(state, "" + (milisfinal - milisinicial) + ";" + ((milisfinal - milisinicial) / 1000) + ";" + div + "");

                if (database.remFromTaskList(state, currentTask)) {
                    System.out.println("Task n " + currentTask + " removed with success");
                } else {
                    System.out.println("Error task n " + currentTask);
                }

                if (database.sendToNextStage(state, currentTask)) {
                    System.out.println("Task n " + currentTask + " sent to" + (state + 1));
                } else {
                    System.out.println("Error sending task n " + currentTask + " to stage" + (state + 1));
                }

                diftempo = (milisfinal - milisinicial) / 1000;

                database.setClock(diftempo, div, datacont, state);

                datacont++;

            }
        }

        if (database.currentTask(state, "y")) {
            System.out.println(state + " :Processamento Encerrado");

            List<String> workers = new ArrayList<String>();
            workers = database.getVmListFromStage(state); //(ipstage)

            for (int f = 0; f < workers.size(); f++) {

                String command = "java -cp .:./com.mysql.jdbc_5.1.5.jar:./org.opennebula.client.jar:./xmlrpc-client-3.1.2.jar:./xmlrpc-common-3.1.2.jar:./xstream-1.4.8.jar:./xmlpull-1.1.3.1.jar:./ws-commons-util-1.0.2.jar:./xpp3_min-1.1.4c.jar:./  deleteFinalVM " + workers.get(f) + " " + ipDB + "";

                try {
                    Runtime run = Runtime.getRuntime();
                    run.exec(command, null, dir);

                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.err.println("ERROR REMOVING VM");
                }

            }

        } else {
            System.out.println(state + " :Error ending processing");
        }
        Thread.sleep(1000);

        
        if (state == 3) {
            Calendar cal2 = Calendar.getInstance();
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH;mm;ss");

            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("END APP: " + sdf2.format(cal2.getTime()));
            milisfinal1 = cal2.getTimeInMillis();
            milisinicial1 = database.getStartTime();
            System.out.println("TOTAL TIME:" + ((milisfinal1 - milisinicial1) / 1000));
            database.saveLogs(96, "Final time: " + ((milisfinal1 - milisinicial1) / 1000) + "");
        }

    }

}
