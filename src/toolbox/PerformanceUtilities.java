package toolbox;

import org.lwjgl.Sys;

public class PerformanceUtilities
{
  private long lastFrame;
  private int fps;
  private long lastFPS;
  private int outFPS;

  public void startFrameCounter()
  {
    getDelta();
    lastFPS = getTime();

  }

  public int getFPS()
  {
    return outFPS;
  }

  private int getDelta()
  {
    long time = getTime();
    int delta = (int) (time - lastFrame);
    lastFrame = time;

    return delta;
  }

  private long getTime()
  {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }

  public void updateFPS()
  {
    if (getTime() - lastFPS > 1000)
    {
      outFPS = fps;
      fps = 0;
      lastFPS += 1000;
    }
    fps++;
  }

}
