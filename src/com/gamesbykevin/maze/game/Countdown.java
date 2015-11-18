package com.gamesbykevin.maze.game;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.panel.GamePanel;
import com.gamesbykevin.maze.screen.ScreenManager;

import android.graphics.Canvas;

public class Countdown extends Entity implements Disposable
{
    /**
     * Darken background when we delay game start
     */
    private static final int DELAY_VISIBILITY = 185;
    
    /**
     * How long do we delay each animation frame
     */
    private static final long ANIMATION_DURATION = 1100L;
    
    //default dimensions
    private static final int WIDTH = 133;
    private static final int HEIGHT = 201;
	
	public Countdown()
	{
		super();
		
		//position in the middle
		super.setX((GamePanel.WIDTH / 2) - (WIDTH / 2));
		super.setY((GamePanel.HEIGHT / 2) - (HEIGHT / 2));
		
		//set dimensions
		super.setWidth(WIDTH);
		super.setHeight(HEIGHT);
		
		//default animation key
		final String KEY = "DEFAULT";
		
		//create animation
		Animation animation = new Animation(
			Images.getImage(Assets.ImageGameKey.Numbers),
			0,
			0,
			WIDTH,
			HEIGHT,
			4,
			1,
			4
		);
		
		//set animation delay
		animation.setDelay(ANIMATION_DURATION);
		
		//add to sprite sheet
		super.getSpritesheet().add(KEY, animation);
		
		//set the default
		super.getSpritesheet().setKey(KEY);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	/**
	 * Update the count down animation
	 */
	public void update()
	{
		//update animation
		super.getSpritesheet().get().update();
		
		//if the animation is finished, play the music
		if (hasCompleted())
            Assets.playMusic();
	}
	
	/**
	 * Reset the count down
	 */
	public final void reset()
	{
		//reset animation
		super.getSpritesheet().get().reset();
		
        //play sound effect
        Audio.play(Assets.AudioGameKey.Countdown);
	}
	
	/**
	 * Has the count down completed?
	 * @return true if the animation has finished, false otherwise
	 */
	public boolean hasCompleted()
	{
		return super.getSpritesheet().get().hasFinished();
	}
	
	@Override
	public void render(final Canvas canvas) throws Exception
	{
		//darken background
		ScreenManager.darkenBackground(canvas, DELAY_VISIBILITY);
		
		//render image
		super.render(canvas);
	}
}