package actors;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import utils.Count;
import utils.DataMap;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Mapper extends UntypedAbstractActor {

    private  ActorRef reducer;

    public Mapper(ActorRef reducer){
        this.reducer=reducer;
    }


    @Override
    public void onReceive(Object message) {
        if (message instanceof  String){
            String msg = (String) message;
            DataMap dataMap =  evalutateWorld(msg);

            reducer.tell(dataMap,getSelf());
        }
        else {
            unhandled(message);
        }
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
