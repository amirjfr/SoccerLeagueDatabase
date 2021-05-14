package delegates;

import javafx.util.Pair;
import ui.CustomQuery;

import java.util.List;

public interface OperationTabDelegate {
    String dmlQuery(CustomQuery customQuery);

    Pair<String, List<List<String>>> query(CustomQuery customQuery);
}
