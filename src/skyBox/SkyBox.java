package skyBox;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import renderEngine.Loader;
import textures.ModelTexture;

public class SkyBox {

	

	private TexturedModel model;
	private Loader loader;
	private Entity skyEntity;
	
	public SkyBox(Loader loader,TexturedModel model){

		
		this.model=model;
		this.loader=loader;
		setSkyEntity(new Entity(model.getId(), model, new Vector3f(0f,0f,0f), 0f,
				0f, 0f,500f));
		
	}


	public TexturedModel getModel() {
		return model;
	}


	public void setModel(TexturedModel model) {
		this.model = model;
	}


	public Loader getLoader() {
		return loader;
	}


	public void setLoader(Loader loader) {
		this.loader = loader;
	}


	public Entity getSkyEntity() {
		return skyEntity;
	}


	public void setSkyEntity(Entity skyEntity) {
		this.skyEntity = skyEntity;
	}
	
	
}
