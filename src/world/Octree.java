package world;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by wortiz on 11/15/14.
 */
public class Octree extends BoundingBox
{
  Octree[] children;

  float size;

  public Octree(Vector3f center, float size)
  {
    super(new Vector3f(size, size, size), new Vector3f(0f, 0f, 0f));
  }

  public boolean isLeaf()
  {
    return children == null;
  }
}
