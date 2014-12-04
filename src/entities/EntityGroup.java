package entities;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class EntityGroup{
	private Vector3f position,rotation;
	
	public Vector3f xAxis,yAxis,zAxis;
	private float scale;

	public List<Entity> entities=new ArrayList<>();
	
	public EntityGroup( Vector3f position, Vector3f rotation, float scale) {
		 xAxis = new Vector3f(1f,0f,0f);
		 yAxis = new Vector3f(0f,1f,0f);
		 zAxis = new Vector3f(0f,0f,1f);
		
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	
	
}
