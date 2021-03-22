import java.util.Collections;
import java.util.Map;

public class DataReduce
{
    private Map<String, Integer> reduceDataList;

    public Map<String, Integer> getReduceDataList() {
        return reduceDataList;
    }

    public DataReduce(Map<String, Integer> reduceDataList) {
        this.reduceDataList = Collections.unmodifiableMap(reduceDataList);
    }
}
