package vydik.jjbytes.com.Models;

/**
 * Created by manoj on 10/12/2015.
 */
public class PoojaModel {
    String name,value;
    boolean selected = false;
    private boolean checked = false;

    public PoojaModel(String name, String value,boolean selected){
        this.name = name;
        this.value = value;
        this.selected = selected;
    }

    public PoojaModel(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String Name){
        this.name = Name;
    }

    public String getValue(){
        return this.value;
    }

    public void setValue(String Value){
        this.value = Value;
    }

    public boolean isSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }
    /*modify*/
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return name;
    }

    public void toggleChecked() {
        checked = !checked;
    }
}
