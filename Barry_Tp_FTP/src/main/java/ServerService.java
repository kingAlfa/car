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
            case "TYPE":
                handleType(args);
                break;
            case "PORT":
                handlePort(args);
                break;
            case "STOR":
                handleStor(args);
                break;
            case "LIST":
                handleNlst(args);
                break;

            default:
                sendMsgToClient("501 Unknown command");
                break;
        }
    }

    /**
     * Handler for NLST (Named List) command.
     * Lists the directory content in a short format (names only)
     * @param args The directory to be listed
     */
    private void handleNlst(String args)
    {
        if (dataConnection == null || dataConnection.isClosed())
        {
            sendMsgToClient("425 No data connection was established");
        }
        else
        {

            String[] dirContent = nlstHelper(args);

            if (dirContent == null)
            {
                sendMsgToClient("550 File does not exist.");
            }
            else
            {
                sendMsgToClient("125 Opening ASCII mode data connection for file list.");

                for (int i = 0; i < dirContent.length; i++)
                {
                    sendDataMsgToClient(dirContent[i]);
                }

                sendMsgToClient("226 Transfer complete.");
                closeDataConnection();

            }

        }

    }

    /**
     * Send a message to the connected client over the data connection.
     * @param msg Message to be sent
     */
    private void sendDataMsgToClient(String msg)
    {
        if (dataConnection == null || dataConnection.isClosed())
        {
            sendMsgToClient("425 No data connection was established");
            debugOutPut("Cannot send message, because no data connection is established");
        }
        else
        {
            dataOutWriter.print(msg + '\r' + '\n');
        }

    }
    /**
     * A helper for the NLST command. The directory name is obtained by
     * appending "args" to the current directory
     * @param args The directory to list
     * @return an array containing names of files in a directory. If the given
     * name is that of a file, then return an array containing only one element
     * (this name). If the file or directory does not exist, return nul.
     */
    private String[] nlstHelper(String args)
    {
        // Construct the name of the directory to list.
        String filename = currDirectory;
        if (args != null)
        {
            filename = filename + fileSeparator + args;
        }


        // Now get a File object, and see if the name we got exists and is a
        // directory.
        File f = new File(filename);

        if (f.exists() && f.isDirectory())
        {
            return f.list();
        }
        else if (f.exists() && f.isFile())
        {
            String[] allFiles = new String[1];
            allFiles[0] = f.getName();
            return allFiles;
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param file
     */
    private void handleStor(String file) {
        if (file == null)
        {
            sendMsgToClient("501 No filename given");
        }
        else
        {
            File f =  new File(currDirectory + fileSeparator + file);

            if(f.exists())
            {
                sendMsgToClient("550 File already exists");
            }

            else
            {

                // Binary mode
                if (transferMode == TypeDeTransfert.BINARY)
                {
                    BufferedOutputStream fout = null;
                    BufferedInputStream fin = null;

                    sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());

                    try
                    {
                        // create streams
                        fout = new BufferedOutputStream(new FileOutputStream(f));
                        fin = new BufferedInputStream(dataConnection.getInputStream());
                    }
                    catch (Exception e)
                    {
                        debugOutPut("Could not create file streams");
                    }

                    debugOutPut("Start receiving file " + f.getName());

                    // write file with buffer
                    byte[] buf = new byte[1024];
                    int l = 0;
                    try
                    {
                        while (true)
                        {
                            assert fin != null;
                            if ((l = fin.read(buf, 0, 1024)) == -1) break;
                            fout.write(buf,0,l);
                        }
                    }
                    catch (IOException e)
                    {
                        debugOutPut("Could not read from or write to file streams");
                        e.printStackTrace();
                    }

                    //close streams
                    try
                    {
                        fin.close();
                        fout.close();
                    } catch (IOException e)
                    {
                        debugOutPut("Could not close file streams");
                        e.printStackTrace();
                    }


                    debugOutPut("Completed receiving file " + f.getName());

                }

                // ASCII mode
                else
                {
                    sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());

                    BufferedReader rin = null;
                    PrintWriter rout = null;

                    try
                    {
                        rin = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
                        rout = new PrintWriter(new FileOutputStream(f),true);

                    }
                    catch (IOException e)
                    {
                        debugOutPut("Could not create file streams");
                    }

                    String s;

                    try
                    {
                        while(true)
                        {
                            assert rin != null;
                            if ((s = rin.readLine()) == null) break;
                            assert rout != null;
                            rout.println(s);
                        }
                    } catch (IOException e)
                    {
                        debugOutPut("Could not read from or write to file streams");
                        e.printStackTrace();
                    }

                    try
                    {
                        assert rout != null;
                        rout.close();
                        rin.close();
                    } catch (IOException e)
                    {
                        debugOutPut("Could not close file streams");
                        e.printStackTrace();
                    }
                }
                sendMsgToClient("226 File transfer successful. Closing data connection.");

            }
            closeDataConnection();
        }
    }

    /**
     *
     * @param args
     */
    private void handlePort(String args) {
        // Extract IP address and port number from arguments
        String[] stringSplit = args.split(",");
        String hostName = stringSplit[0] + "." + stringSplit[1] + "." +
                stringSplit[2] + "." + stringSplit[3];

        int p = Integer.parseInt(stringSplit[4])*256 + Integer.parseInt(stringSplit[5]);

        // Initiate data connection to client
        openDataConnectionActive(hostName, p);
        sendMsgToClient("200 Command OK");
    }

    /**
     *
     * @param ipAddress
     * @param port
     */
    private void openDataConnectionActive(String ipAddress, int port) {
        try
        {
            dataConnection = new Socket(ipAddress, port);
            dataOutWriter = new PrintWriter(dataConnection.getOutputStream(), true);
            debugOutPut("Data connection - Active Mode - established");
        } catch (IOException e)
        {
            debugOutPut("Could not connect to client data socket");
            e.printStackTrace();
        }
    }

    /**
     *
     * @param mode
     */
    private void handleType(String mode)
    {
        if(mode.toUpperCase().equals("A"))
        {
            transferMode = TypeDeTransfert.ASCII;
            sendMsgToClient("200 OK");
        }
        else if(mode.toUpperCase().equals("I"))
        {
            transferMode = TypeDeTransfert.BINARY;
            sendMsgToClient("200 OK");
        }
        else
            sendMsgToClient("504 Not OK");;

    }

    /**
     *
     * @param file
     */
    private void handleRetr(String file) {
        File f =  new File(currDirectory + fileSeparator + file);

        if(!f.exists())
        {
            sendMsgToClient("550 File does not exist");
        }

        else
        {

            // Binary mode
            if (transferMode == TypeDeTransfert.BINARY)
            {
                BufferedOutputStream fout = null;
                BufferedInputStream fin = null;

                sendMsgToClient("150 Opening binary mode data connection for requested file " + f.getName());

                try
                {
                    //create streams
                    fout = new BufferedOutputStream(dataConnection.getOutputStream());
                    fin = new BufferedInputStream(new FileInputStream(f));
                }
                catch (Exception e)
                {
                    debugOutPut("Could not create file streams");
                }

                debugOutPut("Starting file transmission of " + f.getName());

                // write file with buffer
                byte[] buf = new byte[1024];
                int l = 0;
                try
                {
                    while ((l = fin.read(buf,0,1024)) != -1)
                    {
                        fout.write(buf,0,l);
                    }
                }
                catch (IOException e)
                {
                    debugOutPut("Could not read from or write to file streams");
                    e.printStackTrace();
                }

                //close streams
                try
                {
                    fin.close();
                    fout.close();
                } catch (IOException e)
                {
                    debugOutPut("Could not close file streams");
                    e.printStackTrace();
                }


                debugOutPut("Completed file transmission of " + f.getName());

            }

            // ASCII mode
            else
            {
                sendMsgToClient("150 Opening ASCII mode data connection for requested file " + f.getName());

                BufferedReader rin = null;
                PrintWriter rout = null;

                try
                {
                    rin = new BufferedReader(new FileReader(f));
                    rout = new PrintWriter(dataConnection.getOutputStream(),true);

                }
                catch (IOException e)
                {
                    debugOutPut("Could not create file streams");
                }

                String s;

                try
                {
                    while((s = rin.readLine()) != null)
                    {
                        assert rout != null;
                        rout.println(s);
                    }
                } catch (IOException e)
                {
                    debugOutPut("Could not read from or write to file streams");
                    e.printStackTrace();
                }

                try
                {
                    assert rout != null;
                    rout.close();
                    rin.close();
                } catch (IOException e)
                {
                    debugOutPut("Could not close file streams");
                    e.printStackTrace();
                }
            }
            sendMsgToClient("226 File transfer successful. Closing data connection.");

        }
        closeDataConnection();
    }

    /**
     *
     */
    private void closeDataConnection() {
        try
        {
            dataOutWriter.close();
            dataConnection.close();
            if (dataSocket != null)
            {
                dataSocket.close();
            }


            debugOutPut("Data connection was closed");
        } catch (IOException e)
        {
            debugOutPut("Could not close data connection");
            e.printStackTrace();
        }
        dataOutWriter = null;
        dataConnection = null;
        dataSocket = null;
    }

    /**
     *
     * @param username
     */
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

    /**
     *
     * @param password
     */
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

    /**
     *
     * @param args
     */
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
        File f = new File(filename);
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

    /**
     *
     */
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