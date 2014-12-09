package models;

import textures.ModelTexture;

public class TexturedModel 
{
	private RawModel rawModel;
	private ModelTexture texture;
	private String id;
	
	/**
	 * Create a textured model with an id, model and texture
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
	 * Getters
	 * @return
	 */
	public String getId()
	{
		return id;
	}
	public RawModel getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

}
