package Workers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author vinicius meyer
 */
public class Server {

    public static void main(String[] args) throws Exception {
        try {
            System.out.println("Incializando o servidor...");

            ServerSocket serv = new ServerSocket(6000);
            System.out.println("Servidor iniciado, ouvindo a porta " + 6000);
           
            while (true) {
                Socket clie = serv.accept();
               
                new ServerThread(clie).run();
            }
        } catch (Exception e) {
        }
    }

}
