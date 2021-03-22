package actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import utils.Count;
import utils.DataMap;

import java.util.*;

public class Mapper extends UntypedAbstractActor {

    private  ActorRef reducer;
    private  ActorRef reducer2;
    private List<ActorRef> reducerList =new ArrayList<>();
    private Map<ActorRef,String> mapActor = new HashMap<>();

    public Mapper(ActorRef reducer,ActorRef reducer2){
        this.reducer=reducer;
        this.reducer2=reducer2;
        this.reducerList.add(reducer);
        this.reducerList.add(reducer2);

    }


    @Override
    public void onReceive(Object message) {
        if (message instanceof  String){
            String msg = (String) message;
            DataMap dataMap =  evalutateWorld(msg);
            ActorRef red = this.partition(this.reducerList,msg);
            red.tell(dataMap,getSelf());
        }
        else {
            unhandled(message);
        }
    }

    private ActorRef partition(List<ActorRef> reducerList, String msg) {
        ActorRef redActor = null;
        Random random = new Random();
        for (ActorRef actorRef:reducerList){
            int i = random.nextInt(reducerList.size());
            redActor = reducerList.get(i);
            mapActor.put(redActor,msg);
        }
        return redActor;
    }

    private DataMap evalutateWorld(String msg) {
        List<Count> dataList = new ArrayList<>();
        StringTokenizer parser = new StringTokenizer(msg);
        while (parser.hasMoreTokens()) {
            String word = parser.nextToken().toLowerCase();
            dataList.add(new Count(word, 1));

        }
        return new DataMap(dataList);
    }



}
