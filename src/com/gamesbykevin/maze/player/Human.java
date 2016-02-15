package com.gamesbykevin.maze.player;

import com.gamesbykevin.androidframework.maze.Room;
import com.gamesbykevin.androidframework.maze.Room.Wall;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.text.TimeFormat;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.game.Game;
import com.gamesbykevin.maze.panel.GamePanel;
import com.gamesbykevin.maze.scorecard.Score;
import com.gamesbykevin.maze.screen.OptionsScreen;
import com.gamesbykevin.maze.screen.ScreenManager.State;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class Human extends Player
{
    //track each button if pressed
    private boolean pressedUp = false, pressedDown = false, pressedRight = false, pressedLeft = false;
    
    //our temporary options list
    private List<Wall> options;
    
	//track the time
	private long time;
	
	//this is used to track the time increment
	private long previous;
	
	//do we want to stop the timer
	private boolean stop = false;
	
	//location of player timer
	private static final int TIME_X = GamePanel.WIDTH - 195;
	private static final int TIME_Y = 25;
	
	//time description, and personal best to render to the user
	private String timeDescription = "";
	private String bestDescription = "";
	
	private Paint paint;
	
	public Human(final Game game)
	{
		super(game, true);
		
        //assign metrics
    	paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(16f);
        paint.setTypeface(Font.getFont(Assets.FontGameKey.Default));
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		paint = null;
	}
	
	@Override
	public void reset() throws Exception
	{
		super.reset();
		
		//reset our messages
		this.timeDescription = "";
		this.bestDescription = "";
		
		//stop the time and reset time to 0
		stopTimer();
		setTime(0);
		
		//flag false
		pressUp(false);
		pressDown(false);
		pressRight(false);
		pressLeft(false);
	}
	
	@Override
	public void update() throws Exception
	{
    	//if we want to stop the timer
    	if (stop)
    	{
    		//store the current time
    		previous = System.currentTimeMillis();
    		
    		//flag false
    		stop = false;
    	}
    	else
    	{
    		//update timer
    		updateTime();
    	}
		
		//store velocity
		final double dx = getDX();
		final double dy = getDY();
		
		//update parent
		super.update();
		
		//if the player is at their target, determine if we need to move them again
		if (super.hasTarget())
		{
			//make sure we previously had velocity so we can determine the previous location
			if (dx != 0 || dy != 0)
			{
				//create new list if null
				if (options == null)
					options = new ArrayList<Wall>();
				
				//make sure list is empty
				options.clear();
				
				//get the current room
				final Room room = super.getGame().getLabyrinth().getMaze().getRoom((int)getCol(), (int)getRow());
				
				//flag true that the human visited this room
				if (super.isHuman())
					room.setVisited(true);
				
				if (dx < 0)
				{
					//if the wall does not exist add to the list of available options
					if (!room.hasWall(Wall.West))
						options.add(Wall.West);
					if (!room.hasWall(Wall.South))
						options.add(Wall.South);
					if (!room.hasWall(Wall.North))
						options.add(Wall.North);
				}
				else if (dx > 0)
				{
					//if the wall does not exist add to the list of available options
					if (!room.hasWall(Wall.East))
						options.add(Wall.East);
					if (!room.hasWall(Wall.South))
						options.add(Wall.South);
					if (!room.hasWall(Wall.North))
						options.add(Wall.North);
				}
				else if (dy < 0)
				{
					//if the wall does not exist add to the list of available options
					if (!room.hasWall(Wall.East))
						options.add(Wall.East);
					if (!room.hasWall(Wall.West))
						options.add(Wall.West);
					if (!room.hasWall(Wall.North))
						options.add(Wall.North);
				}
				else if (dy > 0)
				{
					//if the wall does not exist add to the list of available options
					if (!room.hasWall(Wall.East))
						options.add(Wall.East);
					if (!room.hasWall(Wall.West))
						options.add(Wall.West);
					if (!room.hasWall(Wall.South))
						options.add(Wall.South);
				}
				
				//if there is only 1 way to go, we will continue to move the player to the next location
				if (options.size() == 1)
				{
					//flag false for all buttons in game
					this.pressDown(false);
					this.pressLeft(false);
					this.pressRight(false);
					this.pressUp(false);
					
					//let's see which direction we are headed
					switch (options.get(0))
					{
						case West:
							setTarget(getCol() - 1, getRow());
							break;
							
						case East:
							setTarget(getCol() + 1, getRow());
							break;
							
						case North:
							setTarget(getCol(), getRow() - 1);
							break;
							
						case South:
							setTarget(getCol(), getRow() + 1);
							break;
					}
					
					//no need to continue further
					return;
				}
			}
		}
		
    	//make sure we have pressed at least one of the buttons
    	if (hasPressDown() || hasPressUp() || hasPressLeft() || hasPressRight())
    	{
    		//we can't move if the player is already moving
    		if (hasVelocity())
    			return;
    		
    		//don't continue if we are at the goal
    		if (hasGoal())
    			return;
    		
    		//get the current room in the maze
    		Room room = getGame().getLabyrinth().getMaze().getRoom((int)getCol(), (int)getRow());
    	
	    	//move player based on button pressed
	    	if (hasPressDown())
	    	{
	    		//make sure there isn't a wall in the way, and then we can set our target
	    		 if (!room.hasWall(Wall.South))
	    			 setTarget(getCol(), getRow() + 1);
	    	}
	    	else if (hasPressUp())
	    	{
	    		//make sure there isn't a wall in the way, and then we can set our target
	    		 if (!room.hasWall(Wall.North))
	    			 setTarget(getCol(), getRow() - 1);
	    		 
	    		 //we can't press this button at this time
	    		 pressDown(false);
	    	}
	    	
	    	if (hasPressLeft())
	    	{
	    		//make sure there isn't a wall in the way, and then we can set our target
	    		 if (!room.hasWall(Wall.West))
	    			 setTarget(getCol() - 1, getRow());
	    		 
	    		 //we can't press this button at this time
	    		 pressRight(false);
	    	}
	    	else if (hasPressRight())
	    	{
	    		//make sure there isn't a wall in the way, and then we can set our target
	    		 if (!room.hasWall(Wall.East))
	    			 setTarget(getCol() + 1, getRow());
	    		 
	    		 //we can't press this button at this time
	    		 pressLeft(false);
	    	}
    	}
	}
	
	/**
	 * Update the game time
	 */
	private void updateTime()
	{
		//store the previous time
		final long current = System.currentTimeMillis();
		
		//update elapsed time
		setTime(getTime() + (current - previous));
		
		//the previous time is now the current time
		previous = current;
		
		//score object
		Score score = super.getGame().getLevels().getScoreCard().getScore();;
		
		//format the time accordingly
		switch (super.getGame().getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_MODE))
		{
			//timed mode, the timer will count down
			case 1:
				//set the time depending if a score was previously sent
				if (score != null)
				{
					//if time has run out
					if (score.getTime() - getTime() <= 0)
					{
						this.timeDescription = "Time: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, 0);
						
						//set state to game over
						super.getGame().getScreen().setState(State.GameOver);
						
						//assign game over message
						super.getGame().getScreen().getScreenGameover().setMessage("Times Up");
						
						//play sound effect
						Audio.play(Assets.AudioGameKey.TimeOver);
					}
					else
					{
						this.timeDescription = "Time: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, score.getTime() - getTime());
					}
					
					//display personal best
					this.bestDescription = "Best: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, score.getTime());
				}
				else
				{
					this.timeDescription = "Time: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, getTime());
					this.bestDescription = "Best: None";
				}
				break;
				
			//versus computer or free mode will not have a timer
			case 2:
			case 3:
				this.timeDescription = "";
				this.bestDescription = "";
				break;
				
			//casual we will display
			case 0:
			default:
				this.timeDescription = "Time: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, getTime());
				this.bestDescription = "Best: None";
				
				if (score != null)
					this.bestDescription = "Best: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, score.getTime());
				break;
		}
	}
	
	/**
	 * Flag the left direction button pressed
	 * @param result true if the direction button is pressed, false otherwise
	 */
	public void pressLeft(final boolean result)
	{
		this.pressedLeft = result;
	}
	
	/**
	 * Flag the right direction button pressed
	 * @param result true if the direction button is pressed, false otherwise
	 */
	public void pressRight(final boolean result)
	{
		this.pressedRight = result;
	}
	
	/**
	 * Flag the down direction button pressed
	 * @param result true if the direction button is pressed, false otherwise
	 */
	public void pressDown(final boolean result)
	{
		this.pressedDown = result;
	}
	
	/**
	 * Flag the up direction button pressed
	 * @param result true if the direction button is pressed, false otherwise
	 */
	public void pressUp(final boolean result)
	{
		this.pressedUp = result;
	}
	
	/**
	 * Is the left direction button pressed
	 * @return true if the direction button is pressed, false otherwise
	 */
	protected boolean hasPressLeft()
	{
		return this.pressedLeft;
	}
	
	/**
	 * Is the right direction button pressed
	 * @return true if the direction button is pressed, false otherwise
	 */
	protected boolean hasPressRight()
	{
		return this.pressedRight;
	}
	
	/**
	 * Is the up direction button pressed
	 * @return true if the direction button is pressed, false otherwise
	 */
	protected boolean hasPressUp()
	{
		return this.pressedUp;
	}
	
	/**
	 * Is the down direction button pressed
	 * @return true if the direction button is pressed, false otherwise
	 */
	protected boolean hasPressDown()
	{
		return this.pressedDown;
	}
	
	/**
	 * Stop the game timer
	 */
	public void stopTimer()
	{
		this.stop = true;
	}
	
	/**
	 * Set the elapsed time
	 * @param time The desired time (milliseconds)
	 */
	public void setTime(final long time)
	{
		this.time = time;
	}
	
	/**
	 * Get the elapsed time
	 * @return The elapsed time (milliseconds)
	 */
	public long getTime()
	{
		return this.time;
	}
	
	@Override
    public void render(final Canvas canvas) throws Exception
    {
		//render the player
		super.render(canvas);
		
		//render the timers
		canvas.drawText(timeDescription, TIME_X, TIME_Y, paint);
		canvas.drawText(bestDescription, TIME_X, TIME_Y + 25, paint);
		
    }
}