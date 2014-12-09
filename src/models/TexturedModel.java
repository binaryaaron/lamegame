package models;

import textures.ModelTexture;

public class TexturedModel 
{
	
	private RawModel rawModel;
	private ModelTexture texture;
	private String id;
	public TexturedModel(String id, RawModel model,ModelTexture texture){
		
		this.rawModel=model;
		this.texture =texture;
		this.id = id;
	}

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
