package com.gamesbykevin.maze.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.maze.game.controller.Controller;
import com.gamesbykevin.maze.labyrinth.Labyrinth;
import com.gamesbykevin.maze.level.Levels;
import com.gamesbykevin.maze.player.Cpu;
import com.gamesbykevin.maze.player.Human;
import com.gamesbykevin.maze.player.Player;
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
    
    //our controller object
    private Controller controller;
    
    //is the game being reset
    private boolean reset = false;
    
    //our maze labyrinth
    private Labyrinth labyrinth;
    
    //our human player in the game
    private Human human;
    
    //our computer opponent in the game
    private Cpu cpu;
    
    //our game count down
    private Countdown countdown;
    
    //our level object to choose from
    private Levels levels;
    
    public Game(final ScreenManager screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTextSize(16f);
        this.paint.setColor(Color.WHITE);
        this.paint.setLinearText(false);
        
        //create new controller
        this.controller = new Controller(this);
        
        //create new maze object
        this.labyrinth = new Labyrinth(this);
        
        //create our human player
		this.human = new Human(this);
		this.human.setFocus(true);
		
		//create our opponent player
		this.cpu = new Cpu(this);
		
		//create count down
		this.countdown = new Countdown();
		
        //create new select level object
        this.levels = new Levels(this);
    }
    
	/**
	 * Get the player we want to be the focus on screen.
	 * @return The player we will render in the center of the screen at all times
	 */
	public Player getPlayer()
	{
		if (getHuman().hasFocus())
		{
			return getHuman();
		}
		else
		{
			return getCpu();
		}
	}

	/**
	 * 
	 * @return
	 */
	public Countdown getCountdown()
	{
		return this.countdown;
	}
	
	/**
	 * Get the human player.
	 * @return The human player trying to solve the labyrinth
	 */
	public Human getHuman()
	{
		return this.human;
	}
	
	/**
	 * Get the opponent player
	 * @return The opponent who is trying to solve the labyrinth
	 */
	public Cpu getCpu()
	{
		return this.cpu;
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
    public ScreenManager getScreen()
    {
        return this.screen;
    }
    
    /**
     * Get the labyrinth
     * @return The labyrinth object also containing the maze
     */
    public Labyrinth getLabyrinth()
    {
    	return this.labyrinth;
    }
    
	/**
	 * Get the levels reference object
	 * @return The object containing the level # etc
	 */
	public Levels getLevels()
	{
		return this.levels;
	}
    
    @Override
    public void reset() throws Exception
    {
        //flag reset
    	setReset(true);
    }
    
    /**
     * Flag reset
     * @param reset true to reset the game, false otherwise
     */
    private void setReset(final boolean reset)
    {
    	this.reset = reset;
    }
    
    /**
     * Do we have reset flagged?
     * @return true = yes, false = no
     */
    protected boolean hasReset()
    {
    	return this.reset;
    }
    
    /**
     * Get the paint object
     * @return The paint object used to draw text in the game
     */
    public Paint getPaint()
    {
        return this.paint;
    }
    
    /**
     * Update the game based on a motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @throws Exception
     */
    public void update(final int action, final float x, final float y) throws Exception
    {
    	//if reset we can't continue
    	if (hasReset())
    		return;
    	
    	if (!getLevels().hasSelection())
    	{
    		if (action == MotionEvent.ACTION_UP)
    			getLevels().setPress((int)x, (int)y);
    	}
    	else
    	{
	        //only update controller if exists
	        if (getController() != null)
	        	getController().update(action, x, y);
    	}
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //if we are to reset the game
        if (hasReset())
        {
        	//flag reset false
        	setReset(false);
            
            //reset the game elements
            GameHelper.reset(this);
        }
        else
        {
        	//update the game elements
        	GameHelper.update(this);
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
    	GameHelper.render(this, canvas);
    }
    
    @Override
    public void dispose()
    {
        paint = null;
        
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        if (labyrinth != null)
        {
        	labyrinth.dispose();
        	labyrinth = null;
        }
        
		if (human != null)
		{
			human.dispose();
			human = null;
		}
		
		if (cpu != null)
		{
			cpu.dispose();
			cpu = null;
		}
		
		if (countdown != null)
		{
			countdown.dispose();
			countdown = null;
		}
		
		if (levels != null)
		{
			levels.dispose();
			levels = null;
		}
    }
}