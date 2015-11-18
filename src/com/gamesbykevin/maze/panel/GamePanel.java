package com.gamesbykevin.maze.panel;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.maze.MainActivity;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.screen.ScreenManager;
import com.gamesbykevin.maze.thread.MainThread;

import java.util.Random;

/**
 * Game Panel class
 * @author GOD
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Disposable
{
    /**
     * Our random object used to make random decisions
     */
    public static Random RANDOM = new Random(System.nanoTime());
    
    //default dimensions of window for this game
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    
    //the reference to our activity
    private final MainActivity activity;
    
    //the object containing our game screens
    private ScreenManager screen;
    
    //our main game thread
    private MainThread thread;
    
    /**
     * Create a new game panel
     * @param activity Our main activity reference
     */
    public GamePanel(final MainActivity activity)
    {
        //call to parent constructor
        super(activity);
        
        //store context
        this.activity = activity;
            
        //make game panel focusable = true so it can handle events
        super.setFocusable(true);
    }
    
    /**
     * Get the main game thread.<br>
     * If the main thread does not exist, it will be created
     * @return The main game thread
     */
    private MainThread getThread()
    {
    	return this.thread;
    }
    
    /**
     * Get the screen manager 
     * @return The screen manager containing all our screens
     */
    private ScreenManager getScreen()
    {
    	return this.screen;
    }
    
    @Override
    public void dispose()
    {
        //it could take several attempts to stop the thread
        boolean retry = true;
        
        //count number of attempts to complete thread
        int count = 0;
        
        //here we will attempt to stop the thread
        while (retry && count <= MainThread.COMPLETE_THREAD_ATTEMPTS)
        {
            try
            {
                //increase count
                count++;
                
                if (getThread() != null)
                {
                    //set running false, to stop the infinite loop
                	getThread().setRunning(false);

                    //wait for thread to finish
                	getThread().join();
                }
                
                //if we made it here, we were successful
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        //make thread null
        this.thread = null;
        
        //assign null
        RANDOM = null;
        
        if (screen != null)
        {
            screen.dispose();
            screen = null;
        }
        
        //recycle all asset objects
        Assets.recycle();
    }
    
    /**
     * Get the activity
     * @return The activity reference
     */
    public final MainActivity getActivity()
    {
        return this.activity;
    }
    
    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        try
        {
            if (getScreen() != null)
            {
                //calculate the coordinate offset
                final float scaleFactorX = (float)WIDTH / getWidth();
                final float scaleFactorY = (float)HEIGHT / getHeight();

                //adjust the coordinates
                final float x = event.getRawX() * scaleFactorX;
                final float y = event.getRawY() * scaleFactorY;

                //update the events
                return getScreen().update(event, x, y);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return super.onTouchEvent(event);
    }
    
    /**
     * Now that the surface has been created we can create our game objects
     * @param holder Object used to track events
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        try
        {
            //load assets
            Assets.load(getActivity());
            
            //create if null
            if (RANDOM == null)
                RANDOM = new Random(System.nanoTime());
            
            //create the thread if it doesn't exist
            if (getThread() == null)
        		this.thread = new MainThread(getHolder(), this);
            
            //if the thread hasn't been started yet
            if (!getThread().isRunning())
            {
                //start the thread
            	getThread().setRunning(true);
            	getThread().start();
            }
            
            //flag the thread as not paused
            getThread().setPause(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    	//finish the activity
    	getActivity().finish();
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        //does anything need to be done here?
    }
    
    /**
     * Update the game state
     */
    public void update()
    {
        try
        {
            //make sure the screen is created first before the thread starts
            if (getScreen() == null)
            {
                //load all assets
                Assets.load(getActivity());

                //create new screen manager
                this.screen = new ScreenManager(this);
            }
            else
            {
            	getScreen().update();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
    	draw(canvas);
    }
    
    @Override
    public void draw(Canvas canvas)
    {
        if (canvas != null)
        {
            //store the canvas state
            final int savedState = canvas.save();
            
            try
            {
                //make sure the screen object exists
                if (getScreen() != null)
                {
                    final float scaleFactorX = getWidth() / (float)GamePanel.WIDTH;
                    final float scaleFactorY = getHeight() / (float)GamePanel.HEIGHT;

                    //scale to the screen size
                    canvas.scale(scaleFactorX, scaleFactorY);
                
                    //render the main screen containing the game and other screens
                    getScreen().render(canvas);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            //restore previous canvas state
            canvas.restoreToCount(savedState);
        }
    }
}