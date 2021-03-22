import java.util.Collections;
import java.util.List;

public class DataMap
{
    private List<Count> dataList;

    public List<Count> getDataList() {
        return dataList;
    }

    public DataMap(List<Count> dataList) {
        this.dataList = Collections.unmodifiableList(dataList);
    }
}
