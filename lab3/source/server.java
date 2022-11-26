import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.*;
import java.util.*;
import java.util.Locale;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(4080), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    public static Map<String, String> queryToMap(String query) {
	    if(query == null) {
		return null;
	    }
	    Map<String, String> result = new HashMap<>();
	    for (String param : query.split("&")) {
		String[] entry = param.split("=");
		if (entry.length > 1) {
		    result.put(entry[0], entry[1]);
		}else{
		    result.put(entry[0], "");
		}
	    }
	    return result;
	}

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
        String response = "";
	Map<String,String> queryMap = queryToMap(t.getRequestURI().getQuery());
        String str = queryMap.get("str");
        String upperCase = String.valueOf(str.chars().filter(Character::isUpperCase).count());
        String lowerCase = String.valueOf(str.chars().filter(Character::isLowerCase).count());
        String digit = String.valueOf(str.chars().filter(Character::isDigit).count());
        
        String special = String.valueOf(str.chars().filter((s)-> !Character.isDigit(s) && !Character.isLetter(s) && !Character.isWhitespace(s)).count());
        response = "{\"upperCase\":"+upperCase+",\"lowerCase\":"+lowerCase+",\"digits\":"+digit+",\"special\":"+special+"}";
        t.getResponseHeaders().set("Content-Type", "application/json");
        
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
