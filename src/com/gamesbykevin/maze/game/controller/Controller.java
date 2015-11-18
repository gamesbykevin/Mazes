package com.gamesbykevin.maze.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.game.Game;
import com.gamesbykevin.maze.panel.GamePanel;
import com.gamesbykevin.maze.screen.OptionsScreen;
import com.gamesbykevin.maze.screen.ScreenManager;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be our game controller
 * @author GOD
 */
public class Controller implements IController
{
    //all of the buttons for the player to control
    private HashMap<Assets.ImageGameKey, Button> buttons;
    
    //our game object reference
    private final Game game;
    
    /**
     * The dimensions of the buttons, to be rendered to the user
     */
    private final static int BUTTON_DIMENSION = 64;

    //location of exit button
    private final static int EXIT_X = (int)(BUTTON_DIMENSION * .25);
    private final static int EXIT_Y = (BUTTON_DIMENSION / 4);
    
    //location of sound button
    private final static int SOUND_X = EXIT_X + (int)(BUTTON_DIMENSION * 1.5);
    private final static int SOUND_Y = EXIT_Y;
    
    //location of pause button
    private final static int PAUSE_X = SOUND_X + (int)(BUTTON_DIMENSION * 1.5);
    private final static int PAUSE_Y = SOUND_Y;
    
    //offset the controller coordinates
    private final static int CONTROLLER_OFFSET_X = 25;
    private final static int CONTROLLER_OFFSET_Y = 25;
    
    //the size of the game controller buttons
    private final static int HORIZONTAL_BUTTON_WIDTH = 133;
    private final static int HORIZONTAL_BUTTON_HEIGHT = 106;
    private final static int VERTICAL_BUTTON_WIDTH = 106;
    private final static int VERTICAL_BUTTON_HEIGHT = 133;
    
    //location of the controller movement buttons
    private final static int CONTOLLER_X_LEFT = CONTROLLER_OFFSET_X;
    private final static int CONTOLLER_Y_LEFT = GamePanel.HEIGHT - CONTROLLER_OFFSET_Y - HORIZONTAL_BUTTON_HEIGHT;
    private final static int CONTOLLER_X_RIGHT = CONTOLLER_X_LEFT + (CONTROLLER_OFFSET_X * 2) + HORIZONTAL_BUTTON_WIDTH;
    private final static int CONTOLLER_Y_RIGHT = CONTOLLER_Y_LEFT;
    
    //location of the controller movement buttons
    private final static int CONTOLLER_X_DOWN = GamePanel.WIDTH - CONTROLLER_OFFSET_X - VERTICAL_BUTTON_WIDTH;
    private final static int CONTOLLER_Y_DOWN = GamePanel.HEIGHT - CONTROLLER_OFFSET_Y - VERTICAL_BUTTON_HEIGHT;
    private final static int CONTOLLER_X_UP = CONTOLLER_X_DOWN;
    private final static int CONTOLLER_Y_UP = CONTOLLER_Y_DOWN - (CONTROLLER_OFFSET_Y * 2) - VERTICAL_BUTTON_HEIGHT;
    
