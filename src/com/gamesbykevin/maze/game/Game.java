package com.gamesbykevin.maze.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.text.TimeFormat;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.game.controller.Controller;
import com.gamesbykevin.maze.labyrinth.Labyrinth;
import com.gamesbykevin.maze.scorecard.Score;
import com.gamesbykevin.maze.scorecard.ScoreCard;
import com.gamesbykevin.maze.screen.ScreenManager;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
{
    //our main screen object reference
    private final ScreenManager screen;
    
    //paint object to draw text
    private Paint paint;
    
    //our storage object used to save data
    private ScoreCard scorecard;
    
    //our controller object
    private Controller controller;
    
    //is the game being reset
    private boolean reset = false;
    
    //our maze labyrinth
    private Labyrinth labyrinth;
    
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        //this.paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
        this.paint.setTextSize(16f);
        this.paint.setColor(Color.WHITE);
        this.paint.setLinearText(false);
        
        //create new controller
        this.controller = new Controller(this);
        
        //create score card to track best score
        this.scorecard = new ScoreCard(this, screen.getPanel().getActivity());
    }
    
    /**
     * Get the controller
     * @return Our controller object reference
     */
    public Controller getController()
    {
        return this.controller;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public ScreenManager getMainScreen()
    {
        return this.screen;
    }
    
    @Override
    public void reset() throws Exception
    {
        //flag reset
        reset = true;
    }
    
    /**
     * Get our score card
     * @return Our score card to track the user personal best score
     */
    public ScoreCard getScoreCard()
    {
        return this.scorecard;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @throws Exception
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y) throws Exception
    {
        //only update game if no controller buttons were clicked
        if (getController() != null && !getController().updateMotionEvent(event, x, y))
        {
        	
        }
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //make sure we aren't resetting
        if (reset)
        {
        	//flag reset false
            reset = false;
            
            //create new maze
            this.labyrinth = new Labyrinth();
        }
        else
        {
        	this.labyrinth.update();
        }
    }
    
    /**
     * Get the paint object
     * @return The paint object used to draw text in the game
     */
    public Paint getPaint()
    {
        return this.paint;
    }
    
    @Override
    public void dispose()
    {
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        paint = null;
        
        if (scorecard != null)
        {
            scorecard.dispose();
            scorecard = null;
        }
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
    	if (this.labyrinth != null)
    		this.labyrinth.render(canvas);
    	
        //make sure we aren't resetting
        if (!reset)
        {
            //render the controller for specific states
            if (screen.getState() != ScreenManager.State.GameOver && 
                screen.getState() != ScreenManager.State.Ready && 
                screen.getState() != ScreenManager.State.Options)
            {
                if (getController() != null)
                    getController().render(canvas);
            }
        }
    }
}