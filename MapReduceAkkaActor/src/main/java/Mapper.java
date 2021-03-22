import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;

import java.util.List;

public class Mapper extends UntypedAbstractActor {

    private ActorRef reducer;
    
    @Override
    public void onReceive(Object message) throws Throwable {

    }


}
