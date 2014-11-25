package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;
import toolbox.MathUtil;
import world.BoundingBox;

public class Entity {
	private static final float initialMass = 100f;
	private TexturedModel model;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	public float mass;

	public float getSize()
	{
		return size;
	}

	public float getHalfSize()
	{
		return halfSize;
	}

	private float size;
	private float halfSize;
	private BoundingBox box;
	public Vector3f vel = new Vector3f(0f,0f,0f);
	
  public Entity(Entity ent)
	{
		this(ent.model, new Vector3f(ent.position), ent.rotX, ent.rotY, ent.rotZ, ent.scale*0.9f);
	}

	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ,float scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale=scale;
		box = model.getRawModel().getBoundingBox().deepCopy();
		box.scale(0.9f*scale);
		size = MathUtil.vectorDist(box.getMax(), box.getMin());
		halfSize = size * 0.5f;
		box.translate(position);
		mass = initialMass * scale;
	}

	
	public void translate(float dx,float dy,float dz){
		this.position.x+=dx;
		this.position.y+=dy;
		this.position.z+=dz;
		box.translate(dx, dy, dz);
	}

	public void translate(Vector3f vel)
	{
		position.translate(vel.x, vel.y, vel.z);
		box.translate(vel);
	}

	public void move()
	{
		translate(vel);
	}
	
	public void rotatate(float dx,float dy,float dz){
		this.rotX+=dx;
		this.rotY+=dy;
		this.rotZ+=dz;
		
	}

	public BoundingBox getBox()
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


}
