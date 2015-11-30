package vydik.jjbytes.com.Models;

/**
 * Created by Manoj on 11/13/2015.
 */
public class GetUserLoginData {

    String id,fname,lname,email,mobile,locality,state,address,userid,image;

    public GetUserLoginData(String id,String fname,String lname,String email,String mobile,String locality,String state,
                            String address,String userid,String image){
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.mobile = mobile;
        this.locality = locality;
        this.state = state;
        this.address = address;
        this.userid = userid;
        this.image = image;
    }

    public GetUserLoginData(){
        super();
    }

    public String getId(){
        return id;
    }
    public void setId(String ID){
        this.id = ID;
    }

    public String getFname(){
        return fname;
    }

    public void setFname(String Fname){
        this.fname = Fname;
    }

    public String getLname(){
        return lname;
    }

    public void setLname(String LName){
        this.lname = LName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String Email){
        this.email = Email;
    }

    public String getMobile(){
        return mobile;
    }

    public void setMobile(String Mobile){
        this.mobile = Mobile;
    }

    public String getLocality(){
        return locality;
    }

    public void setLocality(String Locality){
        this.locality = Locality;
    }

    public String getState(){
        return state;
    }

    public void setState(String State){
        this.state = State;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String Address){
        this.address = Address;
    }

    public String getUserid(){
        return userid;
    }

    public void setUserid(String UserId){
        this.userid =UserId;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String Image){
        this.image = Image;
    }
}