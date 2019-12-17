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
        int command = 0;
        JsonObject body;
        while(command != -1) {
            command = in.nextInt();
            in.nextLine();
            body = new JsonObject();
            HttpRequest req;
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
                    req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI + "/create")).setHeader("Content-type", "application/json").build();
                    try {
                        HttpResponse response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                        System.out.println(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "list");
                    body.addProperty("id", id);
                    req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI + "/list")).setHeader("Content-type", "application/json").build();
                    try {
                        HttpResponse response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                        System.out.println(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "all");
                    body.addProperty("id", id);
                    req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI + "/all")).setHeader("Content-type", "application/json").build();
                    try {
                        HttpResponse response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                        System.out.println(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                    body.addProperty("dst",dst);
                    body.addProperty("msg",msg);
                    body.addProperty("copy",copy);
                    req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI + "/send")).setHeader("Content-type", "application/json").build();
                    try {
                        HttpResponse response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                        System.out.println(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                    body.addProperty("msg",msgID);
                    body.addProperty("receipt",receipt);
                    req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI + "/receipt")).setHeader("Content-type", "application/json").build();
                    try {
                        HttpResponse response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                        System.out.println(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                	System.out.println("Enter user id:");
                	id = in.nextInt();in.nextLine();
                	body.addProperty("type", "new");
                	body.addProperty("id", id);
                	req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI + "/new")).setHeader("Content-type", "application/json").build();
                	try {
                        HttpResponse response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                        System.out.println(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                	id = in.nextInt();in.nextLine();
                    msgID = in.nextInt();in.nextLine();
                	body.addProperty("type", "recv");
                	body.addProperty("id", id);
                	body.addProperty("msg", msgID);
                	req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI + "/recv")).setHeader("Content-type", "application/json").build();
                	try {
                        HttpResponse response = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                        System.out.println(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                	id = in.nextInt();in.nextLine();
                    msgID = in.nextInt();in.nextLine();
                	body.addProperty("type", "status");
                	body.addProperty("id", id);
                	body.addProperty("msg", msgID);
                	req = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(body.toString())).uri(new URI(URI + "/status")).setHeader("Content-type", "application/json").build();
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
    }

    public static void Menu(){
        System.out.println("Menu:");
        System.out.println("-1 - Leave");
        System.out.println("1 - Create Message box");
        System.out.println("2 - List users with a message box");
        System.out.println("3 - list all new messages in a user�s message box");
        System.out.println("4 - List all messages in a user’s message box");
        System.out.println("5 - Send a message to a user’s message box");
        System.out.println("7 - Receipt message sent by a client after receiving and validating a message from a message box");
        System.out.println("6 - Receive a message from a user�s message box");
        System.out.println("9 - Check the reception status of a sent message");
    }
}
