package gameObjects;

import org.lwjgl.util.vector.Vector3f;
import world.BoundingBox;
import world.Box;

/**
 * Created by aarongonzales on 11/21/14.
 */
public abstract class GameObject implements GObject
{
  protected Vector3f position = new Vector3f();
  protected Vector3f velocity = new Vector3f();
  protected Vector3f rotation = new Vector3f();
  protected Box bbox;
  protected Boolean model = false;
  private static Boolean DEBUG = true;
  protected int hitPoints;
  protected float mass;


  public void setVelocity(Vector3f velocity)
  {
    this.velocity = velocity;
  }

  /**
   * Updates the velocity by a constant in all directions
   *
   * @param increase the amount to increase the velocity by. may be negative.
   */
  @Override public void increaseVelocity(float increase)
  {
    velocity.set(velocity.getX()+increase, velocity.getY() + increase, velocity.getZ() + increase);
  }

  /**
   * Increases the x velocity by a constant factor.
   *
   * @param increase an integer amount for increasing
   */
  @Override public void increaseVx(float increase)
  {
    velocity.setX(velocity.getX() + increase);
  }

  /**
   * Increases the y velocity by a constant factor.
   *
   * @param increase an integer amount for increasing
   */
  public void increaseVy(float increase)
  {
    velocity.setY(velocity.getY() + increase);
  }

  /**
   * Increases the z velocity by a constant factor.
   *
   * @param increase an integer amount for increasing
   */
  @Override public void increaseVz(float increase)
  {
    velocity.setZ(velocity.getZ() + increase);
  }


  @Override public Vector3f getVelocity()
  {
    return velocity;
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
   * Generic method to return the type of object.
   */
  @Override public String getType()
  {
    return "GameObject";
  }

  /**
   * Gets this object's bounding box.
   */
  @Override public Box getBBox()
  {
    return this.bbox;
  }

  /**
   * Unimplemented in this class
   * @return
   */
  @Override public String toString()
  {
    return null;
  }

  /**
   * Updates the hit points of the object
   * @param dmg
   */
  public void damageObject(int dmg)
  {
    this.hitPoints -= dmg;
    if (hitPoints <= 0)
    {
      uponDeath(this);
    }
  }

  /**
   * Heals the hit points of the object
   * @param heal
   */
  public void healObject(int heal)
  {
    this.hitPoints += heal;
  }

  /**
   * Gets the objects hit point value
   */
  public int getHitPoints()
  {
    return this.hitPoints;
  }

  /**
   * What happens when you die?
   * @param go the game object to die
   */
  protected void uponDeath(GameObject go)
  {
   //unimplemented in GameObject()
  }

  /**
   * Gets the game object's mass
   * @return - float mass
   */
  public float getMass()
  {
    return mass;
  }

  /**
   * Sets this object's mass -
   * could be used by powerups
   * @param m - float for mass
   */
  public void setMass(float m)
  {
    mass = m;
  }


}
