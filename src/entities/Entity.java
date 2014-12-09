package entities;

import java.util.HashMap;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import toolbox.MathUtil;
import world.BoundingBox;

import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;

/**
 * Entity is the basic object of a game. Each object has various parameters
 * that dictate its position and movement throughout the world, as well as the
 * amount of damage it can take and so on. Parent class for Asteroid and Player.
 */
public class Entity
{
  private static final float initialMass = 100f;
  protected TexturedModel model;

  // rotation is the rotation relative to the parent entity group,
  // the three rotation floats are rotation relative to the world
  public Vector3f position;

  private Vector3f rotation;
  private float rotX, rotY, rotZ;
  public Quaternion orientation;
  private Matrix4f basis = new Matrix4f();
  public Matrix4f rotationMatrix = new Matrix4f();
  public Matrix4f matrix = new Matrix4f();
  protected float scale;
  public float mass;

  private float size;
  private float halfSize;
  protected BoundingBox box;
  public Vector3 vel = new Vector3(0f, 0f, 0f);
  public Vector3 qPos = new Vector3(0f, 0f, 0f);
  protected int hitPoints = 1000;
  protected int damage = 1;
  protected String id;

  public boolean drawShadow = true;
  protected int clientId = -1;
  public EntityType type;
  public int score = 0;
  public long entScoreStep = 0;

  /**
   * Serves as a short listing of various amounts of health, mass, and damage
   * an entity may have.
   */
  public enum EntityType {
    SHIP(100, 20, 1000),
    CRYSTAL(10000, 0, 20000),
    LASER(50, 1000, 3),
    PLANET(100000, 50, 20000000),
    ASTEROID(500, 20, 20000);

    public final float mass;
    public final int damage;
    public final int health;

    EntityType(float mass, int damage, int health)
    {
      this.mass = mass;
      this.damage = damage;
      this.health = health;
    }
  }

  /**
   * Lookup table for damage done when an entity is hit
   */
  private static Map<String, EntityType> entMap;

  static
  {
    entMap = new HashMap<>();
    entMap.put("l", EntityType.LASER);
    entMap.put("A", EntityType.ASTEROID);
    entMap.put("P", EntityType.PLANET);
    entMap.put("S", EntityType.SHIP);
    entMap.put("C", EntityType.CRYSTAL);
  }

  /**
   * default constructor
   */
  public Entity()
  {

  }

  /**
   * Returns a new entity built with the following parameters
   * @param id
   * @param model
   * @param position
   * @param rotX
   * @param rotY
   * @param rotZ
   * @param scale
   * @param clientId
   */
  public Entity(String id, TexturedModel model, Vector3f position, float rotX,
      float rotY, float rotZ, float scale, int clientId)
  {
    super();
    this.clientId = clientId;
    this.id = id;
    this.model = model;
    this.position = position;
    this.rotation = new Vector3f(rotX, rotY, rotZ);
    this.rotX = rotX;
    this.rotY = rotY;
    this.rotZ = rotZ;
    this.scale = scale;
    vel.reset();
    orientation = new Quaternion();
    orientation.x(rotX);
    orientation.y(rotY);
    orientation.z(rotZ);
    // basis will be a matrix that holds the directional vectors
    basis.setIdentity();
    String firstChar = id.substring(0,1);

    if (entMap.containsKey(firstChar))
    {
      type = entMap.get(firstChar);
      damage = type.damage;
      hitPoints = type.health;
      mass = type.mass * scale;
    }

    if (model != null)
    {
      box = model.getRawModel().getBoundingBox().deepCopy();
      box.scale(0.9f * scale);
      size = MathUtil.vectorDist(box.getMax(), box.getMin());
      halfSize = size * 0.5f;
      box.translate(position);
      mass = initialMass * scale;
    }
  }

  /**
   * Constructor that takes an additional string for the id. used to pass the id
   * from the textured model's id.
   */
  public Entity(String id, TexturedModel model, Vector3f position, float rotX,
      float rotY, float rotZ, float scale)
  {
    super();
    this.id = id;
    this.model = model;
    this.position = position;
    this.rotation = new Vector3f(rotX, rotY, rotZ);
    this.rotX = rotX;
    this.rotY = rotY;
    this.rotZ = rotZ;
    this.scale = scale;
    vel.reset();
    orientation = new Quaternion();
    orientation.x(rotX);
    orientation.y(rotY);
    orientation.z(rotZ);
    // basis will be a matrix that holds the directional vectors
    basis.setIdentity();
    if (model != null)
    {
      box = model.getRawModel().getBoundingBox().deepCopy();
      box.scale(0.9f * scale);
      size = MathUtil.vectorDist(box.getMax(), box.getMin());
      halfSize = size * 0.5f;
      box.translate(position);
      mass = initialMass * scale;
    }

    if (!id.isEmpty())
    {
      String firstChar = id.substring(0, 1);
      if (entMap.containsKey(firstChar))
      {
        type = entMap.get(firstChar);
        damage = type.damage;
        hitPoints = type.health;
        mass = type.mass * scale;
      }

      if (firstChar.equals("l"))
      {
        box = Globals.projectileBoundingBox.deepCopy();
        box.scale(scale);
        box.translate(position);
      }
    }

  }

  /**
   * recreate the object in a different location with starting health
   * @param position
   */
  public void respawn(Vector3f position)
  {
    hitPoints = type.health;
    setPosition(position);
    vel.reset();
  }

  /**
   * Move the object without changing orientation
   * @param vec3
   */
  public void quadTranslate(Vector3 vec3)
  {
    this.box.setPosition(vec3);
    this.position.x = vec3.x();
    this.position.y = vec3.y();
    this.position.z = vec3.z();
  }

