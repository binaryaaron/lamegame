/**
 * Thanks to youtube user ThinMatrix
 * Class that defines entities, which are objects with borders to be
 * rendered in the game board
 */
package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Entity
{
  private TexturedModel model;
  private Vector3f position;
  private float rotX, rotY, rotZ; //expected in radians
  private float scale;

  /**
   * Defines an entity with a given model, position, rotation and scale.
   *
   * @param model
   * @param position
   * @param rotX
   * @param rotY
   * @param rotZ
   * @param scale
   */
  public Entity(TexturedModel model, Vector3f position, float rotX, float rotY,
      float rotZ, float scale)
  {
    super();
    this.model = model;
    this.position = position;
    this.rotX = rotX;
    this.rotY = rotY;
    this.rotZ = rotZ;
    this.scale = scale;
  }

  /**
   * Moves the object by the given amount
   *
   * @param dx
   * @param dy
   * @param dz
   */
  public void translate(float dx, float dy, float dz)
  {
    this.position.x += dx;
    this.position.y += dy;
    this.position.z += dz;

  }

  /**
   * Rotates the object by the given amount
   *
   * @param dx
   * @param dy
   * @param dz
   */
  public void rotatate(float dx, float dy, float dz)
  {
    this.rotX += dx;
    this.rotY += dy;
    this.rotZ += dz;

  }

  /**
   * getters and setters
   *
   * @param / @return
   */
  public float getScale()
  {
    return scale;
  }

  public void setScale(float scale)
  {
    this.scale = scale;
  }

  public TexturedModel getModel()
  {
    return model;
  }

  public void setModel(TexturedModel model)
  {
    this.model = model;
  }

  public Vector3f getPosition()
  {
    return position;
  }

  public void setPosition(Vector3f position)
  {
    this.position = position;
  }

  public float getRotX()
  {
    return rotX;
  }

  public void setRotX(float rotX)
  {
    this.rotX = rotX;
  }

  public float getRotY()
  {
    return rotY;
  }

  public void setRotY(float rotY)
  {
    this.rotY = rotY;
  }

  public float getRotZ()
  {
    return rotZ;
  }

  public void setRotZ(float rotZ)
  {
    this.rotZ = rotZ;
  }

}
