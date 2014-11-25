package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;
import models.Vertex;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import world.BoxUtilities;

public class OBJLoader
{

  /**
   * build a new Raw Model by parsing a .obj file.
   * The target .obj file must contain vertex coordinates, uv, normal vectors, and face indices
   * simpleShape is true for lowpoly objects. A more expensive, more accurate method is employed 
   * for simple shapes
   * 
   * @param fileName
   * @param loader
   * @param simpleShape
   * @return
   */
  public static RawModel loadObjModel(String fileName, Loader loader,
      boolean simpleShape)
  {

    //Read file all obj files must be in res folder
    FileReader fr = null;
    try
    {
      fr = new FileReader(new File("res/" + fileName + ".obj"));
    }
    catch (FileNotFoundException e)
    {
      System.out.println("res/" + fileName + ".obj" + " not found.");
      e.printStackTrace();
    }

    BufferedReader reader = new BufferedReader(fr);
    String line;
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
      while (true)
      {
        line = reader.readLine();
        String[] currentLine = line.split(" ");
        if (line.startsWith("v "))
        {
          Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
              Float.parseFloat(currentLine[2]),
              Float.parseFloat(currentLine[3]));
          vertices.add(vertex);
        }
        else if (line.startsWith("vt "))
        {
          Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
              Float.parseFloat(currentLine[2]));
          textures.add(texture);
        }
        else if (line.startsWith("vn "))
        {
          Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
              Float.parseFloat(currentLine[2]),
              Float.parseFloat(currentLine[3]));
          normals.add(normal);
        }
        else if (line.startsWith("f "))
        {

          for (int i = 0; i < vertices.size() * 3; i++)
          {
            normalsArrayList.add(null);
          }

          for (int i = 0; i < vertices.size() * 2; i++)
          {
            textureArrayList.add(null);
          }

          break;
        }
      }

      while (line != null)
      {
        if (!line.startsWith("f "))
        {
          line = reader.readLine();
          continue;
        }
        String[] currentLine = line.split(" ");
        String[] vertex1 = currentLine[1].split("/");
        String[] vertex2 = currentLine[2].split("/");
        String[] vertex3 = currentLine[3].split("/");
        processVertex(vertex1, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);
        processVertex(vertex2, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);
        processVertex(vertex3, indices, textures, normals, vertices,
            textureArrayList, normalsArrayList, proccessedVertecies,
            simpleShape);
        line = reader.readLine();
      }

      reader.close();
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
