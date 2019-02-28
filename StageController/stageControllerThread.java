/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stageController;


import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.net.Socket;
import javax.imageio.ImageIO;

/**
 *
 * @author vinicius meyer
 */
public class stageControllerThread extends Thread{
    private Socket socket = null;
    private DataInputStream dataInputStream = null;

    public stageControllerThread(Socket cliente) {
        this.socket = cliente;

    }

    public void run() {
        String nome = "";
        int ped = 0, x = 0, y = 0;
        String[] tabela = new String[2];
        try {
			
            dataInputStream = new DataInputStream(socket.getInputStream());
            nome = dataInputStream.readUTF();
            tabela = nome.split(";");
            nome = tabela[0];
            ped = Integer.parseInt(tabela[1]);

            BufferedImage image = ImageIO.read(socket.getInputStream());
            x = image.getWidth();
            y = image.getHeight();
            
            StageController.getImage().createGraphics().drawImage(image, 0, y * ped, null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}