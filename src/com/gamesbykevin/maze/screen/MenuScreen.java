package com.gamesbykevin.maze.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.maze.MainActivity;
import com.gamesbykevin.maze.assets.Assets;

/**
 * Our main menu
 * @author ABRAHAM
 */
public class MenuScreen implements Screen, Disposable
{
    //the logo
    private final Bitmap logo;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //list of button options
    private SparseArray<Button> buttons;
    
    /**
     * Button text to display to exit the game
     */
    public static final String BUTTON_TEXT_EXIT_GAME = "Exit";
    
    /**
     * Button text to display to rate the game
     */
    public static final String BUTTON_TEXT_RATE_APP = "Rate";
    
    /**
     * Button text to display to start a new game
     */
    public static final String BUTTON_TEXT_START_GAME = "Start";
    
    /**
     * Button text to display for the options
     */
    public static final String BUTTON_TEXT_OPTIONS = "Options";
    
    /**
     * Button text to display for more games
     */
    public static final String BUTTON_TEXT_MORE_GAMES = "More Games";
    
    //icon dimension
    public static final int ICON_DIMENSION = 80;
    
    //start new game, and did we notify user
    private boolean reset = false, notify = false;
    
    //buttons to access each button in the list
    public static final int INDEX_BUTTON_START = 0;
    public static final int INDEX_BUTTON_OPTIONS = 1;
    public static final int INDEX_BUTTON_MORE = 2;
    public static final int INDEX_BUTTON_RATE = 3;
    public static final int INDEX_BUTTON_INSTRUCTIONS = 4;
    public static final int INDEX_BUTTON_FACEBOOK = 5;
    public static final int INDEX_BUTTON_TWITTER = 6;
    public static final int INDEX_BUTTON_EXIT = 7;
    
    public MenuScreen(final ScreenManager screen)
    {
        //store reference to the logo
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create button hash map
        this.buttons = new SparseArray<Button>();
        
        int x = ScreenManager.BUTTON_X;
        int y = ScreenManager.BUTTON_Y;
        
        //add start button
        addButton(x, y, BUTTON_TEXT_START_GAME, INDEX_BUTTON_START);
        
        //add options button
        x += ScreenManager.BUTTON_X_INCREMENT;
        addButton(x, y, BUTTON_TEXT_OPTIONS, INDEX_BUTTON_OPTIONS);
        
        //add rate button
        x += ScreenManager.BUTTON_X_INCREMENT;
        addButton(x, y, BUTTON_TEXT_RATE_APP, INDEX_BUTTON_RATE);
        
        //add more games button
        y += ScreenManager.BUTTON_Y_INCREMENT;
        x = ScreenManager.BUTTON_X;
        addButton(x, y, BUTTON_TEXT_MORE_GAMES, INDEX_BUTTON_MORE);
        
        //add exit button
        x += ScreenManager.BUTTON_X_INCREMENT;
        addButton(x, y, BUTTON_TEXT_EXIT_GAME, INDEX_BUTTON_EXIT);
        
        //add instructions button
        x += ScreenManager.BUTTON_X_INCREMENT - 15;
        addIcon(x, y, INDEX_BUTTON_INSTRUCTIONS, Assets.ImageMenuKey.Instructions);
        
        //add face book button
        x += ICON_DIMENSION + 10;
        addIcon(x, y, INDEX_BUTTON_FACEBOOK, Assets.ImageMenuKey.Facebook);
        		
        //add twitter button
        x += ICON_DIMENSION + 10;
        addIcon(x, y, INDEX_BUTTON_TWITTER, Assets.ImageMenuKey.Twitter);
    }
    
    private void addIcon(final int x, final int y, final int index, final Assets.ImageMenuKey key)
    {
    	//create a new button
    	Button button = new Button(Images.getImage(key));
    	
    	//set the location
    	button.setX(x);
    	button.setY(y);
    	
    	//set the dimensions
    	button.setWidth(ICON_DIMENSION);
    	button.setHeight(ICON_DIMENSION);
    	
    	//update the boundary
    	button.updateBounds();
    	
    	//add to list
    	this.buttons.put(index, button);
    }
    
