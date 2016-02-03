package com.gamesbykevin.maze.level;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.game.Game;
import com.gamesbykevin.maze.game.IGame;
import com.gamesbykevin.maze.panel.GamePanel;
import com.gamesbykevin.maze.scorecard.ScoreCard;
import com.gamesbykevin.maze.screen.OptionsScreen;

import android.graphics.Canvas;

public class Levels implements IGame 
{
    //the current level index
    private int levelIndex = 1;
	
    //the page index
    private int pageIndex = 0;
    
    //the buttons on the level screen
    private Button levelOpen, levelSolved, pageNext, pagePrevious;
    
    //the dimensions for the levels on each page
    private static final int COLS = 8;
    private static final int ROWS = 4;
    
    //the location and size of our buttons
    private static final int START_X = 10;
    private static final int START_Y = 10;
    private static final int PADDING = 20;
    private static final int DIMENSION = 80;
    
    //the total number of levels per page
    private static final int LEVELS_PER_PAGE = COLS * ROWS; 
    
    //the number of pages to select a level
    private static final int PAGES = 20;
    
    //the total number of levels
    public static final int TOTAL_LEVELS = LEVELS_PER_PAGE * PAGES;
    
    //(x, y) coordinates
    private int checkX, checkY;
    
    //do we check the coordinates
    private boolean check = false;
    
    //paint reference object
    private final Game game;
    
    //has a level selection been made
    private boolean selected = false;
    
    //the description to display
    private String description = "";
    
    //our storage object used to save data
    private ScoreCard scorecard;
    
    public Levels(final Game game)
    {
    	//assign game reference
    	this.game = game;
    	
        //create score card to track best score
        this.scorecard = new ScoreCard(game, game.getScreen().getPanel().getActivity());
    	
    	//create buttons
    	levelOpen = new Button(Images.getImage(Assets.ImageGameKey.LevelOpen));
    	levelOpen.setDescription(0, "");
    	levelOpen.setWidth(DIMENSION);
    	levelOpen.setHeight(DIMENSION);
    	levelSolved = new Button(Images.getImage(Assets.ImageGameKey.LevelSolved));
    	levelSolved.setDescription(0, "");
    	levelSolved.setWidth(DIMENSION);
    	levelSolved.setHeight(DIMENSION);
    	pageNext = new Button(Images.getImage(Assets.ImageGameKey.PageNext));
    	pageNext.setX(START_X + ((COLS-1) * DIMENSION) + ((COLS-1) * PADDING));
    	pageNext.setY(START_Y + (ROWS * DIMENSION) + (ROWS * PADDING) - 15);
    	pageNext.setWidth(DIMENSION);
    	pageNext.setHeight(DIMENSION);
    	pageNext.updateBounds();
    	pagePrevious = new Button(Images.getImage(Assets.ImageGameKey.PagePrevious));
    	pagePrevious.setX(START_X);
    	pagePrevious.setY(pageNext.getY());
    	pagePrevious.setWidth(DIMENSION);
    	pagePrevious.setHeight(DIMENSION);
    	pagePrevious.updateBounds();
    }
    
    /**
     * Get our score card
     * @return Our score card to track the users personal best score
     */
    public ScoreCard getScoreCard()
    {
        return this.scorecard;
    }
    
    /**
     * Set the description
     * @param description The text to display to the user
     */
    public void setDescription(final String description)
    {
    	this.description = description;
    }
    
    /**
     * Assign the coordinates to check when the screen is touched 
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void setPress(final int x, final int y)
    {
    	//store the location
    	this.checkX = x;
    	this.checkY = y;
    	
    	//flag check the coordinates
    	this.check = true;
    }
    
	/**
	 * Assign the level
	 * @param levelIndex The desired level
	 */
	public void setLevelIndex(final int levelIndex)
	{
		this.levelIndex = levelIndex;
		
		//make sure we stay in range
		if (getLevelIndex() >= TOTAL_LEVELS)
			setLevelIndex(0);
		if (getLevelIndex() < 0)
			setLevelIndex(TOTAL_LEVELS - 1);
	}
	
	/**
	 * Get the current level
	 * @return The current level
	 */
	public int getLevelIndex()
	{
		return this.levelIndex;
	}
    
