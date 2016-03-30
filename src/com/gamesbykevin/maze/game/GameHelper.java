package com.gamesbykevin.maze.game;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.text.TimeFormat;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.screen.OptionsScreen;
import com.gamesbykevin.maze.screen.ScreenManager.State;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;

/**
 * This class will contain game helper methods
 * @author GOD
 *
 */
public final class GameHelper 
{
    /**
     * The length to vibrate the phone when you beat a level
     */
    private static final long VIBRATION_DURATION = 750;
	
	/**
	 * Go to the next level with the current game settings
	 * @param game Our game object reference
	 * @throws Exception
	 */
	public static void nextLevel(final Game game) throws Exception
	{
		//assign the next level index
		game.getLevels().setLevelIndex(game.getLevels().getLevelIndex() + 1);
		
        //reset and create new maze
        game.getLabyrinth().reset();
        
        //reset the controller
        game.getController().reset();
	}
	
	/**
	 * Reset the game.<br>
	 * Here we will reset the players as well as generate a new maze
	 * @param game Our game object reference
	 * @throws Exception
	 */
	protected static void reset(final Game game) throws Exception
	{
        //do we render isometric
        final boolean isometric = game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_RENDER) == 0;
        
        //set the render according to the user selection and then reset
        game.getLabyrinth().setIsometric(isometric);
        
        //our description of the maze size
        String description = "";
        
