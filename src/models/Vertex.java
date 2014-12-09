package models;

public class Vertex implements Comparable
{
  public int vertexInd;
  public int normalInd;
  public int textureInd;

  /**
   * Represents a single vertex on a model
   * remembers texture, has a normal
   * @param vertex
   * @param texture
   * @param normal
   */
  public Vertex(int vertex, int texture, int normal)
  {
    this.vertexInd = vertex;
    this.textureInd = texture;
    this.normalInd = normal;
  }
  
  /**
   * checks if two vertices have the same index
   */
  @Override
  public boolean equals(Object o)
  {
    return this.vertexInd==((Vertex) o).vertexInd;
  }
  
  
  /**
   * Compares two vertices
   * @param vIn
   * @return
   */
  public int compareTo(Vertex vIn)
  {
    if (this.vertexInd == vIn.vertexInd)
    {
      return 0;
    }
    else if (this.vertexInd > vIn.vertexInd)
    {
      return 1;
    }
    else
    {
      return -1;
    }
  }

  /**
   * necessary compareTo override, vertex is never equal to a non-vertex
   */
  @Override
  public int compareTo(Object arg0)
  {
    return 0;
  }
}
