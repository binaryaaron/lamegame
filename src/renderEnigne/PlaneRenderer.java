package renderEnigne;

import java.util.List;

import models.RawModel;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import plane.Plane;
import shaders.PlaneShader;
import textures.ModelTexture;
import toolbox.MathUtil;

public class PlaneRenderer {
	private PlaneShader shader;
	
	public PlaneRenderer(PlaneShader shader, Matrix4f projectionMatrix){
		this.shader =shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		
	}
	
	
	public void render(List<Plane> terrains){
		for(Plane terrain:terrains){
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(),
					GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
			//System.out.println(terrain.getModel().getVertexCount()+";"+terrain.getTexture().getTextureID());
		}
		
	
	}
	
	
	private void prepareTerrain(Plane terrain){
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture= terrain.getTexture();
		shader.loadShineVariables(texture.getShadeDamper(), texture.getReflectivity());

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}
	
	private void unbindTexturedModel(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Plane terrain){
		Matrix4f transformationMatrix = MathUtil.createTransformationMatrix(
				new Vector3f(terrain.getX(),0,terrain.getZ()), 0f, 0f, 0f, 1f);
		shader.loadTransformationMatrix(transformationMatrix);
		
	}
	

}
