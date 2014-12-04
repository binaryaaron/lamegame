package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager
{

  private static  int width = 1920;
  private static  int height = 1080;
  private static final int FPS_CAP = 120;

  public static void createDisplay()
  {

    ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(
        true).withProfileCore(true);

    try
    {
      DisplayMode[] modes = Display.getAvailableDisplayModes();
      
      for (int i=0;i<modes.length;i++) 
      {
          DisplayMode current = modes[i];
      }
      Display.setDisplayMode(modes[modes.length-2]);  

      Display.create(new PixelFormat(),attribs);
      Display.setTitle("lamegame");

    }
    catch (LWJGLException e)
    {
      e.printStackTrace();
    }

    GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

  }

  public static void updateDisplay()
  {
    Display.sync(FPS_CAP);
    Display.update();

  }

  public static void closeDisplay()
  {
    Display.destroy();

  }

}
