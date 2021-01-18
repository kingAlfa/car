import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    private int clientPort = 5000;
    private ServerSocket welcomeSocket;
    boolean serverRunning = true;

    public static void main(String[] args) {
        new MainServer();
    }

    public MainServer(){
        try{
            welcomeSocket = new ServerSocket(clientPort);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Le Server FTP est ouvert sur le port :"+clientPort);
        int noOfThreads = 0;
        while (serverRunning){
            try{
                Socket client =welcomeSocket.accept();
                int dataport = clientPort + noOfThreads +1;

                ServerService s = new ServerService(client,dataport);

                System.out.println("New connection to the server created");
                noOfThreads++;
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
