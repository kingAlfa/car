package ftp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Ftp {
    private String user;
    private Socket socket = null,socketData = null;
    private boolean DEBUG = true;
    private String host;
    private int port;

    private BufferedWriter writer, writerData;
    private BufferedInputStream readerData;
    private BufferedInputStream reader;
    private String dataIp;
    private int dataPort;

    public Ftp(String ipAddress, int pPort, String pUser){
        user = pUser;
        port = pPort;
        host = ipAddress;

    }

    public Ftp(String pUser){
        this("127.0.0.1",21,pUser);
    }

    public void connect() throws IOException {
        if(socket != null)
            throw new IOException("La connexion au FTP est deja activée");

        socket = new Socket(host,port);

        reader = new BufferedInputStream(socket.getInputStream());
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        String response = read();

        if(!response.startsWith("220")){
            throw new IOException("Erreur de connexion au FTP : \n"+response);
        }
        send("USER "+user);
        response = read();

        if(!response.startsWith("331")){
            throw new IOException("Erreur de connexion avec le compte : \n"+response);
        }

        //Pour le moment je ne fais pas de gestion de mot de passe
        String passwd = "";
        send("PASS"+passwd);
        response = read();
        if(!response.startsWith("230")){
            throw new IOException("Erreur de connexion avec le compte Utilisateur "+response);
        }
    } //fin de la methode connection

    public void createDataSocket() throws UnknownHostException, IOException{
        socketData = new Socket(dataIp,dataPort);
        readerData = new BufferedInputStream(socketData.getInputStream());
        writerData = new BufferedWriter(new OutputStreamWriter(socketData.getOutputStream()));
    }
    public void quit(){
        try{
            send("QUIT");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                try{
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    socket = null;
                }
            }
        }
    }

    public String cwd(String dir) throws IOException{
       send("CWD " + dir);
       return read();
    }

    public String pwd() throws IOException{
        send("PWD");
        return read();
    }


    private void send(String command) throws IOException{
        command +="\r\n";
        if(DEBUG)
            log(command);
        writer.write(command);
        writer.flush();
    }

    private String read() throws IOException{
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b,0,stream);
        
        if(DEBUG)
            log(response);
        return response;
    }

    public String readData() throws IOException{
        String response = "";
        byte[] b = new byte[1024];
        int stream ;
        while((stream =readerData.read(b)) != -1){
            response += new String(b,0,stream);
        }
        if(DEBUG)
            log(response);
        return response;
    }

    private void log(String response) {
        System.out.println(">> "+response);
    }

    public void debugMode(boolean active){
        DEBUG = active;
    }

}
