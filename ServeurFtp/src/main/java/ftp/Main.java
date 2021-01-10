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

            System.out.println("Vous etes connecté");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("BYE...");
    }
}
