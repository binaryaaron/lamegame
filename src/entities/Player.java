package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

/**
 * Player is the main class for a given player. It extends Entity and is build
 * take advantage of the work entities do with some added methods for handling
 * player-specific things.
 */
public class Player extends Entity
{
  public long lastFired = System.currentTimeMillis();
  public static final float playerScale = 3f;
  public int missileSound = 0;

  /**
   * Creates a new Player object
   * @param id player's id
   * @param model player's ship model
   * @param position vector3f of position
   * @param rotX rotation value x
   * @param rotY rotation value y
   * @param rotZ rotation value z
   * @param clientId the client's id (unique from id)
   */
  public Player(String id, TexturedModel model, Vector3f position, float rotX,
      float rotY, float rotZ, int clientId)
  {
    super(id, model, position, rotX, rotY, rotZ, playerScale, clientId);
    box = Globals.shipBoundingBox.deepCopy();
    box.scale(playerScale * 0.9f);
    box.translate(position);
  }

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
   * Overrides Entity's tostring method for passing info back and forth from
   * the server
   * @return
   */
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
    result.append(missileSound).append(delimiter);
    return result.toString();
  }
}
