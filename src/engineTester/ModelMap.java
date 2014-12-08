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

public class ModelMap
{
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
  private Map<String, TexturedModel> texturedModelList = new HashMap<>();

  Loader loader = new Loader();
  StaticShader shader = new StaticShader();
  RawModel speedLabel;
  RawModel healthLabel;
  RawModel scoreLabel;
  RawModel xLabel;
  RawModel yLabel;
  ModelTexture textTx;

  public ModelMap()
  {
    RawModel ship = OBJLoader.loadObjModel("SpaceShip2", loader, true);
    RawModel ship2 = OBJLoader.loadObjModel("SpaceShip2", loader, true);
    RawModel modelAsteroid = OBJLoader
        .loadObjModel("SquareRock3", loader, true);
    RawModel stoneAsteroid = OBJLoader
        .loadObjModel("SquareRock4", loader, true);
    RawModel planetObj = OBJLoader.loadObjModel("planet", loader, true);
    RawModel laserObj = OBJLoader.loadObjModel("laser", loader, true);
    RawModel cone = OBJLoader.loadObjModel("cone", loader, true);

    //HUD TEXT
    speedLabel = FontLoader.loadFontModel("78", loader, true, 1);
    healthLabel = FontLoader.loadFontModel("0%", loader, true, 1);
    scoreLabel = FontLoader.loadFontModel("3 frags", loader, true, 1);
    xLabel = FontLoader.loadFontModel("X", loader, true, 1);
    yLabel = FontLoader.loadFontModel("Y", loader, true, 1);

    ModelTexture shipTexture = new ModelTexture(
        loader.loadTexture("SciFi_FighterMK_diffuse"));
    ModelTexture ship2Texture = new ModelTexture(
        loader.loadTexture("space_frigate_6_color"));
    ModelTexture asteroidTexture = new ModelTexture(
        loader.loadTexture("stone_texture"));
    ModelTexture stoneTexture = new ModelTexture(
        loader.loadTexture("RockRed2"));
    ModelTexture planetTexture = new ModelTexture(loader.loadTexture("planet"));
    ModelTexture laserTexture = new ModelTexture(loader.loadTexture("green"));
    ModelTexture greenTexture = new ModelTexture(loader.loadTexture("green"));

    textTx = new ModelTexture(loader.loadTexture("font"));

    //	    ModelTexture speedTexture = new ModelTexture(loader.loadTexture("font"));
    //      ModelTexture healthTexture = new ModelTexture(loader.loadTexture("font"));
    //      ModelTexture scoreTexture = new ModelTexture(loader.loadTexture("font"));
    //      ModelTexture xTexture = new ModelTexture(loader.loadTexture("font"));
    //      ModelTexture yTexture = new ModelTexture(loader.loadTexture("font"));

    TexturedModel texturedSpeed = new TexturedModel("H001", speedLabel, textTx);
    TexturedModel texturedHealth = new TexturedModel("H002", healthLabel,
        textTx);
    TexturedModel texturedScore = new TexturedModel("H003", scoreLabel, textTx);
    TexturedModel texturedX = new TexturedModel("H004", xLabel, textTx);
    TexturedModel texturedY = new TexturedModel("H005", yLabel, textTx);

    TexturedModel texturedModelAsteroid = new TexturedModel("A001",
        modelAsteroid, asteroidTexture);
    TexturedModel texturedModelAsteroid2 = new TexturedModel("A002",
        stoneAsteroid, stoneTexture);
    TexturedModel texturedModelStone = new TexturedModel("A003", modelAsteroid,
        stoneTexture);
    TexturedModel texturedShip = new TexturedModel("S001", ship, shipTexture);
    TexturedModel texturedShip2 = new TexturedModel("S002", ship2,
        ship2Texture);
    TexturedModel texturedPlanet = new TexturedModel("Plan", planetObj,
        planetTexture);
    TexturedModel texturedLaser = new TexturedModel("lase", laserObj,
        laserTexture);
    TexturedModel greenCone = new TexturedModel("gCone", cone,
        greenTexture);

    texturedModelList.put("H001", texturedSpeed);
    texturedModelList.put("H002", texturedHealth);
    texturedModelList.put("H003", texturedScore);

    texturedModelList.put("A001", texturedModelAsteroid);
    texturedModelList.put("A002", texturedModelAsteroid2);
    texturedModelList.put("A003", texturedModelStone);
    texturedModelList.put("S002", texturedShip);
    texturedModelList.put("S001", texturedShip2);
    texturedModelList.put("lase", texturedLaser);
    texturedModelList.put("Plan", texturedPlanet);
    texturedModelList.put("gCone", greenCone);

  }

  public Map<String, TexturedModel> getTexturedModelList()
  {
    return texturedModelList;
  }

  public TexturedModel setSpeedText(String input)
  {
    this.speedLabel = FontLoader.loadFontModel(input, loader, true, 1);
    return (new TexturedModel("H001", this.speedLabel, textTx));
  }

  public TexturedModel setHealthText(String input)
  {
    this.healthLabel = FontLoader.loadFontModel(input, loader, true, 1);
    return (new TexturedModel("H002", this.healthLabel, textTx));
  }

  public TexturedModel setScoreText(String input)
  {
    this.scoreLabel = FontLoader.loadFontModel(input, loader, true, 1);
    return (new TexturedModel("H003", this.scoreLabel, textTx));
  }

}
