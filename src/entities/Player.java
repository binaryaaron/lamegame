package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import world.BoundingBox;

/**
 *
 */
public class Player extends Entity
{
  public long lastFired = System.currentTimeMillis();
  public static final float playerScale = 3f;
  public int missileSound = 0;

  /**
   * Default constructor for a Player. Initializes position to
   * 0,0,0
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
