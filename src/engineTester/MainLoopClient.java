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

  public final static boolean PRINT_FPS = false;
  public static WalkerClient myClient = null;

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
    String outputToServer = null;
    String inputFromServer = null;

    List<Entity> renderList = new ArrayList<>();
    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();

    //controls for physics testing with two asteroids
    //wasd control leftest asteroid, arrows control rightmost.
    //holding shift will slow down the shifting speed.
    String hostName = "Manticore";
    int socketVal = 4444;

    Socket mySocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    try
    {
      mySocket = new Socket(hostName, socketVal);
      out = new PrintWriter(mySocket.getOutputStream(), true);
      in = new BufferedReader(
          new InputStreamReader(mySocket.getInputStream()));
    }
    catch (UnknownHostException e)
    {
      System.err.println("Don't know about host: " + hostName);
      System.exit(1);
    }
    catch (IOException e)
    {
      System.err
          .println("Couldn't get I/O for the connection to: " + hostName);
      System.exit(1);
    }

    /* Perform object movement as long as the window exists */
    while (!Display.isCloseRequested())
    {
      String[] sceneInfo = myClient.getInputFromServer().split(";");
      renderList.clear();
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
        //      System.out.println(object.charAt(0));
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

          Entity tmp_Entity = new Entity(id, modelMap.getTexturedModelList().get(id),
              new Vector3f(x, y, z), xr, yr, zr, s);
          renderList.add(tmp_Entity);
        }
      }

      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }

      String toSend = "";
      if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard
          .isKeyDown(Keyboard.KEY_RSHIFT))
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
      out.println(toSend);
      DisplayManager.updateDisplay();
      try
      {
        Thread.sleep(25);
      }
      catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

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

      //send update string to server
      outputToServer = "";//clear send string
      for (
          Entity ent
          : renderList)

      {
        outputToServer.concat(ent.toString());
      }

      renderer.cleanUp();
      loader.cleanUp();
      DisplayManager.closeDisplay();
    }
  }

}
