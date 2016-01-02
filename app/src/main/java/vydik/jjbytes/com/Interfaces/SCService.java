package vydik.jjbytes.com.Interfaces;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import vydik.jjbytes.com.Models.Track;

/**
 * Created by user on 12/29/2015.
 */
public interface SCService {
    @GET("/admin_vydik/admin_vydik_project/audio/json_view.php")
    public void getRecentTracks(@Query("created_at[from]") String date, Callback<List<Track>> cb);
}
