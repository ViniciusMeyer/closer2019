package Workers;

/*  
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.imageio.ImageIO;

/**
 *
 * @author vinicius meyer
 */
public class ServerThread {

    private Socket socket = null;
    private DataInputStream dataInputStream = null;

    public ServerThread(Socket cliente) {
        this.socket = cliente;
    }

    public void run() {
        String[] tabela = new String[3];
        int ped;
        String ipretorno, name, fil;
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            System.out.println("Server is Waiting for request... " + sdf.format(cal.getTime()));
            System.out.println("Connected with: " + socket.getInetAddress());
            dataInputStream = new DataInputStream(socket.getInputStream());
            name = dataInputStream.readUTF();
            tabela = name.split(";");
            name = tabela[0];
            ped = Integer.parseInt(tabela[1]);
            ipretorno = tabela[2];
            fil = tabela[3];

            System.out.println("Server Read from client: " + name);
			BufferedImage image = ImageIO.read(socket.getInputStream());
            System.out.println(image.getWidth() + " * " + image.getHeight() + " = " + image.getWidth() * image.getHeight());
            System.out.println("Server: Task received");

            Filter filter = new Filter();
			
			 if (fil.equals("1")) {
                image = filtro.grayScale(image);          //STAGE 1 action
            }
            if (fil.equals("2")) {
                image = filtro.negative(image);           //STAGE 2 action
            }
            if (fil.equals("3")) {
                image = filtro.threshold(image, 150);    //STAGE 3 action
            }
            
            Socket clientSocket = null;
            OutputStream outputStream = null;
            DataOutputStream dataOutputStream = null;
            int rPort = Integer.parseInt(fil) + 6000; // 6000 + stage
            System.out.println(rPort);
            clientSocket = new Socket(ipretorno, rPort);
            System.out.println("iniciando retorno para porta " + rPort);
            outputStream = clientSocket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(name + ";" + ped);
            ImageIO.write(image, "JPG", clientSocket.getOutputStream());
            outputStream.flush();
            clientSocket.close();
            System.out.println("Return task");


            Calendar cal1 = Calendar.getInstance();
            System.out.println("Taskocessed and saved. " + sdf.format(cal1.getTime()));
            

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
