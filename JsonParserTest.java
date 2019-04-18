import java.io.ByteArrayInputStream;

public class JsonParserTest {
    public static void main(String args[]) throws Exception {
        // String sampleJSON = "[{\"name\": {\"firstName\":\"Kishu\", \"lastName\": \"Agarwal\"}},{\"name\": {\"firstName\":\"Kishu\", \"lastName\": \"Agarwal\"}}]";
        String sampleJSON = "[{\"name\":{\"first_name\":\"kishu\",\"last_name\":\"agarwal\",\"age\":26}}]";
        JsonValue jsonArray = (JsonArray) Parser.parse(new ByteArrayInputStream(sampleJSON.getBytes()));
        System.out.println(jsonArray);
    }
}