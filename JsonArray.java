import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonArray extends JsonValue implements Iterable<JsonObject> {
    private List<JsonObject> objects = new ArrayList<>();

    public void add(JsonObject jsonObject) {
        objects.add(jsonObject);
    }

    @Override
    public String toString() {
        return objects.toString();
    }

    @Override
    public Iterator<JsonObject> iterator() {
        return objects.iterator();
    }
}