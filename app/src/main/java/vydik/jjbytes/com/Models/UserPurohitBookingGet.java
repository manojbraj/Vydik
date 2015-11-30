package vydik.jjbytes.com.Models;

/**
 * Created by Manoj on 11/16/2015.
 */
public class UserPurohitBookingGet {
    String id,user_id,pujaname,purohitname,data,price,address,flagvalue;

    public UserPurohitBookingGet(String id,String user_id,String pujaname,String purohitname,String data,String price,String address,
                                 String flagvalue){
        this.id = id;
        this.user_id = user_id;
        this.pujaname = pujaname;
        this.purohitname = purohitname;
        this.data = data;
        this.price = price;
        this.address = address;
        this.flagvalue = flagvalue;
    }

    public UserPurohitBookingGet(){
        super();
    }

    public String getId(){
        return id;
    }
    public void setId(String ID){
        this.id = ID;
    }

    public String getUser_id(){
        return user_id;
    }

    public void setUser_id(String User_id){
        this.user_id = User_id;
    }

    public String getPujaname(){
        return pujaname;
    }

    public void setPujaname(String PujaName){
        this.pujaname = PujaName;
    }

    public String getPurohitname(){
        return purohitname;
    }

    public void setPurohitname(String PurohitName){
        this.purohitname = PurohitName;
    }

    public String getData(){
        return data;
    }

    public void setData(String Date){
        this.data = Date;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String Price){
        this.price = Price;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String Address){
        this.address = Address;
    }

    public String getFlagvalue(){
        return flagvalue;
    }

    public void setFlagvalue(String FlagValue){
        this.flagvalue = FlagValue;
    }

}