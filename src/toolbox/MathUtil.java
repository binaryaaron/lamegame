package toolbox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.ra4king.opengl.util.math.Matrix4;
import com.ra4king.opengl.util.math.Vector3;

import entities.Camera;
import entities.Entity;
import entities.Laser;

public class MathUtil
{

  //translation for global world objects
  public static Matrix4f createTransformationMatrix(Vector3f translation,
      float rx, float ry, float rz, float scale)
  {

    Matrix4f matrix = new Matrix4f();
    matrix.setIdentity();

    Matrix4f.translate(translation, matrix, matrix);
    Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1, 0, 0), matrix,
        matrix);
    Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0, 1, 0), matrix,
        matrix);
    Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0, 0, 1), matrix,
        matrix);
    //Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

    return createTransformationMatrix(new Entity("", null,translation,rx,ry,rz,scale));
    //return matrix;
  }

  public static Matrix4f createTransformationMatrix(Entity entity)
  {
    
    float scale = entity.getScale();
    Matrix4f matrix = new Matrix4f();
    Matrix4 viewMatrix = entity.orientation.toMatrix().translate(
        new Vector3(entity.position.x, entity.position.y, entity.position.z));

    matrix = mat4fToMat4(viewMatrix.inverse());
   
    Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

    return matrix;
  }
  
  public static Matrix4f createTransformationMatrix(Laser entity)
  {
    
    float scale = entity.getScale();
    Matrix4f matrix = new Matrix4f();
    Matrix4 viewMatrix = entity.orientation.toMatrix().translate(
        new Vector3(entity.position.x, entity.position.y, entity.position.z));

    matrix = mat4fToMat4(viewMatrix.inverse());
   
    Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);

    return matrix;
  }

  public static Matrix4f createViewMatrix(Camera camera)
  {

    Matrix4f matrix = new Matrix4f();
    Matrix4 viewMatrix = camera.orientation.toMatrix().translate(
        new Vector3(camera.position.x, camera.position.y, camera.position.z));
    matrix = mat4fToMat4(viewMatrix);

    return matrix;

  }

  public static Matrix4f mat4fToMat4(Matrix4 viewMatrix)
  {
    Matrix4f matrix = new Matrix4f();
    matrix.m00 = viewMatrix.get(0, 0);
    matrix.m01 = viewMatrix.get(0, 1);
    matrix.m02 = viewMatrix.get(0, 2);
    matrix.m03 = viewMatrix.get(0, 3);
    matrix.m10 = viewMatrix.get(1, 0);
    matrix.m11 = viewMatrix.get(1, 1);
    matrix.m12 = viewMatrix.get(1, 2);
    matrix.m13 = viewMatrix.get(1, 3);
    matrix.m20 = viewMatrix.get(2, 0);
    matrix.m21 = viewMatrix.get(2, 1);
    matrix.m22 = viewMatrix.get(2, 2);
    matrix.m23 = viewMatrix.get(2, 3);
    matrix.m30 = viewMatrix.get(3, 0);
    matrix.m31 = viewMatrix.get(3, 1);
    matrix.m32 = viewMatrix.get(3, 2);
    matrix.m33 = viewMatrix.get(3, 3);

    return matrix;
  }

  public static float vectorDist(Vector3f max, Vector3f min)
  {
    float x = max.x - min.x;
    float y = max.y - min.y;
    float z = max.z - min.z;
    return (float) Math.sqrt(x*x + y*y + z*z);
  }
}
