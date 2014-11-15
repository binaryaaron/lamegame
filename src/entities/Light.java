/**
 * Thanks to youtube user thinMatrix
 * Class that defines light objects.
 * Lights have a position and a color. An object's normals define how light
 * affects that object.
 */
package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light
{
  private Vector3f position;
  private Vector3f color;

  /**
   * Create a light with this position and color
   *
   * @param position
   * @param color
   */
  public Light(Vector3f position, Vector3f color)
  {
    super();
    this.position = position;
    this.color = color;
  }

  /**
   * getters and setters
   *
   * @param / @return
   */
  public Vector3f getPosition()
  {
    return position;
  }

  public void setPosition(Vector3f position)
  {
    this.position = position;
  }

  public Vector3f getColor()
  {
    return color;
  }

  public void setColor(Vector3f color)
  {
    this.color = color;
  }

}
