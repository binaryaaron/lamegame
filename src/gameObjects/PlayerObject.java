package gameObjects;

import org.lwjgl.util.vector.Vector3f;

/**
 * PlayerObject is the concrete implementation of Player. It holds data for
 * every relevant player field, including position, velocity, acceleration,
 * health, id, and boolean flags to denote if the person is playing(connected)
 * or alive.
 * Created by aarongonzales on 11/14/14.
 */
public class PlayerObject implements Comparable<PlayerObject>, Player
{
  private short health;
  private Vector3f position = new Vector3f();
  private Vector3f velocity = new Vector3f();
  private Vector3f accel = new Vector3f();
  private Boolean model = false;
  public static short PLAYER_COUNT = 0;
  public final short playerId;
  private Boolean alive = true;
  private Boolean playing = true;
  private static Boolean DEBUG = true;


  /**
   * Default constructor for a PlayerObject. Initializes position to
   * 0,0,0
   */
  public PlayerObject()
  {
    // check for number of players
    if (PLAYER_COUNT == 4)
    {
      System.out.println("Cannot create more than four players!");
      playerId = -1;
      return;
    }
    if(DEBUG) System.out.println("Initializing PlayerObject");
    this.health = 100;
    //Each player starts in a different part of the world
    if (PLAYER_COUNT == 0)
    {
      this.initPlayerOne();
    }
    else
    {
      this.initPlayer();
    }
    this.velocity.set(0,0,0);
    this.accel.set(0,0,0);
    PlayerObject.PLAYER_COUNT++;
    this.playerId = PLAYER_COUNT;
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
  }

  /**
   * Gets the player's health.
   * @return short health
   */
  @Override public short getHealth()
  {
    return health;
  }

  /**
   * Sets the player's health
   * @param health the updated health score
   */
  @Override public void setHealth(short health)
  {
    this.health = health;
  }

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

  public static Boolean getDEBUG()
  {
    return DEBUG;
  }

  public static void setDEBUG(Boolean DEBUG)
  {
    PlayerObject.DEBUG = DEBUG;
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

  /**
   * Gets the 'alive' flag for this player.
   */
  @Override public boolean getAlive()
  {
    return alive;
  }

  /**
   * Sets the 'alive' flag for this player.
   */
  @Override public void setAlive(Boolean alive)
  {
    this.alive = alive;
  }

  @Override public short getPlayerId()
  {
    return playerId;
  }

  /**
   * Gets the status of the player - are they playing the game? logging off?
   * delete them!
   * @return if the player is playing or not
   */
  @Override public Boolean getPlaying()
  {
    return playing;

  }

  @Override public void setPlaying(Boolean playing)
  {
    this.playing = playing;
  }

  /**
   * Provides natural sorting for players based on id. Can easily be changed.
   * @param otherPlayerObject another player object
   * @return player with lower id
   */
  @Override
  public int compareTo(PlayerObject otherPlayerObject)
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
   * Intended only for debugging.
   *
   * <P>Here, the contents of every field are placed into the result, with
   * one field per line.
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    String delimiter = ":";

    result.append(playerId + delimiter );
    result.append(position + delimiter);
    result.append(velocity + delimiter);
    result.append(accel + delimiter);
    result.append(health + delimiter );
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
    result.append(" Health: " + health + NEW_LINE);
    result.append(" Position: " + position + NEW_LINE);
    result.append(" Velocity: " + velocity + NEW_LINE );
    result.append(" Acceleration: " + accel + NEW_LINE);
    result.append(" Alive: " + alive + NEW_LINE);
    result.append(" Playing: " + playing + NEW_LINE);
    result.append("}");

    return result.toString();
  }

}
