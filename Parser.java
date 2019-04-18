import java.io.InputStream;
import java.io.PushbackInputStream;

public class Parser {

    /**
     * Get the next non-whitespace byte in the input stream
     * @param jsonStream
     * @return
     * @throws Exception
     */
    private static int getNextByte(PushbackInputStream jsonStream) throws Exception {
        int b = jsonStream.read();
        while (b == ' ') {
            b = jsonStream.read();
        }
        return b;
    }

    private static JsonArray parseJsonArray(PushbackInputStream jsonStream) throws Exception  {
        int b = getNextByte(jsonStream);
        assert b == '[';

        JsonArray jsonArray = new JsonArray();

        do {
            jsonArray.add(parseJsonObject(jsonStream));
            b = getNextByte(jsonStream);
        } while (b == ',');

        b = getNextByte(jsonStream);
        assert b == ']';

        System.out.println("Parsed Json Array");
        return jsonArray;
    }

    /**
     * Parse a string value in the input stream wrapped inside double quotes
     * @param jsonStream
     * @return
     * @throws Exception
     */
    private static String parseString(PushbackInputStream jsonStream) throws Exception  {
        StringBuilder builder = new StringBuilder();
        int b = getNextByte(jsonStream);
        assert b == '\"';

        b = jsonStream.read();
        while (b != '\"') {
            builder.append((char) b);
            b = jsonStream.read();
        }

        System.out.println("Parsed String: " + builder.toString());
        return builder.toString();
    }

    /**
     * Parse a int value in the input stream surrounded by non-integer values
     * @param jsonStream
     * @return
     * @throws Exception
     */
    private static int parseNumber(PushbackInputStream jsonStream) throws Exception  {
        int num = 0;
        int b = getNextByte(jsonStream);

        assert b >= '0' && b <= '9';

        do {
            num = 10 * num + (b - '0');
            b = jsonStream.read();
        } while (b >= '0' && b <= '9');

        // We have read an extra byte from input stream so push it back
        jsonStream.unread(b);

        System.out.println("Parsed integer: " + num);
        return num;
    }

    /**
     * Parse json object from the input stream. Json object is surrounded by curly braces.
     * @param jsonStream
     * @return
     * @throws Exception
     */
    private static JsonObject parseJsonObject(PushbackInputStream jsonStream) throws Exception {
        int b = getNextByte(jsonStream);
        assert b == '{';

        JsonObject jsonObject = new JsonObject();

        // Each json object has possibly many key value pairs with each pair separated by ',' from each other
        // and key value separated by ':'

        do {
            String key = parseString(jsonStream);

            b = getNextByte(jsonStream);
            assert b == ':';

            Object value = parseJsonValue(jsonStream);

            jsonObject.add(key, value);

            b = getNextByte(jsonStream);
        } while (b == ',');

        assert b == '}';

        System.out.println("Parsed Json Object: " + jsonObject);
        return jsonObject;
    }

    /**
     * JsonValue could be either Jsonobject, JsonArray, String, or integer value
     * @param jsonStream
     * @return
     * @throws Exception
     */
    private static Object parseJsonValue(PushbackInputStream jsonStream) throws Exception {
        int firstByte = getNextByte(jsonStream);
        if (firstByte == '\"') {
            jsonStream.unread(firstByte);
            return parseString(jsonStream);
        } else if (firstByte >= '0' && firstByte <= '9') {
            jsonStream.unread(firstByte);
            return parseNumber(jsonStream);
        } else {
            jsonStream.unread(firstByte);
            return parseInternal(jsonStream);
        }
    }


    private static Object parseInternal(PushbackInputStream jsonStream) throws Exception {
        int firstByte = getNextByte(jsonStream);
        switch (firstByte) {
            case '{':
                jsonStream.unread(firstByte);
                return parseJsonObject(jsonStream);
            case '[':
                jsonStream.unread(firstByte);
                return parseJsonArray(jsonStream);
            default:
                throw new IllegalArgumentException("Illegal json");
        }
    }

    public static Object parse(InputStream jsonStream) throws Exception {
        return parseInternal(new PushbackInputStream(jsonStream));
    }


}