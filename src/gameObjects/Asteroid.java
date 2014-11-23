package gameObjects;

import org.lwjgl.util.vector.Vector3f;
import world.BoundingBox;

import java.util.Random;

// bounding box sizes
// create from bigger asteroid
// hit points


/**
 * Asteroid is the main asteroid class, obviously.
 * It is a concrete implementation of GameObject and can be extended.
 * Created by aarongonzales on 11/18/14.
 */
public class Asteroid extends GameObject
{
  private static short ASTEROID_COUNT = 0;
  private static Boolean DEBUG = true;
  private int id;


  public Asteroid()
  {
    // intializes asteroid with between 100 and 1000 HP
    this.hitPoints = Globals.randInt(1000,100);
    ASTEROID_COUNT++;
    this.velocity = new Vector3f(0,0,0);
    // position should be random within the game board
    this.position = new Vector3f(Globals.randInt(),Globals.randInt(),Globals.randInt());
    int tmpSize = Globals.randInt(15, 4);
    this.bbox = new BoundingBox(position, tmpSize, tmpSize, tmpSize);
    this.model = true;
    this.hitPoints = 500;
    this.id = ASTEROID_COUNT;

  }

  /**
   * Kills this object. Should add functionality to break the asteroids in
   * small chunks - currently unimplemented
   * @param go the game object to die
   */
  @Override
  protected void uponDeath(GameObject go)
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
    result.append(velocity + delimiter);
    result.append(hitPoints + delimiter );
    return result.toString();
  }
}

