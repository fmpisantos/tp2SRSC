import CriptoUtils.Cripto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.net.http.HttpRequest;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.Scanner;

import static CriptoUtils.Cripto.*;

public class Main {

    private static final String URI = "https://localhost:8080";

    public static void main(String[] args) throws Exception {
        RestTemplate r = restTemplateBuilder().build();
        Scanner in = new Scanner(System.in);
        int command = 0;
        JsonObject body = new JsonObject();
        HttpRequest req;
        ResponseEntity<Object> putEnt;
        Gson gson = new Gson();
        String msgID;
        int id;
        int uuid = -1;
        KeyStore ks;
        char[] passphrase = "queremosovinte".toCharArray();
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("./store/client.jks"), passphrase);
        String password;
        String username="";
        PublicKey pk = null;
        PrivateKey p = null;
        String publicKey = "";
        String auth = "";
        loginMenu();
        command = in.nextInt();
        switch (command){
            case 0:
                System.out.println("Enter uuid:");
                uuid = in.nextInt();
                in.nextLine();
                System.out.println("Password:");
                password = in.nextLine();
                body.addProperty("uuid",uuid);
                body.addProperty("password",encryptThisString(password));
                post(r,URI + "/register", body,"");
                break;
            case 1:
                System.out.println("Enter uuid:");
                uuid = in.nextInt();
                in.nextLine();
                System.out.println("Password:");
                password = in.nextLine();
                body.addProperty("uuid",uuid);
                body.addProperty("password",encryptThisString(password));
                putEnt = post(r,URI + "/login", body,"");
                if(putEnt!=null)
                    auth = putEnt.getHeaders().get("Authorization").get(0);
                break;
        }
        while (command != -1) {
            Menu();
            command = in.nextInt();
            in.nextLine();
            body = new JsonObject();
            switch (command) {
                case 0:
                    System.out.println("Enter uuid:");
                    uuid = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "create");
                    body.addProperty("uuid", uuid);
                    body.addProperty("key", publicKey);
                    putEnt = post(r, URI + "/create", body,auth);
                    if(putEnt!=null)
                        System.err.println(putEnt.getBody().toString());
                    break;
                case 1:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "list");
                    body.addProperty("id", id);
                    putEnt = post(r, URI + "/list", body,auth);
                    if(putEnt!=null)
                        System.err.println(putEnt.getBody().toString());
                    break;
                case 2:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "all");
                    body.addProperty("id", id);
                    putEnt = post(r, URI + "/all", body,auth);
                    if(putEnt!=null)
                        System.err.println(putEnt.getBody().toString());
                    break;
                case 3:
                    System.out.println("Enter your user id:");
                    id = in.nextInt();
                    in.nextLine();
                    System.out.println("Enter destination user id:");
                    int dst = in.nextInt();
                    in.nextLine();
                    System.out.println("Enter your message:");
                    String msg = in.nextLine();
                    body.addProperty("type", "send");
                    body.addProperty("src", id);
                    body.addProperty("dst", dst);
                    JsonObject bla = new JsonObject();
                    bla.addProperty("dst", dst);
                    putEnt = post(r, URI + "/destinfo", bla,"");
                    if(putEnt!=null) {
                        JsonObject response = gson.toJsonTree(putEnt.getBody()).getAsJsonObject();
                        byte[] msgEncrypted = Cripto.encrypt(msg.getBytes(), getKey(Base64.getDecoder().decode(response.get("key").getAsString())));
                        body.addProperty("msg", Base64.getEncoder().encodeToString(msgEncrypted));
                        body.addProperty("copy", Base64.getEncoder().encodeToString(Cripto.encrypt(msg.getBytes(), pk)));
                        putEnt = post(r, URI + "/send", body, auth);
                        if (putEnt != null)
                            System.err.println(putEnt.getBody().toString());
                    }
                    break;
                case 4:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    body.addProperty("type", "new");
                    body.addProperty("id", id);
                    putEnt = post(r, URI + "/new", body,auth);
                    if(putEnt!=null)
                    System.err.println(putEnt.getBody().toString());
                    break;
                case 5:
                    System.out.println("Enter user id:");
                    id = in.nextInt();
                    in.nextLine();
                    System.out.println("Enter message id:");
                    msgID = in.nextLine();
                    body.addProperty("type", "recv");
                    body.addProperty("id", id);
                    body.addProperty("msg", msgID);
                    putEnt = post(r, URI + "/recv", body,auth);

                    String clearTextMessage = "";
                    gson = new Gson();
                    if(putEnt!=null) {
                        JsonObject msg64 = gson.toJsonTree(putEnt.getBody()).getAsJsonObject();
                        byte[] decodedMsg = Base64.getDecoder().decode(msg64.getAsJsonArray("result").get(1).getAsString());
                        byte[] decryptedMsg = Cripto.decrypt(decodedMsg, p);
                        clearTextMessage = new String(decryptedMsg);
                        System.err.println("Sender: " + msg64.getAsJsonArray("result").get(0).getAsInt());
                        System.err.println("Msg: " + clearTextMessage);
                    }

                    body = new JsonObject();
                    body.addProperty("type", "receipt");
                    body.addProperty("id", id);
                    body.addProperty("msg", msgID);
                    body.addProperty("receipt", Base64.getEncoder().encodeToString(Cripto.sign(p, clearTextMessage.getBytes())));
                    post(r, URI + "/receipt", body,auth);
                    break;
                case 6:
                    System.out.println("Enter your user id:");
                    id = in.nextInt();
                    in.nextLine();
                    System.out.println("Enter message id:");
                    msgID = in.nextLine();
                    body.addProperty("type", "status");
                    body.addProperty("id", id);
                    body.addProperty("msg", msgID);
                    putEnt = post(r, URI + "/status", body,auth);
                    if(putEnt!=null){
                        JsonObject result = gson.toJsonTree(putEnt.getBody()).getAsJsonObject();
                        result = result.get("result").getAsJsonObject();
                        msg = result.get("msg").getAsString();
                        byte[] msgByteArr = Cripto.decrypt(Base64.getDecoder().decode(msg), p);
                        result.addProperty("msg",Base64.getEncoder().encodeToString(msgByteArr));
                        JsonArray arr = result.get("receipts").getAsJsonArray();
                        arr.forEach((JsonElement rec)->{
                            JsonObject newJ = rec.getAsJsonObject();
                            JsonObject bla2 = new JsonObject();
                            bla2.addProperty("dst", newJ.get("id").getAsInt());
                            ResponseEntity<Object> putEnt2 = post(r, URI + "/destinfo", bla2,"");
                            Gson gson2 = new Gson();
                            if(putEnt2!=null) {
                                JsonObject response = gson2.toJsonTree(putEnt2.getBody()).getAsJsonObject();
                                try {
                                    if(!verifySignature(getKey(Base64.getDecoder().decode(response.get("key").getAsString())),Base64.getDecoder().decode(newJ.get("receipt").getAsString()),msgByteArr))
                                        arr.remove(rec);
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (SignatureException e) {
                                    e.printStackTrace();
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        result.add("receipts",arr);
                        System.err.println(result);
                    }
                    break;
                case 7:
                    main(new String[]{});
                    break;
                case -1:
                    System.out.println("System Exit!");
                default:
                    System.err.println("Wrong Command try again!");
                    break;
            }
        }
        ks.store(new java.io.FileOutputStream("./store/client.jks"), passphrase);
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
    public static void loginMenu(){
        System.out.println("0 - Register");
        System.out.println("1 - Login");
    }
    public static void Menu(){
        System.out.println("Menu:");
        System.out.println("-1 - Leave");
        System.out.println("0 - Create Message box");
        System.out.println("1 - List users with a message box");
        System.out.println("2 - List all new messages in a user's message box");
        System.out.println("3 - List all messages in a user’s message box");
        System.out.println("4 - Send a message to a user’s message box");
        System.out.println("5 - Receive a message from a user's message box");
        System.out.println("6 - Check the reception status of a sent message");
        System.out.println("7 - Logout");
    }

    public static ResponseEntity<Object> post(RestTemplate r,String url,JsonObject body,String auth){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",auth);
        HttpEntity<JsonObject> request = new HttpEntity<JsonObject>(body, headers);
        if(auth.isEmpty())
            try {
                return r.postForEntity(url, body, Object.class);
            } catch (HttpStatusCodeException e) {
                System.err.println(e.getResponseBodyAsString());
            }
        else
            try {
                return r.postForEntity(url, request, Object.class);
            } catch (HttpStatusCodeException e) {
                System.err.println(e.getResponseBodyAsString());
            }
        return null;
    }
}
