import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.*;
import java.util.Locale;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.util.*;

public class Test {

    static Map<String,String> queryMap;
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(4080), 0);
        
        server.createContext("/", new MyHandler());
        server.setExecutor(null); 
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
	  //System.out.println(queryMap);
        if(queryMap == null){
        System.out.println("no query param provided");
            response = "Hello World from java!\n";
            t.sendResponseHeaders(200, response.length());
	    //System.out.println("Served hello world...");
        }else if(queryMap.get("cmd").equals("time")){
            Instant inst = Instant.now();
            ZonedDateTime plTime = inst.atZone(ZoneId.of("Europe/Warsaw"));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            response = "Hello World from java!\n"+sdf.format(plTime);
            t.sendResponseHeaders(200, response.length());
	    //System.out.println("Served time...");
	    }
	 else if(queryMap.get("cmd").equals("rev")){
	    response = "Hello World from java!\n";
	    StringBuilder input1 = new StringBuilder();
	    input1.append(response);
	    input1.reverse();
	    response = input1.toString();
	    t.sendResponseHeaders(200, response.length());
	    //System.out.println("Served rev...");
	    
	    }else{
	    response = "Incorrect query param";
	    StringBuilder input1 = new StringBuilder();
		
	    t.sendResponseHeaders(400, response.length());
	    }
	   OutputStream os = t.getResponseBody();
	   os.write(response.getBytes());
           os.close();
        }
    }

}
