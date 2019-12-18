package srsc2.tp2Srsc.Controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import srsc2.tp2Srsc.Crypto.Utils;
import srsc2.tp2Srsc.Objects.NewUser;
import srsc2.tp2Srsc.Objects.ServerActions;

@RestController
public class Controller {

    ServerActions sa =  new ServerActions();

    @RequestMapping(value = "/teste", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Running..");
    }

    public ResponseEntity response(String body,boolean uuidOnBody,String token){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
        boolean canCont = false;
        if(uuidOnBody)
            canCont = Utils.checkToken(jsonObject.get("uuid").getAsInt(),token);
        else if(jsonObject.has("id")) {
            try {
                canCont = Utils.checkToken(sa.getUUID(jsonObject.get("id").getAsInt()),token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                canCont = Utils.checkToken(sa.getUUID(jsonObject.get("src").getAsInt()),token);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(canCont) {
            String resp = sa.executeCommand(jsonObject);
            boolean error = resp.contains("\"error\":");
            return ResponseEntity.status(!error ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED).body(resp);
        }else {
            JsonObject j = new JsonObject();
            j.addProperty("error","Unauthorized");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(j.toString());
        }
    }

    public ResponseEntity response(String body){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
        String resp = sa.executeCommand(jsonObject);
        boolean error = resp.contains("\"error\":");
        return ResponseEntity.status(!error?HttpStatus.OK:HttpStatus.EXPECTATION_FAILED).body(resp);
    }

    /*
        Request:
        {
         "type": "create",
         "uuid": <user uuid>,
         <other attributes>
        }
        Response:
        {
         "result": <user id>
        }
    */
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> create(@RequestBody String body,@RequestHeader("Authorization") String token) {
        return response(body,true,token);
    }

    /*
        Request:
        {
         "type": "list",
         "id": <optional user id>
        }
        Response:
        {
         "result": [{user-data}, ...]
        }
    */
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> list(@RequestBody String body){
        return response(body);
    }
    /*
        Request:
        {
         "type": "new",
         "id": <user id>
        }
        Response:
        {
         "result": [<message identifiers>]
        }
    */
    @RequestMapping(value = "/new", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> newUser(@RequestBody String body,@RequestHeader("Authorization") String token) {
        return response(body,false,token);
    }
    /*
        Request:
        {
         "type": "all",
         "id": <user id>
        }
        Response:
        {
         "result": [[<received messages' identifiers>][sent messages' identifiers]]
        }
    */
    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> all(@RequestBody String body,@RequestHeader("Authorization") String token) {
        return response(body,false,token);
    }

    /*
        Request:
        {
         "type": "send",
         "src": <source id>,
         "dst": <destination id>,
         "msg": <JSON or base64 encoded>,
         "copy": <JSON or base64 encoded>
        }
        Response:
        {
         "result": [<message identifier>,<receipt identifier>]
        }
    */
    @RequestMapping(value = "/send", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> send(@RequestBody String body,@RequestHeader("Authorization") String token) {
        return response(body,false,token);
    }

    /*
        Request:
        {
         "type": "recv",
         "id": <user id>,
         "msg": <message id>
        }
        Response:
        {
         "result": [<source id,<base64 encoded message>]
        }
    */
    
    @RequestMapping(value = "/recv", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> recv(@RequestBody String body,@RequestHeader("Authorization") String token) {
        return response(body,false,token);
    }

    /*
        Request:
        {
         "type": "receipt",
         "id": <user id of the message box>,
         "msg": <message id>,
         "receipt": <signature over cleartext message>
        }
        Response:
        The server will not reply to this message, nor will it validate its correctness.
    */

    @RequestMapping(value = "/receipt", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> receipt(@RequestBody String body,@RequestHeader("Authorization") String token) {
        return response(body,false,token);
    }
    /*
        Request:
        {
         "type": "status",
         "id": <user id of the receipt box>
         "msg": <sent message id>
        }
        Response:
       {
            "result":{
                "msg": <base64 encoded sent message>,
                "receipts": [
                 {
                 "data":<date>,
                 "id":<id of the receipt sender>,
                 "receipt":<base64 encoded receipt>
                 },
                ...]
                 }
        }
    */
    @RequestMapping(value = "/status", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> status(@RequestBody String body,@RequestHeader("Authorization") String token) {
        return response(body,false,token);
    }


    /*
        Request:
        {
         "uuid": <uuid>,
         "password": <encripted password>
        }
        Response:
            200 OK or:
            {
                "error": <Error>
            }
    */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> register(@RequestBody NewUser user){
        try {
            JsonObject j = new JsonObject();
            j.addProperty("error","User already exists");
            return !sa.createUser(user.uuid,user.password,user.iv)?ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(j.toString()):ResponseEntity.status(HttpStatus.OK).body("");
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject j = new JsonObject();
            j.addProperty("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(j.toString());
        }
    }

    /*
        Request:
        {
         "uuid": <uuid>,
         "password": <encripted password>
        }
        Response:
            200 OK + jwt in Authorization Header  or:
            {
                "error": <Error>
            }
    */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody NewUser user){
        try {
            if(!sa.login(user.uuid,user.password)){
                JsonObject j = new JsonObject();
                j.addProperty("error","Wrong PassWord");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(j.toString());
            }else{
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("Authorization","Bearer "+ Utils.getNewToken(user.uuid));
                return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject j = new JsonObject();
            j.addProperty("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(j.toString());
        }
    }
    /*
        Request:
        {
         "dst": <id>
        }
        Response:
        {
         "key": <destPubKey>
        }
    */
    @RequestMapping(value = "/destinfo", method = RequestMethod.POST)
    public ResponseEntity<?> getDestInfo(@RequestBody String body) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
        int destID = jsonObject.get("dst").getAsInt();
        JsonObject destPbKey = null;
        try {
            destPbKey = sa.getPBKey(destID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean error = destPbKey.has("\"error\":");
        return ResponseEntity.status(!error?HttpStatus.OK:HttpStatus.EXPECTATION_FAILED).body(destPbKey.toString());
    }
}
