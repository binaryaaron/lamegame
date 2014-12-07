package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager
{
  
  private static final int FPS_CAP = 120;
  private static DisplayMode []modes;
  private static boolean fullScreen = false;
  
  public static void createDisplay()
  {

    ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(
        true).withProfileCore(true);

    try
    {
      modes = Display.getAvailableDisplayModes();
      DisplayMode temp = modes[0];
      //set default mode to 1280x720
      for(DisplayMode mode : modes)
      {
        if(mode.getHeight() == 720 && mode.getWidth() == 1280)
        {
          modes[0] = mode;
          mode = temp;
        }
      }
      Display.setDisplayMode(modes[0]);
      Display.create(new PixelFormat(),attribs);
      
      Display.setTitle("ThinMatrixTutorialWindow");

    }
    catch (LWJGLException e)
    {
      e.printStackTrace();
    }

    GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

  }

  /**
   * Change the resolution of the window
   * @param i
   */
  public static void changeResolution(int i)
  {
    i%=modes.length;
    try 
    {
      Display.setDisplayMode(modes[i]);
    } 
    catch (LWJGLException e) 
    {
      e.printStackTrace();
    }
    GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());    
  }
  
  /**
   * Change the screen to switch fullscreen on/off
   */
  public static void changeFullScreen()
  {
    try 
    {
      fullScreen = !fullScreen;
      Display.setFullscreen(fullScreen);
      Display.setDisplayMode(Display.getDisplayMode());
    } 
    catch (LWJGLException e) 
    {
      e.printStackTrace();
    }
    GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());  
  }
  public static void updateDisplay()
  {
//    Display.sync(FPS_CAP);
    Display.update();

  }

  public static void closeDisplay()
  {
    Display.destroy();

  }

}
