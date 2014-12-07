package world;

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
   * Utility method to create a bounding box from vertices, slow
   * @param vertices vertices of an object
   * @return the bounding box for that object
   */
  public static BoundingBox createBoundingBoxFromVertices(Vector3f[] vertices)
  {
    Vector3f first = vertices[0];
    Vector3f min = new Vector3f(first.x, first.y, first.z);
    Vector3f max = new Vector3f(first.x, first.y, first.z);

    // Brute force search
    for (Vector3f vec : vertices)
    {
      min.x = vec.x < min.x ? vec.x : min.x;
      min.y = vec.y < min.y ? vec.y : min.y;
      min.z = vec.z < min.z ? vec.z : min.z;
      max.x = vec.x > max.x ? vec.x : max.x;
      max.y = vec.y > max.y ? vec.y : max.y;
      max.z = vec.z > max.z ? vec.z : max.z;
    }

   

    return new BoundingBox(min, max);
  }

}
