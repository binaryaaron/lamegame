package world;

import junit.framework.TestCase;
import org.junit.Before;
import org.lwjgl.util.vector.Vector3f;

public class BoxUtilitiesTest extends TestCase
{
  Box b1;
  Box b2;
  Box b3;
  Box b4;
  Box b5;
  Box b6;

  @Before public void setUp() throws Exception
  {
    Vector3f min = new Vector3f(0f, 0f, 0f);
    Vector3f max = new Vector3f(5f, 5f, 5f);

    b1 = new BoundingBox(min, max);
    b2 = new BoundingBox(new Vector3f(-5f, -5f, -5f), min);
    b3 = new BoundingBox(new Vector3f(4f, 5f, 5f), new Vector3f(8f, 8f, 8f));

    b4 = new BoundingBox(new Vector3f(5f, 4f, 5f), new Vector3f(8f, 8f, 8f));

    b5 = new BoundingBox(new Vector3f(5f, 5f, 4f), new Vector3f(8f, 8f, 8f));

    b6 = new BoundingBox(new Vector3f(4f, 4f, 4f), new Vector3f(8f, 8f, 8f));
  }

  public void testCollision() throws Exception
  {
    // check identity
    assertTrue(BoxUtilities.collision(b1, b1));
    assertTrue(BoxUtilities.collision(b2, b2));
    assertTrue(BoxUtilities.collision(b3, b3));
    assertTrue(BoxUtilities.collision(b4, b4));
    assertTrue(BoxUtilities.collision(b5, b5));
    assertTrue(BoxUtilities.collision(b6, b6));

    assertFalse(BoxUtilities.collision(b1, b2));
    assertFalse(BoxUtilities.collision(b2, b1));

    assertFalse(BoxUtilities.collision(b1, b3));
    assertFalse(BoxUtilities.collision(b3, b1));

    assertFalse(BoxUtilities.collision(b1, b4));
    assertFalse(BoxUtilities.collision(b4, b1));

    assertFalse(BoxUtilities.collision(b1, b5));
    assertFalse(BoxUtilities.collision(b5, b1));

    assertTrue(BoxUtilities.collision(b1, b6));
    assertTrue(BoxUtilities.collision(b6, b1));
  }

  public void testCreateBoundingBoxFromVertices() throws Exception
  {

  }
}