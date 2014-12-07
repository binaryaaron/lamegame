package physics;

import com.ra4king.opengl.util.math.Vector3;
import entities.Entity;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by wortiz on 11/16/14.
 */
public class PhysicsUtilities
{

  private static final float EPSILON = (float) 1e-7;
  /**
   * Elastic collision of 2 objects
   * http://en.wikipedia.org/wiki/Momentum
   *
   * @param first - an entity
   * @param first - an entity
   */
  public static void elasticCollision(Entity first, Entity second)
  {
    Vector3 vec1 = first.vel;
    Vector3 vec2 = second.vel;

    Vector3 u1 = new Vector3(vec1);
    Vector3 u2 = new Vector3(vec2);
    Vector3 q1 = new Vector3(vec1);
    Vector3 q2 = new Vector3(vec2);

    float mass1 = first.mass;
    float mass2 = second.mass;
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

    Vector3 diff = new Vector3(first.position.x, first.position.y, first.position.z);

    diff.sub(new Vector3(second.position.x, second.position.y, second.position.z));
    normalize(diff);
    diff.mult(0.01f);

    vec1.add(diff);
    vec2.sub(diff);

//    System.out.println(vec1);
//    System.out.println(vec2);
  }

  private static void normalize(Vector3 vec)
  {
    float length = vec.length();
    if (vec.length() > EPSILON)
    {
      vec.divide(length);
    }
  }

}
