/*** 
 * Thanks to youtube user ThinMatrix
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI
 */
package engineTester;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import gameObjects.Asteroid;
import gameObjects.Globals;
import models.RawModel;
import models.TexturedModel;
import server.WalkerClient;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.ra4king.opengl.util.Utils;
import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector;
import com.ra4king.opengl.util.math.Vector3;

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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

//import javafx.scene.media.*;


import java.net.*;

import javax.swing.*;

public class MainGameLoop
{

  private static final int BUFFER_SIZE = 128000;
  private static File soundFile;
  private static AudioInputStream audioStream;
  private static AudioFormat audioFormat;
  private static SourceDataLine sourceLine;
  public final static boolean PRINT_FPS = false;
  public final static boolean MUSIC = false;
  private final static boolean PHYSICS_DEBUG = false;
  private final static boolean ASTEROIDS = true;
  private final static boolean SERVER_TEST = false;
  private static int nAsteroids = 200;
  static Entity player;

  public static void main(String[] args) throws IOException
  {
    DisplayManager.createDisplay();
    Loader loader = new Loader();
    ModelMap modelMap = new ModelMap();

    Camera camera = new Camera();
    camera.followObj = player;

    // create skybox, this is not an entity so it is seperate
    RawModel skyBox = OBJLoader.loadObjModel("SkyDome", loader, true);
    ModelTexture skyTexture = new ModelTexture(loader.loadTexture("RedSky"));
    TexturedModel texturedSkyBox = new TexturedModel("SkyBox2",skyBox, skyTexture);
    SkyBox skyBoxEntity = new SkyBox(loader, texturedSkyBox);
    //
    RawModel laser = OBJLoader.loadObjModel("missle", loader, true);
    ModelTexture laserTexture = new ModelTexture(loader.loadTexture("Missle"));
    TexturedModel texturedLaser = new TexturedModel("laser", laser, laserTexture);
    Entity laserEntity = new Entity("laser", texturedLaser, new Vector3f(0, 0, 0), 0, 0,
        0, 2f);

    modelMap.getTexturedModelList()
        .put("Play", modelMap.getTexturedModelList().get("S002"));
    // create lights and camera for the player. camera position should be set in
    // parsing routine
    Light light = new Light(new Vector3f(10f, 5f, 2000f),
        new Vector3f(1f, 0.8f, 0.8f));

    MasterRenderer renderer = new MasterRenderer(camera);

    PerformanceUtilities pu = new PerformanceUtilities();

    // this will be information read from a socket
    // format: ID,x,y,z,rotx,rot,roty,rotz,scale
    String testInput;

    if (MUSIC)
    {
      //  new javafx.embed.swing.JFXPanel();
      String bip = "res/explosion-04.wav";

      playSound(bip);

      File file = new File(bip);
      System.out.println(file.toURI().toString());
      //	    Media hit = new Media(file.toURI().toString());
      //	    MediaPlayer mediaPlayer = new MediaPlayer(hit);
      //	    mediaPlayer.play();
    }

    if (PHYSICS_DEBUG)
    {
      testInput = "A001,1,0,-20,0,0,0,1;" + "A002,-1,0,-20,0,0,0,0.5;" +
          "A002,-3,0,-20,0,0,0,0.5;" + "A002,-4,0,-20,0,0,0,0.5;" +
          "A002,-5,0,-20,0,0,0,0.5;" + "A002,-4,2,-20,0,0,0,0.5;"
          + "A002,-4,-2,-20,0,0,0,0.5;" + "A002,-4,-3,-20,0,0,0,0.5;";

    }
    else if (SERVER_TEST)
    {
      WalkerClient wc = null;
      try
      {
        wc = new WalkerClient(args);

      }
      catch (IOException e)
      {
        e.printStackTrace();
      }

    }
    else if (ASTEROIDS)
    {
      Random rand = new Random();
      testInput = "Play,1000,0,4,0,0.66,0,0.03;Plan,0,0,0,0,0,0,100;";
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
        testInput = testInput.concat(
            "A00" + a + "," + x + "," + y + "," + z + ",0,0,0," + s + ";");
      }

    }

    else
    {
      testInput = "S001,0,0,-20,0,0,0,0.01;" + "S002,0,15,-20,0,0,0,0.3;"
          + "A001,4,2,-3,0,0,0,1;" + "Cam,0,0,3,0,90,0,1";
    }

    List<Entity> renderList = parseGameStateString(testInput, modelMap);
    List<Entity> missileList = new ArrayList<>();
   // List<Entity> renderList=new ArrayList<>();
       if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();
    int timeTilExplode = 50;
    /* Perform object movement as long as the window exists */
    WalkerClient wc = null;
    if (SERVER_TEST) wc = new WalkerClient(args);

