package com.gamesbykevin.maze.labyrinth;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.maze.Room;
import com.gamesbykevin.androidframework.maze.Room.Wall;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.labyrinth.Labyrinth.TileKey;

public final class LabyrinthHelper 
{
	//dimensions for the isometric and top down animations
	private static final int ANIMATION_WIDTH_ISOMETRIC = 100;
	private static final int ANIMATION_HEIGHT_ISOMETRIC = 65;
	private static final int ANIMATION_WIDTH_TOP_DOWN = 64;
	private static final int ANIMATION_HEIGHT_TOP_DOWN = 64;
	
	/**
	 * Add the default animations
	 * @param labyrinth Object we want to add the animations to
	 */
	protected static void setupAnimations(final Labyrinth labyrinth)
	{
		int w = ANIMATION_WIDTH_ISOMETRIC;
		int h = ANIMATION_HEIGHT_ISOMETRIC;
		
		addAnimation(labyrinth, TileKey.IsometricNWE, 0, 0, w, h);
		addAnimation(labyrinth, TileKey.IsometricNWS, 100, 0, w, h);
		addAnimation(labyrinth, TileKey.IsometricNSE, 200, 0, w, h);
		addAnimation(labyrinth, TileKey.IsometricNSEW, 300, 0, w, h);
		addAnimation(labyrinth, TileKey.IsometricSWE, 400, 0, w, h);
		addAnimation(labyrinth, TileKey.IsometricNW, 0, 65, w, h);
		addAnimation(labyrinth, TileKey.IsometricNE, 100, 65, w, h);
		addAnimation(labyrinth, TileKey.IsometricSE, 200, 65, w, h);
		addAnimation(labyrinth, TileKey.IsometricSW, 300, 65, w, h);
		addAnimation(labyrinth, TileKey.IsometricNS, 400, 65, w, h);
		addAnimation(labyrinth, TileKey.IsometricWE, 0, 130, w, h);
		addAnimation(labyrinth, TileKey.IsometricN, 100, 130, w, h);
		addAnimation(labyrinth, TileKey.IsometricS, 200, 130, w, h);
		addAnimation(labyrinth, TileKey.IsometricE, 300, 130, w, h);
		addAnimation(labyrinth, TileKey.IsometricW, 400, 130, w, h);
		addAnimation(labyrinth, TileKey.IsometricGoal, 400, 195, w, h);
		
		w = ANIMATION_WIDTH_TOP_DOWN;
		h = ANIMATION_HEIGHT_TOP_DOWN;
		
		addAnimation(labyrinth, TileKey.TopDownWE, 0, 200, w, h);
		addAnimation(labyrinth, TileKey.TopDownNS, 64, 200, w, h);
		addAnimation(labyrinth, TileKey.TopDownSE, 128, 200, w, h);
		addAnimation(labyrinth, TileKey.TopDownSWE, 192, 200, w, h);
		addAnimation(labyrinth, TileKey.TopDownSW, 256, 200, w, h);
		addAnimation(labyrinth, TileKey.TopDownS, 0, 264, w, h);
		addAnimation(labyrinth, TileKey.TopDownE, 64, 264, w, h);
		addAnimation(labyrinth, TileKey.TopDownNSE, 128, 264, w, h);
		addAnimation(labyrinth, TileKey.TopDownNSEW, 192, 264, w, h);
		addAnimation(labyrinth, TileKey.TopDownNWS, 256, 264, w, h);
		addAnimation(labyrinth, TileKey.TopDownW, 0, 328, w, h);
		addAnimation(labyrinth, TileKey.TopDownN, 64, 328, w, h);
		addAnimation(labyrinth, TileKey.TopDownNE, 128, 328, w, h);
		addAnimation(labyrinth, TileKey.TopDownNWE, 192, 328, w, h);
		addAnimation(labyrinth, TileKey.TopDownNW, 256, 328, w, h);
		addAnimation(labyrinth, TileKey.TopDownGoal, 0, 392, w, h);
	}
	
	/**
	 * 
	 * @param labyrinth
	 * @param key
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	private static void addAnimation(final Labyrinth labyrinth, final TileKey key, final int x, final int y, final int w, final int h)
	{
		labyrinth.getSpritesheet().add(key, new Animation(Images.getImage(Assets.ImageGameKey.Road), x, y, w, h));
	}
	
	/**
	 * 
	 * @param labyrinth
	 * @param room
	 */
	protected static void assignAnimation(final Labyrinth labyrinth, final Room room, final boolean isometric)
	{
		if (!room.hasWall(Wall.East) && !room.hasWall(Wall.West) && !room.hasWall(Wall.North) && !room.hasWall(Wall.South))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownNSEW : TileKey.IsometricNSEW);
		}
		else if (!room.hasWall(Wall.East) && !room.hasWall(Wall.West) && !room.hasWall(Wall.North))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownNWE : TileKey.IsometricNWE);
		}
		else if (!room.hasWall(Wall.East) && !room.hasWall(Wall.West) && !room.hasWall(Wall.South))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownSWE : TileKey.IsometricSWE);
		}
		else if (!room.hasWall(Wall.East) && !room.hasWall(Wall.North) && !room.hasWall(Wall.South))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownNSE : TileKey.IsometricNSE);
		}
		else if (!room.hasWall(Wall.West) && !room.hasWall(Wall.North) && !room.hasWall(Wall.South))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownNWS : TileKey.IsometricNWS);
		}
		else if (!room.hasWall(Wall.West) && !room.hasWall(Wall.East))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownWE : TileKey.IsometricWE);
		}
		else if (!room.hasWall(Wall.West) && !room.hasWall(Wall.North))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownNW : TileKey.IsometricNW);
		}
		else if (!room.hasWall(Wall.West) && !room.hasWall(Wall.South))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownSW : TileKey.IsometricSW);
		}
		else if (!room.hasWall(Wall.East) && !room.hasWall(Wall.North))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownNE : TileKey.IsometricNE);
		}
		else if (!room.hasWall(Wall.East) && !room.hasWall(Wall.South))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownSE : TileKey.IsometricSE);
		}
		else if (!room.hasWall(Wall.South) && !room.hasWall(Wall.North))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownNS : TileKey.IsometricNS);
		}
		else if (!room.hasWall(Wall.North))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownN : TileKey.IsometricN);
		}
		else if (!room.hasWall(Wall.South))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownS : TileKey.IsometricS);
		}
		else if (!room.hasWall(Wall.West))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownW : TileKey.IsometricW);
		}
		else if (!room.hasWall(Wall.East))
		{
			labyrinth.getSpritesheet().setKey((!isometric) ? TileKey.TopDownE : TileKey.IsometricE);
		}
	}
}