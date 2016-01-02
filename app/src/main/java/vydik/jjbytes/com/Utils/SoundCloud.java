package vydik.jjbytes.com.Utils;

import retrofit.RestAdapter;
import vydik.jjbytes.com.Interfaces.SCService;
import vydik.jjbytes.com.constants.Config;

/**
 * Created by user on 12/29/2015.
 */
public class SoundCloud {

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder().setEndpoint(Config.API_URL).build();
    private static final SCService SERVICE = REST_ADAPTER.create(SCService.class);

    public static SCService getService() {
        return SERVICE;
    }
}
