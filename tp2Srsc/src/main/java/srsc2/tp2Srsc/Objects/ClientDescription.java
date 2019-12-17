package srsc2.tp2Srsc.Objects;

import com.google.gson.JsonElement;

import java.io.OutputStream;

class ClientDescription implements Comparable {

String id;		 // id extracted from the JASON description
JsonElement description; // JSON description of the client, including id
OutputStream out;	 // Stream to send messages to the client

ClientDescription ( String id, JsonElement description, OutputStream out )
{
    this.id = id;
    this.description = description;
    this.out = out;
}

public int
compareTo ( Object x )
{
    return ((ClientDescription) x).id.compareTo ( id );
}

}
