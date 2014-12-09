package entities;

import org.lwjgl.util.vector.Vector3f;
import world.BoundingBox;

import java.util.Random;

/**
 * Handy class for holding the various constants that the game needs, as well
 * as other convenience static methods
 */
public class Globals
{

  public static final Random RAND = new Random();
  public static final int WORLD_SIZE = 3000;
  public static boolean HARD_MODE = false;
  public static boolean EASY_MODE = false;
  public static final BoundingBox projectileBoundingBox;
  public static final BoundingBox shipBoundingBox;
  public static final float PLANET_RADIUS = 800f;
  public static int WINPOINTS=5;

  static {
    projectileBoundingBox = new BoundingBox(new Vector3f(-0.5f,-0.5f,-0.5f), new Vector3f(0.5f, 0.5f, 0.5f));
    shipBoundingBox = new BoundingBox(new Vector3f(-2.5f,-2.5f,-2.5f), new Vector3f(2.5f, 2.5f, 2.5f));
  }
  /**
   * Returns a pseudo-random number between min and max, inclusive.
   * The difference between min and max can be at most
   * <code>Integer.MAX_VALUE - 1</code>.
   *
   * @param min Minimum value
   * @param max Maximum value.  Must be greater than min.
   * @return Integer between min and max, inclusive.
   * @see java.util.Random#nextInt(int)
   */
  public static int randInt(int max, int min) {
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    return RAND.nextInt((max - min) + 1) + min;
  }

  /**
   * Returns a pseudo-random number between the world's coordinate values
   * e.g., (-100, 100) in each axis
   *
   * @return Integer between world size and not, inclusive.
   * @see java.util.Random#nextInt(int)
   */
  public static float randInt() {
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    return RAND.nextInt((WORLD_SIZE - WORLD_SIZE*-1) + 1) + WORLD_SIZE*-1;
  }
}
