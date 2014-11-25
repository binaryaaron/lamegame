package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private Vector3f position=new Vector3f(0,0,0);
	private float x = 0;
	private float y = 0;
	private float z = 0;
	
	private float pitch;
	private float yaw;
	private float roll;

	
	public Camera(){}
	
	
	
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
		
		
		
    if(Keyboard.isKeyDown(Keyboard.KEY_UP))
    {
     z++;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
    {
      z--;
    }      
    if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
    {
      x++;
    }
    if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
    {
      x--;
    }	

    position=new Vector3f(x,y,z);
	}
	
	
	
	
	
	
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
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

}
