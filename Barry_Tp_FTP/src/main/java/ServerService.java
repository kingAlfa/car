import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerService extends Thread{
    private final boolean DEBUGMODE = true;

    //gestion des direction
    private String root;
    private String currentDir;
    private final String separateur = "/";
    // gestion de connexion
    private Socket clientSocket;
    private PrintWriter clientOutWriter;
    private BufferedReader clientIn;
    //gestion des données
    private ServerSocket dataSocket;
    private Socket dataConnexion;
    private PrintWriter dataOutWriter;

    //gestion de l'utilisateur
    private statusUser currentUserStatus = statusUser.NOTLOGGEDION;
    private String UserValid = "Barry";
    private String PassWord  = "Car2021";

    private int dataPort;
    private typeDeTransfert modeDeTransfer = typeDeTransfert.ASCII;
    private boolean quitCommandLoop = false;

    public ServerService(Socket soClient, int dataPort){
        super();
        this.clientSocket = soClient;
        this.dataPort = dataPort;
        this.currentDir = System.getProperty("user.dir")+"/test";
        this.root = System.getProperty("user.dir");
    }

    public  void run(){
        try{
            clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOutWriter = new PrintWriter(clientSocket.getOutputStream(),true);

            sendMessageToClient("220 Welcome to the Barry FTP SERVER...");
            while(!quitCommandLoop){
                executeCommand(clientIn.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                clientIn.close();
                clientOutWriter.close();
                clientSocket.close();
                debugOutPut("Sockets fermée et le server arreté...");
            } catch (IOException e) {
                e.printStackTrace();
                debugOutPut("La socket ne peut pas être fermée...");
            }
        }
    }

    private void debugOutPut(String s) {
        if(DEBUGMODE){
            System.out.println("Thread "+this.getId() + ": "+s);
        }
    }

    private void executeCommand(String c) {
        int index = c.indexOf(' ');
        String command = ((index == -1)? c.toUpperCase(): (c.substring(0,index)).toUpperCase());
        String args = ((index == -1)? null : c.substring(index + 1,c.length()));

        debugOutPut("Command " +command + "Args : "+ args);

        switch (command){
            case "USER":
                handlerUser(args);
                break;
            case "PASS":
                handlerPass(args);
            case "PWD":
                handlerPwd();
                break;
            default:
                sendMessageToClient("501 Command inconnue");
                break;
        }
    }

    private void handlerPwd() {
    }

    private void handlerPass(String passWord) {
        if(currentUserStatus == statusUser.ENTEREDUSERNAME && passWord.equals(PassWord)){
            currentUserStatus = statusUser.LOGGEDIN;
            sendMessageToClient("230 welcome to FTP-SERVER");
            sendMessageToClient("230 User logged well");
        }
        else if(currentUserStatus == statusUser.LOGGEDIN){
            sendMessageToClient("530 already logged");
        }
        else{
            sendMessageToClient("530 not logged");
        }
    }

    private void handlerUser(String username) {
        if(username.equalsIgnoreCase(UserValid)){
            sendMessageToClient("331 User name Ok, know the password ? ");
            currentUserStatus = statusUser.ENTEREDUSERNAME;
        }
        else if(currentUserStatus == statusUser.LOGGEDIN){
            sendMessageToClient("550 user already logged");
        }
        else{
            sendMessageToClient("530 User not logged...");
        }
    }

    private void sendMessageToClient(String s) {
        clientOutWriter.println(s);
    }


}
