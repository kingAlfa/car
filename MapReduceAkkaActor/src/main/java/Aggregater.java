import akka.actor.UntypedAbstractActor;


import java.util.HashMap;
import java.util.Map;

public class Aggregater extends UntypedAbstractActor {

    private final Map<String,Integer> reduceMap = new HashMap<>();

    @Override
    public void onReceive(Object message) {
        if (message instanceof DataReduce ){
            DataReduce reduce = (DataReduce) message;
            countData(reduce.getReduceDataList());
        }
        else if (message instanceof Messages){
            System.out.println(reduceMap.toString());
        }
        else{
            unhandled(message);
        }
    }

    private void countData(Map<String, Integer> reducedList) {
        int count;
        for (String key : reducedList.keySet()) {
            if (reduceMap.containsKey(key)) {
                count = reducedList.get(key) + reduceMap.get(key);
                reduceMap.put(key, count);
            } else {
                reduceMap.put(key, reducedList.get(key));
            }
        }
    }
}
