package playerObjects;

import org.lwjgl.util.vector.Vector3f;

/**
 * PlayerObject is the concrete implementation of Player. It holds data for
 * every relevant player field, including position, velocity, acceleration,
 * health, id, and boolean flags to denote if the person is playing(connected)
 * or alive.
 * Created by aarongonzales on 11/14/14.
 */
public interface Player
{
  /**
   * Gets the player's health.
   * @return short health
   */
  short getHealth();

  /**
   * Sets the player's health
   * @param health the updated health score
   */
  void setHealth(short health);

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

  /**
   * Gets the 'alive' flag for this player.
   */
  boolean getAlive();

  /**
   * Sets the 'alive' flag for this player.
   */
  void setAlive(Boolean alive);

  byte getPlayerId();

  /**
   * Gets the status of the player - are they playing the game? logging off?
   * delete them!
   * @return if the player is playing or not
   */
  Boolean getPlaying();

  void setPlaying(Boolean playing);
}
