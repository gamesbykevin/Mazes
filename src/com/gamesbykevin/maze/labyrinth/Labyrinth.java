package com.gamesbykevin.maze.labyrinth;

import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.game.Game;
import com.gamesbykevin.maze.game.IGame;
import com.gamesbykevin.maze.panel.GamePanel;
import com.gamesbykevin.maze.player.*;
import com.gamesbykevin.maze.screen.OptionsScreen;
import com.gamesbykevin.maze.thread.MainThread;

import android.graphics.Canvas;

import java.util.Random;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.maze.Maze;
import com.gamesbykevin.androidframework.maze.MazeHelper;
import com.gamesbykevin.androidframework.maze.algorithm.*;

public class Labyrinth extends Entity implements IGame
{
	//our maze object reference
	private Maze maze;
	
	/**
	 * Random object used to generate the levels
	 */
	private static final Random RANDOMIZE = new Random();
	
	/**
	 * How many times do we progress the maze creation per game update
	 */
	private static final int MAZE_CREATE_UPDATES = (MainThread.FPS);
	
	/**
	 * This will contain all keys for the tiles
	 * @author GOD
	 */
	protected enum TileKey
	{
		IsometricNWE, IsometricNWS, IsometricNSE, IsometricNSEW, IsometricSWE, IsometricNW,
		IsometricNE, IsometricSE, IsometricSW, IsometricNS, IsometricWE, IsometricN,
		IsometricS, IsometricE, IsometricW,
		IsometricGoal,
		
		TopDownNWE, TopDownNWS, TopDownNSE, TopDownNSEW,
		TopDownSWE, TopDownNW, TopDownNE, TopDownSE,
		TopDownSW, TopDownNS, TopDownWE, TopDownN,
		TopDownS, TopDownE, TopDownW,
		TopDownGoal
	}
	
    //the start coordinate of the first column, row (0,0)
    private final int startX, startY;
    
    //dimensions to calculate render coordinates
    public static final int WIDTH_ISOMETRIC = 100;
    public static final int HEIGHT_ISOMETRIC = 65;
    public static final int WIDTH_TOP_DOWN = 64;
    public static final int HEIGHT_TOP_DOWN = 64;
    private static final int OFFSET_WIDTH_ISOMETRIC = 8;
    private static final int OFFSET_HEIGHT_ISOMETRIC = OFFSET_WIDTH_ISOMETRIC * 2;
    
    //our game reference object
    private final Game game;
    
    //the different sizes of the maze
    private static final int MAZE_SMALL_COLS = 10;
    private static final int MAZE_SMALL_ROWS = 10;
    private static final int MAZE_MEDIUM_COLS = 20;
    private static final int MAZE_MEDIUM_ROWS = 10;
    private static final int MAZE_LARGE_COLS = 20;
    private static final int MAZE_LARGE_ROWS = 20;
    private static final int MAZE_XLARGE_COLS = 30;
    private static final int MAZE_XLARGE_ROWS = 30;
    private static final int MAZE_XXLARGE_COLS = 40;
    private static final int MAZE_XXLARGE_ROWS = 40;
    
	public Labyrinth(final Game game) throws Exception
	{
		super();

		//store our reference object
		this.game = game;
		
		//make sure animations are setup
		LabyrinthHelper.setupAnimations(this);
        
        //the starting coordinate will be the middle of the screen
        this.startX = (GamePanel.WIDTH / 2);
        this.startY = (GamePanel.HEIGHT / 2);
        
        //set default
        setIsometric(false);
	}
	
