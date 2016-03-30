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
import com.gamesbykevin.maze.MainActivity;
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
    
    //list of button options
    private SparseArray<Button> buttons;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //our storage settings object
    private Settings settings;
    
    //our paint object for this screen
    private Paint paint;
    
    //font size for this screen
    private static final float FONT_SIZE = 18f;
    
    //buttons to access each button in the list
    public static final int INDEX_BUTTON_BACK = 0;
    public static final int INDEX_BUTTON_SOUND = 1;
    public static final int INDEX_BUTTON_RENDER = 2;
    public static final int INDEX_BUTTON_MODE = 3;
    public static final int INDEX_BUTTON_SIZE = 4;
    
    public static final int INDEX_BUTTON_INSTRUCTIONS = 5;
    public static final int INDEX_BUTTON_FACEBOOK = 6;
    public static final int INDEX_BUTTON_TWITTER = 7;
    
    public OptionsScreen(final ScreenManager screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);

        //create button hash map
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
        
        x += ScreenManager.BUTTON_X_INCREMENT;
        addButtonBack(x, y);
        
        x = ScreenManager.BUTTON_X;
        y = ScreenManager.BUTTON_Y + ScreenManager.BUTTON_Y_INCREMENT;
        addButtonMode(x, y);
        
        x += ScreenManager.BUTTON_X_INCREMENT;
        addButtonSize(x, y);
        
        //add instructions button
        x += ScreenManager.BUTTON_X_INCREMENT - 15;
        addIcon(x, y, INDEX_BUTTON_INSTRUCTIONS, Assets.ImageMenuKey.Instructions);
        
        //add face book button
        x += MenuScreen.ICON_DIMENSION + 10;
        addIcon(x, y, INDEX_BUTTON_FACEBOOK, Assets.ImageMenuKey.Facebook);
        		
        //add twitter button
        x += MenuScreen.ICON_DIMENSION + 10;
        addIcon(x, y, INDEX_BUTTON_TWITTER, Assets.ImageMenuKey.Twitter);
        
        //create our settings object last, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    private void addIcon(final int x, final int y, final int index, final Assets.ImageMenuKey key)
    {
    	//create a new button
    	Button button = new Button(Images.getImage(key));
    	
    	//set the location
    	button.setX(x);
    	button.setY(y);
    	
    	//set the dimensions
    	button.setWidth(MenuScreen.ICON_DIMENSION);
    	button.setHeight(MenuScreen.ICON_DIMENSION);
    	
    	//update the boundary
    	button.updateBounds();
    	
    	//add to list
    	this.buttons.put(index, button);
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
        button.addDescription("Size: XX-Large");
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
        button.addDescription("View:  Iso");
        button.addDescription("View:  2D");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.buttons.put(INDEX_BUTTON_RENDER, button);
    }
    
    private void addButtonSound(int x, int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Audio / Vib: On");
        button.addDescription("Audio / Vib: Off");
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
        		
        		//make sure the button exists
        		if (button == null)
        			continue;
        		
        		switch (key)
        		{
	    			case INDEX_BUTTON_BACK:
	    			case INDEX_BUTTON_SOUND:
	    			case INDEX_BUTTON_RENDER:
	    			case INDEX_BUTTON_MODE:
	    			case INDEX_BUTTON_SIZE:
			        	button.positionText(paint);
			        	break;
			        	
		        	default:
		        		break;
        		}
        	}
        }
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
    	//we only want motion event up
    	if (action != MotionEvent.ACTION_UP)
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
				
				//determine which button
				switch (key)
				{
    				case INDEX_BUTTON_BACK:
    					
    	                //store our settings
    	                settings.save();
    	                
    	                //set ready state
    	                screen.setState(ScreenManager.State.Ready);
    	                
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //no need to continue
    	                return false;
    	                
    				case INDEX_BUTTON_SOUND:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    			        
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
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
    				case INDEX_BUTTON_RENDER:
    	    			
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    					
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
    				case INDEX_BUTTON_SIZE:
    				case INDEX_BUTTON_MODE:
    	    			
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
                    	
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
    				case INDEX_BUTTON_INSTRUCTIONS:
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //go to instructions
                        this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
                        
                        //we do not request any additional events
                        return false;
                        
    				case INDEX_BUTTON_FACEBOOK:
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //go to instructions
                        this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_FACEBOOK_URL);
                        
                        //we do not request any additional events
                        return false;
                        
    				case INDEX_BUTTON_TWITTER:
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //go to instructions
                        this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_TWITTER_URL);
                        
                        //we do not request any additional events
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
    	for (int index = 0; index < buttons.size(); index++)
    	{
    		//get the current button
    		Button button = buttons.get(index);

    		//if the button does not exist, skip to the next
    		if (button == null)
    			continue;
    		
			switch (index)
			{
    			case INDEX_BUTTON_BACK:
    			case INDEX_BUTTON_SOUND:
    			case INDEX_BUTTON_RENDER:
    			case INDEX_BUTTON_MODE:
    			case INDEX_BUTTON_SIZE:
    				button.render(canvas, screen.getPaint());
    				break;
    				
    			case INDEX_BUTTON_INSTRUCTIONS:
    			case INDEX_BUTTON_FACEBOOK:
    			case INDEX_BUTTON_TWITTER:
    				button.render(canvas);
    				break;
                    
				default:
					throw new Exception("Index not setup here: " + index);
			}
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