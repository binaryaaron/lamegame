package renderEngine;

import java.util.ArrayList;
import java.util.List;

import models.RawModel;
import models.Vertex;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import world.BoxUtilities;

public class FontLoader 
{

  /**
   * build a new Raw Model by parsing text given
   * size determines how large the text created should be.
   * simpleShape is true for lowpoly objects. A more expensive, more accurate method is employed 
   * for simple shapes
   * 
   * @param fileName
   * @param loader
   * @param simpleShape
   * @return
   */
  public static RawModel loadFontModel(String text, Loader loader,
      boolean simpleShape, int size)
  {
    //co-ordinates of the character on the png
    List<Vector3f> vertices = new ArrayList<>();
    List<Vector2f> textures = new ArrayList<>();
    List<Vector3f> normals = new ArrayList<>();
    List<Integer> indices = new ArrayList<>();
    List<Vertex> proccessedVertecies = new ArrayList<>();
    
    float[] verticesArray = null;
    float[] normalsArray = null;
    float[] textureArray = null;

    List<Float> normalsArrayList = new ArrayList<>();
    List<Float> textureArrayList = new ArrayList<>();

    int[] indicesArray = null;
    
    //parse 
    try
    {
      //assign vertices based on the size of the text
      for(int i = 0; i < text.length(); i++)
      {
        Vector3f up_left = new Vector3f(i*size, size,0);
        Vector3f up_right = new Vector3f(i*size+size, size,0);
        Vector3f down_right = new Vector3f(i*size+size, 0,0);
        Vector3f down_left = new Vector3f(i*size, 0,0);
        
        //add the indices to make a square
        vertices.add(down_left);
        vertices.add(down_right);
        vertices.add(up_right);
        vertices.add(up_left);

        System.out.println("v "+down_left.x+" "+down_left.y+" "+down_left.z);
        System.out.println("v "+down_right.x+" "+down_right.y+" "+down_right.z);
        System.out.println("v "+up_right.x+" "+up_right.y+" "+up_right.z);
        System.out.println("v "+up_left.x+" "+up_left.y+" "+up_left.z);
      }
      for(int i = 0; i < text.length(); i++)
      {
        //create the texture coordinates based on the input text
        char character = text.charAt(i);
        float uv_x = (character%16)/16.0f;
        float uv_y = (character/16)/16.0f;
        
        Vector2f uv_up_left = new Vector2f(uv_x, uv_y);
        Vector2f uv_up_right = new Vector2f(uv_x+1.0f/16.0f, uv_y);
        Vector2f uv_down_right = new Vector2f(uv_x+1.0f/16.0f,(uv_y+1.0f/16.0f));
        Vector2f uv_down_left = new Vector2f(uv_x, (uv_y+1.0f/16.0f));

        
        textures.add(uv_down_left);
        textures.add(uv_down_right);
        textures.add(uv_up_right);
        textures.add(uv_up_left);

        System.out.println("vt "+uv_down_left.x+" "+uv_down_left.y);
        System.out.println("vt "+uv_down_right.x+" "+uv_down_right.y);
        System.out.println("vt "+uv_up_right.x+" "+uv_up_right.y);
        System.out.println("vt "+uv_up_left.x+" "+uv_up_left.y);
      }
      for(int i = 0; i < text.length(); i++)
      {
        //normals are garbage
        normals.add(new Vector3f(0,0,1));
        normals.add(new Vector3f(0,0,1));
        normals.add(new Vector3f(0,0,1));
        normals.add(new Vector3f(0,0,1));
        System.out.println("vn 0.000000 0.000000 1.000000");
        System.out.println("vn 0.000000 0.000000 1.000000");
        System.out.println("vn 0.000000 0.000000 1.000000");
        System.out.println("vn 0.000000 0.000000 1.000000");
      }
      //create a 3d normals array list
      for (int j = 0; j < vertices.size() * 3; j++)
      {
        normalsArrayList.add(null);
      }
      //create a 2d texture array list 
      for (int j = 0; j < vertices.size() * 2; j++)
      {
        textureArrayList.add(null);
      }
      //add faces(quad)
      for(int i = 0; i < text.length(); i++)
      {
        //these are the coordinates for the first triangle of the quad
        String []vertex1 = new String[]{""+(i*4+4),""+(i*4+1),""+(i*4+1)};
        String []vertex2 = new String[]{""+(i*4+2),""+(i*4+3),""+(i*4+1)};
        String []vertex3 = new String[]{""+(i*4+1),""+(i*4+4),""+(i*4+1)};
        processVertex(vertex1, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);
        processVertex(vertex2, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);
        processVertex(vertex3, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);

        System.out.println("f "+(i*4+1)+"/"+(i*4+1)+"/"+(i*4+1)+" "+(i*4+4)+"/"+(i*4+2)+"/"+(i*4+1)+" "+(i*4+2)+"/"+(i*4+3)+"/"+(i*4+1));
        //second triangle of the quad
        String []vertex4 = new String[]{""+(i*4+4),""+(i*4+1),""+(i*4+1)};
        String []vertex5 = new String[]{""+(i*4+3),""+(i*4+2),""+(i*4+1)};
        String []vertex6 = new String[]{""+(i*4+2),""+(i*4+3),""+(i*4+1)};
        processVertex(vertex4, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);
        processVertex(vertex5, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);
        processVertex(vertex6, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);      
        System.out.println("f "+(i*4+4)+"/"+(i*4+2)+"/"+(i*4+1)+" "+(i*4+3)+"/"+(i*4+3)+"/"+(i*4+1)+" "+(i*4+2)+"/"+(i*4+4)+"/"+(i*4+1));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    verticesArray = new float[vertices.size() * 3];
    indicesArray = new int[indices.size()];
    textureArray = new float[textureArrayList.size()];
    normalsArray = new float[normalsArrayList.size()];
    
    for (int i = 0; i < textureArrayList.size(); i++)
    {
      textureArray[i] = textureArrayList.get(i);  
    }
    
    for (int i = 0; i < normalsArrayList.size(); i++)
    {
      normalsArray[i] = normalsArrayList.get(i);

    }

    int vertexPointer = 0;
    for (Vector3f vertex : vertices)
    {
      verticesArray[vertexPointer++] = vertex.x;
      verticesArray[vertexPointer++] = vertex.y;
      verticesArray[vertexPointer++] = vertex.z;

    }

    for (int i = 0; i < indices.size(); i++)
    {
      indicesArray[i] = indices.get(i);
    }

    Vector3f[] vertarray = new Vector3f[vertices.size()];
    for (int i = 0; i < vertices.size(); i++)
    {
      vertarray[i] = vertices.get(i);
    }

    return loader.loadToVAO(verticesArray, textureArray, normalsArray,
        indicesArray, BoxUtilities.createBoundingBoxFromVertices(vertarray));
  }

  /**
   * @param vertexData
   * @param indices
   * @param textures
   * @param normals
   * @param vertecies
   * @param textureArrayList
   * @param normalsArrayList
   * @param proccessedVertecies
   * @param simple
   */
  private static void processVertex(String[] vertexData, List<Integer> indices,
      List<Vector2f> textures, List<Vector3f> normals,
      List<Vector3f> vertecies, List<Float> textureArrayList,
      List<Float> normalsArrayList, List<Vertex> proccessedVertecies,
      boolean simple)
  {
    Vertex trackV = new Vertex(Integer.parseInt(vertexData[0]),
        Integer.parseInt(vertexData[1]), Integer.parseInt(vertexData[2]));

    int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
    if (simple && proccessedVertecies.contains(trackV))
    {
      Vector3f refVector = vertecies.get(trackV.vertexInd - 1);
      vertecies.add(new Vector3f(refVector.x, refVector.y, refVector.z));
      currentVertexPointer = vertecies.size() - 1;

      Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
      textureArrayList.add(currentTex.x);
      textureArrayList.add(1 - currentTex.y);
      Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
      normalsArrayList.add(currentNorm.x);
      normalsArrayList.add(currentNorm.y);
      normalsArrayList.add(currentNorm.z);

    }
    else
    {

      Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
      textureArrayList.set(currentVertexPointer * 2, currentTex.x);
      textureArrayList.set(currentVertexPointer * 2 + 1, 1 - currentTex.y);
      Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
      normalsArrayList.set(currentVertexPointer * 3, currentNorm.x);
      normalsArrayList.set(currentVertexPointer * 3 + 1, currentNorm.y);
      normalsArrayList.set(currentVertexPointer * 3 + 2, currentNorm.z);
    }
    indices.add(currentVertexPointer);
    proccessedVertecies.add(trackV);
  }

}
