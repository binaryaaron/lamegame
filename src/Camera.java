package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;

public class Camera
{
  public Vector3f position = new Vector3f(0, 0, 0);

  public Quaternion orientation;
  private float pitch;
  private float yaw;
  private float roll;
  private Vector3f xAxis;
  private Vector3f yAxis;
  private Vector3f zAxis;
  public Entity followObj;

  public Camera()
  {
    orientation = new Quaternion();
    xAxis = new Vector3f(1f, 0f, 0f);
    yAxis = new Vector3f(0f, 1f, 0f);
    zAxis = new Vector3f(0f, 0f, 1f);
  }

  public void quadTranslate(Vector3 vec3)
  {
    this.position.x = vec3.x();
    this.position.y = vec3.y();
    this.position.z = vec3.z();
  }

  public Vector3f getPosition()
  {
    return position;
  }

  public void setPosition(Vector3f position)
  {
    this.position = position;
  }

  public void setPosition(float x, float y, float z)
  {
    this.position.x = x;
    this.position.y = y;
    this.position.z = z;
  }

  public void move(float x, float y, float z)
  {
    position.x += x;
    position.y += y;
    position.z += z;
  }

  public void move(Vector3 vec3)
  {
    move(vec3.x(), vec3.y(), vec3.z());
  }

  public float getPitch()
  {
    return pitch;
  }

  public void setPitch(float pitch)
  {
    this.pitch = pitch;
  }

  public float getYaw()
  {
    return yaw;
  }

  public void setYaw(float yaw)
  {
    this.yaw = yaw;
  }

  public float getRoll()
  {
    return roll;
  }

  public void setRoll(float roll)
  {
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

  public void setRotation(float rotX, float rotY, float rotZ)
  {
    this.pitch = rotX;
    this.yaw = rotY;
    this.roll = rotZ;

  }

  public void rotate(float rotX, float rotY, float rotZ)
  {
    this.pitch += rotX;
    this.yaw += rotY;
    this.roll += rotZ;
  }

  public String toString()
  {
    String delmiter = ",";
    String output = "Cam" + delmiter;
    output += position.x + delmiter;
    output += position.y + delmiter;
    output += position.z + delmiter;
    output += orientation.x() + delmiter;
    output += orientation.y() + delmiter;
    output += orientation.z() + delmiter;
    output += orientation.w() + delmiter;
    output += 0f + ";";
    return output;
  }
}
