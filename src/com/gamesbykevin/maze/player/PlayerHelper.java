package com.gamesbykevin.maze.player;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.maze.Room;
import com.gamesbykevin.androidframework.maze.Room.Wall;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.player.Player.AnimationKey;

public final class PlayerHelper 
{
	//animation dimension
	private static final int ANIMATION_DIMENSION_ISOMETRIC = 96;
	private static final int ANIMATION_DIMENSION_TOP_DOWN = 68;

	//default animation delay
	private static final long DELAY_DEFAULT = 75L;
	
	/**
	 * Assign the player's standing animation.<br>
	 * This is so the player is not facing a direction they can't move in.
	 * @param player The player we want to set the animation
	 * @param room The room we are assuming the player is in
	 */
	public static void assignStartAnimation(final Player player, final Room room)
	{
		if (!room.hasWall(Wall.South))
		{
			if (player.hasIsometric())
			{
				player.setAnimationKey(AnimationKey.IsometricSouthStand);
			}
			else
			{
				player.setAnimationKey(AnimationKey.TopDownSouthStand);
			}
		}
		else if (!room.hasWall(Wall.East))
		{
			if (player.hasIsometric())
			{
				player.setAnimationKey(AnimationKey.IsometricEastStand);
			}
			else
			{
				player.setAnimationKey(AnimationKey.TopDownEastStand);
			}
		}
		else if (!room.hasWall(Wall.West))
		{
			if (player.hasIsometric())
			{
				player.setAnimationKey(AnimationKey.IsometricWestStand);
			}
			else
			{
				player.setAnimationKey(AnimationKey.TopDownWestStand);
			}
		}
		else if (!room.hasWall(Wall.North))
		{
			if (player.hasIsometric())
			{
				player.setAnimationKey(AnimationKey.IsometricNorthStand);
			}
			else
			{
				player.setAnimationKey(AnimationKey.TopDownNorthStand);
			}
		}
	}
	
	/**
	 * Stop the animation to make the player stand.<br>
	 * Depending on the current animation
	 * @param player The player we want to assign the standing animation
	 */
	protected static void stopWalkAnimation(final Player player)
	{
		switch (player.getAnimationKey())
		{
			case IsometricWestWalk:
				player.setAnimationKey(AnimationKey.IsometricWestStand);
				break;
				
			case IsometricEastWalk:
				player.setAnimationKey(AnimationKey.IsometricEastStand);
				break;
				
			case IsometricNorthWalk:
				player.setAnimationKey(AnimationKey.IsometricNorthStand);
				break;
				
			case IsometricSouthWalk:
				player.setAnimationKey(AnimationKey.IsometricSouthStand);
				break;
		
			case TopDownWestWalk:
				player.setAnimationKey(AnimationKey.TopDownWestStand);
				break;
				
			case TopDownEastWalk:
				player.setAnimationKey(AnimationKey.TopDownEastStand);
				break;
				
			case TopDownNorthWalk:
				player.setAnimationKey(AnimationKey.TopDownNorthStand);
				break;
				
			case TopDownSouthWalk:
				player.setAnimationKey(AnimationKey.TopDownSouthStand);
				break;
				
			default:
				break;
		}
	}

	/**
	 * Assign the correct walking animation.<br>
	 * Here we will take into consideration the players velocity as well as the isometric setting.
	 * @param player The player
	 * @param isometric Are we rendering isometric?
	 */
	protected static void assignWalkAnimation(final Player player)
	{
		//is the animation isometric?
		final boolean isometric = player.hasIsometric();
		
		if (player.getDX() != 0)
		{
			if (player.getDX() > 0)
			{
				if (isometric)
				{
					player.setAnimationKey(AnimationKey.IsometricEastWalk);
				}
				else
				{
					player.setAnimationKey(AnimationKey.TopDownEastWalk);
				}
			}
			else
			{
				if (isometric)
				{
					player.setAnimationKey(AnimationKey.IsometricWestWalk);
				}
				else
				{
					player.setAnimationKey(AnimationKey.TopDownWestWalk);
				}
			}
		}
		else if (player.getDY() != 0)
		{
			if (player.getDY() > 0)
			{
				if (isometric)
				{
					player.setAnimationKey(AnimationKey.IsometricSouthWalk);
				}
				else
				{
					player.setAnimationKey(AnimationKey.TopDownSouthWalk);
				}
			}
			else
			{
				if (isometric)
				{
					player.setAnimationKey(AnimationKey.IsometricNorthWalk);
				}
				else
				{
					player.setAnimationKey(AnimationKey.TopDownNorthWalk);
				}
			}
		}
	}
	
