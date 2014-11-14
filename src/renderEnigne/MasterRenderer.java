package renderEnigne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import plane.Plane;
import shaders.StaticShader;
import shaders.PlaneShader;
import models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer
{
  private static final float FOV = 70;
  private static final float NEAR_PLANE = 0.1f;
  private static final float FAR_PLANE = 1000;
  private Matrix4f projectionMatrix;

  private StaticShader shader = new StaticShader();
  private EntityRenderer renderer;

  private PlaneRenderer terrainRenderer;
  private PlaneShader terrainShader = new PlaneShader();

  private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
  private List<Plane> terrains = new ArrayList<>();

  public MasterRenderer()
  {
    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glCullFace(GL11.GL_BACK);

    createProjectionMatrix();
    renderer = new EntityRenderer(shader, projectionMatrix);
    terrainRenderer = new PlaneRenderer(terrainShader, projectionMatrix);
  }

  public void render(Light sun, Camera camera)
  {

    prepare();

    shader.start();

    shader.loadLight(sun);
    shader.loadViewMatrix(camera);
    renderer.render(entities);
    shader.stop();

    terrainShader.start();
    terrainShader.loadLight(sun);
    terrainShader.loadViewMatrix(camera);
    terrainRenderer.render(terrains);
    terrainShader.stop();

    entities.clear();
    terrains.clear();
  }

  public void processTerrain(Plane terrain)
  {
    terrains.add(terrain);

  }

  public void processEntity(Entity entity)
  {
    TexturedModel entityModel = entity.getModel();
    List<Entity> batch = entities.get(entityModel);
    if (batch != null)
    {
      batch.add(entity);
    }
    else
    {
      List<Entity> newBatch = new ArrayList<Entity>();
      newBatch.add(entity);
      entities.put(entityModel, newBatch);
    }

  }

  public void prepare()
  {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glClearColor(1, 1, 1, 1);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

  }

  private void createProjectionMatrix()
  {
    float aspectRatio = (float) Display.getWidth()
        / (float) Display.getHeight();
    float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
    float x_scale = y_scale / aspectRatio;
    float frustum_length = FAR_PLANE - NEAR_PLANE;

    projectionMatrix = new Matrix4f();
    projectionMatrix.m00 = x_scale;
    projectionMatrix.m11 = y_scale;
    projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
    projectionMatrix.m23 = -1;
    projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
    projectionMatrix.m33 = 0;

  }

  public void cleanUp()
  {
    shader.cleanUp();
    terrainShader.cleanUp();
  }

}
