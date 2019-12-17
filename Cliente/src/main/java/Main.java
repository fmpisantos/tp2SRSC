import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.websocket.server.PathParam;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Supplier;

public class Main {

    private static final String URI = "https://localhost:8080";

    public static void main(String[] args) throws Exception {
        RestTemplate r = restTemplateBuilder().build();
        Scanner in = new Scanner(System.in);
        Menu();
        int command = 0;
        JsonObject body;
        while (command != -1) {
            command = in.nextInt();
            in.nextLine();
            body = new JsonObject();
            HttpRequest req;
            ResponseEntity putEnt;
            int msgID;
            int id;
            switch (command) {
                case 1:
                    System.out.println("Enter uuid:");
                    int uuid = in.nextInt();
                    in.nextLine();
                    System.out.println("Other atributes (Enter \"-0\" to stop)");
                    List atts = new ArrayList<String>();
                    String att = in.nextLine();
                    while (!att.equals("-0")) {
                        atts.add(att);
                        att = in.nextLine();
                    }
                    body.addProperty("type", "create");
                    body.addProperty("uuid", uuid);
                    body.addProperty("Attributes", new Gson().toJson(atts));
                    putEnt = r.postForEntity(URI + "/create", body, ResponseEntity.class);
                    System.err.println(putEnt.toString());
                    break;
                case 2:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "list");
                    body.addProperty("id", id);
                    putEnt = r.postForEntity(URI + "/list", body, ResponseEntity.class);
                    System.err.println(putEnt.getBody().toString());
                    break;
                case 4:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "all");
                    body.addProperty("id", id);
                    putEnt = r.postForEntity(URI + "/all", body, ResponseEntity.class);
                    System.err.println(putEnt.getBody().toString());
                    break;
                case 5:
                    System.out.println("Enter your user id:");
                    id = in.nextInt();
                    in.nextLine();
                    System.out.println("Enter destination user id:");
                    int dst = in.nextInt();
                    in.nextLine();
                    String msg = in.nextLine();
                    String copy = in.nextLine();
                    body.addProperty("type", "send");
                    body.addProperty("src", id);
                    body.addProperty("dst", dst);
                    body.addProperty("msg", msg);
                    body.addProperty("copy", copy);
                    putEnt = r.postForEntity(URI + "/send", body, ResponseEntity.class);
                    System.err.println(putEnt.getBody().toString());
                    break;
                case 7:
                    System.out.println("Enter your user id:");
                    id = in.nextInt();
                    in.nextLine();
                    msgID = in.nextInt();
                    in.nextLine();
                    String receipt = in.nextLine();
                    body.addProperty("type", "receipt");
                    body.addProperty("id", id);
                    body.addProperty("msg", msgID);
                    body.addProperty("receipt", receipt);
                    putEnt = r.postForEntity(URI + "/receipt", body, ResponseEntity.class);
                    System.err.println(putEnt.getBody());
                    break;
                case 3:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "new");
                    body.addProperty("id", id);
                    putEnt = r.postForEntity(URI + "/new", body, ResponseEntity.class);
                    System.err.println(putEnt.getBody().toString());
                    break;
                case 6:
                    id = in.nextInt();
                    in.nextLine();
                    msgID = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "recv");
                    body.addProperty("id", id);
                    body.addProperty("msg", msgID);
                    putEnt = r.postForEntity(URI + "/recv", body, ResponseEntity.class);
                    System.err.println(putEnt.getBody().toString());
                    break;
                case 8:
                    id = in.nextInt();
                    in.nextLine();
                    msgID = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "status");
                    body.addProperty("id", id);
                    body.addProperty("msg", msgID);
                    putEnt = r.postForEntity(URI + "/status", body, ResponseEntity.class);
                    System.err.println(putEnt.getBody().toString());
                    break;
            }
        }
    }

    private static RestTemplateBuilder restTemplateBuilder () throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial( new File("./store/client.jks"), "queremosovinte".toCharArray(), "queremosovinte".toCharArray() )
                .loadTrustMaterial( new File( "./store/trustedstore"), "queremosovinte".toCharArray() ).build();
        CloseableHttpClient client = HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(
                        sslContext,
                        new String[]{"TLSv1.2"},
                        new String[]{"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"},
                        NoopHostnameVerifier.INSTANCE)).build();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory( client );

        return new RestTemplateBuilder()
                .additionalMessageConverters( new GsonHttpMessageConverter() )
                .additionalMessageConverters( new ResourceHttpMessageConverter() )
                .requestFactory(() ->httpComponentsClientHttpRequestFactory);
    }

    public static void Menu(){
        System.out.println("Menu:");
        System.out.println("-1 - Leave");
        System.out.println("1 - Create Message box");
        System.out.println("2 - List users with a message box");
        System.out.println("3 - list all new messages in a user's message box");
        System.out.println("4 - List all messages in a user’s message box");
        System.out.println("5 - Send a message to a user’s message box");
        System.out.println("7 - Receipt message sent by a client after receiving and validating a message from a message box");
        System.out.println("6 - Receive a message from a user's message box");
        System.out.println("9 - Check the reception status of a sent message");
    }
}
