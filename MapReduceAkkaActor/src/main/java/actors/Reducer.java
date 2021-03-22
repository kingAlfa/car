package actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import utils.Count;
import utils.DataMap;
import utils.DataReduce;

import java.util.HashMap;
import java.util.List;

public class Reducer extends UntypedAbstractActor  {
    private ActorRef aggregate;

    public Reducer(ActorRef aggregate){
        this.aggregate=aggregate;
    }


    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof DataMap){
            DataMap dataMap = (DataMap) message;
            DataReduce dataReduce = reduce(dataMap.getDataList());
            aggregate.tell(dataReduce,getSelf());
        }
        else{
            unhandled(message);
        }
    }

    private DataReduce reduce(List<Count> dataList) {
        HashMap<String, Integer> reducedMap = new HashMap<>();
        for (Count wordCount : dataList) {
            if (reducedMap.containsKey(wordCount.getWord())) {
                Integer value = reducedMap.get(wordCount.getWord());
                value++;
                reducedMap.put(wordCount.getWord(), value);
            } else {
                reducedMap.put(wordCount.getWord(), 1);
            }
        }
        return new DataReduce(reducedMap);
    }

}
