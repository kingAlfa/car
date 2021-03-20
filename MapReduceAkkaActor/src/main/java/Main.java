import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main
{
    public static void main(String[] args) {
        System.out.println("Hello World");

        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef greet1,greet2;

        greet1 = system.actorOf(Props.create(GreetingActor.class),"greet1");
        greet2 = system.actorOf(Props.create(GreetingActor.class),"greet2");

        greet1.tell(new Greeting("Bob"),ActorRef.noSender());
        greet2.tell("Alpha",ActorRef.noSender());
    }
}