	@Override
	public void reset() throws Exception
	{
		//the dimensions of the maze
		final int cols;
		final int rows;
		
        //set the size of the maze
        switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_SIZE))
        {
            case 0:
        	default:
        		cols = Labyrinth.MAZE_SMALL_COLS;
        		rows = Labyrinth.MAZE_SMALL_ROWS;
	            break;
	            
            case 1:
            	cols = Labyrinth.MAZE_MEDIUM_COLS;
            	rows = Labyrinth.MAZE_MEDIUM_ROWS;
	            break;
	            
            case 2:
            	cols = Labyrinth.MAZE_LARGE_COLS;
            	rows = Labyrinth.MAZE_LARGE_ROWS;
	            break;
	            
            case 3:
            	cols = Labyrinth.MAZE_XLARGE_COLS;
	            rows = Labyrinth.MAZE_XLARGE_ROWS;
	            break;
	            
            case 4:
            	cols = Labyrinth.MAZE_XXLARGE_COLS;
	            rows = Labyrinth.MAZE_XXLARGE_ROWS;
	            break;
        }
		
		//determine how the maze will be created by the game  mode
		switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_MODE))
		{
			//casual and timed mode
			case 0:
			case 1:
				//set the seed according to the level
				RANDOMIZE.setSeed(game.getLevels().getLevelIndex());
				break;
				
			//versus computer and free mode
			case 2:
			case 3:
				//we set the seed to whatever the current time is
				RANDOMIZE.setSeed(System.nanoTime());
				break;
		}
		
		//pick a random maze algorithm
		switch (RANDOMIZE.nextInt(4))
		{
			case 0:
			default:
				this.maze = new BinaryTree(cols, rows);
				break;
				
			case 1:
				this.maze = new GrowingTree(cols, rows);
				break;
				
			case 2:
				this.maze = new Sidewinder(cols, rows);
				break;
				
			case 3:
				this.maze = new Prims(cols, rows);
				break;
		}
		
		//set the start location inside the maze itself
		getMaze().setStartLocation(0, 0);
		
		//setup the progress bar
		getMaze().getProgress().setScreen(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		getMaze().getProgress().getPaint().setTypeface(Font.getFont(Assets.FontMenuKey.Default));
		getMaze().getProgress().getPaint().setTextSize(32f);
		getMaze().getProgress().setDescription("Generating Maze...  ");
	}
	
	/**
	 * Assign the isometric render.<br>
	 * We will also assign the dimension of a single isometric/2d tile here as well
	 * @param isometric true = will render isometric, false = top down 2d
	 * @throws Exception 
	 */
	public void setIsometric(final boolean isometric) throws Exception
	{
		if (isometric)
		{
			super.setWidth(WIDTH_ISOMETRIC);
			super.setHeight(HEIGHT_ISOMETRIC);
		}
		else
		{
			super.setWidth(WIDTH_TOP_DOWN);
			super.setHeight(HEIGHT_TOP_DOWN);
		}
	}

	/**
	 * Are we rendering the isometric animation?
	 * @return true = yes, false = no
	 */
	public boolean hasIsometric()
	{
		return game.getPlayer().hasIsometric();
	}
	
	/**
	 * Here we will generate the maze, and if complete update etc....
	 * @throws Exception
	 */
	@Override
	public void update() throws Exception
	{
		//if the maze has been generated, no need to continue
		if (getMaze().isGenerated())
			return;

		//update the maze generation multiple times per game update
		for (int i = 0; i < MAZE_CREATE_UPDATES; i++)
		{
			if (!getMaze().isGenerated())
			{
				getMaze().update(RANDOMIZE);
			}
			else
			{
				break;
			}
		}
		
		//if the maze has been created, calculate cost and the finish will be the highest cost
		if (getMaze().isGenerated())
		{
			//identify the finish
			MazeHelper.locateFinish(getMaze());
			
			//reset the players, now that maze has been generated
			game.getHuman().reset();
    		game.getCpu().reset();
			
			//check the start room of the maze and make the player face an open direction
			PlayerHelper.assignStartAnimation(
				game.getHuman(), 
				getMaze().getRoom(getMaze().getStartCol(), getMaze().getStartRow())
			);
			
			PlayerHelper.assignStartAnimation(
				game.getCpu(), 
				getMaze().getRoom(getMaze().getStartCol(), getMaze().getStartRow())
			);
			
			//mark all room in maze as unvisited, this is for the AI
			markUnvisited();
			
    		//reset the count down
            game.getCountdown().reset();
		}
	}
	
	/**
	 * Mark all rooms unvisited in the current maze.<br>
	 * We do this because the AI tracks which rooms have been visited to solve.<br>
	 * If the maze has not been created yet, nothing will happen.
	 */
	public final void markUnvisited()
	{
		if (getMaze() == null)
			return;
		
		//check every column
		for (int col = 0; col < getMaze().getCols(); col++)
		{
			//check every row
			for (int row = 0; row < getMaze().getRows(); row++)
			{
				getMaze().getRoom(col, row).setVisited(false);
			}
		}
	}
	
	/**
	 * Get the maze object
	 * @return The maze object
	 */
	public Maze getMaze()
	{
		return this.maze;
	}
	
	@Override
	public void dispose()
	{
		if (maze != null)
		{
			maze.dispose();
			maze = null;
		}
	}
	
	/**
	 * Get the x-coordinate
	 * @param col Column
	 * @param row Row
	 * @return The x-coordinate at the specified location
	 */
	public double getCoordinateX(final double col, final double row)
	{
		if (hasIsometric())
		{
			final double offsetX = (
				(
					(col - game.getPlayer().getCol()) - (row - game.getPlayer().getRow())
				) * ((WIDTH_ISOMETRIC - OFFSET_WIDTH_ISOMETRIC) / 2)
			);
			
			return (startX + offsetX);
		}
		else
		{
			return (startX + ((col - (game.getPlayer().getCol() + .5)) * WIDTH_TOP_DOWN));
		}
	}
	
	/**
	 * Get the y-coordinate
	 * @param col Column
	 * @param row Row
	 * @return The y-coordinate at the specified location
	 */
	public double getCoordinateY(final double col, final double row)
	{
		if (hasIsometric())
		{
			final double offsetY = (
				(
					(col - game.getPlayer().getCol()) + (row - game.getPlayer().getRow())
				) * ((HEIGHT_ISOMETRIC - OFFSET_HEIGHT_ISOMETRIC) / 2)
			);
			
			return (startY + offsetY);
		}
		else
		{
			return (startY + ((row - (game.getPlayer().getRow() + .5)) * HEIGHT_TOP_DOWN));
		}
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//we can't render if the object is not null
		if (getMaze() == null)
			return;
		
		if (!getMaze().isGenerated())
		{
			//if not generated render progress
			getMaze().render(canvas);
		}
		else
		{
			for (int row = 0; row < getMaze().getRows(); row++)
			{
				for (int col = 0; col < getMaze().getCols(); col++)
				{
					//set the location
					super.setX(getCoordinateX(col, row));
					super.setY(getCoordinateY(col, row));

					//if the location is not on the screen we won't need to render
					if (getX() + getWidth() < 0 || getX() > GamePanel.WIDTH)
						continue;
					if (getY() + getHeight() < 0 || getY() > GamePanel.HEIGHT)
						continue;
					
					//assign the appropriate animation based on the room
					LabyrinthHelper.assignAnimation(this, getMaze().getRoom(col, row), hasIsometric());
					
					//render the current animation at the current location
					super.render(canvas);
					
					//if this is the goal, also render the goal tile 
					if (getMaze().getFinishCol() == col && getMaze().getFinishRow() == row)
					{
						//assign the goal key
						super.getSpritesheet().setKey(hasIsometric() ? TileKey.IsometricGoal : TileKey.TopDownGoal);
						
						//draw it
						super.render(canvas);
					}
				}
			}
		}
	}
}