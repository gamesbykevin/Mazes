package com.gamesbykevin.maze.labyrinth;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.panel.GamePanel;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.maze.Maze;
import com.gamesbykevin.androidframework.maze.algorithm.RecursiveBacktracking;
import com.gamesbykevin.androidframework.maze.algorithm.AldousBroder;
import com.gamesbykevin.androidframework.maze.algorithm.BinaryTree;
import com.gamesbykevin.androidframework.maze.algorithm.Ellers;
import com.gamesbykevin.androidframework.maze.algorithm.GrowingTree;
import com.gamesbykevin.androidframework.maze.algorithm.HuntKill;
import com.gamesbykevin.androidframework.maze.algorithm.Kruskals;
import com.gamesbykevin.androidframework.maze.algorithm.Prims;
import com.gamesbykevin.androidframework.maze.algorithm.Sidewinder;
import com.gamesbykevin.androidframework.maze.algorithm.Wilsons;

public class Labyrinth implements Disposable 
{
	private Maze maze;
	
	public Labyrinth() throws Exception
	{
		final int rows = 10;
		final int cols = 20;
		final int d = 32;
		final int x = 50;
		final int y = 100;

		//pick a random maze algorithm
		switch (GamePanel.RANDOM.nextInt(10))
		{
			case 0:
				this.maze = new RecursiveBacktracking(cols, rows);
				break;
				
			case 1:
				this.maze = new AldousBroder(cols, rows);
				break;
				
			case 2:
				this.maze = new BinaryTree(cols, rows);
				break;
				
			case 3:
				this.maze = new Ellers(cols, rows);
				break;
				
			case 4:
				this.maze = new GrowingTree(cols, rows);
				break;
				
			case 5:
				this.maze = new HuntKill(cols, rows);
				break;
				
			case 6:
				this.maze = new Kruskals(cols, rows);
				break;
				
			case 7:
				this.maze = new Prims(cols, rows);
				break;
				
			case 8:
				this.maze = new Sidewinder(cols, rows);
				break;
				
			case 9:
			default:
				this.maze = new Wilsons(cols, rows);
				break;
		}
		
		
		//setup maze
		this.maze.setX(x);
		this.maze.setY(y);
		this.maze.setD(d);
		
		//setup the progress bar
		this.maze.getProgress().setScreen(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		this.maze.getProgress().getPaint().setTypeface(Font.getFont(Assets.FontMenuKey.Default));
		this.maze.getProgress().getPaint().setTextSize(32f);
		this.maze.getProgress().setDescription("Generating Maze...  ");
	}
	
	public void update() throws Exception
	{
		if (!maze.isGenerated())
		{
			//we update several times
			maze.update(GamePanel.RANDOM);
			maze.update(GamePanel.RANDOM);
			maze.update(GamePanel.RANDOM);
			maze.update(GamePanel.RANDOM);
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
		
	}
	
	public void render(final Canvas canvas)
	{
		maze.render(canvas);
	}
}
