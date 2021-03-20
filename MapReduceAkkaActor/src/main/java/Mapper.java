import akka.actor.ActorRef;

import java.util.List;

public interface Mapper
{
    public ActorRef partition(List<Reducer> r, String mot);
}