    private void addButton(final int x, final int y, final String description, final int index)
    {
    	//create a new button
    	Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
    	
    	//set the location
    	button.setX(x);
    	button.setY(y);
    	
    	//update the boundary
    	button.updateBounds();
    	
    	if (description != null)
    	{
    		button.addDescription(description);
    		button.positionText(screen.getPaint());
    	}
    	
    	//add to list
    	this.buttons.put(index, button);
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //do we need anything here
    }
    
    @Override
    public boolean update(final int action, final float x, final float y) throws Exception
    {
        //if the game is to reset, don't continue
        if (reset)
            return false;
        
        if (action == MotionEvent.ACTION_UP)
        {
        	for (int index = 0; index < buttons.size(); index++)
        	{
        		if (buttons.get(index).contains(x, y))
        		{
        			switch (index)
        			{
	        			case INDEX_BUTTON_START:
	                        //flag reset
	                        reset = true;
	                        
	                        //flag notify false
	                        notify = false;
	                        
	                        //we do not request any additional events
	                        return false;
	                        
	        			case INDEX_BUTTON_OPTIONS:
	                        //set the state
	                        screen.setState(ScreenManager.State.Options);
	                        
	                        //play sound effect
	                        Audio.play(Assets.AudioMenuKey.Selection);
	                        
	                        //we do not request any additional events
	                        return false;
	                        
	        			case INDEX_BUTTON_MORE:
	                        //play sound effect
	                        Audio.play(Assets.AudioMenuKey.Selection);
	                        
	                        //go to web page
	                        this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_MORE_GAMES_URL);
	                        
	                        //we do not request any additional events
	                        return false;
	                        
	        			case INDEX_BUTTON_RATE:
	                        //play sound effect
	                        Audio.play(Assets.AudioMenuKey.Selection);
	                        
	                        //go to web page
	                        this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
	                        
	                        //we do not request any additional events
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
	        				
	        			case INDEX_BUTTON_EXIT:
	                        //play sound effect
	                        Audio.play(Assets.AudioMenuKey.Selection);
	                        
	                        //exit game
	                        this.screen.getPanel().getActivity().finish();
	                        
	                        //we do not request any additional events
	                        return false;
	                        
	    				default:
	    					throw new Exception("Index not setup here: " + index);
        			}
        		}
        	}
        }
        
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
    	//only reset if we notified the user by displaying the splash screen
        if (reset && notify)
        {
            //load game assets
            Assets.load(screen.getPanel().getActivity());

            //create the game
            screen.getScreenGame().createGame();

            //set running state
            screen.setState(ScreenManager.State.Running);

            //play sound effect
            Audio.play(Assets.AudioMenuKey.Selection);
            
            //we are done resetting
            reset = false;
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (reset)
        {
            //render splash screen
            canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
            
            //we notified the user
            notify = true;
        }
        else
        {
	        //draw main logo
	        canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);
	
	        //draw the menu buttons
	        if (buttons != null)
	        {
	        	for (int index = 0; index < buttons.size(); index++)
	        	{
	        		//get the current button
	        		Button button = buttons.get(index);

	        		//if the button does not exist, skip to the next
	        		if (button == null)
	        			continue;
	        		
        			switch (index)
        			{
	        			case INDEX_BUTTON_START:
	        			case INDEX_BUTTON_OPTIONS:
	        			case INDEX_BUTTON_MORE:
	        			case INDEX_BUTTON_RATE:
	        			case INDEX_BUTTON_EXIT:
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
        }
    }
    
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
        	for (int index = 0; index < buttons.size(); index++)
        	{
        		if (buttons.get(index) != null)
        		{
        			buttons.get(index).dispose();
        			buttons.put(index, null);
        		}
        	}
            
            buttons.clear();
            buttons = null;
        }
    }
}