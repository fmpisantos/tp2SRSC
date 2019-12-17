package srsc2.tp2Srsc.Objects;

public class NewUser {
    public int uuid;
    public String password;
    public String iv;


    public NewUser(){}
    public NewUser(int uuid,String password,String iv){
        this.uuid = uuid;
        this.password = password;
        this.iv = iv;
    }

    public NewUser(int uuid,String password){
        this.uuid = uuid;
        this.password = password;
        this.iv = "";
    }
}
