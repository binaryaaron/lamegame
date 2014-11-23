package models;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vertex implements Comparable
{

  public int vertexInd;
  public int normalInd;
  public int textureInd;

  public Vertex(int vertex, int texture, int normal)
  {
    this.vertexInd = vertex;
    this.textureInd = texture;
    this.normalInd = normal;

  }
@Override
  public boolean equals(Object o){
    
  
  
  return this.vertexInd==((Vertex) o).vertexInd;
    
    
    
  }
  
  
  // This calls two verticies identical if they
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

  @Override
  public int compareTo(Object arg0)
  {
    // TODO Auto-generated method stub
    return 0;
  }

}