	private void setPageIndex(final int pageIndex)
	{
		this.pageIndex = pageIndex;
		
		//keep the page index in bounds
		if (getPageIndex() >= PAGES)
			setPageIndex(0);
		if (getPageIndex() < 0)
			setPageIndex(PAGES - 1);
	}
	
	private int getPageIndex()
	{
		return this.pageIndex;
	}
	
	@Override
	public void reset() throws Exception
	{
		this.check = false;
	}

	@Override
	public void update() throws Exception
	{
		//do we need to check the coordinates
		if (check)
		{
			//flag check false
			check = false;
			
			//don't continue if we have a selection
			if (hasSelection())
				return;
			
			if (pageNext.contains(checkX, checkY))
			{
				//change the index
				setPageIndex(getPageIndex() + 1);
			}
			else if (pagePrevious.contains(checkX, checkY))
			{
				//change the index
				setPageIndex(getPageIndex() - 1);
			}
			else
			{
				int count = 0;
				
				for (int row = 0; row < ROWS; row++)
				{
					for (int col = 0; col < COLS; col++)
					{
						//determine the coordinates
						final int x = START_X + (col * DIMENSION) + (col * PADDING);
						final int y = START_Y + (row * DIMENSION) + (row * PADDING);
						
						//if in the bounds of the level we made a selection
						if (checkX >= x && checkX <= x + DIMENSION && checkY >= y && checkY <= y + DIMENSION)
						{
							//assign the level number index
							setLevelIndex((getPageIndex() * LEVELS_PER_PAGE) + count);
							
							//flag a selection was found
							setSelection(true);
							
							//reset the maze
							game.getLabyrinth().reset();
							
							//we are done here
							return;
						}
						
						//increase the count
						count++;
					}
				}
			}
		}
	}
	
	/**
	 * Flag selection
	 * @param selected true if we selected a level, false otherwise
	 */
	public void setSelection(final boolean selected)
	{
		this.selected = selected;
	}
	
	/**
	 * Do we have a selection?
	 * @return true if we selected a level, false otherwise
	 */
	public boolean hasSelection()
	{
		return this.selected;
	}
	
	@Override
	public void dispose()
	{
		if (levelOpen != null)
		{
			levelOpen.dispose();
			levelOpen = null;
		}
		
		if (levelSolved != null)
		{
			levelSolved.dispose();
			levelSolved = null;
		}
		
		if (pageNext != null)
		{
			pageNext.dispose();
			pageNext = null;
		}
		
		if (pagePrevious != null)
		{
			pagePrevious.dispose();
			pagePrevious = null;
		}
		
        if (scorecard != null)
        {
            scorecard.dispose();
            scorecard = null;
        }
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//if a selection was not made, render the select screen
		if (!hasSelection())
		{
			//the size of the levels
			final int size = game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_SIZE);
			
			//the level number
			int count = 1;
			
			for (int row = 0; row < ROWS; row++)
			{
				for (int col = 0; col < COLS; col++)
				{
					//determine the coordinates
					final int x = START_X + (col * DIMENSION) + (col * PADDING);
					final int y = START_Y + (row * DIMENSION) + (row * PADDING);
					
					//level number to be displayed
					int levelNumber = (getPageIndex() * LEVELS_PER_PAGE) + count;
					
					//our temporary button
					final Button button;
					
					//if the level has been solved, the button will be different
					if (getScoreCard().hasScore(levelNumber - 1, size))
					{
						//assign the button reference
						button = levelSolved;
					}
					else
					{
						//assign the button reference
						button = levelOpen;
					}
					
					//set the description
					button.setDescription(0, "" + levelNumber);
					
					//position the button
					button.setX(x);
					button.setY(y);
	
					//position the number in the center
					button.positionText(game.getPaint());
					
					//render the button
					button.render(canvas, game.getPaint());
					
					//increase the count
					count++;
				}
			}
			
			//render the next page buttons
			pageNext.render(canvas);
			pagePrevious.render(canvas);
			
			//draw page # and description
			canvas.drawText(
				description + " - " + 
				"Page: " + (getPageIndex() + 1) + " of " + PAGES,
				(int)(GamePanel.WIDTH * .33),
				GamePanel.HEIGHT - PADDING, 
				game.getPaint()
			);
		}
	}
}