  /**
   * Moves the player using a vector3, elementwise addition
   * @param vec3 the vector of velocity to increase the entity by
   */
  public void move(Vector3 vec3)
  {
    box.translate(vec3.x(), vec3.y(), vec3.z());
    position.x += vec3.x();
    position.y += vec3.y();
    position.z += vec3.z();
  }

  /**
   * Translates the position vector using three floats
   *
   * @param dx x direction
   * @param dy y direction
   * @param dz z direction
   */
  public void translate(float dx, float dy, float dz)
  {
    this.position.x += dx;
    this.position.y += dy;
    this.position.z += dz;
    box.translate(dx, dy, dz);
  }

  public void multPos(float s)
  {
    position.x *= s;
    position.y *= s;
    position.z *= s;
    box.setPosition(position);
  }

  /**
   * Getters and setters
   * @return
   */
  public float getSize()
  {
    return size;
  }

  public float getHalfSize()
  {
    return halfSize;
  }

  public void translate(Vector3f vel)
  {
    position.translate(vel.x, vel.y, vel.z);
    box.translate(vel);
  }

  public void move()
  {
    move(vel);
  }

  public Matrix4f setNewBasis(float rotX, float rotY, float rotZ)
  {
    Matrix4f rotMat = new Matrix4f();
    rotMat.m00 = (float) (Math.cos(rotX) * Math.cos(rotZ) - Math.sin(rotX)
        * Math.cos(rotY) * Math.sin(rotZ));
    rotMat.m10 = (float) (Math.sin(rotX) * Math.cos(rotZ) + Math.cos(rotX)
        * Math.cos(rotY) * Math.sin(rotZ));
    rotMat.m20 = (float) (Math.sin(rotY) * Math.sin(rotZ));

    rotMat.m01 = (float) (-Math.cos(rotX) * Math.sin(rotZ) - Math.sin(rotX)
        * Math.cos(rotY) * Math.cos(rotZ));
    rotMat.m11 = (float) (-Math.sin(rotX) * Math.sin(rotZ) + Math.cos(rotX)
        * Math.cos(rotY) * Math.cos(rotZ));
    rotMat.m21 = (float) (Math.sin(rotY) * Math.cos(rotZ));

    rotMat.m02 = (float) (Math.sin(rotY) * Math.sin(rotX));
    rotMat.m12 = (float) (-Math.sin(rotY) * Math.cos(rotX));
    rotMat.m22 = (float) (Math.cos(rotY));
    return rotMat;
  }

  public BoundingBox getBox()
  {
    return box;
  }

  public float getScale()
  {
    return scale;
  }

  public void setScale(float scale)
  {
    this.scale = scale;
  }

  public TexturedModel getModel()
  {
    return model;
  }

  public void setModel(TexturedModel model)
  {
    this.model = model;
  }

  public Vector3f getPosition()
  {
    return position;
  }

  public void setPosition(Vector3f position)
  {
    this.position = new Vector3f(position);
    this.box.setPosition(position);
  }

  public float getRotX()
  {
    return rotX;
  }

  public void setRotX(float rotX)
  {
    this.rotX = rotX;
  }

  /**
   * Increases the x vel by a constant factor.
   *
   * @param increase
   *          an integer amount for increasing
   */
  public void increaseVx(float increase)
  {
    vel.x(vel.x() + increase);
  }

  /**
   * Increases the y vel by a constant factor.
   *
   * @param increase
   *          an integer amount for increasing
   */
  public void increaseVy(float increase)
  {
    vel.y(vel.y() + increase);
  }

  /**
   * Increases the z vel by a constant factor.
   *
   * @param increase
   *          an integer amount for increasing
   */
  public void increaseVz(float increase)
  {
    vel.z(vel.z() + increase);
  }

  public Vector3 getVelocity()
  {
    return vel;
  }

  /**
   * Generic method to return the type of object.
   */
  public String getType()
  {
    return "GameObject";
  }

  /**
   * toString allows the entity to be parsed by the server's string handlers. it
   * builds the string by the id, positions, orientations, scale, and if needed,
   * the client id, hit points, and velocity length
   *
   * @return String
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
    if (id.startsWith("S"))
    {
      result.append(clientId).append(delimiter);
      result.append(hitPoints).append(delimiter);
      result.append(vel.length()).append(delimiter);
    }
    return result.toString();
  }

  /**
   * Updates the hit points of the object
   *
   * @param dmg
   */
  public void damageObject(int dmg)
  {
    if (type == EntityType.LASER)
    {
      hitPoints -= 1;
    }
    else
    {
      this.hitPoints -= dmg;
    }
    if (hitPoints <= 0)
    {
      uponDeath(this);
    }
  }

  /**
   * Set entity health to 0
   */
  public void kill()
  {
    hitPoints = 0;
  }

  /**
   * Heals the hit points of the object
   *
   * @param heal
   */
  public void healEntity(int heal)
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
   *
   * @param ent
   *          the game object to die
   */
  protected void uponDeath(Entity ent)
  {

  }

  /**
   * Updates the object with a random inital velocity - used for creation
   */
  public void randomVel()
  {
    vel.set(Globals.RAND.nextFloat()* 2 - 1,Globals.RAND.nextFloat()* 2 - 1,Globals.RAND.nextFloat()* 2 - 1);
    vel.mult(Globals.RAND.nextFloat() * 3);
  }

  /**
   * Gets the entity's id.
   *
   * @return
   */
  public String getId()
  {
    return id;
  }

  /**
   * Get hurt by something, hurt something else.
   * All damage is based on collisions
   * @param first
   * @param second
   */
  public static void inflictDamage(Entity first, Entity second)
  {
    first.damageObject(second.damage);
    second.damageObject(first.damage);
  }
}
