package physics;

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
  public static void elasticCollision(float mass1, Vector3f vec1, float mass2,
      Vector3f vec2)
  {
    Vector3f u1 = new Vector3f(vec1);
    Vector3f u2 = new Vector3f(vec2);
    Vector3f q1 = new Vector3f(vec1);
    Vector3f q2 = new Vector3f(vec2);

    float c1 = (mass1 - mass2) / (mass1 + mass2);
    float c2 = (2 * mass2) / (mass1 + mass2);
    float k1 = (mass2 - mass1) / (mass1 + mass2);
    float k2 = (2 * mass1) / (mass1 + mass2);

    u1.scale(c1);
    u2.scale(c2);
    q1.scale(k1);
    q2.scale(k2);

    Vector3f.add(u1, u2, vec1);
    Vector3f.add(q1, q1, vec2);
  }
}
