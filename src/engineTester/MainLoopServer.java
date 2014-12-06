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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import gameObjects.Asteroid;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.ra4king.opengl.util.Utils;
import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;

import physics.PhysicsUtilities;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import server.WalkerServer;
import server.WalkerThread;
import skyBox.SkyBox;
import textures.ModelTexture;
import toolbox.PerformanceUtilities;
import entities.Camera;
import entities.Entity;
import entities.Light;
import world.BoxUtilities;

import javax.swing.*;

public class MainLoopServer
{

  public final static boolean PRINT_FPS = false;
  private final static boolean PHYSICS_DEBUG = true;
  public static WalkerServer myServer;
  private static int loop = 0;
  private volatile static int clientConnections;
  static Entity player;
  private static int nAsteroids = 200;

  public static void main(String[] args)
  {

    DisplayManager.createDisplay();
    Loader loader = new Loader();

    ModelMap modelMap = new ModelMap();

    Camera camera = new Camera();
    camera.followObj = player;

    // create skybox, this is not an entity so it is seperate
    RawModel skyBox = OBJLoader.loadObjModel("SkyBox2", loader, true);
    ModelTexture skyTexture = new ModelTexture(loader.loadTexture("SkyBox2"));
    TexturedModel texturedSkyBox = new TexturedModel("SkyBox2", skyBox,
        skyTexture);
    RawModel laser = OBJLoader.loadObjModel("missle", loader, true);
    ModelTexture laserTexture = new ModelTexture(loader.loadTexture("Missle"));
    TexturedModel texturedLaser = new TexturedModel("laser", laser,
        laserTexture);
    SkyBox skyBoxEntity = new SkyBox(loader, texturedSkyBox);

    modelMap.getTexturedModelList()
        .put("Play", modelMap.getTexturedModelList().get("S002"));

    // create lights and camera for the player. camera position should be set in
    // parsing routine
    Light light = new Light(new Vector3f(10f, 5f, 2000f), new Vector3f(1.0f,
        1.0f, 1.0f));
    MasterRenderer renderer = new MasterRenderer(camera);

    PerformanceUtilities pu = new PerformanceUtilities();

    String outputToClient = null;

    try
    {
      myServer = new WalkerServer();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    String startString;
    Random rand = new Random();
    startString = "Play,1000,0,4,0,0.66,0,0.03;Plan,0,0,0,0,0,0,100;";
    for (int i = 0; i < nAsteroids; i++)
    {
      int a = rand.nextInt(2) + 1;

      int y = rand.nextInt(20) - 10;
      int r = 0;
      int x = rand.nextInt(3000) - 1500;
      int z = rand.nextInt(3000) - 1500;
      while (r < 562500 || r > 1822500)
      {
        x = rand.nextInt(3000) - 1500;
        z = rand.nextInt(3000) - 1500;
        r = x * x + z * z;
      }

      float s = rand.nextFloat() * 50;
      startString = startString.concat("A00" + a + "," + x + "," + y + "," + z
          + ",0,0,0," + s + ";");
    }

    List<Entity> renderList = parseGameStateString(startString, modelMap);
    List<Entity> missileList = new ArrayList<>();

    String[] sceneInfo = startString.split(";");
    // for (String object : sceneInfo)
    // {
    // String[] currentLine = object.split(",");
    // String id;
    // float x, y, z, xr, yr, zr, s;
    // // translate all imput data into appropriate entities;
    // x = Float.parseFloat(currentLine[1]);
    // y = Float.parseFloat(currentLine[2]);
    // z = Float.parseFloat(currentLine[3]);
    // xr = Float.parseFloat(currentLine[4]);
    // yr = Float.parseFloat(currentLine[5]);
    // zr = Float.parseFloat(currentLine[6]);
    // s = Float.parseFloat(currentLine[7]);
    // // System.out.println(object.charAt(0));
    // if (object.startsWith("Cam"))
    // {
    // camera.setPosition(new Vector3f(x, y, z));
    // camera.setPitch(xr);
    // camera.setYaw(yr);
    // camera.setRoll(zr);
    // }
    //
    // else
    // {
    // id = currentLine[0];
    //
    // Entity tmp_Entity = new Entity(id, modelMap.getTexturedModelList().get(
    // id), new Vector3f(x, y, z), xr, yr, zr, s);
    // renderList.add(tmp_Entity);
    // }
    // }

    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();
    float scale;

    Quaternion orientation = player.orientation;
    Vector3 position = new Vector3(player.position.x,
        player.position.y, player.position.z);
    Vector3 cameraPos = position.copy();
    Vector3 missilePos = position.copy();
    camera.quadTranslate(cameraPos);
    camera.orientation = orientation.copy();
    /* Perform object movement as long as the window exists */
    // TODO change server to no display output, loop while threadlist is not
    // empty
    while (!Display.isCloseRequested())
    {
      long startingTime = System.currentTimeMillis();
      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }

      for (int i = 0; i < myServer.threadList.size(); i++)
      {
        String inputFromClient = myServer.inputFromClient.get(i);
        inputFromClient = getInput(i);
        if (inputFromClient != null)
        {
          String[] clientInput = inputFromClient.split(";");
          for (String input : clientInput)
          {

            if (input.equals("KEY_LSHIFT") || input.equals("KEY_RSHIFT"))
            {
              scale = 0.01f;
            }

            orientation = player.orientation;
            position = new Vector3(player.position.x,
                player.position.y, player.position.z);
            cameraPos = position.copy();
            missilePos = position.copy();

            // in your update cod
            float speed = 0.02f;// (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) |
                                // Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 20
                                // : 50) * deltaTime / (float)1e9;
            float rotSpeed = 0.5f;

            // pitch
            int dy = Mouse.getDY();
            if (input.equals("KEY_W"))
            {
              orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(1, 0, 0))
                  .mult(orientation);
            }
            if (input.equals("KEY_S"))
            {
              orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(1, 0, 0))
                  .mult(orientation);
            }
            // yaw
            int dx = Mouse.getDX();
            if (input.equals("KEY_A"))
            {
              orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(0, 1, 0))
                  .mult(orientation);
            }
            if (input.equals("KEY_D"))
            {
              orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(0, 1, 0))
                  .mult(orientation);
            }
            // roll
            if (input.equals("KEY_E")) orientation = Utils.angleAxisDeg(
                rotSpeed, new Vector3(0, 0, 1)).mult(orientation);
            if (input.equals("KEY_Q")) orientation = Utils.angleAxisDeg(
                -rotSpeed, new Vector3(0, 0, 1)).mult(orientation);

            orientation.normalize();

            Quaternion inverse = orientation.copy().inverse();

            Vector3 delta = new Vector3();

            if (input.equals("KEY_DOWN")) delta.z(-speed);
            if (input.equals("KEY_UP")) delta.z(delta.z() + speed);

            if (input.equals("KEY_RIGHT")) delta.x(-speed);
            if (input.equals("KEY_LEFT")) delta.x(delta.x() + speed);

            if (input.equals("KEY_SPACE")) delta.y(-speed);
            if (input.equals("KEY_LCONTROL")) delta.y(delta.y() + speed);
            if (input.equals("KEY_RSHIFT"))
            {

              // fire a missile

              Vector3 deltaMis = delta.copy();
              deltaMis.y(5 * player.getScale());
              deltaMis.z(-5 * player.getScale());

              missilePos.add(inverse.mult(deltaMis));

              Entity missle = new Entity("laser", texturedLaser, new Vector3f(
                  0, 0, 0), 0, 0, 0, 0.3f);
              // missle.setPosition(player.position);
              missle.quadTranslate(missilePos);

              missle.orientation = player.orientation.copy();
              float pv = 5f;
              missle.vel = player.vel.copy().add(
                  inverse.mult(new Vector3(0, 0, pv)));

              renderList.add(missle);
            }

            Vector3 deltaCam = delta.copy();
            deltaCam.y(-2 * player.getScale());
            deltaCam.z(-9 * player.getScale());



              player.vel.add(inverse.mult(delta));
              cameraPos.add(inverse.mult(deltaCam));
              cameraPos.add(player.vel);
              // player.move();
              player.orientation = orientation.copy();
              camera.quadTranslate(cameraPos);
              camera.orientation = orientation.copy();

          }
        }
      }

      long time = System.currentTimeMillis();
      if (time - lastTime > 17)
      {
        lastTime = time;
        // renderList.addAll(objList);
        // renderList.addAll(missileList);
        for (Entity ent : renderList)
        {
          ent.move();
          for (Entity other : renderList)
          {
            if (ent == other)
            {
              continue;
            }
            if (BoxUtilities.collision(ent.getBox(), other.getBox()))
            {
              if (ent == player)
              {
                continue;
              }
              PhysicsUtilities.elasticCollision(ent, other);
            }
          }
        }
      }

      camera.position = player.position;

      // ///instead of rendering, build strings and send them to the client
      outputToClient = "";// clear the String
      for (Entity ent : renderList)// TODO do I need a for each thread here?
      {
        outputToClient += ent.toString() + ";";
      }
      outputToClient +=
          "Cam," + camera.position.x + "," + camera.position.y + ","
              + camera.position.z + "," + camera.orientation.x() + ","
              + camera.orientation.y() + "," + camera.orientation.z() + "," + camera.orientation.w() + "," + 0f + ";";

      // for (WalkerThread wt : myServer.threadList)
      for (int i = 0; i < myServer.threadList.size(); i++)
      {
        WalkerThread wt = myServer.threadList.get(i);
        wt.updateServerGameState(outputToClient);
      }

      // render each entity passed to the client
      // for (Entity ent : renderList)
      // {
      // renderer.processEntity(ent);
      // }
      //
      // renderer.processSkyBox(skyBoxEntity);
      // renderer.render(light, camera);
      // DisplayManager.updateDisplay();

      if (PRINT_FPS)
      {
        pu.updateFPS();
        System.out.println(pu.getFPS());
      }
      long endTime = System.currentTimeMillis();
      // System.out.println("execution time " + (endTime - startingTime));
    }// end of while(!display...)

    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();

  }// end of main

  private static List<Entity> parseGameStateString(String testInput,
      ModelMap modelMap)
  {

    List<Entity> renderList = new ArrayList<>();
    String[] sceneInfo = testInput.split(";");
    for (String object : sceneInfo)
    {
      String[] currentLine = object.split(",");
      String id;
      float x, y, z, xr, yr, zr, s;
      // translate all input data into appropriate entities;
      id = currentLine[0];
      x = Float.parseFloat(currentLine[1]);
      y = Float.parseFloat(currentLine[2]);
      z = Float.parseFloat(currentLine[3]);
      xr = Float.parseFloat(currentLine[4]);
      yr = Float.parseFloat(currentLine[5]);
      zr = Float.parseFloat(currentLine[6]);
      s = Float.parseFloat(currentLine[7]);

      if (object.startsWith("Play"))
      {

        player = new Entity("S002", modelMap.getTexturedModelList().get(id),
            new Vector3f(x, y, z), xr, yr, zr, s);

        renderList.add(player);

      }
      else
      {

        Entity tmp_Entity = new Entity("A001", modelMap.getTexturedModelList()
            .get(id), new Vector3f(x, y, z), xr, yr, zr, s);
        renderList.add(tmp_Entity);
      }
    }
    return renderList;
  }

  public static String getInput(int i)
  {
    // start with first element in walker thread, expand to multiplayer
    String input = myServer.threadList.get(i).getClientInput();
    loop++;
    return input;
  }
}
