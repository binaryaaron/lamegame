/*** 
 * Thanks to youtube user ThinMatrix
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI
 */
package engineTester;

import java.util.Random;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import plane.Plane;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.TextDraw;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.PerformanceUtilities;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop
{

  public final static boolean PRINT_FPS = true;
  public static int nAsteroids = 5;

  public static void main(String[] args)
  {

    DisplayManager.createDisplay();
    Loader loader = new Loader();
    StaticShader shader = new StaticShader();
    // Renderer renderer =new Renderer(shader);
    RawModel modelShip = OBJLoader.loadObjModel("SkyBox", loader);
    RawModel modelAsteroid = OBJLoader.loadObjModel("Rock", loader);
    RawModel stoneAsteroid = OBJLoader.loadObjModel("Rock", loader);

    ModelTexture asteroidTexture = new ModelTexture(
        loader.loadTexture("RockRed2"));
    ModelTexture shipTexture = new ModelTexture(loader.loadTexture("RockRed2"));
    ModelTexture stoneTexture = new ModelTexture(
        loader.loadTexture("stone_texture"));

    Entity[] asteroids = new Entity[nAsteroids];
    Entity[] stones = new Entity[nAsteroids];
    Vector3f[] asteroidRotation = new Vector3f[nAsteroids];
    Vector3f[] asteroidMotion = new Vector3f[nAsteroids];

    TexturedModel texturedModelAsteroid = new TexturedModel(modelAsteroid,
        asteroidTexture);
    TexturedModel texturedModelStone = new TexturedModel(stoneAsteroid,
        stoneTexture);
    TexturedModel texturedModelShip = new TexturedModel(modelShip, shipTexture);

    texturedModelStone.getTexture().setReflectivity(1);
    texturedModelStone.getTexture().setShadeDamper(10);

    TextDraw textDraw = new TextDraw();

    // if(PRINT_FPS)textDraw.init();
    Random rand = new Random();

    /* create asteroids with random positions and velocity direction*/
    for (int i = 0; i < nAsteroids; i++)
    {
      asteroids[i] = new Entity(texturedModelAsteroid, new Vector3f(
          (rand.nextFloat() - 0.5f) * 50, (rand.nextFloat() - 0.5f) * 50,
          (rand.nextFloat() - 0.5f) * 50), (rand.nextFloat() - 0.5f),
          (rand.nextFloat() - 0.5f), (rand.nextFloat() - 0.5f),
          (rand.nextFloat() + 0.5f) * 3);

      stones[i] = new Entity(texturedModelStone, new Vector3f(
          (rand.nextFloat() - 0.5f) * 50, (rand.nextFloat() - 0.5f) * 50,
          (rand.nextFloat() - 0.5f) * 50), (rand.nextFloat() - 0.5f),
          (rand.nextFloat() - 0.5f), (rand.nextFloat() - 0.5f),
          (rand.nextFloat() + 0.5f));

      asteroidRotation[i] = new Vector3f((rand.nextFloat() - 0.5f),
          (rand.nextFloat() - 0.5f), (rand.nextFloat() - 0.5f));
      asteroidMotion[i] = new Vector3f((rand.nextFloat() - 0.5f) * 0.02f,
          (rand.nextFloat() - 0.5f) * 0.02f, (rand.nextFloat() - 0.5f) * 0.02f);

    }
    /* load the ship in front of the camera */
    Entity ship = new Entity(texturedModelShip, new Vector3f(0f, 0f, -20f), 0f,
        0f, 0f, 3f);
    Light light = new Light(new Vector3f(10f, 5f, 2000f), new Vector3f(1.0f,
        1.0f, 1.0f));

    Plane terrain = new Plane(-1, -1, loader, new ModelTexture(
        loader.loadTexture("stone_texture")));
    Plane terrain2 = new Plane(0, -1, loader, new ModelTexture(
        loader.loadTexture("stone_texture")));

    Camera camera = new Camera();
    MasterRenderer renderer = new MasterRenderer();
    PerformanceUtilities pu = new PerformanceUtilities();
    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }
    /* Perform object movement as long as the window exists */
    while (!Display.isCloseRequested())
    {

      /* rotate and move each asteroid */
      for (int i = 0; i < nAsteroids; i++)
      {
        Entity ass = asteroids[i];
        Entity stone = stones[i];
        Vector3f rot = asteroidRotation[i];
        Vector3f mot = asteroidMotion[i];

        ass.rotatate(rot.x, rot.y, rot.z);
        ass.translate(mot.x, mot.y, mot.z);

        stone.rotatate(rot.x, rot.y, rot.z);
        stone.translate(mot.x, mot.y, mot.z);
        /* reverse an asteroid's direction if they reach the bounds of the field */
        if (ass.getPosition().x > 300 || ass.getPosition().x < -300)
        {
          mot.x = -mot.x;

        }

        if (ass.getPosition().y > 300 || ass.getPosition().y < -300)
        {
          mot.y = -mot.y;

        }
        if (ass.getPosition().z > 300 || ass.getPosition().z < -300)
        {
          mot.y = -mot.y;

        }

      }

      camera.move();
      /* render each asteroid/stone */
      for (Entity ass : asteroids)
      {
        renderer.processEntity(ass);
      }
      for (Entity stone : stones)
      {
        renderer.processEntity(stone);
      }

      // renderer.processTerrain(terrain);
      // renderer.processTerrain(terrain2);

      // debugging for frame and texture on ship
      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_UP))
      {
        ship.rotatate(0.2f, 0.0f, 0f);
      }

      if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
      {
        ship.rotatate(-0.2f, 0.0f, 0f);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
      {
        ship.rotatate(0.0f, 0.2f, 0f);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
      {
        ship.rotatate(0.0f, -0.2f, 0f);
      }

      renderer.processEntity(ship);
      renderer.render(light, camera);

      // if(pu.getFPS()!=textDraw.fps){
      //textDraw.setText(Integer.toString(pu.getFPS()));
      // textDraw.fps=pu.getFPS();
      //textDraw.render();
      // }

      DisplayManager.updateDisplay();
      if (PRINT_FPS)
      {
        pu.updateFPS();

        System.out.println(pu.getFPS());
      }

    }
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
  }

}