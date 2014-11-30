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
  private static int loop=0;

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
    String inputFromClient=null;
    String outputToClient=null;

    if (PHYSICS_DEBUG)
    {
      //start server
      try
      {
        myServer=new WalkerServer();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      
      //wait for 1 connection to the server
//       while(myServer.threadList.size()==0)
//      {
//        //do nothing
//      }
      try
      {
        Thread.sleep(5000);
      } catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      //get first line of input from server
      //TODO unnecessary if server does all calc (it knows start location)
//      for(WalkerThread wt: myServer.threadList)
//      {
//        inputFromClient=wt.getClientInput();
//      }

//      if(testInput==null)
//      {
      inputFromClient = "A001,1,0,-20,0,0,0,1;" + "A002,-1,0,-20,0,0,0,0.5;" +
          "A002,-3,0,-20,0,0,0,0.5;" + "A002,-4,0,-20,0,0,0,0.5;"  +
          "A002,-5,0,-20,0,0,0,0.5;" + "A002,-4,2,-20,0,0,0,0.5;"  + "A002,-4,-2,-20,0,0,0,0.5;"
          + "A002,-4,-3,-20,0,0,0,0.5;";
//      }
    }

    else
    {
      inputFromClient = "S001,0,0,-20,0,0,0,0.01;" + "S002,0,15,-20,0,0,0,0.3;" + "A001,4,2,-3,0,0,0,1;" + "Cam,0,0,3,0,90,0,1";
    }

    List<Entity> renderList = new ArrayList<>();

    String[] sceneInfo = inputFromClient.split(";");
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

        Entity tmp_Entity = new Entity(modelMap.getTexturedModelList().get(id), new Vector3f(x, y, z), xr, yr, zr, s);
        renderList.add(tmp_Entity);
      }
    }

    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();

    /* Perform object movement as long as the window exists */
    //TODO change server to no display output, loop while threadlist is not empty
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
        Float scale = 0.001f;
        Entity Asteroid1 = renderList.get(1);
        Entity Asteroid2 = renderList.get(0);

        long time = System.currentTimeMillis();
        if (time - lastTime > 25)
        {
          for (Entity ent : renderList)
          {
            ent.move();
//            System.out.println(ent);
            for (Entity other : renderList)
            {
              if (BoxUtilities.collision(ent.getBox(), other.getBox()))
              {
                PhysicsUtilities.elasticCollision(200, ent.vel, 200,
                    other.vel);
              }
            }
          }
          lastTime = time;
        }
        
        //camera controls
        else
        {
          camera.move();
        }
                
        inputFromClient=getInput();
        //detecting keys should only happen on client side
        if(inputFromClient!=null)
        {
          if (inputFromClient.equals("KEY_LSHIFT") || inputFromClient.equals("KEY_RSHIFT"))
          {
            scale = 0.0001f;
          }
          if (inputFromClient.equals("KEY_RIGHT"))
          {
            Asteroid2.vel.x +=scale;
          }
          if (inputFromClient.equals("KEY_LEFT"))
          {
            Asteroid2.vel.x -=scale;
          }
          if (inputFromClient.equals("KEY_UP"))
          {
            Asteroid2.vel.y += scale;
          }
          if (inputFromClient.equals("KEY_DOWN"))
          {
            Asteroid2.vel.y -= scale;
  
          }
          // /
          if (inputFromClient.equals("KEY_D"))
          {
            Asteroid1.vel.x += scale;
          }
          if (inputFromClient.equals("KEY_A"))
          {
            Asteroid1.vel.x -= scale;
          }
          if (inputFromClient.equals("KEY_W"))
          {
            Asteroid1.vel.y += scale;
  
          }
          if (inputFromClient.equals("KEY_S"))
          {
            Asteroid1.vel.y -= scale;
          }
        }
        
      }
      
      /////instead of rendering, build strings and send them to the client
      outputToClient="";//clear the String
      for(Entity ent:renderList)
      {
        outputToClient.concat(ent.toString());
      }
      
      for(WalkerThread wt: myServer.threadList)
      {
//        wt.updateServerGameState(outputToClient);
      }      
      
			//render each entity passed to the client      
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

    }//end of while(!display...)
    
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();    
    
  }//end of main
  
  public static String getInput()
  {    
    //start with first element in walker thread, expand to multiplayer
    String input=myServer.threadList.get(0).getClientInput();
    loop++;
    return input;
  }
}
