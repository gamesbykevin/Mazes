package com.gamesbykevin.maze.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.storage.settings.Settings;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    private SparseArray<Button> buttons;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //our storage settings object
    private Settings settings;
    
    //our paint object for this screen
    private Paint paint;
    
    //font size for this screen
    private static final float FONT_SIZE = 18f;
    
    //buttons to access each button list
    public static final int INDEX_BUTTON_BACK = 0;
    public static final int INDEX_BUTTON_SOUND = 1;
    public static final int INDEX_BUTTON_RENDER = 2;
    public static final int INDEX_BUTTON_MODE = 3;
    public static final int INDEX_BUTTON_SIZE = 4;
    
    public OptionsScreen(final ScreenManager screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);

        //create buttons hash map
        this.buttons = new SparseArray<Button>();

        //store our screen reference
        this.screen = screen;
        
        //create our paint object for this menu
        this.paint = new Paint(screen.getPaint());
        
        //change font size
        this.paint.setTextSize(FONT_SIZE);
        
        //start coordinates
        int y = ScreenManager.BUTTON_Y;
        int x = ScreenManager.BUTTON_X;
        
        addButtonSound(x, y);
        
        x += ScreenManager.BUTTON_X_INCREMENT;
        addButtonRender(x, y);
        
        x = ScreenManager.BUTTON_X;
        y = ScreenManager.BUTTON_Y + ScreenManager.BUTTON_Y_INCREMENT;
        addButtonMode(x, y);
        
        x += ScreenManager.BUTTON_X_INCREMENT;
        addButtonSize(x, y);
        
        x += ScreenManager.BUTTON_X_INCREMENT;
        y = ScreenManager.BUTTON_Y + (ScreenManager.BUTTON_Y_INCREMENT / 2);
        addButtonBack(x, y);
        
        //create our settings object last, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    /**
     * Get the list of buttons.<br>
     * We typically use this list to help load/set the settings based on the index of each button.
     * @return The list of buttons on the options screen
     */
    public SparseArray<Button> getButtons()
    {
    	return this.buttons;
    }
    
    private void addButtonMode(int x, int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Mode: Casual");
        button.addDescription("Mode: Timed");
        button.addDescription("Mode: Vs cpu");
        button.addDescription("Mode: Free");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.buttons.put(INDEX_BUTTON_MODE, button);
    }
    
    private void addButtonSize(int x, int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Size: Small");
        button.addDescription("Size: Medium");
        button.addDescription("Size: Large");
        button.addDescription("Size: X-Large");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.buttons.put(INDEX_BUTTON_SIZE, button);
    }
    
    private void addButtonBack(int x, int y)
    {
        //the back button
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Go  Back");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.buttons.put(INDEX_BUTTON_BACK, button);
    }
    
    private void addButtonRender(int x, int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("View:  2D");
        button.addDescription("View:  Isometric");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.buttons.put(INDEX_BUTTON_RENDER, button);
    }
    
    private void addButtonSound(int x, int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Sound: Enabled");
        button.addDescription("Sound: Disabled");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.buttons.put(INDEX_BUTTON_SOUND, button);
    }
    
    /**
     * Assign the index.
     * @param key The key of the button we want to change
     * @param index The desired index
     */
    public void setIndex(final int key, final int index)
    {
    	buttons.get(key).setIndex(index);
    }
    
    /**
     * Get the index selection of the specified.<br>
     * @return The user selection of the desired render (top down, isometric, etc...)
     */
    /**
     * Get the index selection of the specified button
     * @param key The key of the button we want to check
     * @return The current selection for the specified button key
     */
    public int getIndex(final int key)
    {
    	return buttons.get(key).getIndex();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        if (buttons != null)
        {
        	for (int key = 0; key < buttons.size(); key++)
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//make sure the buttons are positioned correctly
        		if (button != null)
			        button.positionText(paint);
        	}
        }
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
    	//we only want motion event up
    	if (event.getAction() != MotionEvent.ACTION_UP)
    		return true;
    	
        if (buttons != null)
        {
        	for (int key = 0; key < buttons.size(); key++)
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//if the button does not exist skip to the next
        		if (button == null)
        			continue;
        		
    			//if we did not select this button, skip to the next
    			if (!button.contains(x, y))
    				continue;
    			
				//change index
				button.setIndex(button.getIndex() + 1);
				
				//position the text
		        button.positionText(paint);
				
				//determine which button
				switch (key)
				{
    				case INDEX_BUTTON_BACK:
    					
    	                //store our settings
    	                settings.save();
    	                
    	                //set ready state
    	                screen.setState(ScreenManager.State.Ready);
    	                
    	                //play sound effect
    	                //Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //no need to continue
    	                return false;
    	                
    				case INDEX_BUTTON_SOUND:
    					
                        //flip setting
                        Audio.setAudioEnabled(!Audio.isAudioEnabled());
                        
                        //we also want to update the audio button in the controller so the correct is displayed
                        if (screen.getScreenGame() != null && screen.getScreenGame().getGame() != null)
                        {
                        	//make sure the controller exists
                    		if (screen.getScreenGame().getGame().getController() != null)
                    			screen.getScreenGame().getGame().getController().reset();
                        }
                        
                        //play sound effect
                        //Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
    				case INDEX_BUTTON_RENDER:
    					
                        //play sound effect
                        //Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
    				case INDEX_BUTTON_SIZE:
    				case INDEX_BUTTON_MODE:
                    	
                        //play sound effect
                        //Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
                    default:
                    	throw new Exception("Key not setup here: " + key);
				}
        	}
        }
    	
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw our main logo
        canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);
        
        //draw the menu buttons
    	for (int i = 0; i < buttons.size(); i++)
    	{
    		if (buttons.get(i) != null)
    			buttons.get(i).render(canvas, paint);
    	}
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
        	paint = null;
    	
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (buttons != null)
        {
        	for (int i = 0; i < buttons.size(); i++)
        	{
        		if (buttons.get(i) != null)
        		{
        			buttons.get(i).dispose();
        			buttons.put(i, null);
        		}
        	}
        	
        	buttons.clear();
        	buttons = null;
        }
    }
}