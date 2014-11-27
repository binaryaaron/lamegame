package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;

import world.BoundingBox;

public class Entity {
	private TexturedModel model;
	
	//rotation is the rotation relative to the parent entity group,
	//the three rotation floats are rotation relative to the world
	public Vector3f position;

  private Vector3f rotation;
	private float rotX,rotY,rotZ;
	public Vector3f xAxis,yAxis,zAxis,worldxAxis,worldyAxis,worldzAxis,center,front,right;
	public Quaternion orientation;
	private Matrix4f basis=new Matrix4f();
	public Matrix4f rotationMatrix=new Matrix4f();
	public Matrix4f matrix=new Matrix4f();
	private float scale;
	private BoundingBox box;
	public Vector3f vel = new Vector3f(0f,0f,0f);
	float alpha,beta,gamma;;


	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ,float scale) {
		super();
		center=new Vector3f(0,0,0);
		front=new Vector3f(0,0,1);
		right=new Vector3f(1,0,0);
		this.model = model;
		this.position = position;
		this.rotation=new Vector3f(rotX,rotY,rotZ);
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale=scale;
		
		alpha=0;
		beta=0;
		gamma=0;
		orientation=new Quaternion();
		worldxAxis = new Vector3f(1f,0f,0f);
		worldyAxis = new Vector3f(0f,1f,0f);
		worldzAxis = new Vector3f(0f,0f,1f);
		
		
		xAxis = new Vector3f(scale,0f,0f);
		yAxis = new Vector3f(0f,scale,0f);
		zAxis = new Vector3f(0f,0f,scale);
		
		//basis will be a matrix that holds the directional vectors
		basis.setIdentity();
		if (model!=null){
		box = model.getRawModel().getBoundingBox().deepCopy();
		box.scale(0.9f*scale);
		box.translate(position);}
	}

	public void quadTranslate(Vector3 vec3){
	  this.position.x=vec3.x();
	  this.position.y=vec3.y();
	  this.position.z=vec3.z();
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
	
	public void rotateX(double dx){
		dx=(dx*(3.141592/180));
		double sign=Math.signum(dx);
		Matrix4f.rotate((float) dx, xAxis, basis, basis);
    updateDirections();
		//rotX+=dx;
		front.x+=yAxis.x*dx;
		front.y+=yAxis.y*dx;
		front.z+=yAxis.z*dx;
		
//		front.y =(float)(front.y*Math.cos(dx)+front.z*Math.sin(dx)*sign);
//		front.z =(float)(-front.y*Math.sin(dx)*sign+front.z*Math.cos(dx));
		
		

		
		
	}

	public void rotateY(double dy){
		dy=Math.toRadians(dy);
		double sign=Math.signum(dy);
		front.x+=xAxis.x*dy;
    front.y+=xAxis.y*dy;
    front.z+=xAxis.z*dy;
		
    right.x+=zAxis.x*dy;
    right.y+=zAxis.y*dy;
    right.z+=zAxis.z*dy;

		Matrix4f.rotate((float) dy, yAxis, basis, basis);
		updateDirections();
		System.out.println("rotY:"+zAxis+";"+ xAxis+";"+ position);

		
	}
	public void rotateZ(double dz){
		dz=Math.toRadians(dz);
		double sign=Math.signum(dz);
//		right.y+=(float) Math.sin(dz);
//		right.x+=(float) Math.cos(dz);
//		
		Matrix4f.rotate((float) dz, zAxis, basis, basis);
		updateDirections();
		System.out.println("rotZ");
	}
	
	
	private void updateDirections(){
		xAxis.x=basis.m00;
		xAxis.y=basis.m01;
		xAxis.z=basis.m02;
		
		yAxis.x=basis.m10;
		yAxis.y=basis.m11;
		yAxis.z=basis.m12;
		
		zAxis.x=basis.m20;
		zAxis.y=basis.m21;
		zAxis.z=basis.m22;
		
		//euler Angles
//		float alphaE=Vector3f.angle(xAxis, worldxAxis);
//		float betaE=Vector3f.angle(yAxis, worldyAxis);
//		float gammaE=Vector3f.angle(zAxis, worldzAxis);
//		rotationMatrix=setNewBasis(alphaE,betaE,gammaE);
		
		//System.out.println("basis:");
		//System.out.println(basis);
		//System.out.println(alpha);
		//System.out.println(beta);
		//System.out.println("rotMat:");
		//System.out.println(rotationMatrix);
		
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
