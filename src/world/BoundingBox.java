package world;

import org.lwjgl.util.vector.Vector3f;

/**
 * A bounding box class that implements box, currently just for testing
 */
public class BoundingBox implements Box
{

  Vector3f min;
  Vector3f max;
  Vector3f center;

  /**
   * Create a bounding box from a minimum vector of coords and max vector of coords
   * @param min min vals x,y,z
   * @param max max vals x,y,z
   */
  public BoundingBox(Vector3f min, Vector3f max)
  {
    this.min = min;
    this.max = max;
    updateCenter();
  }

  /**
   * Utility method to update the center for quick lookup on min/max change
   */
  private void updateCenter()
  {
    float x = min.x + (max.x - min.x) * 0.5f;
    float y = min.y + (max.y - min.y) * 0.5f;
    float z = min.z + (max.z - min.z) * 0.5f;
    center = new Vector3f(x,y,z);
  }

  /**
   * Dimensions of the object
   *
   * @return x, y, z
   */
  @Override public Vector3f getDimensions()
  {
    return new Vector3f(max.x - min.x, max.y - min.y, max.z - min.z);
  }

  /**
   * Set the objects dimensions
   *
   * @param dimensions
   */
  @Override public void setDimensions(Vector3f dimensions)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Position of the box ("top-left" corner position for now)
   *
   * @return postion x,y,z
   */
  @Override public Vector3f getPosition()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * The center of the bounding box
   *
   * @return center x,y,z
   */
  @Override public Vector3f getCenter()
  {
    return center;
  }

  /**
   * Set the position
   *
   * @param position
   */
  @Override public void setPosition(Vector3f position)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Looks like this will need to be changed to reduce multiplications but not sure
   *
   * @return vector describing orientation
   */
  @Override public Vector3f getOrientation()
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Set the orientation
   *
   * @param orientation
   */
  @Override public void setOrientation(Vector3f orientation)
  {
    throw new UnsupportedOperationException();
  }

  /**
   * Min vector values for bounding box
   *
   * @return x, y, z
   */
  @Override public Vector3f getMin()
  {
    return min;
  }

  /**
   * Set the min values
   *
   * @param min x,y,z
   */
  @Override public void setMin(Vector3f min)
  {
    this.min = min;
    updateCenter();
  }

  /**
   * Get max values
   *
   * @return x, y, z
   */
  @Override public Vector3f getMax()
  {
    return max;
  }

  /**
   * Set max values
   *
   * @param max x,y,z
   */
  @Override public void setMax(Vector3f max)
  {
    this.max = max;
    updateCenter();
  }

}
