package world;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by wortiz on 11/15/14.
 */
public class BoxUtilities
{
  /**
   * Basic collision detection of two boxes
   * @param box1 first box
   * @param box2 second box
   * @return true if they are colliding
   */
  public static boolean collision(Box box1, Box box2)
  {
    Vector3f box1min = box1.getMin();
    Vector3f box1max = box1.getMax();
    Vector3f box2min = box2.getMin();
    Vector3f box2max = box2.getMax();

    return box1min.x < box2max.x && box1max.x > box2min.x &&
        box1min.y < box2max.y && box1max.y > box2min.y &&
        box1min.z < box2max.z && box1max.z > box2min.z;
  }

  /**
   * Some simple tests that should be expanded on
   */
  public static void checkCollision()
  {
    Vector3f min = new Vector3f(0f, 0f, 0f);
    Vector3f max = new Vector3f(5f,5f,5f);

    Box b1 = new BoundingBox(min, max);
    Box b2 = new BoundingBox(new Vector3f(-5f, -5f, -5f), min);

    // Should be true
    System.out.println(collision(b1, b1));
    System.out.println(collision(b2, b2));

    // Should be false
    System.out.println(collision(b1, b2));
    System.out.println(collision(b2, b1));

    Box b3 = new BoundingBox(new Vector3f(4f,5f,5f), new Vector3f(8f,8f,8f));
    // Should be false;
    System.out.println(collision(b1,b3));
    System.out.println(collision(b3, b1));

    Box b4 = new BoundingBox(new Vector3f(5f,4f,5f), new Vector3f(8f,8f,8f));
    // Should be false;
    System.out.println(collision(b1,b4));
    System.out.println(collision(b4, b1));

    Box b5 = new BoundingBox(new Vector3f(5f,5f,4f), new Vector3f(8f,8f,8f));
    // Should be false;
    System.out.println(collision(b1,b5));
    System.out.println(collision(b5, b1));

    Box b6 = new BoundingBox(new Vector3f(4f,4f,4f), new Vector3f(8f,8f,8f));
    // Should be true
    System.out.println(collision(b1, b6));
    System.out.println(collision(b6, b1));
  }

  public static void main(String[] args)
  {
    checkCollision();
  }

}
