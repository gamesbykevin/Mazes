package com.gamesbykevin.maze.player;

import java.util.ArrayList;
import java.util.List;

import com.gamesbykevin.androidframework.base.Cell;
import com.gamesbykevin.androidframework.maze.Room;
import com.gamesbykevin.androidframework.maze.Room.Wall;
import com.gamesbykevin.maze.game.Game;
import com.gamesbykevin.maze.panel.GamePanel;

import android.graphics.Canvas;

public class Cpu extends Player
{
	//optional directions
	private List<Wall> options;
	
	//our path in the maze
	private List<Cell> path;
	
	//all of the locations the cpu has visited
	private List<Cell> visited;
	
	public static final double VELOCITY = .1;
	
	//do we update/render the computer
	private boolean visible = false;
	
	public Cpu(final Game game)
	{
		super(game, false);
		
		//set the velocity
		super.setVelocityLimit(VELOCITY);
		
		//create new list of options
		this.options = new ArrayList<Wall>();
		
		//create list for our path
		this.path = new ArrayList<Cell>();
		
		//create list for the visited
		this.visited = new ArrayList<Cell>();
	}
	
	@Override
	public void dispose()
	{
		if (this.path != null)
			this.path.clear();
		if (this.visited != null)
			this.visited.clear();
		if (this.options != null)
			this.options.clear();
		
		this.path = null;
		this.visited = null;
		this.options = null;
		
		super.dispose();
	}
	
	@Override
	public void reset() throws Exception
	{
		super.reset();
		
		//clear our lists
		options.clear();
		path.clear();
		visited.clear();
		
		//mark all rooms in the maze unvisited (if exist)
		if (getGame().getLabyrinth() != null)
			getGame().getLabyrinth().markUnvisited();
	}
	
	@Override
	public void update() throws Exception
	{
    	//don't continue if not visible
    	if (!isVisible())
    		return;
		
		//update parent
		super.update();
		
		//if we aren't moving, we can plan our next move
		if (!hasVelocity())
		{
			//we have solved the maze if at the finish
			if (super.hasGoal())
				return;
			
			//clear our optional list
			options.clear();
			
    		//get the current room in the maze
    		Room room = getGame().getLabyrinth().getMaze().getRoom((int)getCol(), (int)getRow());
    		
    		//determine which directions are available, and we haven't visited
    		if (!room.hasWall(Wall.East) && !hasVisited(getCol() + 1, getRow()))
    			options.add(Wall.East);
    		if (!room.hasWall(Wall.West) && !hasVisited(getCol() - 1, getRow()))
    			options.add(Wall.West);
    		if (!room.hasWall(Wall.North) && !hasVisited(getCol(), getRow() - 1))
    			options.add(Wall.North);
    		if (!room.hasWall(Wall.South) && !hasVisited(getCol(), getRow() + 1))
    			options.add(Wall.South);
    		
    		//if we still don't have any options we will have to back track
    		if (options.isEmpty())
    		{
    			//location is no good, so we remove it
    			path.remove(path.size() - 1);
    			
				//set the target to the previous location
				super.setTarget(path.get(path.size() - 1).getCol(), path.get(path.size() - 1).getRow());
    		}
    		else
    		{
	    		//store the current location
				int col = (int)getCol();
				int row = (int)getRow();
				
				//pick a random direction available
				switch (options.get(GamePanel.RANDOM.nextInt(options.size())))
				{
	    			case North:
	    				row--;
	    				break;
	    				
	    			case South:
	    				row++;
	    				break;
	    				
	    			case East:
	    				col++;
	    				break;
	    				
	    			case West:
	    				col--;
	    				break;
				}
				
				/**
				 * If the path is empty we are just starting and need to mark the current location
				 */
				if (path.isEmpty())
					markTarget((int)getCol(), (int)getRow());
				
				markTarget(col, row);
    		}
		}
	}
	
	/**
	 * Mark the target<br>
	 * 
	 * 1) Set the target<br>
	 * 2) Add the location to our current path<br>
	 * 3) Mark the room visited in the maze<br>
	 * @param col Column
	 * @param row Row
	 */
	private void markTarget(final int col, final int row)
	{
		//set the target
		super.setTarget(col, row);
		
		//also add the new location to our path
		path.add(new Cell(col, row));
		
		//also mark the location as visited (if we haven't already)
		if (!hasVisited(col, row))
			visited.add(new Cell((int)col, (int)row));
	}
	
	/**
	 * Have we visited the specified location?
	 * @param col Column
	 * @param row Row
	 * @return true if the player visited the location, false otherwise
	 */
	private boolean hasVisited(final double col, final double row)
	{
		//check our list to see if we have visited
		for (Cell cell : visited)
		{
			//if we have the location, return true
			if (cell.hasLocation(col, row))
				return true;
		}
		
		//return false because we did not find the location
		return false;
	}
	
	public void setVisible(final boolean visible)
	{
		this.visible = visible;
	}
	
	public boolean isVisible()
	{
		return this.visible;
	}
	
    @Override
    public void render(final Canvas canvas) throws Exception
    {
    	//don't render anything if not visible
    	if (!isVisible())
    		return;
    	
    	//render the animation
    	super.render(canvas);
    }
}