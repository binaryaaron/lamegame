package physics;

import com.ra4king.opengl.util.math.Vector3;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by wortiz on 11/16/14.
 */
public class PhysicsUtilities
{

  /**
   * Elastic collision of 2 objects
   * http://en.wikipedia.org/wiki/Momentum
   *
   * @param mass1 mass of first object
   * @param vec1  velocity of first object, new velocity replaces the values
   * @param mass2
   * @param vec2
   */
  public static void elasticCollision(float mass1, Vector3 vec1, float mass2,
      Vector3 vec2)
  {
    Vector3 u1 = new Vector3(vec1);
    Vector3 u2 = new Vector3(vec2);
    Vector3 q1 = new Vector3(vec1);
    Vector3 q2 = new Vector3(vec2);

    float c1 = (mass1 - mass2) / (mass1 + mass2);
    float c2 = (2 * mass2) / (mass1 + mass2);
    float k1 = (mass2 - mass1) / (mass1 + mass2);
    float k2 = (2 * mass1) / (mass1 + mass2);
    u1.mult(c1);
    u2.mult(c2);
    q1.mult(k2);
    q2.mult(k1);

    vec1.set(u1);
    vec2.set(q1);
    vec1.add(u2);
    vec2.add(q2);
  }
}
