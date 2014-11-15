/**
 * A class that holds a raw model and that model's intended texture
 */
package models;

import textures.ModelTexture;

public class TexturedModel
{

  private RawModel rawModel;
  private ModelTexture texture;

  /**
   * Create a texturedmodel
   *
   * @param model
   * @param texture
   */
  public TexturedModel(RawModel model, ModelTexture texture)
  {
    this.rawModel = model;
    this.texture = texture;
  }

  /**
   * getters and setters
   *
   * @return / @param
   */
  public RawModel getRawModel()
  {
    return rawModel;
  }

  public ModelTexture getTexture()
  {
    return texture;
  }

}
