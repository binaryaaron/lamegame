/**
 * Thanks to youtube user ThinMatrix
 * Create a raw model with certices and an ID (position in the VAO)
 */
package models;

public class RawModel
{
  private int vaoID;
  private int vertexCount;

  /**
   * RawModel creation
   *
   * @param vaoIDin
   * @param vertexCountIn
   */
  public RawModel(int vaoIDin, int vertexCountIn)
  {
    vaoID = vaoIDin;
    vertexCount = vertexCountIn;

  }

  /**
   * Getters and setters
   *
   * @return / @return
   */
  public int getVaoID()
  {
    return vaoID;
  }

  public void setVaoID(int vaoID)
  {
    this.vaoID = vaoID;
  }

  public int getVertexCount()
  {
    return vertexCount;
  }

  public void setVertexCount(int vertexCount)
  {
    this.vertexCount = vertexCount;
  }

}
