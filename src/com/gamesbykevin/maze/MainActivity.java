package com.gamesbykevin.maze;

import com.gamesbykevin.maze.panel.GamePanel;
import com.purplebrain.giftiz.sdk.GiftizSDK;
import com.purplebrain.giftiz.sdk.GiftizSDK.Inner.ButtonNeedsUpdateDelegate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity implements ButtonNeedsUpdateDelegate
{
    /**
     * Our web site address where more games can be found
     */
    public static final String WEBPAGE_MORE_GAMES_URL = "http://gamesbykevin.com";

    /**
     * The web address where this game can be rated
     */
    public static final String WEBPAGE_RATE_URL = "https://play.google.com/store/apps/details?id=com.gamesbykevin.maze";

    /**
     * The url that contains the instructions for the game
     */
    public static final String WEBPAGE_GAME_INSTRUCTIONS_URL = "http://gamesbykevin.com/2015/11/18/maze";
    
    /**
     * The face book url
     */
    public static final String WEBPAGE_FACEBOOK_URL = "https://www.facebook.com/gamesbykevin";
    
    /**
     * The twitter url
     */
    public static final String WEBPAGE_TWITTER_URL = "https://twitter.com/gamesbykevin";
    
    /**
     * Called when the activity is first created
     * @param savedInstanceState 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //turn the title off
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set the screen to full screen
        super.getWindow().setFlags(
        	WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        	WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        
        //call parent create
        super.onCreate(savedInstanceState);
        
        //set content view
        super.setContentView(R.layout.main);
        
        // Register to know when to update Giftiz Button
        GiftizSDK.Inner.setButtonNeedsUpdateDelegate(this);
        
        // Connect Click action
  		ImageView selfManagedButton = (ImageView) findViewById(R.id.self_managed_button);
  		
  		//set on click listener
  		selfManagedButton.setOnClickListener(new OnClickListener() {
        	  @Override public void onClick(View v) {
        		  GiftizSDK.Inner.buttonClicked(MainActivity.this);
        	  }
  		});
  		
        //get the game panel view
        final GamePanel panel = ((GamePanel)findViewById(R.id.surfaceView));
        
        //add callback to panel
        panel.getHolder().addCallback(panel);
    }
    
    /**
     * Override the finish call
     */
    @Override
    public void finish()
    {
        //call parent
        super.finish();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStart()
    {
        //call parent
        super.onStart();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStop()
    {
        //call parent
        super.onStop();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onDestroy()
    {
        //get the game panel
        GamePanel panel = ((GamePanel)findViewById(R.id.surfaceView));
        
        //cleanup game panel
        if (panel != null)
        {
            panel.dispose();
            panel = null;
        }
        
        //finish the activity
        this.finish();
        
        //perform final cleanup
        super.onDestroy();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onPause()
    {
        super.onPause();
        
    	//giftiz integration
    	GiftizSDK.onPauseMainActivity(this);
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onResume()
    {
    	super.onResume();
    	
    	//giftiz integration
    	GiftizSDK.onResumeMainActivity(this);
    	
    	//configure button 
    	updateButtonImage();
    }
    
    /**
     * Show the giftiz button
     */
    public void showButton()
    {
    	//show the button
    	runOnUiThread(new Runnable() {
    	     @Override
    	     public void run() {
 	        	((ImageView)findViewById(R.id.self_managed_button)).setVisibility(View.VISIBLE);
    	    }
    	});
    }
    
    /**
     * Hide the giftiz button
     */
    public void hideButton()
    {
    	//hide the button
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((ImageView)findViewById(R.id.self_managed_button)).setVisibility(View.INVISIBLE);
			}
    	});
    }
    
    @Override // Callback to update button
	public void buttonNeedsUpdate() 
    {
		updateButtonImage();
	}
    
	private void updateButtonImage() 
	{
		// pick the right button image according to the button status
		ImageView selfManagedButton = (ImageView) findViewById(R.id.self_managed_button);
        switch (GiftizSDK.Inner.getButtonStatus(this)) 
        {
        	case ButtonInvisible : selfManagedButton.setVisibility(View.GONE);break;
        	case ButtonNaked : selfManagedButton.setImageResource(R.drawable.giftiz_logo_self);break;
        	case ButtonBadge : selfManagedButton.setImageResource(R.drawable.giftiz_logo_badge_self);break;
        	case ButtonWarning : selfManagedButton.setImageResource(R.drawable.giftiz_logo_warning_self);break;
        }
	}
	
	public void missionCompleted(View v) 
	{
    	GiftizSDK.missionComplete(this);
    }
	
    /**
     * Navigate to the desired web page
     * @param url The desired url
     */
    public void openWebpage(final String url)
    {
        //create action view intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        
        //the content will be the web page
        intent.setData(Uri.parse(url));
        
        //start this new activity
        startActivity(intent);        
    }
}