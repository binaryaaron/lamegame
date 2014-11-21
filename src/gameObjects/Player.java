package gameObjects;

/**
 * PlayerObject is the concrete implementation of Player. It holds data for
 * every relevant player field, including position, velocity, acceleration,
 * health, id, and boolean flags to denote if the person is playing(connected)
 * or alive.
 * Created by aarongonzales on 11/14/14.
 */
public interface Player extends GObject
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
   * Gets the 'alive' flag for this player.
   */
  boolean getAlive();

  /**
   * Sets the 'alive' flag for this player.
   */
  void setAlive(Boolean alive);

  short getPlayerId();

  /**
   * Gets the status of the player - are they playing the game? logging off?
   * delete them!
   * @return if the player is playing or not
   */
  Boolean getPlaying();

  void setPlaying(Boolean playing);

  /**
   * Removes the player from the game. Handles resetting the player count and recycling the ID.
   */
  public void removePlayer();
}