    Vector3 velocity = new Vector3();

    while (!Display.isCloseRequested())
    {
       /* Perform object movement as long as the window exists */

      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }

      // Camera to follow player.

      Vector3f camShiftZ = null;
      Vector3f camShiftY = null;

      // controls for physics testing with two asteroids
      // wasd control leftest asteroid, arrows control rightmost.
      // holding shift will slow down the shifting speed.
      //controls for physics testing with two asteroids
      //wasd control leftest asteroid, arrows control rightmost.
      //holding shift will slow down the shifting speed.
      float rotateShift = 0.0f;
      if (PHYSICS_DEBUG)
      {

        Quaternion orientation = camera.orientation;
        Vector3 cameraPos = new Vector3(camera.position.x, camera.position.y,
            camera.position.z);

        //in your update cod
        float speed = 0.1f;//(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) | Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 20 : 50) * deltaTime / (float)1e9;
        float rotSpeed = 1.5f * speed;

        //pitch
        int dy = Mouse.getDY();
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
          orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(1, 0, 0))
              .mult(orientation);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
          orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(1, 0, 0))
              .mult(orientation);
        }
        //yaw
        int dx = Mouse.getDX();
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
          orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(0, 1, 0))
              .mult(orientation);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
          orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(0, 1, 0))
              .mult(orientation);
        }
        //roll
        if (Keyboard.isKeyDown(Keyboard.KEY_E))
          orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(0, 0, 1))
              .mult(orientation);
        if (Keyboard.isKeyDown(Keyboard.KEY_Q))
          orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(0, 0, 1))
              .mult(orientation);

        orientation.normalize();

        Quaternion inverse = orientation.copy().inverse();

        Vector3 delta = new Vector3();

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) delta.z(-speed);
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) delta.z(delta.z() + speed);

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) delta.x(-speed);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) delta.x(delta.x() + speed);

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) delta.y(-speed);
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
          delta.y(delta.y() + speed);

        Vector3 deltaCam = delta.copy();

        cameraPos.add(inverse.mult(deltaCam));

        camera.quadTranslate(cameraPos);
        camera.orientation = orientation.copy();

        Float scale = 0.001f;
        Entity Asteroid1 = renderList.get(1);
        Entity Asteroid2 = renderList.get(0);

        long time = System.currentTimeMillis();
        if (time - lastTime > 25)
        {
          timeTilExplode--;
          if (timeTilExplode == 0)
          {
            Entity ent = renderList.get(0);
            renderList.remove(ent);
           // renderList.addAll(explodeEntity(ent));
            timeTilExplode = 50;
          }
         // renderList.addAll(objList);
        //  renderList.addAll(missileList);
          for (Entity ent : renderList)
          {
            ent.move();
            for (Entity other : renderList)
            {
              if (BoxUtilities.collision(ent.getBox(), other.getBox()))
              {
                PhysicsUtilities.elasticCollision(ent,other);
              }
            }
          }
          lastTime = time;
        }


      }

      else
      {

        Float scale = 0.1f;

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard
            .isKeyDown(Keyboard.KEY_RSHIFT))
        {
          scale = 0.01f;
        }

        Quaternion orientation = player.orientation;
        Vector3 position = new Vector3(player.position.x, player.position.y,
            player.position.z);
        Vector3 cameraPos = position.copy();
        Vector3 missilePos = position.copy();
        
        //in your update cod
        float speed = 0.02f;//(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) | Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 20 : 50) * deltaTime / (float)1e9;
        float rotSpeed = 0.5f;

        //pitch
        int dy = Mouse.getDY();
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        {
          orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(1, 0, 0))
              .mult(orientation);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        {
          orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(1, 0, 0))
              .mult(orientation);
        }
        //yaw
        int dx = Mouse.getDX();
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        {
          orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(0, 1, 0))
              .mult(orientation);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        {
          orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(0, 1, 0))
              .mult(orientation);
        }
        //roll
        if (Keyboard.isKeyDown(Keyboard.KEY_E))
          orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(0, 0, 1))
              .mult(orientation);
        if (Keyboard.isKeyDown(Keyboard.KEY_Q))
          orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(0, 0, 1))
              .mult(orientation);

        orientation.normalize();

        Quaternion inverse = orientation.copy().inverse();

        Vector3 delta = new Vector3();

        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) delta.z(-speed);
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) delta.z(delta.z() + speed);

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) delta.x(-speed);
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) delta.x(delta.x() + speed);

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) delta.y(-speed);
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
          delta.y(delta.y() + speed);
        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
        {
        	
        	//fire a missile
        	
            Vector3 deltaMis = delta.copy();
            deltaMis.y(5 * player.getScale());
            deltaMis.z(-5 * player.getScale());
        	
            missilePos.add(inverse.mult(deltaMis));
         
        	
        	Entity missle = new Entity("laser", texturedLaser, new Vector3f(0, 0, 0), 0, 0,
        	        0, 0.3f);
        	//missle.setPosition(player.position);
            missle.quadTranslate(missilePos);

        	missle.orientation = player.orientation.copy();
          float pv = 5f;
          missle.vel =  player.vel.copy().add(inverse.mult(new Vector3(0, 0, pv)));
          
          missileList.add(missle);
          renderList.add(missle);
          
          if(missileList.size()>200){
            renderList.remove(missileList.get(0));
            missileList.remove(0);

          }
        }
        Vector3 deltaCam = delta.copy();
        deltaCam.y(-2 * player.getScale());
        deltaCam.z(-9 * player.getScale());

        long time = System.currentTimeMillis();
        if (time - lastTime > 25)
        {
         //   renderList.addAll(objList);
         //   renderList.addAll(missileList);
          
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
                  System.out.println("time: " + time);
                  System.out.println("Should be a collision here!");
                }
                System.out.println("Elastic collision pre vel " + ent.vel);
                PhysicsUtilities.elasticCollision(ent, other);
                System.out.println("Elastic collision post vel " + ent.vel);
              }
            }
          }
          lastTime = time;
          player.vel.add(inverse.mult(delta));
          cameraPos.add(inverse.mult(deltaCam));
          cameraPos.add(player.vel);
          laserEntity.move();
          //player.move();
          player.orientation = orientation.copy();
          camera.quadTranslate(cameraPos);
          camera.orientation = orientation.copy();
        }

        //Mouse.setCursorPosition(50,50);

        //Utils.angleAxisDeg

        // System.out.println(player.getRotX()+";"+player.getRotY()+";"+player.getRotZ());
        // System.out.println(Math.cos(player.getRotX())+";"+Math.cos(player.getRotY())+";"+Math.cos(player.getRotZ()));
        // System.out.println(Math.cos(player.getRotX()*(3.14/180))+";"+Math.cos(player.getRotY()*(3.14/180))+";"+Math.cos(player.getRotZ()*(3.14/180)));

        //        float sinx = (float) Math.sin(player.getRotX() * 3.14 / 180) * scale;
        //        float cosx = (float) Math.cos(player.getRotX() * (3.14 / 180)) * scale;
        //        float siny = (float) Math.sin(player.getRotY() * 3.14 / 180) * scale;
        //        float cosy = (float) Math.cos(player.getRotY() * (3.14 / 180)) * scale;
        //        float sinz = (float) Math.sin(player.getRotZ() * 3.14 / 180) * scale;
        //        float cosz = (float) Math.cos(player.getRotZ() * (3.14 / 180)) * scale;
        //
        //        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
        //        {
        //          player.translate(-siny * cosz*3, sinx * cosz*3, -cosy * cosx*3);
        //        }
        //        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        //        {
        //          player.translate(siny * cosz, -sinx * cosz, cosy * cosx);
        //        }
        //        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        //        {
        //          player.translate(cosy * cosz, sinz * cosx, -siny * cosx);
        //        }
        //        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        //        {
        //          player.translate(-cosy * cosz, sinz * cosx, siny * cosx);
        //        }
        //
        //        // /
        //        if (Keyboard.isKeyDown(Keyboard.KEY_W))
        //        {
        //          player.rotateX(scale);
        //        }
        //        if (Keyboard.isKeyDown(Keyboard.KEY_S))
        //        {
        //        	player.rotateX(-scale);
        //        }
        //        if (Keyboard.isKeyDown(Keyboard.KEY_A))
        //        {
        //
        //        	 player.rotateY(scale);
        //        	// player.rotate(0.0f, -scale, 0.0f);
        //        }
        //        if (Keyboard.isKeyDown(Keyboard.KEY_D))
        //        {
        //        	 player.rotateY(-scale);
        //        //  player.rotate(0.0f, scale, 0.0f);
        //        }
        //        if (Keyboard.isKeyDown(Keyboard.KEY_Q))
        //        {
        //        	 player.rotateZ(scale);
        //        //  player.rotate(0.0f, 0f, scale);
        //        }
        //        if (Keyboard.isKeyDown(Keyboard.KEY_E))
        //        {
        //        	 player.rotateZ(-scale);
        //         // player.rotate(0.0f, 0f, -scale);
        //        }
      }


      float yOff = 1;
      float zOff = 3;

  
      
			/* render each entity passed to the client */
    //  renderList.addAll(objList);
    //  renderList.addAll(missileList);
      for (Entity ent : renderList)
      {
        renderer.processEntity(ent);

      }
      camera.followObj = player;

      // renderer.processSkyBox(ringEntity);
      renderer.processSkyBox(skyBoxEntity);
      renderer.render(light, camera);

      DisplayManager.updateDisplay();
      if (PRINT_FPS)
      {
        pu.updateFPS();

        System.out.println(pu.getFPS());
      }

    }
