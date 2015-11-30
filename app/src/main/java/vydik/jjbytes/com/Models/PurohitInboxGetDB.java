package vydik.jjbytes.com.Models;

/**
 * Created by Manoj on 11/6/2015.
 */
public class PurohitInboxGetDB {
    private String id,message,date;
    private boolean checked;

    public PurohitInboxGetDB(String id,String message,String date){
        this.id = id;
        this.message = message;
        this.date =date;
    }

    public PurohitInboxGetDB() {
        super();
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId(){
        return id;
    }
    public void setId(String ID){
        this.id = ID;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String Message){
        this.message = Message;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String Date){
        this.date =Date;
    }
}