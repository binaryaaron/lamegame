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
  private final static boolean PHYSICS_DEBUG = true;
  public static WalkerClient myClient=null;

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
    String outputToServer=null;
    String inputFromServer=null;

    if (PHYSICS_DEBUG)
    {
//      //start new client
//      try
//      {
//        myClient=new WalkerClient(args);
//      } catch (IOException e)
//      {
//        e.printStackTrace();
//      }
//      
//      //send first string from client to server      
//      outputToServer = "A001,1,0,-20,0,0,0,1;" + "A002,-1,0,-20,0,0,0,0.5;" +
//          "A002,-3,0,-20,0,0,0,0.5;" + "A002,-4,0,-20,0,0,0,0.5;"  +
//          "A002,-5,0,-20,0,0,0,0.5;" + "A002,-4,2,-20,0,0,0,0.5;"  + "A002,-4,-2,-20,0,0,0,0.5;"
//          + "A002,-4,-3,-20,0,0,0,0.5;";
//      myClient.firstSend(outputToServer);
//      try//wait 1 sec for response from server
//      {
//        Thread.sleep(1000);
//      } catch (InterruptedException e)
//      {
//        e.printStackTrace();
//      }
//      inputFromServer=myClient.updateClientGameState(outputToServer);//maybe this should wait for a response
    }

    else
    {
//      outputToServer = "S001,0,0,-20,0,0,0,0.01;" + "S002,0,15,-20,0,0,0,0.3;" + "A001,4,2,-3,0,0,0,1;" + "Cam,0,0,3,0,90,0,1";
    }

    List<Entity> renderList = new ArrayList<>();
//    String[] sceneInfo = inputFromServer.split(";");
//    for (String object : sceneInfo)
//    {
//      String[] currentLine = object.split(",");
//      String id;
//      float x, y, z, xr, yr, zr, s;
//      // translate all input data into appropriate entities;
//      x = Float.parseFloat(currentLine[1]);
//      y = Float.parseFloat(currentLine[2]);
//      z = Float.parseFloat(currentLine[3]);
//      xr = Float.parseFloat(currentLine[4]);
//      yr = Float.parseFloat(currentLine[5]);
//      zr = Float.parseFloat(currentLine[6]);
//      s = Float.parseFloat(currentLine[7]);
//      System.out.println(object.charAt(0));
//      if (object.startsWith("Cam"))
//      {
//        camera.setPosition(new Vector3f(x, y, z));
//        camera.setPitch(xr);
//        camera.setYaw(yr);
//        camera.setRoll(zr);
//      }
//
//      else
//      {
//        id = currentLine[0];
//        Entity tmp_Entity = new Entity(modelMap.getTexturedModelList().get(id), new Vector3f(x, y, z), xr, yr, zr, s);
//        renderList.add(tmp_Entity);
//      }
//    }

    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();

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
        Float scale = 0.001f;
//        Entity Asteroid1 = renderList.get(1);
//        Entity Asteroid2 = renderList.get(0);

        /*update move happens on server, not client
        long time = System.currentTimeMillis();
        if (time - lastTime > 25)
        {
          for (Entity ent : renderList)
          {
            ent.move();
            System.out.println(ent);
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
        //*/
        String hostName="Manticore";
        int socketVal=4444;
        
        Socket mySocket=null;
        PrintWriter out=null;
        BufferedReader in=null;
        
        try
        {
          mySocket=new Socket(hostName, socketVal);
          out=new PrintWriter(mySocket.getOutputStream(),true);
          in=new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        } catch (UnknownHostException e)
        {
          System.err.println("Don't know about host: "+hostName);
          System.exit(1);
        } catch (IOException e)
        {
          System.err.println("Couldn't get I/O for the connection to: "+hostName);
          System.exit(1);
        }

        while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
          if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
          {
            out.println("KEY_LSHIFT");
          }
      
          if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
          {
            System.out.println("KEY_RIGHT");
            out.println("KEY_RIGHT");
          }
          if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
          {
            out.println("KEY_LEFT");
          }
          if (Keyboard.isKeyDown(Keyboard.KEY_UP))
          {
            out.println("KEY_UP");
          }
          if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
          {
            out.println("KEY_DOWN");
          }
          // /
          if (Keyboard.isKeyDown(Keyboard.KEY_D))
          {
            out.println("KEY_D");
          }
          if (Keyboard.isKeyDown(Keyboard.KEY_A))
          {
            out.println("KEY_A");
          }
          if (Keyboard.isKeyDown(Keyboard.KEY_W))
          {
            out.println("KEY_W");
          }
          if (Keyboard.isKeyDown(Keyboard.KEY_S))
          {
            out.println("KEY_S");
          }
          DisplayManager.updateDisplay();
          try
          {
            Thread.sleep(25);
          } catch (InterruptedException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }

      }

      /*camera move calc happens on server
      //camera controls
      else
      {
        camera.move();
      }
      */

			//render each entity
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
      outputToServer="";//clear send string
      for(Entity ent : renderList)
      {
        outputToServer.concat(ent.toString());
      }
      myClient.updateClientGameState(outputToServer);
    }

    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
  }

}
