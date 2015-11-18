package com.gamesbykevin.maze.scorecard;

/**
 * The score for a level
 * @author GOD
 */
public final class Score 
{
    //the level this time is for
    private final int level;
    
    //the size of the maze
    private final int size;
    
    //the time it took to complete
    private long time;
    
    protected Score(final int level, final int size, final long time)
    {
    	//assign default values
    	this.level = level;
    	this.size = size;
        setTime(time);
    }
    
    /**
     * Get the level index
     * @return The level this score is for
     */
    public int getLevel()
    {
        return this.level;
    }
    
    /**
     * Get the size
     * @return The size of maze for this score
     */
    public int getSize()
    {
        return this.size;
    }
    
    /**
     * Get the time
     * @return The time it took to complete
     */
    public long getTime()
    {
        return this.time;
    }
    
    /**
     * Set the time
     * @param time The time to complete
     */
    public final void setTime(final long time)
    {
        this.time = time;
    }
}