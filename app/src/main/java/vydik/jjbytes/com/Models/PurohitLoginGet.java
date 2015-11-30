package vydik.jjbytes.com.Models;

/**
 * Created by user on 11/30/2015.
 */
public class PurohitLoginGet {
    String id,fName,lName,Email,DOB,Phone,Education,university,State,Address,City,ZipCode,guruName,Locality;

    public PurohitLoginGet(String id,String fName,String lName,String Email,String DOB,String Phone,String Education,String University,String State,String Address,
                           String City,String ZipCode,String GuruName,String Locality){
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.Email = Email;
        this.DOB = DOB;
        this.Phone = Phone;
        this.Education = Education;
        this.university = University;
        this.State = State;
        this.Address = Address;
        this.City = City;
        this.ZipCode = ZipCode;
        this.guruName = GuruName;
        this.Locality = Locality;
    }

    public PurohitLoginGet(){
        super();
    }

    public String getId(){
        return id;
    }
    public void setId(String ID){
        this.id = ID;
    }
    public String getfName(){
        return fName;
    }
    public void setfName(String FName){
        this.fName = FName;
    }
    public String getlName(){
        return lName;
    }
    public void setlName(String LName){
        this.lName = LName;
    }
    public String getEmail(){
        return Email;
    }
    public void setEmail(String Email){
        this.Email = Email;
    }
    public String getDOB(){
        return DOB;
    }
    public void setDOB(String DOB){
        this.DOB = DOB;
    }
    public String getPhone(){
        return Phone;
    }
    public void setPhone(String Phone){
        this.Phone = Phone;
    }
    public String getEducation(){
        return Education;
    }
    public void setEducation(String Education){
        this.Education = Education;
    }
    public String getUniversity(){
        return university;
    }
    public void setUniversity(String University){
        this.university = University;
    }
    public String getState(){
        return State;
    }
    public void setState(String State){
        this.State = State;
    }
    public String getAddress(){
        return Address;
    }
    public void setAddress(String Address){
        this.Address = Address;
    }
    public String getCity(){
        return City;
    }
    public void setCity(String City){
        this.City = City;
    }
    public String getZipCode(){
        return ZipCode;
    }
    public void setZipCode(String ZipCode){
        this.ZipCode = ZipCode;
    }
    public String getGuruName(){
        return guruName;
    }
    public void setGuruName(String GuruName){
        this.guruName = GuruName;
    }
    public String getLocality(){
        return Locality;
    }
    public void setLocality(String Locality){
        this.Locality = Locality;
    }

}