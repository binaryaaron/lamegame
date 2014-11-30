package entities;

import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import world.BoundingBox;

/**
 * Asteroid is the main asteroid class, obviously.
 * It is a concrete implementation of GameObject and can be extended.
 * Created by aarongonzales on 11/18/14.
 */
public class Asteroid extends Entity
{
  private static short ASTEROID_COUNT = 0;
  private static Boolean DEBUG = true;
  private String id;
  private final int intId;



  public Asteroid()
  {
    // intializes asteroid with between 100 and 1000 HP
    this.hitPoints = Globals.randInt(1000,100);
    ASTEROID_COUNT++;
    this.vel= new Vector3f(0,0,0);
    // position should be random within the game board
    this.position = new Vector3f(Globals.randInt(),Globals.randInt(),Globals.randInt());
    int tmpSize = Globals.randInt(15, 4);
    this.box = new BoundingBox(position, tmpSize, tmpSize, tmpSize);
    this.hitPoints = 500;
    // check this
    this.id = String.format("%03d", ASTEROID_COUNT);
    this.intId = ASTEROID_COUNT;
    this.mass = Globals.randInt(5000, 100);
  }
  public Asteroid(TexturedModel model)
  {
    // intializes asteroid with between 100 and 1000 HP
    this.hitPoints = Globals.randInt(1000,100);
    ASTEROID_COUNT++;
    this.vel= new Vector3f(0,0,0);
    // position should be random within the game board
    this.position = new Vector3f(Globals.randInt(),Globals.randInt(),Globals.randInt());
    int tmpSize = Globals.randInt(15, 4);
    this.box = new BoundingBox(position, tmpSize, tmpSize, tmpSize);
    this.model = model;
    this.hitPoints = 500;
    // check this
    this.id = String.format("%04d", ASTEROID_COUNT);
    this.intId = ASTEROID_COUNT;
    this.mass = Globals.randInt(5000, 100);
  }

  /**
   * Kills this object. Should add functionality to break the asteroids in
   * small chunks - currently unimplemented
   * @param ent the game object to die
   */
//  @Override
  protected void uponDeath(Entity ent)
  {
    System.out.println("asteroid down! make a new one?");
  }

  /**
   * Converts the asteroid's current stats to a string for sending back and forth
   * across the network
   * @return String with the id, position, velocity, hp, and other fields
   */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    String delimiter = ":";
    result.append("A" + id + delimiter );
    result.append(position + delimiter);
    result.append(hitPoints + delimiter );
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
    result.append(" ID: " + id + NEW_LINE);
    result.append(" Health: " + hitPoints + NEW_LINE);
    result.append(" Position: " + position + NEW_LINE);
    result.append(" Velocity: " + vel + NEW_LINE );
    result.append("}");

    return result.toString();
  }
}

