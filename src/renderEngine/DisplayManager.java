/** 
 * Thanks to youtube user ThinMatrix
 * DisplayManager controls which opengl version is used and how the display
 * screen is created.
 */
package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager
{

  // Define the width and height of the screen in pixels
  private static final int WIDTH = 1560;
  private static final int HEIGHT = 1080;
  // The maximum frames per second possible in the display
  private static final int FPS_CAP = 120;

  public static void createDisplay()
  {
    // Define the openGL version and properties.
    // Currently uses forward-compatible openGL 3.2 with core profiling
    ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(
        true).withProfileCore(true);

    // Create the window and set its title
    try
    {
      Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
      Display.create(new PixelFormat(), attribs);
      Display.setTitle("v0.002");
    }
    catch (LWJGLException e)
    {
      e.printStackTrace();
    }

    GL11.glViewport(0, 0, WIDTH, HEIGHT);

  }

  /**
   * Update the display by syncing with the FPS and displaying the drawn scene.
   */
  public static void updateDisplay()
  {
    Display.sync(FPS_CAP);
    Display.update();

  }

  /**
   * Simple method to destroy the display. Normally called when the window is
   * closed.
   */
  public static void closeDisplay()
  {
    Display.destroy();

  }

}