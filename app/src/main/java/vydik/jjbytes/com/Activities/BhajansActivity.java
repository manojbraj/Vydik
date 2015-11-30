package vydik.jjbytes.com.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.share.model.GameRequestContent;

/**
 * Created by Manoj on 10/21/2015.
 */
public class BhajansActivity extends ActionBarActivity {
    LinearLayout Play1,Play2,Play3,Play4,Play5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bhajans_activity);

        Play1 = (LinearLayout) findViewById(R.id.play_one);
        Play2 = (LinearLayout) findViewById(R.id.play_two);
        Play3 = (LinearLayout) findViewById(R.id.play_three);
        Play4 = (LinearLayout) findViewById(R.id.play_four);
        Play5 = (LinearLayout) findViewById(R.id.play_five);

        Play1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BhajansActivity.this,MusicPlayer.class);
                intent.putExtra("song","ganeshaAsta");
                startActivity(intent);
            }
        });

        Play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BhajansActivity.this, MusicPlayer.class);
                intent.putExtra("song", "ganeshaNama");
                startActivity(intent);
            }
        });

        Play3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BhajansActivity.this, MusicPlayer.class);
                intent.putExtra("song", "chandrachoodeya");
                startActivity(intent);
            }
        });

        Play4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BhajansActivity.this, MusicPlayer.class);
                intent.putExtra("song", "lakshmi");
                startActivity(intent);
            }
        });

        Play5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BhajansActivity.this, MusicPlayer.class);
                intent.putExtra("song", "swarupeeya");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
