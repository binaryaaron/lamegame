/*** 
 * Thanks to youtube user ThinMatrix
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI
 */
package engineTester;

import java.util.ArrayList;
import java.util.List;

import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import skyBox.SkyBox;
import textures.ModelTexture;
import toolbox.PerformanceUtilities;
import world.BoxUtilities;
import entities.Camera;
import entities.Entity;
import entities.Light;

public class MainGameLoop
{

  public final static boolean PRINT_FPS = false;
  private final static boolean PHYSICS_DEBUG = false;

  public static void main(String[] args)
  {

    DisplayManager.createDisplay();
    Loader loader = new Loader();
    ModelMap modelMap = new ModelMap();

    // create skybox, this is not an entity so it is seperate
    RawModel skyBox = OBJLoader.loadObjModel("SkyBox2", loader, true);
    ModelTexture skyTexture = new ModelTexture(
        loader.loadTexture("SkyBox2"));
    TexturedModel texturedSkyBox = new TexturedModel(skyBox, skyTexture);
    SkyBox skyBoxEntity = new SkyBox(loader, texturedSkyBox);

    //create lights and camera for the player. camera position should be set in
    //parsing routine
    Light light = new Light(new Vector3f(10f, 5f, 2000f), new Vector3f(
        1.0f, 1.0f, 1.0f));
    Camera camera = new Camera();
    MasterRenderer renderer = new MasterRenderer(camera);

    PerformanceUtilities pu = new PerformanceUtilities();

    // this will be information read from a socket
    // format: ID,x,y,z,rotx,rot,y,rotz,scale
    String testInput;

    if (PHYSICS_DEBUG)
    {
      testInput = "A001,1,0,-5,0,0,0,1;" + "A002,-1,0,-5,0,0,0,0.5";

    }

    else
    {
      testInput = "S001,0,0,20,0,0,0,0.01;" + "S002,0,15,-20,0,0,0,0.5;"
          + "T001,0,0,100,0,0,0,1;" + "Cam,0,0,1,0,180,0,1";
    }

    List<Entity> renderList = new ArrayList<>();

    String[] sceneInfo = testInput.split(";");
    for (String object : sceneInfo)
    {
      String[] currentLine = object.split(",");
      String id;
      float x, y, z, xr, yr, zr, s;
      // translate all imput data into appropriate entities;
      x = Float.parseFloat(currentLine[1]);
      y = Float.parseFloat(currentLine[2]);
      z = Float.parseFloat(currentLine[3]);
      xr = Float.parseFloat(currentLine[4]);
      yr = Float.parseFloat(currentLine[5]);
      zr = Float.parseFloat(currentLine[6]);
      s = Float.parseFloat(currentLine[7]);
      System.out.println(object.charAt(0));
      if (object.startsWith("Cam"))
      {
        camera.setPosition(new Vector3f(x, y, z));
        camera.setPitch(xr);
        camera.setYaw(yr);
        camera.setRoll(zr);
      }

      else
      {

        id = currentLine[0];

        Entity tmp_Entity = new Entity(modelMap.getTexturedModelList().get(
            id), new Vector3f(x, y, z), xr, yr, zr, s);
        renderList.add(tmp_Entity);
      }
    }

    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }
    /* Perform object movement as long as the window exists */
    while (!Display.isCloseRequested())
    {
      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }

      //controls for physics testing with two asteroids
      //wasd control leftest asteroid, arrows control rightmost.
      //holding shift will slow down the shifting speed.
      if (PHYSICS_DEBUG)
      {
        Float scale = 0.1f;
        Entity Asteroid1 = renderList.get(1);
        Entity Asteroid2 = renderList.get(0);

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard
            .isKeyDown(Keyboard.KEY_RSHIFT))
        {
          scale = 0.01f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
          Asteroid2.translate(scale, 0.0f, 0f);
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            Asteroid2.translate(-scale, 0.0f, 0f);
          }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
          Asteroid2.translate(-scale, 0.0f, 0f);
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            Asteroid2.translate(scale, 0.0f, 0f);
          }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
        {
          Asteroid2.translate(0.0f, scale, 0f);
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            Asteroid2.translate(0.0f, -scale, 0f);
          }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
          Asteroid2.translate(0.0f, -scale, 0f);
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            Asteroid2.translate(0.0f, scale, 0f);
          }
        }
        // /
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
          Asteroid1.translate(scale, 0.0f, 0f);
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            Asteroid1.translate(-scale, 0.0f, 0f);
          }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
          Asteroid1.translate(-scale, 0.0f, 0f);
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            Asteroid1.translate(scale, 0.0f, 0f);
          }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
          Asteroid1.translate(0.0f, scale, 0f);
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            Asteroid1.translate(0.0f, -scale, 0f);
          }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
          Asteroid1.translate(0.0f, -scale, 0f);
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            Asteroid1.translate(0.0f, scale, 0f);
          }
        }

      }

      //camera controls
      else
      {
        camera.move();
      }

			/* render each entity passed to the client */
      for (Entity ent : renderList)
      {
        renderer.processEntity(ent);
      }

      renderer.processSkyBox(skyBoxEntity);
      renderer.render(light, camera);

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
