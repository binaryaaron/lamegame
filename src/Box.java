package world;

import org.lwjgl.util.vector.Vector3f;

/**
 * 3D box interface, describes a box through position orientation and dimension
 *
 * May want to remove methods later
 */
public interface Box
{

  /**
   * Check if the box contains this point
   * @param point point to check
   * @return true if contains false otherwise
   */
  public boolean contains(Vector3f point);

  /**
   * Check if the box contains this box
   * @param box box to check
   * @return true if contains false otherwise
   */
  public boolean contains(Box box);

  /**
   * Dimensions of the object
   * @return x,y,z
   */
  public Vector3f getDimensions();

  /**
   * Set the objects dimensions
   * @param dimensions
   */
  public void setDimensions(Vector3f dimensions);

  /**
   * Position of the box ("top-left" corner position for now)
   * @return postion x,y,z
   */
  public Vector3f getPosition();

  /**
   * The center of the bounding box
   * @return center x,y,z
   */
  public Vector3f getCenter();

  /**
   * Set the position
   * @param position
   */
  public void setPosition(Vector3f position);

  /**
   * Looks like this will need to be changed to reduce multiplications but not sure
   * @return vector describing orientation
   */
  public Vector3f getOrientation();

  /**
   * Set the orientation
   * @param orientation
   */
  public void setOrientation(Vector3f orientation);

  /**
   * Min vector values for bounding box
   * @return x,y,z
   */
  public Vector3f getMin();

  /**
   * Set the min values
   * @param min x,y,z
   */
  public void setMin(Vector3f min);

  /**
   * Get max values
   * @return x,y,z
   */
  public Vector3f getMax();

  /**
   * Set max values
   * @param max x,y,z
   */
  public void setMax(Vector3f max);

  /**
   * Translate the box in space by trans
   * @param trans x,y,z
   */
  public void translate(Vector3f trans);
}
