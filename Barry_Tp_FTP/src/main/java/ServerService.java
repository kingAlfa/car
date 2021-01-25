import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class ServerService extends Thread {

    // Path information
    private final String root;
    private String currDirectory;
    private final String fileSeparator = "/";


    // control connection
    private final Socket controlSocket;
    private PrintWriter controlOutWriter;
    private BufferedReader controlIn;


    // data Connection
    private ServerSocket dataSocket;
    private Socket dataConnection;
    private PrintWriter dataOutWriter;


    private TypeDeTransfert transferMode = TypeDeTransfert.ASCII;


    // user properly logged in?
    private StatusUser currentUserStatus = StatusUser.NOTLOGGEDIN;
    private String validUser;

    // user map that will contain the user and their password
    private final HashMap<String,String> userMap;

    //This variable will determine when the server will be stopped
    private boolean quitCommandLoop = false;

    public ServerService(Socket soClient,HashMap<String,String> usermap) {
        super();
        this.controlSocket = soClient;
        this.currDirectory = System.getProperty("user.dir")+"/data";
        this.root = System.getProperty("user.dir");

        this.userMap= usermap;

    }

    /**
     * This methode make run the server and it's belongs to the Thread
     *
     */
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
     * Printing the thread id and command from the client
     * @param s the command line from client
     */
    private void debugOutPut(String s) {
        System.out.println("Thread " + this.getId() + ": " + s);
    }

    /**
     * Manage all the command that received from client
     * @param c the command from client
     */
    private void executeCommand(String c) {
        //split
        int index = c.indexOf(' ');
        String command = ((index == -1)? c.toUpperCase() : (c.substring(0, index)).toUpperCase());
        String args = ((index == -1)? null : c.substring(index+1));

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
                assert args != null;
                handleType(args);
                break;

            case "PORT":
                assert args != null;
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

                for (String s : dirContent) {
                    sendDataMsgToClient(s);
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
     * Manage the command put
     * The command put will put the given file in the current directory.
     * This function can switch with ASCII or BINARY mode
     * @param file the given file
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
                    int l;
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
     * Extract the IP address and port number from arguments that's given by client
     * @param args given argument
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
     * Create the connection with active mode to allow transferring data
     * @param ipAddress the given IP address
     * @param port the given port
     */
    private void openDataConnectionActive(String ipAddress, int port)
    {
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
     * Switch the transferring mode between ASCII or BINARY mode
     * @param mode the transferring mode
     */
    private void handleType(String mode)
    {
        if(mode.equalsIgnoreCase("A"))
        {
            transferMode = TypeDeTransfert.ASCII;
            sendMsgToClient("200 OK");
        }
        else if(mode.equalsIgnoreCase("I"))
        {
            transferMode = TypeDeTransfert.BINARY;
            sendMsgToClient("200 OK");
        }
        else
            sendMsgToClient("504 Not OK");

    }

    /**
     * Manage the get command
     * @param file the given file
     */
    private void handleRetr(String file)
    {
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
                int l ;
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

    /**
     * Close the connexion when the transfer is finished
     */
    private void closeDataConnection()
    {
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
     * Manage the command user
     * User name must be contained in the user Map
     * @param username the user name
     */
    private void handleUser(String username)
    {
        if (userMap.containsKey(username))
        {
            sendMsgToClient("331 User name okay, need password");
            currentUserStatus = StatusUser.ENTEREDUSERNAME;
            validUser = username;
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
     * Manage user password
     * The user name must be in user map
     * @param password the password of the current user
     */
    private void handlePass(String password)
    {
        // User has entered a valid username and password is correct
        String pass = userMap.get(validUser);

        if (currentUserStatus == StatusUser.ENTEREDUSERNAME && password.equals(pass))
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
     * Change the working directory
     * @param args the new directory
     */
    private void handleCwd(String args)
    {
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
     * Print working directory
     */
    private void handlePwd()
    {
        sendMsgToClient("257 \"" + currDirectory + "\"");
    }

    /**
     * Close the server and exit
     */
    private void handleQuit()
    {
        sendMsgToClient("221 Closing connection");
        quitCommandLoop = true;
    }

    /**
     * Print the message to the client from the server
     * @param s response message
     */
    private void sendMsgToClient(String s) {
        controlOutWriter.println(s);
    }

}