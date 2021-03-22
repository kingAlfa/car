import actors.Master;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class MainRemote
{
    public static void main(String[] args) {
        final String fileName = "file.txt";
        ActorSystem system = ActorSystem.create("AppRemote");

        final ActorRef masterActor = system.actorOf(Props.create(Master.class),"master");

        final ActorSelection  master = system.actorSelection("akka.tcp//MapReduceAkkaActor@localhost:2252/user/master");
        
    }
}
