package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position=new Vector3f(0,0,0);
	private  float pitch;
	private float yaw;
	private float roll;
	private Vector3f xAxis;
	private Vector3f yAxis;
	private Vector3f zAxis;
	
	public Camera(){
		
		xAxis=new Vector3f(1f,0f,0f);
		yAxis=new Vector3f(0f,1f,0f);
		zAxis=new Vector3f(0f,0f,1f);
	}
	
	
	
	public void move(){

		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			pitch++;

		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			pitch--;
		}

		
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			yaw-=1;

		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			yaw+=1;

		}
		
		
		
		
		
	}
	
	
	
	
	
	
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public void setPosition(float x,float y,float z) {
    this.position.x = x;
    this.position.y = y;
    this.position.z = z;
  }
	public float getPitch() {
		return pitch;
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	public float getRoll() {
		return roll;
	}
	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	
  public Vector3f getxAxis()
  {
    return xAxis;
  }



  public Vector3f getyAxis()
  {
    return yAxis;
  }



  public Vector3f getzAxis()
  {
    return zAxis;
  }



  public void setRotation(float rotX,float rotY,float rotZ) {
    this.pitch = rotX;
    this.yaw = rotY;
    this.roll = rotZ;
    
  }
  
  public void setNewBasis(float rotX,float rotY,float rotZ){
    xAxis.x=(float)(Math.cos(rotX)*Math.cos(rotZ)-Math.sin(rotX)*Math.cos(rotY)*Math.sin(rotZ));
    xAxis.y=(float)(Math.cos(rotX)*Math.cos(rotZ)+Math.cos(rotX)*Math.cos(rotY)*Math.sin(rotZ));
    xAxis.z=(float)(Math.sin(rotY)*Math.sin(rotZ));

    yAxis.x=(float)(-Math.cos(rotX)*Math.sin(rotZ)-Math.sin(rotX)*Math.cos(rotY)*Math.cos(rotZ));
    yAxis.y=(float)(-Math.sin(rotX)*Math.sin(rotZ)+Math.cos(rotX)*Math.cos(rotY)*Math.cos(rotZ));
    yAxis.z=(float)(Math.sin(rotY)*Math.cos(rotZ));
    
    zAxis.x=(float)(Math.sin(rotY)*Math.sin(rotX));
    zAxis.y=(float)(-Math.sin(rotY)*Math.cos(rotX));
    zAxis.z=(float)(Math.cos(rotY));
    
    
    
    
  }
  
  public void rotate(float rotX,float rotY,float rotZ) {
	    this.pitch += rotX;
	    this.yaw += rotY;
	    this.roll += rotZ;
	    
	    
	    
	  }
}
