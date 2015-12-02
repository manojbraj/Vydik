package vydik.jjbytes.com.Models;

/**
 * Created by user on 12/1/2015.
 */
public class GetPurohitLanguages {
    String id,Language;
    public GetPurohitLanguages(String id,String language){
        this.id = id;
        this.Language = language;
    }

    public GetPurohitLanguages(){
        super();
    }

    public String getId(){
        return id;
    }
    public void setId(String ID){
        this.id = ID;
    }

    public String getLanguages(){
        return Language;
    }
    public void setLanguages(String Language){
        this.Language = Language;
    }
}