        //set the description of the maze
        switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_SIZE))
        {
            case 0:
        	default:
        		description = "Size: Small";
	            break;
	            
            case 1:
        		description = "Size: Medium";
	            break;
	            
            case 2:
        		description = "Size: Large";
	            break;
	            
            case 3:
        		description = "Size: X-Large";
	            break;
	            
            case 4:
        		description = "Size: XX-Large";
	            break;
        }
        
        //reset the controller
        game.getController().reset();
		
		//assign render for the players
        game.getHuman().setIsometric(isometric);
        game.getCpu().setIsometric(isometric);
		
        //check the game mode
        switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_MODE))
        {
        	//casual
	        case 0:
	        	//need to select level
	        	game.getLevels().setSelection(false);
	        	game.getLevels().setDescription(description);
	        	
	        	//hide the computer
	        	game.getCpu().setVisible(false);
	        	break;
	        	
	        //timed
	        case 1:
	        	//need to select level
	        	game.getLevels().setSelection(false);
	        	game.getLevels().setDescription(description);
	        	
	        	//hide the computer
	        	game.getCpu().setVisible(false);
	        	break;
	        	
	        //versus computer
	        case 2:
	        	//don't have to select level
	        	game.getLevels().setSelection(true);
	        	
	        	//hide the computer
	        	game.getCpu().setVisible(true);
	        	
	        	//reset the maze
	        	game.getLabyrinth().reset();
	        	break;
	        	
	        //free
	        case 3:
	        	//don't have to select level
	        	game.getLevels().setSelection(true);
	        	
	        	//hide the computer
	        	game.getCpu().setVisible(false);
	        	
	        	//reset the maze
	        	game.getLabyrinth().reset();
	        	break;
        }
	}
	
	/**
	 * Update the game.<br>
	 * @param game Our game object reference
	 * @throws Exception
	 */
	protected static void update(final Game game) throws Exception
	{
		if (!game.getLevels().hasSelection())
		{
			game.getLevels().update();
		}
		else
		{
	    	//make sure maze has been generated before updating other objects
	    	if (game.getLabyrinth().getMaze().isGenerated())
	    	{
	    		//make sure the count down is done
	    		if (game.getCountdown().hasCompleted())
	    		{
		        	//update the controller (if needed)
	    			game.getController().update();
		    		
					//update the players
	    			game.getHuman().update();
	    			game.getCpu().update();
					
					//if either have reached the goal, set game over
					if (game.getHuman().hasGoal() || game.getCpu().hasGoal())
					{
						//make sure option is enabled
						if (game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_SOUND) == 0)
						{
			        		//get our vibrate object
			        		Vibrator v = (Vibrator) game.getScreen().getPanel().getActivity().getSystemService(Context.VIBRATOR_SERVICE);
			        		 
							//vibrate for a specified amount of milliseconds
							v.vibrate(VIBRATION_DURATION);
						}
						
						//flag the game over screen
						game.getScreen().setState(State.GameOver);
						game.getScreen().getScreenGameover().setMessage("");
						
						//check who wins to determine what happens next
						if (game.getHuman().hasGoal())
						{
							switch (game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_MODE))
							{
								//we will update the score if casual or timed mode
								case 0:
								case 1:
									//update the score, and get the result (if successful)
									boolean success = game.getLevels().getScoreCard().update(
										game.getLevels().getLevelIndex(), 
										game.getScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_SIZE), 
										game.getHuman().getTime()
									);
									
									//if we have a new high score
									if (success)
									{
										//play a different sound effect if we have a new high score
										Audio.play(Assets.AudioGameKey.NewHighScore);
										
										//assign the game over message
										game.getScreen().getScreenGameover().setMessage("New Best: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, game.getHuman().getTime()));
									}
									else
									{
										//display the current time
										game.getScreen().getScreenGameover().setMessage("Your Time: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, game.getHuman().getTime()));
										
										//no new high score so play this
										Audio.play(Assets.AudioGameKey.Congratulations);
									}
									break;
								
								//you win versus computer
								case 2:
									//play sound effect
									Audio.play(Assets.AudioGameKey.YouWin);
									
									//you win
									game.getScreen().getScreenGameover().setMessage("You Win");
									break;
									
								//other game modes we won't update the best score, just play sound
								case 3:
								default:
									//play sound effect
									Audio.play(Assets.AudioGameKey.Congratulations);
									
									//display message
									game.getScreen().getScreenGameover().setMessage("Congratulations");
									break;
							}
						}
						else if (game.getCpu().hasGoal())
						{
							//play sound effect
							Audio.play(Assets.AudioGameKey.YouLose);
							
							//you lose
							game.getScreen().getScreenGameover().setMessage("You Lose");
						}
					}
	    		}
	    		else
	    		{
	    			game.getCountdown().update();
	    		}
	    	}
	    	else
	    	{
	        	//update the creation of the maze
	    		game.getLabyrinth().update();
	    	}
		}
	}
	
	/**
	 * Render the game elements
	 * @param canvas Object to write pixel data
	 * @throws Exception
	 */
    protected static void render(final Game game, final Canvas canvas) throws Exception
    {
    	if (game.getLevels() != null)
    	{
    		//if we haven't made a selection yet
    		if (!game.getLevels().hasSelection())
    		{
    			//render the level select screen
    			game.getLevels().render(canvas);
    			
    			//no need to continue
    			return;
    		}
    	}
    	
    	//make sure labyrinth object exists
    	if (game.getLabyrinth() == null)
    		return;

		//render the maze (if not created yet, the progress will be rendered)
    	game.getLabyrinth().render(canvas);
		
		//if we are resetting, no need to continue
		if (game.hasReset())
			return;
		
    	//make sure the maze exists first, and has been created
    	if (game.getLabyrinth().getMaze() != null && game.getLabyrinth().getMaze().isGenerated())
    	{
			//render the players in order since we have an isometric view
            if (game.getHuman().getRow() > game.getCpu().getRow())
            {
            	//render the human last
            	game.getCpu().render(canvas);
            	game.getHuman().render(canvas);
            }
            else if (game.getHuman().getRow() < game.getCpu().getRow())
            {
            	//render the opponent last
            	game.getHuman().render(canvas);
            	game.getCpu().render(canvas);
            }
            else
            {
            	if (game.getHuman().getCol() > game.getCpu().getCol())
            	{
                	//render the human last
            		game.getCpu().render(canvas);
            		game.getHuman().render(canvas);
            	}
            	else
            	{
                	//render the human last
            		game.getCpu().render(canvas);
            		game.getHuman().render(canvas);
            	}
            }
            
            //make sure the players are not at the goal when rendering the count down or d-pad
			if (!game.getHuman().hasGoal() && !game.getCpu().hasGoal())
			{
				//as long as it isn't game over, render the following
				if (game.getScreen().getState() != State.GameOver)
				{
			    	//if we have a delay we darken the background otherwise we render the d-pad
					if (!game.getCountdown().hasCompleted())
					{
						game.getCountdown().render(canvas);
					}
					else
					{
						game.getController().renderDPad(canvas);
					}
				}
			}
    	}
    	
    	/**
    	 * As long  as the players are not at the goal.
    	 * Render the typical menu buttons, so the player always has an option
    	 */
		if (!game.getHuman().hasGoal() && !game.getCpu().hasGoal())
			game.getController().render(canvas);
    }
}