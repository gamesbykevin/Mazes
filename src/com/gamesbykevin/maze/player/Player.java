package com.gamesbykevin.maze.player;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.maze.game.Game;
import com.gamesbykevin.maze.game.IGame;
import com.gamesbykevin.maze.labyrinth.Labyrinth;
import com.gamesbykevin.maze.panel.GamePanel;

import android.graphics.Canvas;

public abstract class Player extends Entity implements IGame
{
	/**
	 * The rate at which the player can move
	 */
	public static final double VELOCITY_DEFAULT = .125;
	
	//the speed at which the player can move
	private double velocity;
	
	/**
	 * No velocity
	 */
	public static final int VELOCITY_NONE = 0;
	
	//the target destination we want to move to
	private double targetCol, targetRow;
	
	/**
	 * The different animation keys for the player
	 * @author GOD
	 *
	 */
	protected enum AnimationKey
	{
		IsometricNorthWalk,
		IsometricNorthStand,
		IsometricSouthWalk,
		IsometricSouthStand,
		IsometricWestWalk,
		IsometricWestStand,
		IsometricEastWalk,
		IsometricEastStand,
		
		TopDownNorthWalk,
		TopDownNorthStand,
		TopDownSouthWalk,
		TopDownSouthStand,
		TopDownWestWalk,
		TopDownWestStand,
		TopDownEastWalk,
		TopDownEastStand,
	}
	
	/**
	 * The default pixel size of the player for isometric
	 */
	public static final int DEFAULT_DIMENSION_ISOMETRIC = 96;
	
	/**
	 * The default pixel size of the player for top down
	 */
	public static final int DEFAULT_DIMENSION_TOP_DOWN = 34;
	
	//are we rendering the isometric animations
	private boolean isometric = true;
	
	//is this player human?
	private final boolean human;
	
	//our maze reference object 
	private final Game game;
	
	//does this player have the perspective
	private boolean focus = false;
	
	public Player(final Game game, final boolean human)
	{
		super();
		
		//store our reference object
		this.game = game;
		
		//assign human status
		this.human = human;
		
		//setup the animations
		PlayerHelper.setupAnimations(this);
		
		//set the default 
		setVelocityLimit(VELOCITY_DEFAULT);
	}
	
	/**
	 * Set the velocity speed
	 * @param velocity The pixel speed at which the player can move
	 */
	protected void setVelocityLimit(final double velocity)
	{
		this.velocity = velocity;
	}
	
	/**
	 * Get the velocity speed
	 * @return The pixel speed at which the player can move
	 */
	protected double getVelocityLimit()
	{
		return this.velocity;
	}
	
	/**
	 * Assign this player focus.<br>
	 * If this player has focus, every object will be rendered around this player
	 * @param focus true = yes, false = no
	 */
	public void setFocus(final boolean focus)
	{
		this.focus = focus;
	}
	
	/**
	 * Does this player have focus?<br>
	 * If this player has focus, every object will be rendered around this player 
	 * @return true = yes, false = no
	 */
	public boolean hasFocus()
	{
		return this.focus;
	}
	
	/**
	 * Get our game object reference
	 * @return Our game object containing references to all objects
	 */
	protected final Game getGame()
	{
		return this.game;
	}
	
	/**
	 * Is the player at the maze goal?
	 * @return true if the player (column, row) equals the current maze finish (column, row)
	 */
	public boolean hasGoal()
	{
		return (hasGoal(super.getCol(), super.getRow()));
	}
	
	/**
	 * Is the player at the specified (col, row) goal
	 * @param col column
	 * @param row row
	 * @return true if the player location matches the specified location, false otherwise
	 */
	public boolean hasGoal(final double col, final double row)
	{
		//if the location of the finish matches this location we have the specified goal
		if (getGame().getLabyrinth().getMaze().getFinish().hasLocation(col, row))
			return true;
		
		//return false
		return false;
	}
	
	/**
	 * Is the player human?
	 * @return true = yes, false = no
	 */
	public boolean isHuman()
	{
		return this.human;
	}
	
	/**
	 * Assign the isometric render.<br>
	 * We will also assign the dimension of a single isometric/2d tile here as well
	 * @param isometric true = will render isometric, false = top down 2d
	 * @throws Exception 
	 */
	public void setIsometric(final boolean isometric) throws Exception
	{
		this.isometric = isometric;
	}

	/**
	 * Are we rendering the isometric animation?
	 * @return true = yes, false = no
	 */
	public boolean hasIsometric()
	{
		return this.isometric;
	}
	
	/**
	 * Assign the dimension of the player 
	 * @param dimension This will be assigned the width/height
	 */
	public void setDimensions(final int dimension)
	{
		super.setWidth(dimension);
		super.setHeight(dimension);
	}

