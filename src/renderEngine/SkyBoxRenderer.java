/**
 * Thanks to youtube user ThinMatrix
 * Responsible for rendering planes
 */
package renderEngine;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.SkyBoxShader;
import skyBox.SkyBox;
import textures.ModelTexture;
import toolbox.MathUtil;
import entities.Camera;

public class SkyBoxRenderer
{
  private SkyBoxShader shader;
  private Camera camera;

  /**
   * Create a planeRenderer with a plane shader and a projection matrix
   * @param shader
   * @param projectionMatrix
   */
  public SkyBoxRenderer(SkyBoxShader shader, Matrix4f projectionMatrix,Camera camera)
  {
    this.shader = shader;
    this.camera=camera;
    
    shader.start();
    shader.loadProjectionMatrix(projectionMatrix);
    shader.stop();
  }

  /**
   * Render each plane
   * @param sky
   */
  public void render(SkyBox sky)
  {
    
      prepareTerrain(sky);
      loadModelMatrix(sky);
      GL11.glDrawElements(GL11.GL_TRIANGLES, sky.getModel().getRawModel()
          .getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
      unbindTexturedModel();
  }

  /**
   * Prepares terrains to be rendered
   * @param sky
   */
  private void prepareTerrain(SkyBox sky)
  {
    RawModel rawModel = sky.getModel().getRawModel();
    GL30.glBindVertexArray(rawModel.getVaoID());

    GL20.glEnableVertexAttribArray(0);
    GL20.glEnableVertexAttribArray(1);
    GL20.glEnableVertexAttribArray(2);
    ModelTexture texture = sky.getModel().getTexture();
    shader.loadShineVariables(texture.getShadeDamper(),
        texture.getReflectivity());

    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
  }

  /**
   * Unbinds terrains
   */
  private void unbindTexturedModel()
  {
    GL20.glDisableVertexAttribArray(0);
    GL20.glDisableVertexAttribArray(1);
    GL20.glDisableVertexAttribArray(2);
    GL30.glBindVertexArray(0);
  }

  /**
   * Load a model matrix based on the terrain into the shader
   * @param sky
   */
  private void loadModelMatrix(SkyBox sky)
  {
    Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(
        camera.getPosition(), 0f, 0f, 0f, sky.getSkyEntity().getScale());
    shader.loadTransformationMatrix(transformationMatrix);
  }
}
