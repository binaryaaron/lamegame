package models;

import textures.ModelTexture;

public class TexturedModel 
{
	private RawModel rawModel;
	private ModelTexture texture;
	private String id;
	
	/**
	 * Create a textured model with an id, model and texture
	 * a textured model is a rawmodel with a texture mapped to the rawmodel's data
	 * @param id
	 * @param model
	 * @param texture
	 */
	public TexturedModel(String id, RawModel model,ModelTexture texture)
	{
		this.rawModel=model;
		this.texture =texture;
		this.id = id;
	}

	/**
	 * getter
	 * @return
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * getter
	 * @return
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * getter
	 * @return
	 */
	public ModelTexture getTexture() {
		return texture;
	}

}