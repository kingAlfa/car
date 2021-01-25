# Serveur FTP
## Autor
*Barry Mamadou*
## Goal
It is a question of carrying out a program server Ftp. This server must be able to be 
used by the command line ftp client
### Work to be done
- implement a scenario that allows the user to authenticate by providing a login and a password
- Implement the QUIT command to disconnect
- Implement the commands dir, get, put, ls and cd
### Running The server
- Start the ftp server via the file [The jar file](out/artifacts/Barry_Tp_FTP_jar/Barry_Tp_FTP.jar) 
with the command **java -jar name_file.jar**
- From the terminal launch the ftp client on localhost, and the port number given by the server
- So you can use the commands available on the server
### Extension
To add a command, it is a question of specifying in the method executeCommand the name of the 
command and then of implementing the method which manages this command. A more objective solution 
is to implement the COMMAND design pattern. In this case we could extend the application more easily. 