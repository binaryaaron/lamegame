/*** 
 * Thanks to youtube user ThinMatrix
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI
 */
package engineTester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;
import gameObjects.Asteroid;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import physics.PhysicsUtilities;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import server.WalkerClient;
import skyBox.SkyBox;
import textures.ModelTexture;
import toolbox.PerformanceUtilities;
import entities.Camera;
import entities.Entity;
import entities.Light;
import world.BoxUtilities;

import javax.swing.*;

public class MainLoopClient
{

  public final boolean PRINT_FPS = false;
  public WalkerClient myClient = null;

  public MainLoopClient(String[] args)
  {
    Entity player = null;
    DisplayManager.createDisplay();
    Loader loader = new Loader();
    ModelMap modelMap = new ModelMap();

    // create skybox, this is not an entity so it is seperate
    RawModel skyBox = OBJLoader.loadObjModel("SkyDome", loader, true);
    ModelTexture skyTexture = new ModelTexture(loader.loadTexture("RedSky"));
    TexturedModel texturedSkyBox = new TexturedModel("SkyBox2",skyBox, skyTexture);
    SkyBox skyBoxEntity = new SkyBox(loader, texturedSkyBox);

    // create lights and camera for the player. camera position should be set in
    // parsing routine
    Light light = new Light(new Vector3f(10f, 5f, 2000f), new Vector3f(1.0f,
        1.0f, 1.0f));
    Camera camera = new Camera();
    MasterRenderer renderer = new MasterRenderer(camera);

    PerformanceUtilities pu = new PerformanceUtilities();

    // this will be information read from a socket
    // format: ID,x,y,z,rotx,rot,y,rotz,scale
    String outputToServer = null;
    String inputFromServer = null;

    List<Entity> renderList = new ArrayList<>();
    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();

    // controls for physics testing with two asteroids
    // wasd control leftest asteroid, arrows control rightmost.
    // holding shift will slow down the shifting speed.
    String hostName = "Manticore";
    int socketVal = 4444;

    try
    {
      myClient = new WalkerClient(args);
    }
    catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to: " + hostName);
      System.exit(1);
    }
    /* Perform object movement as long as the window exists */
    while (!Display.isCloseRequested())
    {
      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }

      // Get render/objects from server
      getServerState(renderList, camera, modelMap);

      // Limit keyboard sends
      long time = System.currentTimeMillis();
      if (time - lastTime > 17)
      {
        lastTime = time;
        sendKeyBoard();
      }

      // Render entities from server
      for (Entity ent : renderList)
      {
        renderer.processEntity(ent);
      }

      // Process rendering
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

  public void getServerState(List<Entity> renderList, Camera camera, ModelMap modelMap)
  {
    String[] sceneInfo = myClient.getInputFromServer().split(";");
    renderList.clear();
    for (String object : sceneInfo)
    {
      if (!object.equals(""))
      {
        String[] currentLine = object.split(",");
        String id;
        float x, y, z, xr, yr, zr, s, w;
        // translate all input data into appropriate entities;
        x = Float.parseFloat(currentLine[1]);
        y = Float.parseFloat(currentLine[2]);
        z = Float.parseFloat(currentLine[3]);
        xr = Float.parseFloat(currentLine[4]);
        yr = Float.parseFloat(currentLine[5]);
        zr = Float.parseFloat(currentLine[6]);
        w = Float.parseFloat(currentLine[7]);
        s = Float.parseFloat(currentLine[8]);
        // System.out.println(object.charAt(0));
        id = currentLine[0];

        Entity tmp_Entity = new Entity(id, modelMap.getTexturedModelList()
            .get(id), new Vector3f(x, y, z), xr, yr, zr, s);
        tmp_Entity.orientation.w(w);
        if (object.startsWith("S002"))
        {
          Quaternion inverse = tmp_Entity.orientation.copy().inverse();
          Vector3 deltaCam = new Vector3(0,-2 * tmp_Entity.getScale(),-9 * tmp_Entity.getScale());
          deltaCam = inverse.mult(deltaCam);
          camera.setPosition(new Vector3f(x, y, z));
          camera.move(deltaCam);
          camera.orientation = tmp_Entity.orientation.copy();
        }
        renderList.add(tmp_Entity);
      }
    }
  }

  public void sendKeyBoard()
  {
    String toSend = ";";
    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
    {
      toSend += "KEY_LSHIFT;";
    }

    if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
    {
      toSend += "KEY_RIGHT;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
    {
      toSend += "KEY_LEFT;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_UP))
    {
      toSend += "KEY_UP;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
    {
      toSend += "KEY_DOWN;";
    }
    // /
    if (Keyboard.isKeyDown(Keyboard.KEY_D))
    {
      toSend += "KEY_D;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_A))
    {
      toSend += "KEY_A;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_W))
    {
      toSend += "KEY_W;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_S))
    {
      toSend += "KEY_S;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_Q))
    {
      toSend += "KEY_Q;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_E))
    {
      toSend += "KEY_E;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_B))
    {
      toSend += "KEY_B;";
    }
    myClient.sendToServer(toSend);
  }

  public static void main(String[] args)
  {
    new MainLoopClient(args);
  }
}
