import java.io.*;
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
    private DataOutputStream clientOut;
    //gestion des données
    private ServerSocket dataSocket;
    private Socket dataConnexion;
    private PrintWriter dataOutWriter;
    private OutputStream os;

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
        this.currentDir = System.getProperty("user.dir")+"/src";
        this.root = System.getProperty("user.dir");
    }

    public  void run(){
        try{
            clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientOut = new DataOutputStream(clientSocket.getOutputStream());
            //clientOutWriter = new PrintWriter(clientSocket.getOutputStream(),true);

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
                clientOut.close();
                //clientOutWriter.close();
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

    private void executeCommand(String c) throws IOException {
        int index = c.indexOf(' ');
        String command = ((index == -1)? c.toUpperCase(): (c.substring(0,index)).toUpperCase());
        String args = ((index == -1)? null : c.substring(index + 1,c.length()));

        debugOutPut("Command " +command + "  Args : "+ args);

        switch (command){
            case "USER":
                handlerUser(args);
                break;
            case "PASS":
                handlerPass(args);
            case "PWD":
                handlerPwd();
                break;
            case "QUIT":
                handlerQuit();
                break;
            case "CWD":
                handlerCwd(args);
                break;
            default:
                sendMessageToClient("501 Command inconnue");
                break;
        }
    }

    private void handlerCwd(String args) throws IOException {
        String filename = currentDir;

        // cas de cwd ..
        if(args.equals("..")){
             int ind= filename.lastIndexOf(separateur);
             if(ind > 0){
                 filename = filename.substring(0,ind);
             }
        }

        // cas de cwd . qui ne fait rien
        else if((args!= null ) && !args.equals(".")){
            filename = filename + separateur + args;
        }

        File f = new File(filename);
        if(f.exists() && f.isDirectory() && (filename.length() >= root.length())){
            currentDir = filename;
            sendMessageToClient("250 The current  directory has been changed to "+ currentDir);
        }
        else{
            sendMessageToClient("550 File unavailable");
        }
    }

    private void handlerQuit() throws IOException {
        sendMessageToClient("221 Closing connection ");
        quitCommandLoop = true;
    }

    private void handlerPwd() throws IOException {
        sendMessageToClient("257 \""+ currentDir + "\"");
    }

    private void handlerPass(String passWord) throws IOException {
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

    private void handlerUser(String username) throws IOException {
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

    private void sendMessageToClient(String s) throws IOException {
        //clientOutWriter.println(s);
        clientOut.writeBytes(s+ "\r\n");
    }


}
