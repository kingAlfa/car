package ftp;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            Ftp ftp = new Ftp("barry");
            System.out.println("Connexion au FTP");

            ftp.connect();
            ftp.debugMode(true);

            System.out.println("==================================================");
            System.out.println("Vous etes maintenant  connecté au FTP");
            System.out.println("Vous avez droit au commands QUIT et PWD");
            System.out.println("==================================================");

            String respponse = "";
            boolean encore = true;

            while (encore){
                respponse = sc.nextLine().toUpperCase();

                switch (respponse){
                    case "PWD":
                        System.out.println(ftp.pwd());
                        break;
                    case "QUIT":
                        ftp.quit();
                        encore = false;
                        break;
                    default: System.err.println("Command inconnue !");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("BYE...");
    }
}
