package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import toolbox.MathUtil;

public class StaticShader extends ShaderProgram
{

  private static final String VERTEX_FILE = "res/vertexShader.txt";
  private static final String FRAGMENT_FILE = "res/fragmentShader.txt";

  private int location_TransformationMatrix;
  private int location_ProjectionMatrix;
  private int location_ViewMatrix;
  private int location_lightPosition;
  private int location_lightColor;
  private int location_shineDamper;
  private int location_reflectivity;
  private int location_drawShadow;

  /**
 * Static Shader is used on in-game Entities
 * It applies shaders that create shadows, shine, and textures
 */
public StaticShader()
  {
    super(VERTEX_FILE, FRAGMENT_FILE);

  }

  /* (non-Javadoc)
 * @see shaders.ShaderProgram#bindAttributes()
 */
@Override
  protected void bindAttributes()
  {
    super.bindAttribute(0, "position");
    super.bindAttribute(1, "textureCoords");
    super.bindAttribute(2, "normal");

  }

  /* (non-Javadoc)
 * @see shaders.ShaderProgram#getAllUniformLocations()
 */
@Override
  protected void getAllUniformLocations()
  {
    location_TransformationMatrix = super
        .getUniformLocation("transformationMatrix");
    location_ProjectionMatrix = super.getUniformLocation("projectionMatrix");
    location_ViewMatrix = super.getUniformLocation("viewMatrix");
    location_lightPosition = super.getUniformLocation("lightPosition");
    location_lightColor = super.getUniformLocation("lightColor");
    location_shineDamper = super.getUniformLocation("shineDamper");
    location_reflectivity = super.getUniformLocation("reflectivity");
    location_drawShadow = super.getUniformLocation("drawShadow");
  }

  /**
   * 
 * @param drawShadow
 */
public void loadDrawShadow(boolean drawShadow){
    super.loadBoolean(location_drawShadow, drawShadow);

  }

  /**
 * @param shineDamper
 * @param reflectivity
 */
public void loadShineVariables(float shineDamper, float reflectivity)
  {
    super.loadFloat(location_shineDamper, shineDamper);
    super.loadFloat(location_reflectivity, reflectivity);

  }

  /**
 * @param matrix
 */
public void loadTransformationMatrix(Matrix4f matrix)
  {

    super.loadMatrix(location_TransformationMatrix, matrix);

  }

  /**
 * @param light
 */
public void loadLight(Light light)
  {
    super.loadVector(location_lightPosition, light.getPosition());
    super.loadVector(location_lightColor, light.getColor());

  }

  /**
 * @param camera
 */
public void loadViewMatrix(Camera camera)
  {

    Matrix4f viewMatrix = MathUtil.createViewMatrix(camera);
    super.loadMatrix(location_ViewMatrix, viewMatrix);

  }

  /**
 * @param matrix
 */
public void loadProjectionMatrix(Matrix4f matrix)
  {

    super.loadMatrix(location_ProjectionMatrix, matrix);

  }

}
