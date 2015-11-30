package vydik.jjbytes.com.Models;

/**
 * Created by user on 11/30/2015.
 */
public class GetPurohitPujaList {
    String id,PujaName,PujaWithPackage,PujaWithoutPackage,ExpertLevel,Languages;

    public GetPurohitPujaList(String id,String PujaWithPackage,String PujaWithoutPackage,String ExpertLevel,String languages){
        this.id = id;
        this.PujaWithPackage = PujaWithPackage;
        this.PujaWithoutPackage = PujaWithoutPackage;
        this.ExpertLevel = ExpertLevel;
        this.Languages =languages;
    }
    public GetPurohitPujaList(){
        super();
    }
    public String getId(){
        return id;
    }
    public void setId(String ID){
        this.id = ID;
    }
    public String getPujaName(){
        return PujaName;
    }
    public void setPujaName(String PujaName){
        this.PujaName = PujaName;
    }
    public String getPujaWithPackage(){
        return PujaWithPackage;
    }
    public void setPujaWithPackage(String PujaWithPackage){
        this.PujaWithPackage = PujaWithPackage;
    }
    public String getPujaWithoutPackage(){
        return PujaWithoutPackage;
    }
    public void setPujaWithoutPackage(String PujaWithoutPackage){
        this.PujaWithoutPackage = PujaWithoutPackage;
    }
    public String getExpertLevel(){
        return ExpertLevel;
    }
    public void setExpertLevel(String ExpertLevel){
        this.ExpertLevel = ExpertLevel;
    }
    public String getLanguages(){
        return Languages;
    }
    public void setLanguages(String Language){
        this.Languages = Language;
    }
}
