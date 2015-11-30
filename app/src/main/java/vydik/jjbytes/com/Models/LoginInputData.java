package vydik.jjbytes.com.Models;

/**
 * Created by manoj on 10/3/2015.
 */
public class LoginInputData {
    private String email,mobile,type;

    public LoginInputData(String email,String mobile,String type){
        this.email = email;
        this.mobile = mobile;
        this.type = type;
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
