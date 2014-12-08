package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import world.BoundingBox;

/**
 *
 */
public class Player extends Entity
{
  public static short PLAYER_COUNT = 0;
  //private final short playerId;
  private Boolean alive = true;
  private Boolean playing = true;
  private static Boolean DEBUG = true;
  public int score = 0;

  /**
   * Default constructor for a Player. Initializes position to
   * 0,0,0
   */
  public Player(String id, TexturedModel model, Vector3f position, float rotX,
      float rotY, float rotZ, float scale, int clientId)
  {
    super(id, model, position, rotX, rotY, rotZ, scale, clientId);
  }

  //  /**
  //   * Initializes the first player's position. Will be updated to handle creating
  //   * in non-asteroid or on top of another player, but for now, player one gets
  //   * the center of the world. Can also get selected model.
  //   */
  //  private void initPlayerOne()
  //  {
  //    this.position.set(0,0,0);
  //    this.model = true;
  //
  //    this.bbox = new BoundingBox(position, SIZE, SIZE, SIZE);
  //    //this.bbox = new BoundingBox(new Vector3f(-10, -10, -10), new Vector3f(10,10,10));
  //
  //  }
  //
  //  /**
  //   * Initializes a player other than player one's position. Likewise
  //   * initPlayerOne(), it should check to make sure it doesn't put the player
  //   * on top of another player or asteroid.
  //   *
  //   * Currently set to put a new player at 50x in a new direction based on player
  //   * count. All ships should be on the same plane. Also used to initialize a
  //   * player's model.
  //   */
  //  private void initPlayer()
  //  {
  //    this.position.set(PLAYER_COUNT*50, 0, 0);
  //    this.model = true;
  //    this.bbox = new BoundingBox(position, SIZE, SIZE, SIZE);
  //  }

  /**
   * Gets the player's health.
   *
   * @return short health
   */
  public int getHitPoints()
  {
    return hitPoints;
  }

  /**
   * Sets the player's health
   *
   * @param health the updated health score
   */
  public void setHealth(int health)
  {
    hitPoints = health;
  }

  /**
   * Gets the 'alive' flag for this player.
   */
  public boolean getAlive()
  {
    return alive;
  }

  /**
   * Sets the 'alive' flag for this player.
   */
  public void setAlive(Boolean alive)
  {
    this.alive = alive;
  }

  /**
   * Gets the status of the player - are they playing the game? logging off?
   * delete them!
   *
   * @return if the player is playing or not
   */
  public Boolean getPlaying()
  {
    return playing;

  }

  public void setPlaying(Boolean playing)
  {
    this.playing = playing;
  }

  //  /**
  //   * Provides natural sorting for players based on id. Can easily be changed.
  //   * @param otherPlayer another player object
  //   * @return player with lower id
  //   */
  //  @Override
  //  public int compareTo(Player otherPlayer)
  //  {
  //    return (this.getPlayerId() > otherPlayer.getPlayerId() ) ? -1: (this.getPlayerId() > otherPlayer
  //        .getPlayerId()) ? 1:0 ;
  //  }

  /**
   * Kills this player object. Should send a signal to the screen and say
   * that the player is dead, allowing the player to respawn or something
   * could assign a new player with the same ID as this one ...?
   *
   * @param player the game object to die
   */
  protected void uponDeath(Player player)
  {
    System.out.println("I'm dead!");
  }

  public int getClientID()
  {
    return clientId;
  }

  @Override
  public String toString()
  {
    StringBuilder result = new StringBuilder();
    String delimiter = ",";
    result.append(id).append(delimiter);
    result.append(position.x).append(delimiter);
    result.append(position.y).append(delimiter);
    result.append(position.z).append(delimiter);
    result.append(orientation.x()).append(delimiter);
    result.append(orientation.y()).append(delimiter);
    result.append(orientation.z()).append(delimiter);
    result.append(orientation.w()).append(delimiter);
    result.append(scale).append(delimiter);
    result.append(clientId).append(delimiter);
    result.append(hitPoints).append(delimiter);
    result.append(vel.length()).append(delimiter);
    result.append(score).append(delimiter);
    return result.toString();
  }

}
