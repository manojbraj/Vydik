package vydik.jjbytes.com.Models;

/**
 * Created by manoj on 10/3/2015.
 */
public class LoginGetDB {
    private String email,mobile,type,id;
    private boolean checked;

    public LoginGetDB(String id,String email,String mobile,String type){
        this.id = id;
        this.email = email;
        this.mobile = mobile;
        this.type = type;
    }

    public LoginGetDB() {
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
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
