import actors.Master;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("AkkaMapReduce");

        ActorRef master = system.actorOf(Props.create(Master.class),"master");

        final String fileName = "file.txt";
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null){
                master.tell(line,master);
                line= reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.sleep(500);

    }
}
