/**
 * Thanks to youtube user ThinMatrix
 * Defines the camera and methods for the camera.
 * The camera is where the user sees the board.
 * Includes a Keyboard listener.
 */
package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera
{
  private Vector3f position = new Vector3f(0, 0, 0);
  private float pitch;
  private float yaw;
  private float roll;

  public Camera()
  {
  }

  /**
   * Move the camera based on user input
   */
  public void move()
  {

    if (Keyboard.isKeyDown(Keyboard.KEY_W))
    {
      pitch++;

    }
    if (Keyboard.isKeyDown(Keyboard.KEY_S))
    {
      pitch--;
    }

    if (Keyboard.isKeyDown(Keyboard.KEY_A))
    {
      yaw -= 1;

    }
    if (Keyboard.isKeyDown(Keyboard.KEY_D))
    {
      yaw += 1;

    }

  }

  /**
   * Getters and setters
   */
  public Vector3f getPosition()
  {
    return position;
  }

  public void setPosition(Vector3f position)
  {
    this.position = position;
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

}
