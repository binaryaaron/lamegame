/**
 * Thanks to youtube user ThinMatrix
 * Master renderer controls the entity and plane renderer,
 * and is responsible for rendering the final scene to be displayed
 */
package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import shaders.SkyBoxShader;
import shaders.StaticShader;
import skyBox.SkyBox;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MasterRenderer
{
  private static final float FOV = 70;
  private static final float NEAR_PLANE = 0.1f;
  private static final float FAR_PLANE = 500001;
  private Matrix4f projectionMatrix;

  private StaticShader shader = new StaticShader();
  private EntityRenderer renderer;

  private SkyBoxRenderer skyBoxRenderer;
  private SkyBoxShader skyBoxShader = new SkyBoxShader();



  private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
  private Map<TexturedModel, List<Entity>> hudEntities = new HashMap<>();
  private List <SkyBox> skyBox;
  private Camera camera;
  /**
   * When the master renderer is created, create a projection matrix and
   * renderers
   */
  public MasterRenderer(Camera camera)
  {
    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glCullFace(GL11.GL_BACK);
    this.camera=camera;
    createProjectionMatrix();
    renderer = new EntityRenderer(shader, projectionMatrix);
    skyBox=new ArrayList<>();
    skyBoxRenderer = new SkyBoxRenderer(skyBoxShader, projectionMatrix,camera);
  }

  /**
   * Renders a scene using the main light, camera position, and entities
   * 
   * @param sun
   * @param camera
   */
  public void render(Light sun, Camera camera)
  {

    prepare();

    
    skyBoxShader.start();
    skyBoxShader.loadLight(sun);
    skyBoxShader.loadViewMatrix(camera);
    for(SkyBox skyElement:skyBox){
    skyBoxRenderer.render(skyElement);
    }
    skyBoxShader.stop();
   
    shader.start();
    shader.loadLight(sun);
    shader.loadViewMatrix(camera);
    renderer.render(entities);
    shader.stop();


    
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    shader.start();
    shader.loadLight(sun);
    shader.loadViewMatrix(new Camera());
    renderer.render(hudEntities);
    shader.stop();

    hudEntities.clear();
    entities.clear();
    skyBox.clear();
  }

  /**
   * UPDATE THIS
   */
  public void processSkyBox(SkyBox skyBox)
  {
   this.skyBox.add(skyBox);
   skyBox.getSkyEntity().position=camera.position;
  }

  /**
   * add an entity to the batch
   * 
   * @param entity
   */
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
  public void processHudEntity(Entity entity)
  {
    TexturedModel entityModel = entity.getModel();
    List<Entity> batch = hudEntities.get(entityModel);
    if (batch != null)
    {
      batch.add(entity);
    }
    else
    {
      List<Entity> newBatch = new ArrayList<Entity>();
      newBatch.add(entity);
      hudEntities.put(entityModel, newBatch);
    }
    
  }
  /**
   * Sets up a scene for rendering
   */
  public void prepare()
  {

    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glClearColor(1, 1, 1, 1);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

  }

  /**
   * Creates the projection matrix. The projection matrix represents the user's
   * field of view.
   */
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

  /**
   * cleans up excess memory in the shaders
   */
  public void cleanUp()
  {
    shader.cleanUp();
    skyBoxShader.cleanUp();
  }

}
