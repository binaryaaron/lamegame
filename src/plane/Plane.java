/**
 * Thanks to youtube user ThinMatrix
 * Plane is a thin, flat entity
 */
package plane;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;

public class Plane
{
  private static final float SIZE = 80;
  private static final int VERTEX_COUNT = 16;

  private float x;
  private float z;
  private RawModel model;
  private ModelTexture texture;

  /**
   * Create a new plane with a texture, position, and loader
   * @param gridX
   * @param gridZ
   * @param loader
   * @param texture
   */
  public Plane(int gridX, int gridZ, Loader loader, ModelTexture texture)
  {
    this.texture = texture;
    this.x = gridX * SIZE;
    this.z = gridZ * SIZE;

    this.model = generateTerrain(loader);
  }

  /**
   * Creates the terrain in the plane
   * @param loader
   * @return
   */
  private RawModel generateTerrain(Loader loader)
  {
    int count = VERTEX_COUNT * VERTEX_COUNT;
    float[] vertices = new float[count * 3];
    float[] normals = new float[count * 3];
    float[] textureCoords = new float[count * 3];
    int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
    int vertexPointer = 0;
    //set the normals(for lighting) and texture positions
    for (int i = 0; i < VERTEX_COUNT; i++)
    {
      for (int j = 0; j < VERTEX_COUNT; j++)
      {
        vertices[vertexPointer * 3] = -(float) j / ((float) VERTEX_COUNT - 1)
            * SIZE;
        vertices[vertexPointer * 3 + 1] = 0;
        vertices[vertexPointer * 3 + 1] = -(float) i
            / ((float) VERTEX_COUNT - 1) * SIZE;
        normals[vertexPointer * 3] = 0;
        normals[vertexPointer * 3 + 1] = 1;
        normals[vertexPointer * 3 + 2] = 0;
        textureCoords[vertexPointer * 2] = (float) j
            / ((float) VERTEX_COUNT - 1);
        textureCoords[vertexPointer * 2 + 1] = (float) i
            / ((float) VERTEX_COUNT - 1);
        vertexPointer++;
      }
    }
    int pointer = 0;
    //create a shape out of squares
    for (int gz = 0; gz < VERTEX_COUNT - 1; gz++)
    {
      //create one side of a square out of two triangles
      for (int gx = 0; gx < VERTEX_COUNT - 1; gx++)
      {
        int topLeft = (gz * VERTEX_COUNT) + gx;
        int topRight = topLeft + 1;
        int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
        int bottomRight = bottomLeft + 1;
        //first triangle
        indices[pointer++] = topLeft;
        indices[pointer++] = bottomLeft;
        indices[pointer++] = topRight;
        //second triangle
        indices[pointer++] = topRight;
        indices[pointer++] = bottomLeft;
        indices[pointer++] = bottomRight;
      }
    }
    return loader.loadToVAO(vertices, textureCoords, normals, indices);
  }
  
  /**
   * getters and setters
   * @return / @param
   */
  public RawModel getModel()
  {
    return model;
  }

  public void setModel(RawModel model)
  {
    this.model = model;
  }

  public float getX()
  {
    return x;
  }

  public float getZ()
  {
    return z;
  }

  public ModelTexture getTexture()
  {
    return texture;
  }
}
