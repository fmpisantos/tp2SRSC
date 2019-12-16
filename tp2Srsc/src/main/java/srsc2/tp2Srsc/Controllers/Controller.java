package srsc2.tp2Srsc.Controllers;

import com.google.gson.JsonObject;
import org.springframework.web.bind.annotation.RequestBody;
import srsc2.tp2Srsc.Objects.ServerActions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    ServerActions sa =  new ServerActions();

    @RequestMapping(value = "/teste", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Running..");
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

    public ResponseEntity response(JsonObject body){
        String resp = sa.executeCommand(body);
        boolean error = resp.contains("\"error\":");
        return ResponseEntity.status(!error?HttpStatus.OK:HttpStatus.EXPECTATION_FAILED).body(sa.executeCommand(body));
    }
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> create(@RequestBody JsonObject body) {
        return response(body);
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
    public ResponseEntity<?> list(@RequestBody JsonObject body){
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
    public ResponseEntity<?> newUser(@RequestBody JsonObject body) {
        return response(body);
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
    public ResponseEntity<?> all(@RequestBody JsonObject body) {
        return response(body);
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
    public ResponseEntity<?> send(@RequestBody JsonObject body) {
        return response(body);
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
    public ResponseEntity<?> receipt(@RequestBody JsonObject body) {
        return response(body);
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
    public ResponseEntity<?> status(@RequestBody JsonObject body) {
        return response(body);
    }
}
