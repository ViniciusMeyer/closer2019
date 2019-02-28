/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ElasticityManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.opennebula.client.ClientConfigurationException;

/**
 *
 *
 * @author vinicius meyer
 */
public class AverageElasticity {

    public static void main(String[] args) throws FileNotFoundException, IOException, ClientConfigurationException, SQLException, ClassNotFoundException, InterruptedException {

        double t_higher = 20;
        double t_low = 80;

        int qntStages = 3, x = 0;
        boolean[] ti = new boolean[qntStages];
        boolean[] add = new boolean[qntStages];
        String command = "";
        String[] linhaAnterior = {"x", "x", "x"};

        String[][] workers = new String[3][100];
        int[] qnt_vms = new int[qntStages];
        int[] cont_cpu = new int[qntStages];
        File dir = new File("//commom//java//");

        String ipDB = "xxx.xxx.xxx.xxx"; //ip database
        db database = new db(ipDB);

        int m_tempo_total = 0;
        double[] mediatotal = new double[qntStages];

        int[] t_anterior = new int[qntStages];
        int[] t_anterior1 = new int[qntStages];

        double[][] mcpu = new double[qntStages][6];

        ti[0] = false;
        ti[1] = false;
        ti[2] = false;
        add[0] = false;
        add[1] = false;
        add[2] = false;

        cont_cpu[0] = 0;
        cont_cpu[1] = 0;
        cont_cpu[2] = 0;
        t_anterior[0] = 0;
        t_anterior[1] = 0;
        t_anterior[2] = 0;
        t_anterior1[0] = 0;
        t_anterior1[1] = 0;
        t_anterior1[2] = 0;

        mcpu[0][5] = 9999;
        mcpu[1][5] = 9999;
        mcpu[2][5] = 9999;

        
        while (true) {

            for (int i = 0; i < qntStages; i++) {

                List<String> workersList = new ArrayList<String>();
                workersList = database.getVmListFromStage(i + 1);
                qnt_vms[i] = workersList.size();

                for (int j = 0; j < workersList.size(); j++) {
                    workers[i][j] = workersList.get(j);

                }
            }

            String linhaAtual[] = new String[qntStages];

            for (int s = 0; s < qntStages; s++) {
                linhaAtual[s] = database.getCurrentTask(s + 1);
            }

            String[][] times = new String[qntStages][3];

            for (int e = 0; e < qntStages; e++) {
                List<String> t_aux = new ArrayList<String>();
                t_aux = database.getClock(e + 1);
                
                for (int f = 0; f < t_aux.size(); f++) {
                    times[e][f] = t_aux.get(f);
                
                }

            }

            for (int w = 0; w < qntStages; w++) {

                if (!linhaAtual[w].equals("x") && !linhaAtual[w].equals("y") && Integer.parseInt(linhaAtual[w]) > 1) {
        
                    System.out.println(times[w][0]);

                    t_anterior[w] = Integer.parseInt(times[w][0]);
                    
                    
                }

                
                if (linhaAtual[w].equals("y")) {
                t_anterior[w]=0;
                }
                
                if (!linhaAtual[w].equals("x") && !linhaAtual[w].equals("y")) {

                    if (cont_cpu[w] < 6) {
                        mcpu[w][cont_cpu[w]] = database.getMCpuFromStage(w + 1);//teste.getCpu(workers[w][s]);
                        cont_cpu[w]++;

                    } else {
                        mcpu[w][0] = mcpu[w][1];
                        mcpu[w][1] = mcpu[w][2];
                        mcpu[w][2] = mcpu[w][3];
                        mcpu[w][3] = mcpu[w][4];
                        mcpu[w][4] = mcpu[w][5];
                        mcpu[w][5] = database.getMCpuFromStage(w + 1);
                        mediatotal[w] = ((mcpu[w][0] * (0.5)) + (mcpu[w][1] * (0.25)) + (mcpu[w][2] * (0.125)) + (mcpu[w][3] * (0.0625)) + (mcpu[w][4] * (0.03125)) + (mcpu[w][5] * (0.015625)));
                        
                    }

                    if (mediatotal[w] > t_higher && mcpu[w][5] != 9999) {
                        add[w] = true;

                    }
                    if (mediatotal[w] < t_low && mcpu[w][5] != 9999) {

                        ti[w] = true;

                    }

                    if (!linhaAtual[w].equals("x") && !linhaAtual[w].equals("1") && !linhaAtual[w].equals("y")) { //&& !linhaAtual[w].equals(linhaAnterior[w])) {

                        if (t_anterior[2] == 0) {
                            m_tempo_total = (t_anterior[0] + t_anterior[1]) / 2;
                        }
                        if (t_anterior[1] == 0 && t_anterior[2] == 0) {
                            m_tempo_total = t_anterior[0];
                        }
                        if (t_anterior[0] == 0 && t_anterior[1] == 0 && t_anterior[2] == 0) {
                            m_tempo_total = 0;
                        }
                        if (t_anterior[0] != 0 && t_anterior[1] != 0 && t_anterior[2] != 0) {
                            m_tempo_total = (t_anterior[0] + t_anterior[1] + t_anterior[2]) / 3;
                        }
                        
                        
                        if (t_anterior[0] == 0) {
                            m_tempo_total = (t_anterior[1] + t_anterior[2]) / 2;
                        }
                        if (t_anterior[0] == 0 && t_anterior[1] == 0) {
                            m_tempo_total = t_anterior[2];
                        }

                        
                        if (!linhaAtual[w].equals(linhaAnterior[w])) {

                            if (t_anterior[w] > m_tempo_total) {
                                add[w] = true;

                            }

                            if (t_anterior[w] < m_tempo_total && ti[w] == true) {
                            

                                if (!database.checkOneVM(w + 1)) {
                                    command = "java -cp ./com.mysql.jdbc_5.1.5.jar:./org.opennebula.client.jar:./xmlrpc-client-3.1.2.jar:./xmlrpc-common-3.1.2.jar:./xstream-1.4.8.jar:./xmlpull-1.1.3.1.jar:./ws-commons-util-1.0.2.jar:./xpp3_min-1.1.4c.jar:./ subVM " + (w + 1) + "";
                                    try {
                                        Runtime run = Runtime.getRuntime();
                                        run.exec(command, null, dir);

                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                    ti[w] = false;
                                }
                            }
                        }

                        if (add[w] == true) {
                            command = "java -cp ./com.mysql.jdbc_5.1.5.jar:./org.opennebula.client.jar:./xmlrpc-client-3.1.2.jar:./xmlrpc-common-3.1.2.jar:./xstream-1.4.8.jar:./xmlpull-1.1.3.1.jar:./ws-commons-util-1.0.2.jar:./xpp3_min-1.1.4c.jar:./ addVM " + (w + 1) + "";
                            try {
                                Runtime run = Runtime.getRuntime();
                                run.exec(command, null, dir);

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                        System.out.println("   ");
                        linhaAnterior[w] = linhaAtual[w];
                        add[w] = false;
                    } else {

                        if (add[w] == true) {
                            command = "java -cp ./com.mysql.jdbc_5.1.5.jar:./org.opennebula.client.jar:./xmlrpc-client-3.1.2.jar:./xmlrpc-common-3.1.2.jar:./xstream-1.4.8.jar:./xmlpull-1.1.3.1.jar:./ws-commons-util-1.0.2.jar:./xpp3_min-1.1.4c.jar:./ addVM " + (w + 1) + "";
                            try {
                                Runtime run = Runtime.getRuntime();
                                run.exec(command, null, dir);

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                        add[w] = false;
                    }
                }
                if (linhaAtual[w].equals("x")) {
                    database.saveResult(0, "0", qnt_vms[w], 0, (w + 1));
                }
                if (!linhaAtual[w].equals("x") && !linhaAtual[w].equals("y")) {
                    database.saveResult(mediatotal[w], linhaAtual[w], qnt_vms[w], t_anterior[w], (w + 1));
                }
                if (linhaAtual[w].equals("y")) {
                    database.saveResult(0, "0", qnt_vms[w], 0, (w + 1));
                }
            }

            x++;

        }
    }
}
