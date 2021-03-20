import java.io.Serializable;

public class Messages implements Serializable
{
    public String message;

    public Messages(String message){
        this.message=message;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message=message;
    }
}