    /**
     * Default Constructor
     * @param game Object game object reference
     */
    public Controller(final Game game)
    {
        //assign object reference
        this.game = game;
        
        //create temporary list
        List<Assets.ImageGameKey> tmp = new ArrayList<Assets.ImageGameKey>();
        
        //add button unique key to list
        tmp.add(Assets.ImageGameKey.Pause);
        tmp.add(Assets.ImageGameKey.Exit);
        tmp.add(Assets.ImageGameKey.SoundOn);
        tmp.add(Assets.ImageGameKey.SoundOff);
        tmp.add(Assets.ImageGameKey.ControllerDown);
        tmp.add(Assets.ImageGameKey.ControllerLeft);
        tmp.add(Assets.ImageGameKey.ControllerRight);
        tmp.add(Assets.ImageGameKey.ControllerUp);
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageGameKey, Button>();
        
        //add button
        for (Assets.ImageGameKey key : tmp)
        {
            this.buttons.put(key, new Button(Images.getImage(key)));
        }
        
        //update location of our buttons
        this.buttons.get(Assets.ImageGameKey.Pause).setX(PAUSE_X);
        this.buttons.get(Assets.ImageGameKey.Pause).setY(PAUSE_Y);
        this.buttons.get(Assets.ImageGameKey.Exit).setX(EXIT_X);
        this.buttons.get(Assets.ImageGameKey.Exit).setY(EXIT_Y);
        this.buttons.get(Assets.ImageGameKey.SoundOn).setX(SOUND_X);
        this.buttons.get(Assets.ImageGameKey.SoundOn).setY(SOUND_Y);
        this.buttons.get(Assets.ImageGameKey.SoundOff).setX(SOUND_X);
        this.buttons.get(Assets.ImageGameKey.SoundOff).setY(SOUND_Y);
        this.buttons.get(Assets.ImageGameKey.ControllerDown).setX(CONTOLLER_X_DOWN);
        this.buttons.get(Assets.ImageGameKey.ControllerDown).setY(CONTOLLER_Y_DOWN);
        this.buttons.get(Assets.ImageGameKey.ControllerLeft).setX(CONTOLLER_X_LEFT);
        this.buttons.get(Assets.ImageGameKey.ControllerLeft).setY(CONTOLLER_Y_LEFT);
        this.buttons.get(Assets.ImageGameKey.ControllerRight).setX(CONTOLLER_X_RIGHT);
        this.buttons.get(Assets.ImageGameKey.ControllerRight).setY(CONTOLLER_Y_RIGHT);
        this.buttons.get(Assets.ImageGameKey.ControllerUp).setX(CONTOLLER_X_UP);
        this.buttons.get(Assets.ImageGameKey.ControllerUp).setY(CONTOLLER_Y_UP);
        
        for (Assets.ImageGameKey key : tmp)
        {
            //set the dimension of the button
        	switch (key)
        	{
        		case Pause:
        		case Exit:
        		case SoundOn:
        		case SoundOff:
    			default:
    	            this.buttons.get(key).setWidth(BUTTON_DIMENSION);
    	            this.buttons.get(key).setHeight(BUTTON_DIMENSION);
        			break;
        			
        		case ControllerDown:
        		case ControllerUp:
    	            this.buttons.get(key).setWidth(VERTICAL_BUTTON_WIDTH);
    	            this.buttons.get(key).setHeight(VERTICAL_BUTTON_HEIGHT);
        			break;
        			
        		case ControllerLeft:
        		case ControllerRight:
    	            this.buttons.get(key).setWidth(HORIZONTAL_BUTTON_WIDTH);
    	            this.buttons.get(key).setHeight(HORIZONTAL_BUTTON_HEIGHT);
    				break;
        	}

            //update the boundary of all buttons
            this.buttons.get(key).updateBounds();
        }
    }
    
    /**
     * Get our game object reference
     * @return Our game object reference
     */
    private Game getGame()
    {
        return this.game;
    }
    
    /**
     * Update the controller based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @return true if motion event was applied, false otherwise
     * @throws Exception
     */
    public void update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //check if the touch screen was released
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //check if the player hit the controller
            if (buttons.get(Assets.ImageGameKey.Pause).contains(x, y))
            {
                //change the state to paused
                getGame().getScreen().setState(ScreenManager.State.Paused);
            }
            else if (buttons.get(Assets.ImageGameKey.Exit).contains(x, y))
            {
                //change to the exit confirm screen
                getGame().getScreen().setState(ScreenManager.State.Exit);
            }
            else if (buttons.get(Assets.ImageGameKey.SoundOn).contains(x, y))
            {
                //flip setting
                Audio.setAudioEnabled(!Audio.isAudioEnabled());
                
                //determine which button is displayed
                buttons.get(Assets.ImageGameKey.SoundOn).setVisible(Audio.isAudioEnabled());
                buttons.get(Assets.ImageGameKey.SoundOff).setVisible(!Audio.isAudioEnabled());
                
                getGame().getScreen().getScreenOptions().setIndex(
                	OptionsScreen.INDEX_BUTTON_SOUND, 
                	getGame().getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_SOUND) + 1
                );
                
