import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

import java.util.List;

public class Mapper3 extends UntypedAbstractActor implements Mapper{
    @Override
    public void onReceive(Object message) throws Throwable {

    }

    @Override
    public ActorRef partition(List<Reducer> r, String mot) {
        return null;
    }
}
