package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager
{

  private static  int width = 800;
  private static  int height = 600;
  private static final int FPS_CAP = 120;

  public static void createDisplay()
  {

    ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(
        true).withProfileCore(true);

    try
    {
      DisplayMode[] modes = Display.getAvailableDisplayModes();
      
      for (int i=0;i<modes.length;i++) {
          DisplayMode current = modes[i];
          System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
                              current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
      }
      Display.setDisplayMode(modes[modes.length-1]);  

      Display.create(new PixelFormat(),attribs);
      //Display.create();
      Display.setTitle("ThinMatrixTutorialWindow");

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
