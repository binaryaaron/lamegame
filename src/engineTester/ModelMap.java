package engineTester;


import java.util.HashMap;
import java.util.Map;

import models.RawModel;
import models.TexturedModel;
import renderEngine.FontLoader;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import shaders.StaticShader;
import textures.ModelTexture;

public class ModelMap {
	//generate a hashmap of all textured models that we could use in the scene and 
	//associate key them to an int as an id
	/*the current key mapping is as follows
	
	*1-> square rock asteroid
	*2->high poly rock asteroid
	*3->asteroid
	*4->ship,SciFi_FighterMK
	*5->space_frigate_6
	*
	*/
	private Map<String,TexturedModel> texturedModelList = new HashMap<>();
	
	Loader loader = new Loader();
	StaticShader shader = new StaticShader();
	RawModel speedLabel;
	RawModel healthLabel;
	RawModel scoreLabel;
	RawModel xLabel;
	RawModel yLabel;
	ModelTexture textTx;
	
	public ModelMap(){
	    RawModel ship = OBJLoader.loadObjModel("SpaceShip3", loader, true);
	    RawModel ship2 = OBJLoader.loadObjModel("SpaceShip31", loader, true);
	    RawModel modelAsteroid = OBJLoader.loadObjModel("SquareRock3", loader,  true);
	    RawModel stoneAsteroid = OBJLoader.loadObjModel("SquareRock4", loader,  false);
	    //change text label here
	    speedLabel = FontLoader.loadFontModel("78", loader,  true, 1);
	    healthLabel = FontLoader.loadFontModel("100%", loader,  true, 1);
	    scoreLabel = FontLoader.loadFontModel("3 frags", loader,  true, 1);
      speedLabel = FontLoader.loadFontModel("78", loader,  true, 1);
      xLabel = FontLoader.loadFontModel("78", loader,  true, 1);
      yLabel = FontLoader.loadFontModel("78", loader,  true, 1);
	        
	    ModelTexture shipTexture = new ModelTexture(loader.loadTexture("SciFi_FighterMK_diffuse"));
	    ModelTexture ship2Texture = new ModelTexture(loader.loadTexture("space_frigate_6_color"));
	    ModelTexture asteroidTexture = new ModelTexture(loader.loadTexture("stone_texture"));
	    ModelTexture stoneTexture = new ModelTexture(loader.loadTexture("RockRed2"));
	    textTx = new ModelTexture(loader.loadTexture("font"));
      ModelTexture speedTexture = new ModelTexture(loader.loadTexture("font"));
      ModelTexture healthTexture = new ModelTexture(loader.loadTexture("font"));
      ModelTexture scoreTexture = new ModelTexture(loader.loadTexture("font2"));
	    ModelTexture xTexture = new ModelTexture(loader.loadTexture("font"));
	    ModelTexture yTexture = new ModelTexture(loader.loadTexture("font"));
      
	    TexturedModel texturedModelAsteroid = new TexturedModel(modelAsteroid,asteroidTexture);
	    TexturedModel texturedModelAsteroid2 = new TexturedModel(stoneAsteroid,asteroidTexture);
	    TexturedModel texturedModelStone = new TexturedModel(modelAsteroid,stoneTexture);
	    TexturedModel texturedShip = new TexturedModel(ship, shipTexture);
	    TexturedModel texturedShip2 = new TexturedModel(ship2, ship2Texture);
      TexturedModel texturedSpeed = new TexturedModel(speedLabel, speedTexture);
      TexturedModel texturedHealth = new TexturedModel(healthLabel, healthTexture);
      TexturedModel texturedScore = new TexturedModel(scoreLabel, scoreTexture);
      TexturedModel texturedX = new TexturedModel(xLabel, xTexture);
      TexturedModel texturedY = new TexturedModel(yLabel, yTexture);
      
    texturedModelList.put("A001", texturedModelAsteroid);
    texturedModelList.put("A002", texturedModelAsteroid2);
    texturedModelList.put("A003", texturedModelStone);
    texturedModelList.put("S001", texturedShip);
    texturedModelList.put("S002", texturedShip2);
    texturedModelList.put("H001", texturedSpeed);
    texturedModelList.put("H002", texturedHealth);
    texturedModelList.put("H003", texturedScore);
    
	}
    

	
	public Map<String, TexturedModel> getTexturedModelList() {
		return texturedModelList;
	}

  public TexturedModel setSpeedText(String input)
  {
    this.speedLabel = FontLoader.loadFontModel(input, loader,  true, 1);
    return (new TexturedModel(this.speedLabel, new ModelTexture(loader.loadTexture("font"))));  
  }
  public TexturedModel setScoreText(String input)
  {
    this.scoreLabel = FontLoader.loadFontModel(input, loader,  true, 1);
    return (new TexturedModel(this.scoreLabel, new ModelTexture(loader.loadTexture("font"))));  
  }
  public TexturedModel setHealthText(String input)
  {
    this.healthLabel = FontLoader.loadFontModel(input, loader,  true, 1);
    return (new TexturedModel(this.healthLabel, new ModelTexture(loader.loadTexture("font"))));  
  }
	


}
