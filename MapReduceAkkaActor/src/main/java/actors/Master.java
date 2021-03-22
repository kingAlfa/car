package actors;

import actors.Mapper;
import actors.Reducer;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import utils.Messages;

import java.util.Random;

public class Master extends UntypedAbstractActor {

    //Creation des mapper
    private  ActorRef mapper1 = getContext().actorOf(Props.create(Mapper.class),"mapper1");
    private  ActorRef mapper2 = getContext().actorOf(Props.create(Mapper.class),"mapper2");
    private  ActorRef mapper3 = getContext().actorOf(Props.create(Mapper.class),"mapper3");

    //Creation des reducers
    private  ActorRef reducer1 = getContext().actorOf(Props.create(Reducer.class),"reducer1");
    private  ActorRef reducer2 = getContext().actorOf(Props.create(Reducer.class),"reducer2");

    //Creation de l'acteur aggregate
    private  ActorRef aggregater = getContext().actorOf(Props.create(Aggregater.class),"aggregater");

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof String){
            ActorRef mapper = partition();
            mapper.tell(message,getSelf());
        }
        else if (message instanceof Messages){
            aggregater.tell(message,getSelf());
        }
        else{
            unhandled(message);
        }
    }

    private ActorRef partition() {
        Random random = new Random();
        int i = random.nextInt(3);
        if (i==0) return mapper1;
        else if (i==2) return mapper2;
        else return mapper3;
    }
}
