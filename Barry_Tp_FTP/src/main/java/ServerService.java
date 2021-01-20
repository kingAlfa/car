import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerService extends Thread {

    private final boolean debugMode = true;

    // Path information
    private String root;
    private String currDirectory;
    private String fileSeparator = "/";


    // control connection
    private Socket controlSocket;
    private PrintWriter controlOutWriter;
    private BufferedReader controlIn;


    // data Connection
    private ServerSocket dataSocket;
    private Socket dataConnection;
    private PrintWriter dataOutWriter;

    private int dataPort;
    private TypeDeTransfert transferMode = TypeDeTransfert.ASCII;


    // user properly logged in?
    private StatusUser currentUserStatus = StatusUser.NOTLOGGEDIN;
    private String validUser = "barry";
    private String validPassword = "c123";

    private boolean quitCommandLoop = false;


    public ServerService(Socket soClient, int dataPort) {
        super();
        this.controlSocket = soClient;
        this.dataPort = dataPort;
        this.currDirectory = System.getProperty("user.dir")+"/data";
        this.root = System.getProperty("user.dir");

    }

    public void run(){
        try{
            //Input from client
            controlIn = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));

            //output to client
            controlOutWriter = new PrintWriter(controlSocket.getOutputStream(),true);

            //Greeting
            sendMsgToClient("220 welcome to BARRY FTP-SERVER");

            //get new command from client
            while (!quitCommandLoop){
                executeCommand(controlIn.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //clean up
            try{
                controlIn.close();
                controlOutWriter.close();
                controlSocket.close();

                debugOutPut("Sockets closed !!!");
            } catch (IOException e) {
                e.printStackTrace();
                debugOutPut("Could not closed sockets !!!");
            }
        }
    }

    /**
     *
     * @param s
     */
    private void debugOutPut(String s) {
        if (debugMode)
        {
            System.out.println("Thread " + this.getId() + ": " + s);
        }
    }

    /**
     *
     * @param c
     */
    private void executeCommand(String c) {
        //split
        int index = c.indexOf(' ');
        String command = ((index == -1)? c.toUpperCase() : (c.substring(0, index)).toUpperCase());
        String args = ((index == -1)? null : c.substring(index+1, c.length()));

        debugOutPut("Command : "+command+ " Args : "+args);
        switch (command){
            case "USER":
                assert args != null;
                handleUser(args);
                break;

            case "PASS":
                handlePass(args);
                break;

            case "CWD":
                assert args != null;
                handleCwd(args);
                break;
            case "PWD":
                handlePwd();
                break;
            case "RETR":
                handleRetr(args);
                break;    
            case "QUIT":
                handleQuit();
                break;
                
            default:
                sendMsgToClient("501 Unknown command");
                break;
        }
    }

    private void handleRetr(String args) {
    }

    private void handleUser(String username) {
        if (username.toLowerCase().equals(validUser))
        {
            sendMsgToClient("331 User name okay, need password");
            currentUserStatus = StatusUser.ENTEREDUSERNAME;
        }
        else if (currentUserStatus == StatusUser.LOGGEDIN)
        {
            sendMsgToClient("530 User already logged in");
        }
        else
        {
            sendMsgToClient("530 Not logged in");
        }
    }

    private void handlePass(String password) {
        // User has entered a valid username and password is correct
        if (currentUserStatus == StatusUser.ENTEREDUSERNAME && password.equals(validPassword))
        {
            currentUserStatus = StatusUser.LOGGEDIN;
            sendMsgToClient("230-Welcome to Alpha");
            sendMsgToClient("230 User logged in successfully");
        }

        // User is already logged in
        else if (currentUserStatus == StatusUser.LOGGEDIN)
        {
            sendMsgToClient("530 User already logged in");
        }

        // Wrong password
        else
        {
            sendMsgToClient("530 Not logged in");
        }
    }

    private void handleCwd(String args) {
        String filename = currDirectory;

        // go one level up (cd ..)

        if (args.equals(".."))
        {
            int ind = filename.lastIndexOf(fileSeparator);
            if (ind > 0)
            {
                filename = filename.substring(0, ind);
            }
        }

        // if argument is anything else (cd . does nothing)
        else
        {
            filename = filename + fileSeparator + args;
        }

        // check if file exists, is directory and is not above root directory
        debugOutPut(">> DEBUG filename : "+filename);
        File f = new File(filename);
        debugOutPut(">> DEBUG FILE exist :"+f.exists()+"   isDir :"+f.isDirectory()+ " length :"+(f.length() >=root.length()));
        if ((f.exists() && f.isDirectory()) && (filename.length() >= root.length()))
        {
            currDirectory = filename;
            sendMsgToClient("250 The current directory has been changed to " + currDirectory);
        }
        else
        {
            sendMsgToClient("550 Requested action not taken. File unavailable.");
        }
    }

    private void handlePwd() {
        sendMsgToClient("257 \"" + currDirectory + "\"");
    }

    private void handleQuit() {
        sendMsgToClient("221 Closing connection");
        quitCommandLoop = true;
    }

    /**
     *
     * @param s
     */
    private void sendMsgToClient(String s) {
        controlOutWriter.println(s);
    }

}