	/**
	 * Setup animations for the player
	 * @param player
	 */
	protected static void setupAnimations(final Player player)
	{
		addAnimation(player, AnimationKey.IsometricWestWalk, 96, 0, ANIMATION_DIMENSION_ISOMETRIC, 8, true);
		addAnimation(player, AnimationKey.IsometricWestStand, 0, 0, ANIMATION_DIMENSION_ISOMETRIC, 1, false);
		
		addAnimation(player, AnimationKey.IsometricEastWalk, 96, 297, ANIMATION_DIMENSION_ISOMETRIC, 8, true);
		addAnimation(player, AnimationKey.IsometricEastStand, 0, 297, ANIMATION_DIMENSION_ISOMETRIC, 1, false);
		
		addAnimation(player, AnimationKey.IsometricSouthWalk, 96, 200, ANIMATION_DIMENSION_ISOMETRIC, 8, true);
		addAnimation(player, AnimationKey.IsometricSouthStand, 0, 200, ANIMATION_DIMENSION_ISOMETRIC, 1, false);
		
		addAnimation(player, AnimationKey.IsometricNorthWalk, 96, 104, ANIMATION_DIMENSION_ISOMETRIC, 8, true);
		addAnimation(player, AnimationKey.IsometricNorthStand, 0, 104, ANIMATION_DIMENSION_ISOMETRIC, 1, false);
		

		addAnimation(player, AnimationKey.TopDownNorthWalk, 0, 425, ANIMATION_DIMENSION_TOP_DOWN, 6, true);
		addAnimation(player, AnimationKey.TopDownNorthStand, 0, 425, ANIMATION_DIMENSION_TOP_DOWN, 1, false);
		
		addAnimation(player, AnimationKey.TopDownEastWalk, 0, 548, ANIMATION_DIMENSION_TOP_DOWN, 6, true);
		addAnimation(player, AnimationKey.TopDownEastStand, 0, 548, ANIMATION_DIMENSION_TOP_DOWN, 1, false);
		
		addAnimation(player, AnimationKey.TopDownSouthWalk, 0, 653, ANIMATION_DIMENSION_TOP_DOWN, 6, true);
		addAnimation(player, AnimationKey.TopDownSouthStand, 0, 653, ANIMATION_DIMENSION_TOP_DOWN, 1, false);
		
		addAnimation(player, AnimationKey.TopDownWestWalk, 0, 750, ANIMATION_DIMENSION_TOP_DOWN, 6, true);
		addAnimation(player, AnimationKey.TopDownWestStand, 0, 750, ANIMATION_DIMENSION_TOP_DOWN, 1, false);
	}
	
	/**
	 * Add animation to the specified player
	 * @param player
	 * @param key
	 * @param x
	 * @param y
	 * @param d
	 * @param count
	 * @param loop
	 */
	private static void addAnimation(final Player player, final AnimationKey key, final int x, final int y, final int d, final int count, final boolean loop)
	{
		//create animation
		Animation animation = new Animation(
			Images.getImage(
				player.isHuman() ? Assets.ImageGameKey.Player1 : Assets.ImageGameKey.Player2
			),
			x,
			y,
			d,
			d,
			count,
			1,
			count
		);
			
		//assign loop
		animation.setLoop(loop);

		//assign the time delay between each animation
		animation.setDelay(DELAY_DEFAULT);
		
		//add to sprite sheet
		player.getSpritesheet().add(key, animation);
	}
}