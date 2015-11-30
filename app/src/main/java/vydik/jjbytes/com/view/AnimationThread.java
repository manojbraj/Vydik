package vydik.jjbytes.com.view;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.os.Handler;

import vydik.jjbytes.com.Activities.LoginActivity;

public class AnimationThread extends Thread
{
	private LoginActivity main_activity;
	
    private boolean run = false;
   
    private Handler mHandler;
    
    public AnimationThread(LoginActivity activity)
    {
        main_activity = activity;
        mHandler=new Handler(); 
    }

    public void setRunning(boolean r)
    {
        run = r;
    }

    public void run()
    {
       
        while(run)
        {

            try
            {
            	            	
            	Calendar calendar = Calendar.getInstance();
                long time = calendar.getTimeInMillis() - main_activity.startTime;
                
                if(time>1000 && time<1200)
                {
                	mHandler.post(new Runnable() {

                		@Override
                		public void run() {
                        	Calendar calendar = Calendar.getInstance();
                            long time = calendar.getTimeInMillis() - main_activity.startTime;
                            
                            time -= 1000;
                            if (time>200)
                            	time = 200;
                			
                            float Y = main_activity.height/3 - main_activity.height/(4.0f*200)*time;
                			
    	            		//main_activity.logoImage.setY(Y);
    	                   
                		}
                	});
                }
                else if( time>1300)
                {
                	mHandler.post(new Runnable() {

                		@SuppressLint("NewApi")
						@Override
                		public void run() {
    	            	
                        	run = false;
                        	main_activity.loginButton.setAlpha(1);
    	                   
                		}
                	});
                	
                }
                
                Thread.sleep(10);
                     	
            }
            catch(Exception e)
            {

            }
        }
        
    }
}