/**
 * In charge of reading in data to the shader
 * Thanks to youtube user ThinMatrix
 */
package shaders;

import org.lwjgl.util.vector.Matrix4f;

import toolbox.MathUtil;
import entities.Camera;
import entities.Light;

public class StaticShader extends ShaderProgram
{

  private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
  private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

  private int location_TransformationMatrix;
  private int location_ProjectionMatrix;
  private int location_ViewMatrix;
  private int location_lightPosition;
  private int location_lightColor;
  private int location_shineDamper;
  private int location_reflectivity;

  public StaticShader()
  {
    super(VERTEX_FILE, FRAGMENT_FILE);
  }

  /**
   * sets the attributes of data in the VBO
   */
  @Override
  protected void bindAttributes()
  {
    super.bindAttribute(0, "position");
    super.bindAttribute(1, "textureCoords");
    super.bindAttribute(2, "normal");

  }

  /**
   * Gets the location of matrices in the VBO
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
  }

  /**
   * Pass shine damper and reflectivity into the shader
   *
   * @param shineDamper
   * @param reflectivity
   */
  public void loadShineVariables(float shineDamper, float reflectivity)
  {
    super.loadFloat(location_shineDamper, shineDamper);
    super.loadFloat(location_reflectivity, reflectivity);
  }

  /**
   * Pass our transformation matrix into the shader
   *
   * @param matrix
   */
  public void loadTransformationMatrix(Matrix4f matrix)
  {
    super.loadMatrix(location_TransformationMatrix, matrix);
  }

  /**
   * Pass our light color and light position into the shader
   *
   * @param light
   */
  public void loadLight(Light light)
  {
    super.loadVector(location_lightPosition, light.getPosition());
    super.loadVector(location_lightColor, light.getColor());

  }

  /**
   * pass the shader a matrix based on our camera's position and rotation
   *
   * @param camera
   */
  public void loadViewMatrix(Camera camera)
  {
    Matrix4f viewMatrix = MathUtil.createViewMatrix(camera);
    super.loadMatrix(location_ViewMatrix, viewMatrix);
  }

  /**
   * Pass the shader a projection matrix, a matrix based on our perspection
   *
   * @param matrix
   */
  public void loadProjectionMatrix(Matrix4f matrix)
  {
    super.loadMatrix(location_ProjectionMatrix, matrix);
  }

}
