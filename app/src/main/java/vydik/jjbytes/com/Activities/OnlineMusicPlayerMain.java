package vydik.jjbytes.com.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vydik.jjbytes.com.Adapters.SCTrackAdapter;
import vydik.jjbytes.com.Interfaces.SCService;
import vydik.jjbytes.com.Models.Track;
import vydik.jjbytes.com.Utils.SoundCloud;
import vydik.jjbytes.com.Utils.Utilities;
import vydik.jjbytes.com.constants.Config;

/**
 * Created by user on 1/2/2016.
 */
public class OnlineMusicPlayerMain extends Activity {
    private static final String TAG = "MainActivity";
    private List<Track> mListItems;
    private SCTrackAdapter mAdapter;
    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;
    private MediaPlayer mMediaPlayer;
    private ImageView mPlayerControl;
    private ProgressBar ProgressLoader;
    LinearLayout LoaderLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_player_main);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerControl.setImageResource(R.drawable.ic_play);
            }
        });

        LoaderLayout = (LinearLayout) findViewById(R.id.loader_layout);

        mListItems = new ArrayList<Track>();
        ListView listView = (ListView)findViewById(R.id.track_list_view);
        mAdapter = new SCTrackAdapter(this, mListItems);
        listView.setAdapter(mAdapter);

        mSelectedTrackTitle = (TextView)findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView)findViewById(R.id.selected_track_image);
        mPlayerControl = (ImageView)findViewById(R.id.player_control);
        ProgressLoader = (ProgressBar) findViewById(R.id.progress_loader);

        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //LoaderLayout.setVisibility(View.VISIBLE);
                ProgressLoader.setVisibility(View.VISIBLE);
                Track track = mListItems.get(position);
                mSelectedTrackTitle.setText(track.getTitle());
                Picasso.with(OnlineMusicPlayerMain.this).load(track.getArtworkURL()).into(mSelectedTrackImage);

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }

                try {
                    System.out.println("getStreamURL :" + track.getStreamURL());
                    //LoaderLayout.setVisibility(View.GONE);
                    mMediaPlayer.setDataSource(track.getStreamURL());
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e){
                    e.printStackTrace();
                } catch (SecurityException e){
                    e.printStackTrace();
                } catch (IllegalStateException e){
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        CallSoundCloud();

    }

    private void CallSoundCloud() {
        Utilities.displayProgressDialog(OnlineMusicPlayerMain.this, "loading music, Please wait...", false);
        SCService scService = SoundCloud.getService();
        scService.getRecentTracks(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                Utilities.cancelProgressDialog();
                loadTracks(tracks);
            }

            @Override
            public void failure(RetrofitError error) {
                Utilities.cancelProgressDialog();
                Toast.makeText(OnlineMusicPlayerMain.this,"Failed to get songs, something went wrong please try after some time",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTracks(List<Track> tracks) {
        mListItems.clear();
        mListItems.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }

    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            ProgressLoader.setVisibility(View.GONE);
            mMediaPlayer.pause();
            mPlayerControl.setImageResource(R.drawable.ic_play);
        } else {
            ProgressLoader.setVisibility(View.VISIBLE);
            mMediaPlayer.start();
            mPlayerControl.setImageResource(R.drawable.ic_pause);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
