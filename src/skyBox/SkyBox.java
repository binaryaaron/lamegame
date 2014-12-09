package skyBox;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import textures.ModelTexture;

/**
 * SkyBox class for generic sykbox usage
 */
public class SkyBox {

	private TexturedModel model;
	private Loader loader;
	private Entity skyEntity;

	/**
	 * Create a skybox
	 * @param loader loader to use
	 * @param model model to use
	 */
	public SkyBox(Loader loader,TexturedModel model){
		this.model=model;
		this.loader=loader;
		setSkyEntity(new Entity(model.getId(), model, new Vector3f(0f,0f,0f), 0f,
				0f, 0f,100000f));
	}

	/**
	 * Get the skybox model
	 * @return model
	 */
	public TexturedModel getModel() {
		return model;
	}

	/**
	 * Set the skybox model
	 * @param model model to set
	 */
	public void setModel(TexturedModel model) {
		this.model = model;
	}

	/**
	 * Get the entity representing the Sky (for rendering)
	 * @return skybox entity
	 */
	public Entity getSkyEntity() {
		return skyEntity;
	}

	/**
	 * Set the skybox entity (for rendering)
	 * @param skyEntity entity to set
	 */
	public void setSkyEntity(Entity skyEntity) {
		this.skyEntity = skyEntity;
	}
	
	
}
