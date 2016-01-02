package vydik.jjbytes.com.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by manoj on 12/29/2015.
 */

public class Track {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mID;

    @SerializedName("filepath")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    public String getTitle() {
        return mTitle;
    }

    public int getID() {
        return mID;
    }

    public String getStreamURL() {
        return "http://www.vydik.com/admin_vydik/admin_vydik_project/audio/"+mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }
}