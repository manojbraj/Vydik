package vydik.jjbytes.com.view;

import android.content.Context;
import android.widget.ImageView;
import vydik.jjbytes.com.Activities.R;

/**
 * Nothing but an ImageView with a preset image resource
 * @author yildizkabaran
 *
 */
public class ContentView extends ImageView {

  public ContentView(Context context){
    super(context);
    initialize();
  }
  
  private void initialize(){
    // set the content image here
    setImageResource(R.drawable.new_splash_screen_image);
    setScaleType(ScaleType.FIT_XY);
  }
}
