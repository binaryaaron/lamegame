/*** 
 * Thanks to youtube user ThinMatrix
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI
 */
package engineTester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gameObjects.Asteroid;
import models.RawModel;
import models.TexturedModel;
import server.WalkerClient;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import physics.PhysicsUtilities;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import skyBox.SkyBox;
import textures.ModelTexture;
import toolbox.PerformanceUtilities;
import entities.Camera;
import entities.Entity;
import entities.Light;
import world.BoxUtilities;

import javax.swing.*;

public class MainGameLoop
{
  public final static boolean PRINT_FPS = false;
  private final static boolean SERVER_TEST = true;
  private final static boolean ADD_ASTEROID = false;
  static Entity player;
  static Entity asteroid;

  public static void main(String[] args) throws IOException
  {
    // String ipAddress=args[0];
    DisplayManager.createDisplay();
    Loader loader = new Loader();
    ModelMap modelMap = new ModelMap();

    // create skybox, this is not an entity so it is seperate
    RawModel skyBox = OBJLoader.loadObjModel("SkyBox2", loader, true);
    ModelTexture skyTexture = new ModelTexture(loader.loadTexture("SkyBox2"));
    TexturedModel texturedSkyBox = new TexturedModel(skyBox, skyTexture);
    SkyBox skyBoxEntity = new SkyBox(loader, texturedSkyBox);

    modelMap.getTexturedModelList().put("Play",
        modelMap.getTexturedModelList().get("S001"));
    // create lights and camera for the player. camera position should be set in
    // parsing routine
    Light light = new Light(new Vector3f(10f, 5f, 2000f), new Vector3f(1.0f,
        1.0f, 1.0f));
    Camera camera = new Camera();
    MasterRenderer renderer = new MasterRenderer(camera);

    PerformanceUtilities pu = new PerformanceUtilities();

    // this will be information read from a socket
    // format: ID,x,y,z,rotx,rot,roty,rotz,scale
    String testInput;
    String asteroid1;

    if (SERVER_TEST)
    {
      WalkerClient wc = null;
      try
      {
        wc = new WalkerClient(args);

      } catch (IOException e)
      {
        e.printStackTrace();
      }
      testInput = wc.updateClientGameState("Play,0,0,4,0,0,0,0.3;");//start string
      
      int a=0;
      int x=0;
      int y=4;
      int z=0;
      int rot=0;
      int scale=1;
//      asteroid1=("A00"+','+a+','+x+','+y+','+z+','+rot+','+rot+','+rot+','+scale+';');
      asteroid1=("A00,1,0,-5,0,0,0,1");
    }

    List<Entity> renderList = parseGameStateString(testInput, modelMap);

    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();

    /* Perform object movement as long as the window exists */
    WalkerClient wc = null;
    if (SERVER_TEST)wc = new WalkerClient(args);
    while (!Display.isCloseRequested())
    {
      /* Perform object movement as long as the window exists */

      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }

      // Camera to follow player.

      float camxShift;
      float camyShift;
      float camzShift;

      // controls for physics testing with two asteroids
      // wasd control leftest asteroid, arrows control rightmost.
      // holding shift will slow down the shifting speed.
      // controls for physics testing with two asteroids
      // wasd control leftest asteroid, arrows control rightmost.
      // holding shift will slow down the shifting speed.
      float rotateShift = 0.0f;

      Float scale = 0.1f;

      if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
      {
        scale = 0.01f;
      }

      // System.out.println(player.getRotX()+";"+player.getRotY()+";"+player.getRotZ());
      // System.out.println(Math.cos(player.getRotX())+";"+Math.cos(player.getRotY())+";"+Math.cos(player.getRotZ()));
      // System.out.println(Math.cos(player.getRotX()*(3.14/180))+";"+Math.cos(player.getRotY()*(3.14/180))+";"+Math.cos(player.getRotZ()*(3.14/180)));

      float sinx = (float) Math.sin(player.getRotX() * 3.14 / 180) * scale;
      float cosx = (float) Math.cos(player.getRotX() * (3.14 / 180)) * scale;
      float siny = (float) Math.sin(player.getRotY() * 3.14 / 180) * scale;
      float cosy = (float) Math.cos(player.getRotY() * (3.14 / 180)) * scale;
      float sinz = (float) Math.sin(player.getRotZ() * 3.14 / 180) * scale;
      float cosz = (float) Math.cos(player.getRotZ() * (3.14 / 180)) * scale;

      if (Keyboard.isKeyDown(Keyboard.KEY_UP))
      {
        player.translate(-siny * cosz * 3, sinx * cosz * 3, -cosy * cosx * 3);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
      {
        player.translate(siny * cosz, -sinx * cosz, cosy * cosx);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
      {
        player.translate(cosy * cosz, sinz * cosx, -siny * cosx);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
      {
        player.translate(-cosy * cosz, sinz * cosx, siny * cosx);
      }

      // /
      if (Keyboard.isKeyDown(Keyboard.KEY_W))
      {
        player.rotate(scale, 0.0f, 0f);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_S))
      {
        player.rotate(-scale, 0.0f, 0f);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_A))
      {

        player.rotate(0.0f, -scale, 0.0f);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_D))
      {
        player.rotate(0.0f, scale, 0.0f);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_Q))
      {
        player.rotate(0.0f, 0f, scale);
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_E))
      {
        player.rotate(0.0f, 0f, -scale);
      }

      /************************************************************************
       * call client update
       ************************************************************************/
      Vector3f pos = player.getPosition();
      String toSend = "Play," + pos.x + "," + pos.y + "," + pos.z + "," + player.getRotX() + "," + player.getRotY() + "," + player.getRotZ()+ "," + 0.3+';';
      if(ADD_ASTEROID)toSend=toSend.concat(asteroid1);
      testInput = wc.updateClientGameState(toSend);
      renderList.clear();
      renderList = parseGameStateString(testInput, modelMap);

      float yOff = 1;
      float zOff = 3;

      camxShift = ((float) Math.sin(player.getRotY() * 3.14 / 180) * zOff);// *(float) Math.sin(player.getRotZ()*3.14/180);
      camyShift = ((float) (Math.cos(player.getRotX() * 3.14 / 180)) * yOff)- (float) (Math.sin(player.getRotX() * 3.14 / 180)) * zOff;// *(float) Math.cos(player.getRotX()*3.14/180);
      camzShift = -((float) Math.sin(player.getRotX() * 3.14 / 180) * yOff)+ ((float) Math.cos(player.getRotX() * 3.14 / 180) * zOff);// *(float) Math.cos(player.getRotX()*3.14/180);;

      camera.setPosition(player.getPosition().x + camxShift,player.getPosition().y + camyShift, player.getPosition().z+ camzShift);
      camera.setRotation(-player.getRotX(), -player.getRotY(),-player.getRotZ());

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

  private static List<Entity> parseGameStateString(String testInput, ModelMap modelMap)
  {
    List<Entity> renderList = new ArrayList<>();
    String[] sceneInfo = testInput.split(";");
    for (String object : sceneInfo)
    {
      String[] currentLine = object.split(",");
      String id;
      float x, y, z, xRot, yRot, zRot, s;
      // translate all input data into appropriate entities;
      id = currentLine[0];
      x = Float.parseFloat(currentLine[1]);
      y = Float.parseFloat(currentLine[2]);
      z = Float.parseFloat(currentLine[3]);
      xRot = Float.parseFloat(currentLine[4]);
      yRot = Float.parseFloat(currentLine[5]);
      zRot = Float.parseFloat(currentLine[6]);
      s = Float.parseFloat(currentLine[7]);

      if (object.startsWith("Play") && player == null)
      {
        player = new Entity(modelMap.getTexturedModelList().get(id),new Vector3f(x, y, z), xRot, yRot, zRot, s);
      } else
      {
        TexturedModel tm=modelMap.getTexturedModelList().get(id);
        Vector3f vec=new Vector3f(x,y,z);
        Entity tmp_Entity=new Entity(tm, vec, xRot, yRot, zRot, s);
//        Entity tmp_Entity = new Entity(modelMap.getTexturedModelList().get(id),new Vector3f(x, y, z), xr, yr, zr, s);
        renderList.add(tmp_Entity);
      }
    }
    return renderList;
  }

}
