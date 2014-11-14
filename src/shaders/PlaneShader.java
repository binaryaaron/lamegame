package shaders;

import org.lwjgl.util.vector.Matrix4f;

import toolbox.MathUtil;
import entities.Camera;
import entities.Light;

public class PlaneShader extends ShaderProgram
{

  private static final String VERTEX_FILE = "src/shaders/planeVertexShader.txt";
  private static final String FRAGMENT_FILE = "src/shaders/planeFragmentShader.txt";

  private int location_TransformationMatrix;
  private int location_ProjectionMatrix;
  private int location_ViewMatrix;
  private int location_lightPosition;
  private int location_lightColor;
  private int location_shineDamper;
  private int location_reflectivity;

  public PlaneShader()
  {
    super(VERTEX_FILE, FRAGMENT_FILE);

  }

  @Override
  protected void bindAttributes()
  {
    super.bindAttribute(0, "position");
    super.bindAttribute(1, "textureCoords");
    super.bindAttribute(2, "normal");

  }

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

  public void loadShineVariables(float shineDamper, float reflectivity)
  {
    super.loadFloat(location_shineDamper, shineDamper);
    super.loadFloat(location_reflectivity, reflectivity);

  }

  public void loadTransformationMatrix(Matrix4f matrix)
  {

    super.loadMatrix(location_TransformationMatrix, matrix);

  }

  public void loadLight(Light light)
  {
    super.loadVector(location_lightPosition, light.getPosition());
    super.loadVector(location_lightColor, light.getColor());

  }

  public void loadViewMatrix(Camera camera)
  {

    Matrix4f viewMatrix = MathUtil.createViewMatrix(camera);
    super.loadMatrix(location_ViewMatrix, viewMatrix);

  }

  public void loadProjectionMatrix(Matrix4f matrix)
  {

    super.loadMatrix(location_ProjectionMatrix, matrix);

  }

}
