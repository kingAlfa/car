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
    private StatusUser currentUserStatus = StatusUser.NOTLOGGEDION;
    private String UserValid = "Barry";
    private String PassWord  = "Car2021";

    private int dataPort;
    private typeDeTransfert modeDeTransfer = typeDeTransfert.ASCII;
    private boolean quitCommandLoop = false;

    public ServerService(Socket soClient, int dataPort){
        super();
        this.clientSocket = soClient;
        this.dataPort = dataPort;
        this.currentDir = System.getProperty("user.dir")+"/data";
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
        debugOutPut("LA REQUETE "+c);
        int index = c.indexOf(' ');
        String command = ((index == -1)? c.toUpperCase(): (c.substring(0,index)).toUpperCase()).trim();
        String args = ((index == -1)? null : c.substring(index + 1,c.length()));
        debugOutPut("Command " +command + "  Args : "+ args);


        if(command.equals("USER")){
            assert args != null;
            handlerUser(args);
        }
        else if(command.equals("PASS")){
            handlerPass(args);
        }
        else if(command.equals("PWD")){
            handlerPwd();
        }
        else if(command.equals("QUIT")){
            handlerQuit();
        }
        else if(command.equals("CWD")){
            assert args != null;
            controlCwd(args);
        }
        else if(command.equals("RETR")){
            handleRetr(args);
        }
        else{
            sendMessageToClient("501 Command inconnue");
        }

    }

    private void handleRetr(String args) throws IOException {
        File f = new File(currentDir + separateur + args);

        if(!f.exists()){
            sendMessageToClient("550 File does not exist");
        }

        else{
            //binary mode
            if(modeDeTransfer == typeDeTransfert.BINARY){
                BufferedOutputStream fout = null;
                BufferedInputStream fIn= null;

                sendMessageToClient("150 Opening binary mode transfert for the file "+f.getName());

                try{
                    //create streams

                }catch (Exception e){
                    debugOutPut("Could not create file!");
                }
            }
        }
    }

    private void controlCwd(String args) throws IOException {
        String filename = currentDir;

        // cas de cwd ..
        if(args.equals("..")){
             int ind= filename.lastIndexOf(separateur);
             if(ind > 0){
                 filename = filename.substring(0,ind);
             }
        }

        // cas de cwd . qui ne fait rien
        else if((args!= null ) && (!args.equals("."))){
            filename = filename + separateur + args;
        }

        File f = new File(filename);
        if(f.exists() && f.isDirectory() && (filename.length() >= root.length())){
            currentDir = filename;
            sendMessageToClient("250 The current  directory has been changed to "+ currentDir);
            //clientOut.writeBytes("250 The current  directory has been changed to "+ currentDir );
        }
        else{
            sendMessageToClient("550 File unavailable");
           // clientOut.writeBytes("550 file unavailable");
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
        if(currentUserStatus == StatusUser.ENTEREDUSERNAME && passWord.equals(PassWord)){
            currentUserStatus = StatusUser.LOGGEDIN;
            sendMessageToClient("230 welcome to FTP-SERVER");
            sendMessageToClient("230 User logged well");
        }
        else if(currentUserStatus == StatusUser.LOGGEDIN){
            sendMessageToClient("530 already logged");
        }
        else{
            sendMessageToClient("530 not logged");
        }
    }

    private void handlerUser(String username) throws IOException {
        if(username.equalsIgnoreCase(UserValid)){
            sendMessageToClient("331 User name Ok, know the password ? ");
            currentUserStatus = StatusUser.ENTEREDUSERNAME;
        }
        else if(currentUserStatus == StatusUser.LOGGEDIN){
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
