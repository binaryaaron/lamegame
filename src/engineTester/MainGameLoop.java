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
	  private final static boolean PHYSICS_DEBUG = true;
	  private final static boolean ASTEROIDS =false;
	  private final static boolean SERVER_TEST=false;
	  private static int nAsteroids=50;
	  static Entity player;
  public static void main(String[] args) throws IOException
  {
	//String ipAddress=args[0];
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

    if (PHYSICS_DEBUG)
    {
      testInput = "A001,1,0,-20,0,0,0,1;" + "A002,-1,0,-20,0,0,0,0.5";

    }else if(SERVER_TEST)
    {
        WalkerClient wc=null;
        try
        {
          wc=new WalkerClient(args);

        } catch (IOException e)
        {
          e.printStackTrace();
        }
        testInput=wc.updateClientGameState("Play,0,0,4,0,0,0,0.3;");

      }
      else if(ASTEROIDS){
        Random rand=new Random();
        testInput="Play,0,0,4,0,0,0,0.3;";
        for(int i=0;i<nAsteroids;i++){
          int a=rand.nextInt(2)+1;
          int x=rand.nextInt(30)-15;
          int y=rand.nextInt(30)-15;
          int z=rand.nextInt(30)-15;
          float s=rand.nextFloat()*2;
        testInput=testInput.concat("A00"+a+","+x+","+y+","+z+",0,0,0,"+s+";");
        }
        
        
      }

    else
    {
      testInput = "S001,0,0,-20,0,0,0,0.01;" + "S002,0,15,-20,0,0,0,0.3;"
          + "A001,4,2,-3,0,0,0,1;" + "Cam,0,0,3,0,90,0,1";
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

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();

    /* Perform object movement as long as the window exists */
    while (!Display.isCloseRequested())
    {
    	 /* Perform object movement as long as the window exists */
    	WalkerClient wc=null;
    if(SERVER_TEST)    wc =new WalkerClient(args);
      
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
          Asteroid1.move();
          Asteroid2.move();
          lastTime = time;
          if (BoxUtilities.collision(Asteroid1.getBox(), Asteroid2.getBox()))
          {
            PhysicsUtilities.elasticCollision(50, Asteroid1.vel, 200,
                Asteroid2.vel);
          }
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard
            .isKeyDown(Keyboard.KEY_RSHIFT))
        {
          scale = 0.0001f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
          Asteroid2.vel.x +=scale;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
          Asteroid2.vel.x -=scale;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
        {
          Asteroid2.vel.y += scale;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
          Asteroid2.vel.y -= scale;

        }
        // /
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
          Asteroid1.vel.x += scale;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
          Asteroid1.vel.x -= scale;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
          Asteroid1.vel.y += scale;

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
          Asteroid1.vel.y -= scale;
        }

      }

      if(player!=null){
      Vector3f pos=player.getPosition();
      if(SERVER_TEST) testInput=wc.updateClientGameState("Play,"+pos.x+","+pos.y+","+pos.z+","+player.getRotX()+","+player.getRotY()+","+player.getRotZ()+","+0.3);
      renderList=parseGameStateString(testInput,modelMap);
      
      
      float yOff = 1;
      float zOff = 3;

      camxShift = ((float) Math.sin(player.getRotY() * 3.14 / 180) * zOff);// *(float)
                                                                           // Math.sin(player.getRotZ()*3.14/180);
      camyShift = ((float) (Math.cos(player.getRotX() * 3.14 / 180)) * yOff)
          - (float) (Math.sin(player.getRotX() * 3.14 / 180)) * zOff;// *(float)
                                                                     // Math.cos(player.getRotX()*3.14/180);
      camzShift = -((float) Math.sin(player.getRotX() * 3.14 / 180) * yOff)
          + ((float) Math.cos(player.getRotX() * 3.14 / 180) * zOff);// *(float)
                                                                     // Math.cos(player.getRotX()*3.14/180);;

      camera.setPosition(player.getPosition().x + camxShift,
          player.getPosition().y + camyShift, player.getPosition().z
              + camzShift);
      camera.setRotation(-player.getRotX(), -player.getRotY(),
          -player.getRotZ());
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
    
    
    
    private static List<Entity> parseGameStateString(String testInput,ModelMap modelMap){
  	  
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

  	        player = new Entity(modelMap.getTexturedModelList().get(id),
  	            new Vector3f(x, y, z), xr, yr, zr, s);

  	      }
  	      else
  	      {

  	        Entity tmp_Entity = new Entity(modelMap.getTexturedModelList().get(id),
  	            new Vector3f(x, y, z), xr, yr, zr, s);
  	        renderList.add(tmp_Entity);
  	      }
  	    }
  	    return renderList;
    }
    
}
