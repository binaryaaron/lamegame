package gameObjects;
import org.lwjgl.util.vector.Vector3f;
import world.Box;

/**
 * Created by aarongonzales on 11/18/14.
 */
public interface GObject
{
  /**
   * Gets the velocity vector
   * @return vector of velocities
   */
  public Vector3f getVelocity();

  /**
   * Sets the velocity vector
   * @param velocity vector of velocities
   */
  public void setVelocity(Vector3f velocity);


  /**
   * Placeholder method for getting the player's "model" - my generic
   * term for the skin this player has at the moment
   * @return the current skin
   */
  public Boolean getModel();

  /**
   * Placeholder method for updating the player's "model" - my generic
   * term for the skin this player has at the moment
   */
  public void setModel(Boolean model);

  /**
   * Generic method to return the type of object.
   */
  public String getType();

  /**
   * Gets this object's bounding box.
   */
  public Box getBBox();

  /**
   * Updates the velocity by a constant factor in all directions
   */
  public void increaseVelocity(float increase);

  /**
   * Increases the x velocity by a constant factor.
   * @param increase an integer amount for increasing
   */
  public void increaseVx(float increase);


  /**
   * Increases the y velocity by a constant factor.
   * @param increase an integer amount for increasing
   */
  public void increaseVy(float increase);

  /**
   * Increases the z velocity by a constant factor.
   * @param increase an integer amount for increasing
   */
  public void increaseVz(float increase);

  /**
   * Gets the string that will be sent back and forth from the server.
   * @return String
   */
  public String toString();

}
