package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import com.ra4king.opengl.util.Utils;
import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;
import toolbox.MathUtil;
import world.BoundingBox;

public class Entity {
	private TexturedModel model;
	
	//rotation is the rotation relative to the parent entity group,
	//the three rotation floats are rotation relative to the world
	public Vector3f position;

    private Vector3f rotation;
	private float rotX,rotY,rotZ;;
	public Quaternion orientation;
	private Matrix4f basis=new Matrix4f();
	public Matrix4f rotationMatrix=new Matrix4f();
	public Matrix4f matrix=new Matrix4f();
	private float scale;

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
	public Vector3 vel = new Vector3(0f,0f,0f);
	public Vector3 qPos = new Vector3(0f,0f,0f);
	//public Vector3f vel = new Vector3f(0f,0f,0f);

	
  public Entity(Entity ent)
	{
		this(ent.model, new Vector3f(ent.position), ent.rotX, ent.rotY, ent.rotZ, ent.scale*0.9f);
	}

	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ,float scale) {
		super();

		this.model = model;
		this.position = position;
		this.rotation=new Vector3f(rotX,rotY,rotZ);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale=scale;
		
		orientation=new Quaternion();
		orientation.x(rotX);
		orientation.y(rotY);
		orientation.z(rotZ);
		//basis will be a matrix that holds the directional vectors
		basis.setIdentity();
		if (model!=null) {
		box = model.getRawModel().getBoundingBox().deepCopy();
		box.scale(0.9f*scale);
		size = MathUtil.vectorDist(box.getMax(), box.getMin());
		halfSize = size * 0.5f;
		box.translate(position);
		}
	}

	public void quadTranslate(Vector3 vec3){
		this.box.setPosition(vec3);
	  this.position.x=vec3.x();
	  this.position.y=vec3.y();
	  this.position.z=vec3.z();
	}
	
	public void move(Vector3 vec3)
	{
		box.translate(vec3.x(), vec3.y(), vec3.z());
		position.x += vec3.x();
		position.y += vec3.y();
		position.z += vec3.z();
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
		move(vel);
	}
	
	

	
	 public Matrix4f setNewBasis(float rotX,float rotY,float rotZ){
		 
		float cx=(float)Math.cos(rotX);
		float sx=(float)Math.sin(rotX);
		float cy=(float)Math.cos(rotY);
		float sy=(float)Math.sin(rotY);
		float cz=(float)Math.cos(rotZ);
		float sz=(float)Math.sin(rotZ);
		 
		 
		 
		 Matrix4f rotMat=new  Matrix4f();
		 rotMat.m00=(float)(Math.cos(rotX)*Math.cos(rotZ)-Math.sin(rotX)*Math.cos(rotY)*Math.sin(rotZ));
		 rotMat.m10=(float)(Math.sin(rotX)*Math.cos(rotZ)+Math.cos(rotX)*Math.cos(rotY)*Math.sin(rotZ));
		 rotMat.m20=(float)(Math.sin(rotY)*Math.sin(rotZ));

		 rotMat.m01=(float)(-Math.cos(rotX)*Math.sin(rotZ)-Math.sin(rotX)*Math.cos(rotY)*Math.cos(rotZ));
		 rotMat.m11=(float)(-Math.sin(rotX)*Math.sin(rotZ)+Math.cos(rotX)*Math.cos(rotY)*Math.cos(rotZ));
		 rotMat.m21=(float)(Math.sin(rotY)*Math.cos(rotZ));
		    
		 rotMat.m02=(float)(Math.sin(rotY)*Math.sin(rotX));
		 rotMat.m12=(float)(-Math.sin(rotY)*Math.cos(rotX));
		 rotMat.m22=(float)(Math.cos(rotY));

//		 rotMat.m00=(cx*cz)-(cy*sx*sz);
//		 rotMat.m10=(cz*sx)+(cx*cz*cy);
//		 rotMat.m20=sy*sz;
//
//		 rotMat.m01=-(cx*sz)-(cy*cz*sx);
//		 rotMat.m11=(cx*cz*cy)-(sx*sz);
//		 rotMat.m21=cz*sy;
//		    
//		 rotMat.m02=sx*sy;
//		 rotMat.m12=-cx*sy;
//		 rotMat.m22=cy;
		 
		 
		 
		 
		 return rotMat;
		    
		    
		    
		    
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
		this.position = new Vector3f(position);
		this.box.setPosition(position);
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
