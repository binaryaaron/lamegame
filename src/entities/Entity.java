package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;
import world.BoundingBox;
import world.Box;

public class Entity {
	protected TexturedModel model;
	protected float rotX,rotY,rotZ;
	protected float scale;
	protected BoundingBox box;
	public Vector3f vel = new Vector3f(0f,0f,0f);
	protected Vector3f position = new Vector3f();
	protected Vector3f rotation = new Vector3f();
	private static Boolean DEBUG = true;
	protected int hitPoints;


	public Entity(){}

	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ,float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale=scale;
		box = model.getRawModel().getBoundingBox().deepCopy();
		box.scale(0.9f*scale);
		box.translate(position);
	}

	/**
	 * Translates the position vector using three floats
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void translate(float dx,float dy,float dz){
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
		box.translate(dx, dy, dz);
	}

	/**
	 * Translates the position vector using one vector
	 * @param vel - vector
	 */
	public void translate(Vector3f vel)
	{
		position.translate(vel.x, vel.y, vel.z);
		box.translate(vel);
	}

	/**
	 * Moves the entity using the translate method. moves it via the current vel
	 *
	 */
	public void move()
	{
		translate(vel);
	}

	/**
	 * Rotates the entity by three floats
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void rotatate(float dx,float dy,float dz){
		this.rotX+=dx;
		this.rotY+=dy;
		this.rotZ+=dz;
		
	}

	public Box getBox()
	{
		return box;
	}
	
	public float getScale() {
		return scale;
	}


	public void setScale(float scale) {
		this.scale = scale;
	}


	public TexturedModel getModel() {
		return model;
	}


	public void setModel(TexturedModel model) {
		this.model = model;
	}


	public Vector3f getPosition() {
		return position;
	}


	public void setPosition(Vector3f position) {
		this.position = position;
	}


	public float getRotX() {
		return rotX;
	}


	public void setRotX(float rotX) {
		this.rotX = rotX;
	}


	public float getRotY() {
		return rotY;
	}


	public void setRotY(float rotY) {
		this.rotY = rotY;
	}


	public float getRotZ() {
		return rotZ;
	}


	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}



	public void setVelocity(Vector3f vel)
	{
		this.vel= vel;
	}

	/**
	 * Updates the vel by a constant in all directions
	 *
	 * @param increase the amount to increase the vel by. may be negative.
	 */
	public void increaseVelocity(float increase)
	{
		vel.set(vel.getX()+increase, vel.getY() + increase, vel.getZ() + increase);
	}

	/**
	 * Increases the x vel by a constant factor.
	 *
	 * @param increase an integer amount for increasing
	 */
	public void increaseVx(float increase)
	{
		vel.setX(vel.getX() + increase);
	}

	/**
	 * Increases the y vel by a constant factor.
	 *
	 * @param increase an integer amount for increasing
	 */
	public void increaseVy(float increase)
	{
		vel.setY(vel.getY() + increase);
	}

	/**
	 * Increases the z vel by a constant factor.
	 *
	 * @param increase an integer amount for increasing
	 */
	public void increaseVz(float increase)
	{
		vel.setZ(vel.getZ() + increase);
	}


	public Vector3f getVelocity()
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
	 * @param ent the game object to die
	 */
	protected void uponDeath(Entity ent)
	{
		//unimplemented in GameObject()
	}


}
