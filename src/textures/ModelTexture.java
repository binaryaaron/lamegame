/**
 * Class that holds data about a texture
 * Thanks to youtube user ThinMatrix
 */
package textures;

public class ModelTexture
{

  private int textureID;

  //subdued effect
  private float shadeDamper = 1;

  private float reflectivity = 0;

  /**
   * Getters and setters
   *
   * @return / @return
   */
  public float getShadeDamper()
  {
    return shadeDamper;
  }

  public void setShadeDamper(float shadeDamper)
  {
    this.shadeDamper = shadeDamper;
  }

  public float getReflectivity()
  {
    return reflectivity;
  }

  public void setReflectivity(float reflectivity)
  {
    this.reflectivity = reflectivity;
  }

  public ModelTexture(int id)
  {
    this.textureID = id;

  }

  public int getTextureID()
  {
    return textureID;
  }

}