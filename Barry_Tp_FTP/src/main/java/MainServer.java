import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MainServer {
    private int clientPort = 5000;
    private ServerSocket welcomeSocket;
    boolean serverRunning = true;
    private HashMap<String,String> mapUser = new HashMap<>();

    public static void main(String[] args) {
        new MainServer();
    }

    public MainServer(){
        try{
            welcomeSocket = new ServerSocket(clientPort);
            this.mapUser.put("barry","123");
            this.mapUser.put("test","test");
            this.mapUser.put("ftp","2021");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Le Server FTP est ouvert sur le port :"+clientPort);

        while (serverRunning){
            try{
                Socket client =welcomeSocket.accept();

                ServerService s = new ServerService(client,mapUser);

                System.out.println("New connection to the server created");

                s.start();

            } catch (IOException e) {
                System.out.println("Exception on accept");
                e.printStackTrace();
            }
        }
        try{
            welcomeSocket.close();
            System.out.println("Server fermé bye...");
        } catch (IOException e) {
            System.out.println("Probleme lors de la fermeture");
            System.exit(-1);
        }
    }
}
