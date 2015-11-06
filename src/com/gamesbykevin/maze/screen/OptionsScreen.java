package com.gamesbykevin.maze.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.maze.assets.Assets;
import com.gamesbykevin.maze.storage.settings.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of buttons for the sound
    private List<Button> sounds;
    
    //the go back button
    private Button back;
    
    //our main screen reference
    private final ScreenManager screen;
    
    //our storage settings object
    private Settings settings;
    
    //our paint object for this screen
    private Paint paint;
    
    //font size for this screen
    private static final float FONT_SIZE = 18f;
    
    public OptionsScreen(final ScreenManager screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create our paint object for this menu
        this.paint = new Paint(screen.getPaint());
        
        //change font size
        this.paint.setTextSize(FONT_SIZE);
        
        //start coordinates
        int y = ScreenManager.BUTTON_Y;
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonsSound(y);
        
        y += ScreenManager.BUTTON_Y_INCREMENT;
        addButtonsBack(y);
        
        //create our settings object, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    private void addButtonsBack(int y)
    {
        //the back button
        this.back = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        this.back.setText("Go Back");
        this.back.setX(ScreenManager.BUTTON_X);
        this.back.setY(y);
        this.back.updateBounds();
        this.back.positionText(paint);
    }
    
    private void addButtonsSound(int y)
    {
        //add audio option
        this.sounds = new ArrayList<Button>();
        
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Sound: Disabled");
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sounds.add(button);
        
        button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.setText("Sound: Enabled");
        button.setX(ScreenManager.BUTTON_X);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        this.sounds.add(button);
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //do we need anything here
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (back.contains(x, y))
            {
                //store our settings
                settings.save();
                
                //set ready state
                screen.setState(ScreenManager.State.Ready);
                
                //play sound effect
                //Audio.play(Assets.AudioMenuKey.Selection);
                
                //no need to continue
                return false;
            }
            
            for (Button button : sounds)
            {
                if (button.contains(x, y))
                {
                    //flip setting
                    Audio.setAudioEnabled(!Audio.isAudioEnabled());
                    
                    //play sound effect
                    //Audio.play(Assets.AudioMenuKey.Selection);
                    
                    //exit loop
                    return false;
                }
            }
        }
        
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw main logo
        canvas.drawBitmap(logo, ScreenManager.LOGO_X, ScreenManager.LOGO_Y, null);
        
        //draw the menu buttons
        sounds.get(Audio.isAudioEnabled() ? 1 : 0).render(canvas, paint);
        
        //render back button
        back.render(canvas, paint);
    }
    
    @Override
    public void dispose()
    {
        if (back != null)
        {
            back.dispose();
            back = null;
        }
        
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (sounds != null)
        {
            for (Button button : sounds)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            sounds.clear();
            sounds = null;
        }
        
        if (paint != null)
        	paint = null;
    }
}