package entities;

import gameObjects.GameObject;
import world.BoundingBox;

/**
 * Player is the concrete implementation of Player. It holds data for
 * every relevant player field, including position, velocity, acceleration,
 * health, id, and boolean flags to denote if the person is playing(connected)
 * or alive.
 * Created by aarongonzales on 11/14/14.
 */
public class Player extends GameObject implements Comparable<Player>
{
  public static short PLAYER_COUNT = 0;
  private final short playerId;
  private Boolean alive = true;
  private Boolean playing = true;
  private static Boolean DEBUG = true;
  private static final int SIZE = 5;

  /**
   * Default constructor for a Player. Initializes position to
   * 0,0,0
   */
  public Player()
  {
    // check for number of players
    if (PLAYER_COUNT == 4)
    {
      System.out.println("Cannot create more than four players!");
      playerId = -1;
      return;
    }
    if(DEBUG) System.out.println("Initializing Player");
    //Each player starts in a different part of the world
    if (PLAYER_COUNT == 0)
    {
      this.initPlayerOne();
    }
    else
    {
      this.initPlayer();
    }
    velocity.set(0,0,0);
    PLAYER_COUNT++;
    this.playerId = PLAYER_COUNT;
    this.hitPoints = 1000;
    this.mass = 50;
  }

  /**
   * Initializes the first player's position. Will be updated to handle creating
   * in non-asteroid or on top of another player, but for now, player one gets
   * the center of the world. Can also get selected model.
   */
  private void initPlayerOne()
  {
    this.position.set(0,0,0);
    this.model = true;

    this.bbox = new BoundingBox(position, SIZE, SIZE, SIZE);
    //this.bbox = new BoundingBox(new Vector3f(-10, -10, -10), new Vector3f(10,10,10));

  }

  /**
   * Initializes a player other than player one's position. Likewise
   * initPlayerOne(), it should check to make sure it doesn't put the player
   * on top of another player or asteroid.
   *
   * Currently set to put a new player at 50x in a new direction based on player
   * count. All ships should be on the same plane. Also used to initialize a
   * player's model.
   */
  private void initPlayer()
  {
    this.position.set(PLAYER_COUNT*50, 0, 0);
    this.model = true;
    this.bbox = new BoundingBox(position, SIZE, SIZE, SIZE);
  }

  /**
   * Gets the player's health.
   * @return short health
   */
  public int getHitPoints()
  {
    return hitPoints;
  }

  /**
   * Sets the player's health
   * @param health the updated health score
   */
  public void setHealth(int health)
  {
    hitPoints= health;
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

  public short getPlayerId()
  {
    return playerId;
  }

  /**
   * Gets the status of the player - are they playing the game? logging off?
   * delete them!
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

  /**
   * Provides natural sorting for players based on id. Can easily be changed.
   * @param otherPlayerObject another player object
   * @return player with lower id
   */
  @Override
  public int compareTo(Player otherPlayerObject)
  {
    return (this.getPlayerId() > otherPlayerObject.getPlayerId() ) ? -1: (this.getPlayerId() > otherPlayerObject
        .getPlayerId()) ? 1:0 ;
  }

  /**
   * Disconnects the player and removes them from the player count
   * Will not work correctly as implemented - player three disconnects with four players and will not
   * get the proper id if rejoins - they get the wrong assignment
   */
  public void removePlayer()
  {
    PLAYER_COUNT--;
    //
  }

  /**
   * Kills this player object. Should send a signal to the screen and say
   * that the player is dead, allowing the player to respawn or something
   * could assign a new player with the same ID as this one ...?
   * @param go the game object to die
   */
  @Override
  protected void uponDeath(GameObject go)
  {
    System.out.println("I'm dead!");
  }

  /**
   * Converts the player's current stats to a string for sending back and forth
   * across the network
   * @return String with the playerId, position, velocity, health, and other fields
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    String delimiter = ":";
    result.append("P" + playerId + delimiter );
    result.append(position + delimiter);
    result.append(velocity + delimiter);
    result.append(hitPoints + delimiter );
    result.append(alive + delimiter);
    result.append(playing + delimiter);
    return result.toString();
  }

  /**
   * Intended only for debugging.
   *
   * <P>Here, the contents of every field are placed into the result, with
   * one field per line.
   */
  public String debugPrint(Boolean debug) {
    StringBuilder result = new StringBuilder();
    String NEW_LINE = System.getProperty("line.separator");

    result.append(this.getClass().getName() + " Object {" + NEW_LINE);
    result.append(" PlayerID: " + playerId + NEW_LINE);
    result.append(" Health: " + hitPoints + NEW_LINE);
    result.append(" Position: " + position + NEW_LINE);
    result.append(" Velocity: " + velocity + NEW_LINE );
    result.append(" Alive: " + alive + NEW_LINE);
    result.append(" Playing: " + playing + NEW_LINE);
    result.append("}");

    return result.toString();
  }

}