                //if audio enabled, play music
                //if (Audio.isAudioEnabled())
                //    Audio.play(GamePanel.RANDOM.nextBoolean() ? Assets.AudioGameKey.Music1 : Assets.AudioGameKey.Music2, true);
            }
            else if (buttons.get(Assets.ImageGameKey.ControllerDown).contains(x, y))
            {
            	getGame().getHuman().pressUp(false);
            	getGame().getHuman().pressDown(false);
            	getGame().getHuman().pressLeft(false);
            	getGame().getHuman().pressRight(false);
            }
            else if (buttons.get(Assets.ImageGameKey.ControllerLeft).contains(x, y))
            {
            	getGame().getHuman().pressUp(false);
            	getGame().getHuman().pressDown(false);
            	getGame().getHuman().pressLeft(false);
            	getGame().getHuman().pressRight(false);
            }
            else if (buttons.get(Assets.ImageGameKey.ControllerRight).contains(x, y))
            {
            	getGame().getHuman().pressUp(false);
            	getGame().getHuman().pressDown(false);
            	getGame().getHuman().pressLeft(false);
            	getGame().getHuman().pressRight(false);
            }
            else if (buttons.get(Assets.ImageGameKey.ControllerUp).contains(x, y))
            {
            	getGame().getHuman().pressUp(false);
            	getGame().getHuman().pressDown(false);
            	getGame().getHuman().pressLeft(false);
            	getGame().getHuman().pressRight(false);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (buttons.get(Assets.ImageGameKey.ControllerDown).contains(x, y))
            {
            	getGame().getHuman().pressDown(true);
            }
            else if (buttons.get(Assets.ImageGameKey.ControllerLeft).contains(x, y))
            {
            	getGame().getHuman().pressLeft(true);
            }
            else if (buttons.get(Assets.ImageGameKey.ControllerRight).contains(x, y))
            {
            	getGame().getHuman().pressRight(true);
            }
            else if (buttons.get(Assets.ImageGameKey.ControllerUp).contains(x, y))
            {
            	getGame().getHuman().pressUp(true);
            }
        }
        
        //if dragging on screen and not in any of these buttons we will flag false
        if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            if (!buttons.get(Assets.ImageGameKey.ControllerDown).contains(x, y))
            	getGame().getHuman().pressDown(false);
            if (!buttons.get(Assets.ImageGameKey.ControllerLeft).contains(x, y))
            	getGame().getHuman().pressLeft(false);
            if (!buttons.get(Assets.ImageGameKey.ControllerRight).contains(x, y))
            	getGame().getHuman().pressRight(false);
            if (!buttons.get(Assets.ImageGameKey.ControllerUp).contains(x, y))
            	getGame().getHuman().pressUp(false);
        }
    }
    
    @Override
    public void update() throws Exception
    {
    	//nothing needed here
    }
    
    @Override
    public void reset()
    {
    	if (buttons != null)
    	{
	        //determine which button is displayed
	        buttons.get(Assets.ImageGameKey.SoundOn).setVisible(Audio.isAudioEnabled());
	        buttons.get(Assets.ImageGameKey.SoundOff).setVisible(!Audio.isAudioEnabled());
    	}
    }
    
    /**
     * Recycle objects
     */
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            buttons.clear();
            buttons = null;
        }
    }
    
    /**
     * Render the controller
     * @param canvas Write pixel data to this canvas
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw the buttons
        if (buttons != null)
        {
        	//check each key in the hash map
        	for (Assets.ImageGameKey key : buttons.keySet())
        	{
        		//don't continue if button does not exist
        		if (buttons.get(key) == null)
        			continue;
        		
        		//we render some buttons all the time
        		switch (key)
        		{
	        		case ControllerDown:
	        		case ControllerLeft:
	        		case ControllerRight:
	        		case ControllerUp:
	        			continue;
	        			
        			default:
        				buttons.get(key).render(canvas);
        				break;
        		}
        	}
        }
    }
    
    @SuppressWarnings("incomplete-switch")
	public void renderDPad(final Canvas canvas) throws Exception
    {
        //draw the buttons
        if (buttons != null)
        {
        	//check each key in the hash map
        	for (Assets.ImageGameKey key : buttons.keySet())
        	{
        		//don't continue if button does not exist
        		if (buttons.get(key) == null)
        			continue;
        		
        		//here we will render the d-pad
        		switch (key)
        		{
	        		case ControllerDown:
	        		case ControllerLeft:
	        		case ControllerRight:
	        		case ControllerUp:
        				buttons.get(key).render(canvas);
	        			break;
        		}
        	}
        }
    }
}