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
import entities.Laser;
import entities.Light;
import world.BoxUtilities;

import javax.swing.*;

public class MainLoopClient
{
  public final boolean HUD_DEBUG =true;
  public final boolean PRINT_FPS = false;
  public WalkerClient myClient = null;
  private float speed;
  public MainLoopClient(String[] args)
  {
    Entity player = null;
    DisplayManager.createDisplay();
    Loader loader = new Loader();
    ModelMap modelMap = new ModelMap();

    // create skybox, this is not an entity so it is seperate
    RawModel skyBox = OBJLoader.loadObjModel("SkyDome", loader, true);
    ModelTexture skyTexture = new ModelTexture(loader.loadTexture("RedSky"));
    TexturedModel texturedSkyBox = new TexturedModel("SkyBox2", skyBox,
        skyTexture);
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
    List<Laser> lasers = new ArrayList<>();
    
    List<Entity> hudRenderList = new ArrayList<>();

    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }

    //Timer variables
    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();
    long hudDelay = 100;
    long hudStart = 0;

    //Hud Objects
    Entity hud1 = new Entity("H001",modelMap.getTexturedModelList().get(
        "H001"), new Vector3f(0.7f,0.37f,1f),0f,0f,0f, 0.05f);
    Entity hud2 = new Entity("H002",modelMap.getTexturedModelList().get(
        "H002"), new Vector3f(-0.0f,0.07f,1f),0f,0f,0f, 0.05f);
    Entity hud3 = new Entity("H003",modelMap.getTexturedModelList().get(
        "H003"), new Vector3f(0.05f,0.3f,0.8f),0f,0f,0f, 0.05f);
    hudRenderList.add(hud1);
    hudRenderList.add(hud2);
    hudRenderList.add(hud3);

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
      getServerState(renderList,lasers, camera, modelMap);

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

      //render HUD
      if(HUD_DEBUG&&hudDelay+hudStart <System.currentTimeMillis())
      {

        hudStart = System.currentTimeMillis();
        hudRenderList.get(2).setModel(modelMap.setScoreText((""+System.currentTimeMillis()+"  ")));
        hudRenderList.get(1).setModel(modelMap.setHealthText((""+(int)(Math.random()*100))+"% "));

        hudRenderList.get(0).setModel(modelMap.setSpeedText(""+speed));
      }
      for (Entity ent : hudRenderList)
      {
        renderer.processHudEntity(ent);
        // System.out.println(ent.getModel().getRawModel().getVertexCount());
      }

      for (Laser ent : lasers)
      {
        renderer.processLaser(ent);
        // System.out.println(ent.getModel().getRawModel().getVertexCount());
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

  public void getServerState(List<Entity> renderList,List<Laser> lasers, Camera camera,
      ModelMap modelMap)
  {
    String[] sceneInfo = myClient.getInputFromServer().split(";");
    renderList.clear();
    lasers.clear();
    for (String object : sceneInfo)
    {
      if (!object.equals(""))
      {
    	  if(object.startsWith("l")){
    		  String[] currentLine = object.split(",");
    	        String id;
    	        float x, y, z, xr, yr, zr, s, w;
    	        id = currentLine[0];
    	        x = Float.parseFloat(currentLine[1]);
    	        y = Float.parseFloat(currentLine[2]);
    	        z = Float.parseFloat(currentLine[3]);
    	        xr = Float.parseFloat(currentLine[4]);
    	        yr = Float.parseFloat(currentLine[5]);
    	        zr = Float.parseFloat(currentLine[6]);
    	        w = Float.parseFloat(currentLine[7]);
    	        s = Float.parseFloat(currentLine[8]);
          	Laser tmp_laser=new Laser(id, modelMap.getTexturedModelList().get(id),
                    new Vector3f(x, y, z), xr, yr, zr, s);
          	 tmp_laser.orientation.w(w);
          	lasers.add(tmp_laser);
          }
    	  else{
        Entity tmp_Entity;
        String[] currentLine = object.split(",");
        String id;
        float x, y, z, xr, yr, zr, s, w;
        int playerID = -1;
        // translate all input data into appropriate entities;
        id = currentLine[0];
        x = Float.parseFloat(currentLine[1]);
        y = Float.parseFloat(currentLine[2]);
        z = Float.parseFloat(currentLine[3]);
        xr = Float.parseFloat(currentLine[4]);
        yr = Float.parseFloat(currentLine[5]);
        zr = Float.parseFloat(currentLine[6]);
        w = Float.parseFloat(currentLine[7]);
        s = Float.parseFloat(currentLine[8]);
        if (object.startsWith("S"))
        {
          playerID = Integer.parseInt(currentLine[9]);
          tmp_Entity = new Entity(id, modelMap.getTexturedModelList().get(id),
              new Vector3f(x, y, z), xr, yr, zr, s, playerID);
        }
       
        else
        {
          tmp_Entity = new Entity(id, modelMap.getTexturedModelList().get(id),
              new Vector3f(x, y, z), xr, yr, zr, s);
        }
        tmp_Entity.orientation.w(w);
        if (object.startsWith("S")&&playerID==myClient.ID)// &&playerID==myClient.ID
        {
          speed = Float.parseFloat(currentLine[11]);
          Quaternion inverse = tmp_Entity.orientation.copy().inverse();
          Vector3 deltaCam = new Vector3(0, -2 * tmp_Entity.getScale(), -9
              * tmp_Entity.getScale());
          deltaCam = inverse.mult(deltaCam);
          camera.setPosition(new Vector3f(x, y, z));
          camera.move(deltaCam);
          camera.orientation = tmp_Entity.orientation.copy();
        }
        renderList.add(tmp_Entity);
      }
      }}
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
    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
    {
      toSend += "KEY_LCONTROL;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
    {
      toSend += "KEY_RSHIFT;";
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
    {
      toSend += "KEY_SPACE;";
    }
    myClient.sendToServer(toSend);
  }

  public static void main(String[] args)
  {
    new MainLoopClient(args);
  }
}
