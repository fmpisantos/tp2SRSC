import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

    private final static HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private static final String URI = "http://localhost:8080";

    public static void main(String[] args) throws URISyntaxException {
        Scanner in = new Scanner(System.in);
        Menu();
        int command = in.nextInt();
        switch(command){
            case 1:
                in.nextLine();
                System.out.println("Enter uuid:");
                int uuid = in.nextInt();in.nextLine();
                System.out.println("Other atributes (Enter \"-0\" to stop)");
                List atts = new ArrayList<String>();
                String att = in.nextLine();
                while(!att.equals("-0")){
                    atts.add(att);
                    att = in.nextLine();
                }
                JsonObject body = new JsonObject();
                body.addProperty("type", "create");
                body.addProperty("uuid", uuid);
                body.addProperty("Attributes",new Gson().toJson(atts));
                HttpRequest req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI+"/create")).setHeader("Content-type","application/json").build();
                try {
                    HttpResponse response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                    System.out.println(response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static void Menu(){
        System.out.println("Menu:");
        System.out.println("1 - Create Message box");
        System.out.println("2 - List users with a message box");
    }
}
