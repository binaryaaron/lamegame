package textures;

/**
 * Thanks to ThinMatrix
 * ModelTexture is an object that holds and manages textures
 * 
 * @author Paige
 *
 */
public class ModelTexture {
	
	private int textureID;
	
	private float shadeDamper = 1;
	private float reflectivity = 0;
	
	/**
	 * @return
	 */
	public float getShadeDamper() {
		return shadeDamper;
	}

	/**
	 * @param shadeDamper
	 */
	public void setShadeDamper(float shadeDamper) {
		this.shadeDamper = shadeDamper;
	}

	/**
	 * @return
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * @param reflectivity
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	/**
	 * @param id
	 */
	public ModelTexture(int id){
		this.textureID =id;
		
	}

	/**
	 * @return
	 */
	public int getTextureID() {
		return textureID;
	}


}
