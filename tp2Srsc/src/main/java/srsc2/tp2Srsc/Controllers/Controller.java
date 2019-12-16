package srsc2.tp2Srsc.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
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
    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> create() {
        return ResponseEntity.status(HttpStatus.OK).body("Running..");
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
    public ResponseEntity<?> list() {
        return ResponseEntity.status(HttpStatus.OK).body("Running..");
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
    public ResponseEntity<?> newUser() {
        return ResponseEntity.status(HttpStatus.OK).body("Running..");
    }
}