renderList.clear();
    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
  }

  public static Quaternion angleAxisDeg(float angle, Vector3 vec)
  {
    return new Quaternion((float) Math.toRadians(angle), vec);
  }

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

        player = new Entity("S002",modelMap.getTexturedModelList().get(id),
            new Vector3f(x, y, z), xr, yr, zr, s);

        renderList.add(player);

      }
      else
      {

        Entity tmp_Entity = new Entity("A001", modelMap.getTexturedModelList().get(id),
            new Vector3f(x, y, z), xr, yr, zr, s);
        renderList.add(tmp_Entity);
      }
    }
    return renderList;
  }

  public static void playSound(String filename)
  {

    String strFilename = filename;

    try
    {
      soundFile = new File(strFilename);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }

    try
    {
      audioStream = AudioSystem.getAudioInputStream(soundFile);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }

    audioFormat = audioStream.getFormat();

    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
    try
    {
      sourceLine = (SourceDataLine) AudioSystem.getLine(info);
      sourceLine.open(audioFormat);
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
      System.exit(1);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }

    sourceLine.start();

    int nBytesRead = 0;
    byte[] abData = new byte[BUFFER_SIZE];
    while (nBytesRead != -1)
    {
      try
      {
        nBytesRead = audioStream.read(abData, 0, abData.length);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      if (nBytesRead >= 0)
      {
        @SuppressWarnings("unused") int nBytesWritten = sourceLine
            .write(abData, 0, nBytesRead);
      }
    }

    sourceLine.drain();
    sourceLine.close();
  }

/*  public static List<Entity> explodeEntity(Entity ent)
  {
    List<Entity> entList = new LinkedList<>();
    float velScale = 0.01f;
    Entity ent1 = new Entity(ent);
    ent1.translate(ent.getHalfSize() * 0.5f, ent.getHalfSize() * 0.5f,
        0.5f * ent.getHalfSize() * Globals.RAND.nextFloat());
    ent1.vel = new Vector3f(Globals.RAND.nextFloat(), Globals.RAND.nextFloat(),
        -Globals.RAND.nextFloat() + 2f * Globals.RAND.nextFloat());
    ent1.vel.scale(0.1f);
    entList.add(ent1);
    ent1 = new Entity(ent);
    ent1.translate(-ent.getHalfSize() * 0.5f, ent.getHalfSize() * 0.5f,
        0.5f * ent.getHalfSize() * Globals.RAND.nextFloat());
    ent1.vel = new Vector3f(-Globals.RAND.nextFloat(), Globals.RAND.nextFloat(),
        -Globals.RAND.nextFloat() + 2f * Globals.RAND.nextFloat());
    ent1.vel.scale(0.1f);
    entList.add(ent1);
    ent1 = new Entity(ent);
    ent1.translate(ent.getHalfSize() * 0.5f, -ent.getHalfSize() * 0.5f,
        0.5f * ent.getHalfSize() * Globals.RAND.nextFloat());
    ent1.vel = new Vector3f(Globals.RAND.nextFloat(), -Globals.RAND.nextFloat(),
        -Globals.RAND.nextFloat() + 2f * Globals.RAND.nextFloat());
    ent1.vel.scale(0.1f);
    entList.add(ent1);
    ent1 = new Entity(ent);
    ent1.translate(-ent.getHalfSize() * 0.5f, -ent.getHalfSize() * 0.5f,
        -ent.getHalfSize() * Globals.RAND.nextFloat() + 2 * ent.getHalfSize()
            * Globals.RAND.nextFloat());
    ent1.vel = new Vector3f(-Globals.RAND.nextFloat(),
        -Globals.RAND.nextFloat(),
        -Globals.RAND.nextFloat() + 2f * Globals.RAND.nextFloat());
    ent1.vel.scale(0.1f);
    entList.add(ent1);
    return entList;
  }
  */

}
