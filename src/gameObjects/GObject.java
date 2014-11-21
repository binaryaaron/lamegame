package gameObjects;
import org.lwjgl.util.vector.Vector3f;
/**
 * Created by aarongonzales on 11/18/14.
 */
public interface GObject
{
  /**
   * Gets the full position vector.
   * @return vector of floats
   */
  Vector3f getPosition();

  /**
   * Sets the full position vector.
   */
  void setPosition(Vector3f position);

  /**
   * Gets the velocity vector
   * @return vector of velocities
   */
  Vector3f getVelocity();

  /**
   * Sets the velocity vector
   * @param velocity vector of velocities
   */
  void setVelocity(Vector3f velocity);

  /**
   * Updates the velocity by a constant factor in all directions
   */
  void increaseVelocity(byte increase);

  /**
   * Gets the acceleration vector
   * @return vector of floats
   */
  Vector3f getAccel();

  void setAccel(Vector3f accel);

  /**
   * Placeholder method for getting the player's "model" - my generic
   * term for the skin this player has at the moment
   * @return the current skin
   */
  Boolean getModel();

  /**
   * Placeholder method for updating the player's "model" - my generic
   * term for the skin this player has at the moment
   */
  void setModel(Boolean model);

}
