package textures;

public class ModelTexture
{

  private int textureID;

  private float shadeDamper = 1;
  private float reflectivity = 0;

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