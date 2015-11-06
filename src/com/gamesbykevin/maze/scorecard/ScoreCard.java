package com.gamesbykevin.maze.scorecard;

import android.app.Activity;

import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.maze.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Here we will track the best time and save it to the internal storage
 * @author GOD
 */
public final class ScoreCard extends Internal
{
    //list of scores
    private List<Score> scores;
    
    /**
     * New level separator string
     */
    private static final String NEW_LEVEL = ";";
    
    /**
     * This string will separate the level from the time
     */
    private static final String SEPARATOR = "-";
    
    //our game reference object
    private final Game game;
    
    public ScoreCard(final Game game, final Activity activity)
    {
        super("ScoreCard", activity);
        
        //store our game reference object
        this.game = game;
        
        //create new score
        this.scores = new ArrayList<Score>();
        
        //make sure content exists before we try to load it
        if (super.getContent().toString().trim().length() > 0)
        {
            //load file with each level on a new line
            final String[] levels = super.getContent().toString().split(NEW_LEVEL);

            //load each level into our array
            for (int index = 0; index < levels.length; index++)
            {
                //split level data
                String[] data = levels[index].split(SEPARATOR);

                //get the information
                final int modeIndex = Integer.parseInt(data[0]);
                final int difficultyIndex = Integer.parseInt(data[1]);
                final int level = Integer.parseInt(data[2]);
                final long time = Long.parseLong(data[3]);

                //load the score to our list
                update(modeIndex, difficultyIndex, level, time);
            }
        }
    }
    
    /**
     * Update the level with the specified score.<br>
     * If the specified level does not exist for the difficulty, it will be added
     * @param modeIndex The mode index
     * @param difficultyIndex The difficulty index
     * @param level The specified level
     * @param time The time duration
     * @return true if updating the score was successful, false otherwise
     */
    public boolean update(final int modeIndex, final int difficultyIndex, final int level, final long time)
    {
    	return true;
    }
    
    /**
     * Save the scores to the internal storage
     */
    @Override
    public void save()
    {
        //remove all existing content
        super.getContent().delete(0, super.getContent().length());
        
        for (Score score : scores)
        {
            //if content exists, add new line, to separate each level
            if (super.getContent().length() > 0)
                super.getContent().append(NEW_LEVEL);
            
            //write difficulty, level, time
            super.getContent().append(score.getModeIndex());
            super.getContent().append(SEPARATOR);
            super.getContent().append(score.getDifficultyIndex());
            super.getContent().append(SEPARATOR);
            super.getContent().append(score.getLevel());
            super.getContent().append(SEPARATOR);
            super.getContent().append(score.getTime());
        }
        
        //save the content to physical internal storage location
        super.save();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (scores != null)
        {
            scores.clear();
            scores = null;
        }
    }
}