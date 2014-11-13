package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import toolbox.MathUtil;

public class StaticShader extends ShaderProgram
{

  private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
  private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

  private int location_TransformationMatrix;
  private int location_ProjectionMatrix;
  private int location_ViewMatrix;
  private int location_lightPosition;
  private int location_lightColor;

  public StaticShader()
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
