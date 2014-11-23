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
   * Create a bounding box from a "center" position vector and size indicators
   * @param center a vector for the center position
   * @param sizex 1/2 the size of the x direction
   * @param sizey 1/2 the size of the y direction
   * @param sizez 1/2 the size of the z direction
   */
  public BoundingBox(Vector3f center, float sizex, float sizey, float sizez)
  {
    this.center = center;
    this.min = new Vector3f(center.getX() - sizex, center.getY() - sizey, center.getZ() - sizez);
    this.max = new Vector3f(center.getX() + sizex, center.getY() + sizey, center.getZ() + sizez);
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
   * Check if the box contains this point
   *
   * @param point point to check
   * @return true if contains false otherwise
   */
  @Override public boolean contains(Vector3f point)
  {
    return point.x >= min.x && point.x <= max.x &&
        point.y >= min.y && point.y <= max.y &&
        point.z >= min.z && point.z <= max.z;
  }

  /**
   * Check if the box contains this box
   * @param box box to check
   * @return true if contains false otherwise
   */
  @Override public boolean contains(Box box)
  {
    Vector3f boxMin = box.getMin();
    Vector3f boxMax = box.getMax();

    return min.x <= boxMin.x && max.x >= boxMax.x &&
        min.y <= boxMin.y && max.y >= boxMax.y &&
        min.z <= boxMin.z && max.z >= boxMax.z;
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

  /**
   * Translate the box in space by trans
   *
   * @param trans x,y,z
   */
  @Override public void translate(Vector3f trans)
  {
    min.translate(trans.x, trans.y, trans.z);
    max.translate(trans.x, trans.y, trans.z);
    center.translate(trans.x, trans.y, trans.z);
  }

}