	@Override
	public void reset() throws Exception 
	{
		//if the maze exits we will change the location to the start of the maze
		if (getGame().getLabyrinth() != null && getGame().getLabyrinth().getMaze() != null)
		{
			//assign maze start location as our current
			super.setCol(getGame().getLabyrinth().getMaze().getStart());
			super.setRow(getGame().getLabyrinth().getMaze().getStart());
		}
		else
		{
			//we will start at (0, 0)
			super.setCol(0);
			super.setRow(0);
		}
		
		//the target will be the current location
		setTarget(getCol(), getRow());
		
		if (hasIsometric())
		{
			//set a default animation
			setAnimationKey(AnimationKey.IsometricEastStand);
			
			//assign the dimension
			setDimensions(DEFAULT_DIMENSION_ISOMETRIC);
			
			//position player
			super.setX((GamePanel.WIDTH / 2) + (Labyrinth.WIDTH_ISOMETRIC / 2));
			super.setY((GamePanel.HEIGHT / 2) - (Labyrinth.HEIGHT_ISOMETRIC / 4));
		}
		else
		{
			//set a default animation
			setAnimationKey(AnimationKey.TopDownEastStand);
			
			//assign the dimension
			setDimensions(DEFAULT_DIMENSION_TOP_DOWN);

			//position player
			super.setX((GamePanel.WIDTH / 2));
			super.setY((GamePanel.HEIGHT / 2));
		}
		
		//we will not be moving
		super.setDX(VELOCITY_NONE);
		super.setDY(VELOCITY_NONE);
	}
	
	/**
	 * Set the target location for the player.<br>
	 * Here we will also determine the animation for the player to be facing the target.<br>
	 * We will also assign the velocity
	 * @param targetCol Target column
	 * @param targetRow Target row
	 */
	public void setTarget(final double targetCol, final double targetRow)
	{
		//assign the target
		this.targetCol = targetCol;
		this.targetRow = targetRow;
		
		//stop moving
		super.setDX(VELOCITY_NONE);
		super.setDY(VELOCITY_NONE);
		
		//here we will set the velocity
		if (targetCol > getCol())
		{
			super.setDX(getVelocityLimit());
			super.setDY(VELOCITY_NONE);
		}
		else if (targetCol < getCol())
		{
			super.setDX(-getVelocityLimit());
			super.setDY(VELOCITY_NONE);
		}
		else if (targetRow > getRow())
		{
			super.setDX(VELOCITY_NONE);
			super.setDY(getVelocityLimit());
		}
		else if (targetRow < getRow())
		{
			super.setDX(VELOCITY_NONE);
			super.setDY(-getVelocityLimit());
		}
		
		//here we will set the animation
		PlayerHelper.assignWalkAnimation(this);
	}
	
	/**
	 * Does the player have the target?
	 * @return true if the player's (column, row) equals the target (column, row), false otherwise
	 */
	protected boolean hasTarget()
	{
		//check if the player (column, row) equals the target (column, row)
		return super.hasLocation(targetCol, targetRow);
	}
	
	/**
	 * Get the current assigned animation key
	 * @return The current animation
	 */
	protected AnimationKey getAnimationKey()
	{
		return (AnimationKey)super.getSpritesheet().getKey();
	}
	
	/**
	 * Set the animation of the player
	 * @param key The desired animation
	 */
	public final void setAnimationKey(final AnimationKey key)
	{
		super.getSpritesheet().setKey(key);
	}
	
	@Override
    public void update() throws Exception
    {
		//update animation
    	super.getSpritesheet().get().update();
    	
    	//if there is velocity or we are not at our target column
    	if (getDX() != 0 || getDY() != 0 || getCol() != targetCol || getRow() != targetRow)
    	{
    		//if we are close enough
    		if (super.getDistance(getCol(), getRow(), targetCol, targetRow) <= getVelocityLimit())
    		{
    			//stop moving
    			super.setDX(VELOCITY_NONE);
    			super.setDY(VELOCITY_NONE);

    			//place on the target
    			super.setCol(targetCol);
    			super.setRow(targetRow);
    			
    			//stop animation
    			PlayerHelper.stopWalkAnimation(this);
    		}
    		else
    		{
				//update (column, row) location based on velocity
				super.setCol(getCol() + getDX());
				super.setRow(getRow() + getDY());
    		}
    	}
    	
    	//if this player does not have focus, we need to update the (x, y)
    	if (!hasFocus())
    	{
    		//update the coordinates accordingly
    		if (hasIsometric())
    		{
    			//update opponents coordinates, we offset here a little in reference to the human player
    			setX(getGame().getLabyrinth().getCoordinateX(getCol() - .75, getRow() - 1.75));
    			setY(getGame().getLabyrinth().getCoordinateY(getCol() - .75, getRow() + .5));
    		}
    		else
    		{
    			//update opponents coordinates
    			setX(getGame().getLabyrinth().getCoordinateX(getCol() + .5, getRow() + .5));
    			setY(getGame().getLabyrinth().getCoordinateY(getCol() + .5, getRow() + .5));
    		}
    	}
    }
    
	@Override
    public void render(final Canvas canvas) throws Exception
    {
		//if the location is not on the screen we won't need to render
		if (getX() + getWidth() < 0 || getX() > GamePanel.WIDTH)
			return;
		if (getY() + getHeight() < 0 || getY() > GamePanel.HEIGHT)
			return;
		
		//get location
		final double x = getX();
		final double y = getY();
		
		//offset location
		super.setX(x - (getWidth() / 2));
		super.setY(y - (getHeight() / 2));
		
		//render animation
		super.render(canvas);
		
		//restore location
		super.setX(x);
		super.setY(y);
    }
}