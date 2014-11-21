package gameObjects;

import org.lwjgl.util.vector.Vector3f;
/**
 * Created by carlyhendrickson on 11/18/14.
 */
public class Asteroid implements GObject
{
  private Vector3f position = new Vector3f();
  private Vector3f velocity = new Vector3f();
  private Vector3f accel = new Vector3f();
  private Boolean model = false;
  private static short ASTEROID_COUNT = 0;
  private static Boolean DEBUG = true;

  @Override public Vector3f getPosition()
  {
    return position;
  }

  @Override public void setPosition(Vector3f position)
  {
    this.position = position;
  }

  @Override public Vector3f getVelocity()
  {
    return velocity;
  }

  public void setVelocity(Vector3f velocity)
  {
    this.velocity = velocity;
  }

  /**
   * Updates the velocity by a constant in all directions
   *
   * @param increase the amount to increase the velocity by. may be negative.
   *                 must be within range of a byte (-127 -- 127)
   */
  @Override public void increaseVelocity(byte increase)
  {
    velocity.set(velocity.getX()+increase, velocity.getY() + increase, velocity.getZ() + increase);
  }

  @Override public Vector3f getAccel()
  {
    return accel;
  }

  public void setAccel(Vector3f accel)
  {
    this.accel = accel;
  }

  /**
   * Placeholder method for getting the player's "model" - my generic
   * term for the skin this player has at the moment
   * @return the current skin
   */
  @Override public Boolean getModel()
  {
    return model;
  }

  /**
   * Placeholder method for updating the player's "model" - my generic
   * term for the skin this player has at the moment
   * @return the current skin
   */
  @Override public void setModel(Boolean model)
  {
    this.model = model;
  }
}
