package vydik.jjbytes.com.Models;

/**
 * Created by Manoj on 11/6/2015.
 */
public class PurohitBookingInboxGetDB {
    String id,phone_number,puja_name,user_location,user_address,puja_date,user_name,date,purohit_it,user_id;
    private boolean checked;

    public PurohitBookingInboxGetDB(String id,String phone_number,String puja_name,String user_location,String user_address,
                                    String puja_date,String user_name,String date,String purohit_it,String user_id){
        this.id = id;
        this.phone_number = phone_number;
        this.puja_name= puja_name;
        this.user_location = user_location;
        this.user_address = user_address;
        this.puja_date = puja_date;
        this.user_name = user_name;
        this.date = date;
        this.purohit_it = purohit_it;
        this.user_id = user_id;
    }

    public PurohitBookingInboxGetDB() {
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

    public String getPhone_number(){
        return phone_number;
    }

    public void setPhone_number(String PhoneNumber){
        this.phone_number = PhoneNumber;
    }

    public String getPuja_name(){
        return puja_name;
    }

    public void setPuja_name(String PujaName){
        this.puja_name = PujaName;
    }

    public String getUser_location(){
        return user_location;
    }

    public void setUser_location(String UserLocation){
        this.user_location = UserLocation;
    }

    public String getUser_address(){
        return user_address;
    }

    public void setUser_address(String UserAddress){
        this.user_address = UserAddress;
    }

    public String getPuja_date(){
        return puja_date;
    }

    public void setPuja_date(String PujaDate){
        this.puja_date = PujaDate;
    }

    public String getUser_name(){
        return user_name;
    }

    public void setUser_name(String UserName){
        this.user_name = UserName;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String Date){
        this.date = Date;
    }

    public String getPurohit_it(){
        return purohit_it;
    }

    public void setPurohit_it(String PurohitId){
        this.purohit_it = PurohitId;
    }

    public String getUser_id(){
        return user_id;
    }

    public void setUser_id(String UserId){
        this.user_id = UserId;
    }
}