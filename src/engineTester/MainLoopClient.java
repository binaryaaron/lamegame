/*** 
 * Thanks to youtube user ThinMatrix
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI
 */
package engineTester;

import java.io.IOException;
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
import server.WalkerClient;
import skyBox.SkyBox;
import textures.ModelTexture;
import toolbox.PerformanceUtilities;
import audio.AudioManager;

import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;

import entities.Camera;
import entities.Entity;
import entities.Laser;
import entities.Light;

public class MainLoopClient
{
  public final boolean PRINT_FPS = false;
  public final boolean HUD_DEBUG = true;
  private boolean exitRequest = false;
  public WalkerClient myClient = null;

  private static String hostName = "";
  private static int socketVal = -1;
  
  private float speed;

  private int health;
  private long previousTime=0,currentTime=0;

  public MainLoopClient(String[] args)
  {
	AudioManager.createAudio();
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
    
    RawModel barrier = OBJLoader.loadObjModel("SkyBox", loader, true);
    ModelTexture barrierTexture = new ModelTexture(loader.loadTexture("font"));
    TexturedModel texturedBarrier = new TexturedModel("Barrier", barrier,
    		barrierTexture);
    Entity barrierEntity = new Entity("Barrier", texturedBarrier, new Vector3f(0.05f, 0.3f, 0.8f), 0f, 0f, 0f, 3000);
    // create lights and camera for the player. camera position should be set in
    // parsing routine
    Light light = new Light(new Vector3f(1000f, 5f, 500f), new Vector3f(1.0f,
        1.0f, 1.0f));
    Camera camera = new Camera();
    MasterRenderer renderer = new MasterRenderer(camera);

    PerformanceUtilities pu = new PerformanceUtilities();

    // this will be information read from a socket
    // format: ID,x,y,z,rotx,rot,y,rotz,scale
    String outputToServer = null;
    String inputFromServer = null;

    long startTime = System.currentTimeMillis();
    long lastTime = System.currentTimeMillis();
    long hudDelay = 100;
    long hudStart = 0;
    //Hud Objects
    List<Entity> renderList = new ArrayList<>();
    List<Laser> lasers = new ArrayList<>();

    List<Entity> hudRenderList = new ArrayList<>();
    
    float xDiff = -0.2f;
    Entity hud1 = new Entity("H004",modelMap.getTexturedModelList().get(
        "H004"), new Vector3f(xDiff,-0.25f,1f),0f,0f,0f, 0.05f);
    hud1.drawShadow = false;
    Entity hud2 = new Entity("H005",modelMap.getTexturedModelList().get(
        "H005"), new Vector3f(xDiff,-0.07f,1f),0f,0f,0f, 0.05f);
    hud2.drawShadow = false;
    Entity hud3 = new Entity("H006",modelMap.getTexturedModelList().get(
        "H006"), new Vector3f(xDiff,0.11f,1f),0f,0f,0f, 0.05f);
    hud3.drawShadow = false;
    Entity hud4 = new Entity("H007",modelMap.getTexturedModelList().get(
        "H007"), new Vector3f(xDiff,0.28f,1f),0f,0f,0f, 0.05f);
    hud4.drawShadow = false;
    hudRenderList.add(hud1);
    hudRenderList.add(hud2);
    hudRenderList.add(hud3);
    hudRenderList.add(hud4);
    hudStart = System.currentTimeMillis();
    String menuData = "S001,0,0,0,0,0,0,0,0,0,0,0;" +
        "A001,0,0,2,0,0,0,0,0.1;" + 
        "Plan,20,0,70,0,0,0,0,1.1;";
    //random asteroids for the start menu;
    for(int i = 0; i<30; i++)
    {
      int asteroidChoose = (int)(Math.random()*4);
      if(asteroidChoose == 0)
      {
       menuData+="A001,"; 
      }
      else if(asteroidChoose <3)
      {
        menuData+="A002,";
      }
      else
      {
        menuData+="A003,";
      }
      menuData+=(Math.random()*20+10)+","; //x
      menuData+=(Math.random()*30-15)+","; //y
      menuData+=(Math.random()*60+50)+",";//z
      menuData+="0,";//(Math.random()*4)+",";//rx
      menuData+="0,";//(Math.random()*4)+",";//ry
      menuData+="0,";//(Math.random()*4)+",";//rz
      menuData+="0,";//w
      menuData+=(Math.random()*.2+0.6)+";";
    }

    if (PRINT_FPS)
    {
      pu.startFrameCounter();
    }
    Menu.startMainMenu();
    int currentResolution = 0;
    boolean keyReleased = true;
    boolean inMenu = true;

    getOfflineState(renderList, camera, modelMap, menuData);
    renderList.get(1).setPosition(new Vector3f(xDiff+0.1f,Menu.getYPos(),1));
    renderList.get(0).setRotX(0.5f);
    AudioManager.playMusic();
    while (!exitRequest&&inMenu)
    {
      if(Display.isCloseRequested())
      {
        exitRequest = true;
        break;
      }
      boolean keyPressed = false;
      if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        exitRequest = true;
        break;
      }
      if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP))
      {
        if(keyReleased)
        {
          Menu.up();
          renderList.get(1).setPosition(new Vector3f(xDiff+0.1f,Menu.getYPos(),1));
        }
        keyPressed = true;
        keyReleased = false;
      }
      if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN))
      {
        if(keyReleased)
        {
          Menu.down();
          renderList.get(1).setPosition(new Vector3f(xDiff+0.1f,Menu.getYPos(),1));
        }
        keyPressed = true;
        keyReleased = false;
      }
      if(Keyboard.isKeyDown(Keyboard.KEY_RETURN))
      {
        if(keyReleased)
        {
          int result = Menu.choose();
          if(result == 3) exitRequest = true;
          if(result == 0) inMenu = false;
        }
        keyPressed = true;
        keyReleased = false;
      }
      if(Keyboard.isKeyDown(Keyboard.KEY_Q))
      {
        inMenu = false;
      }
      if(!keyPressed)
      {
        keyReleased = true;
      }

      // Render entities from offline renderlist
      for (Entity ent : renderList)
      {
        renderer.processEntity(ent);
      }
      for (Entity ent : hudRenderList)
      {
        renderer.processHudEntity(ent);
      }

      AudioManager.updateAudio();
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

    if(Display.isCloseRequested() || exitRequest)
    {
      renderer.cleanUp();
      loader.cleanUp();
      DisplayManager.closeDisplay();
      return;
    }
    // controls for physics testing with two asteroids
    // wasd control leftest asteroid, arrows control rightmost.
    // holding shift will slow down the shifting speed.

    //Hud Objects
    hud1 = new Entity("H001", modelMap.getTexturedModelList().get(
        "H001"), new Vector3f(0.7f, 0.37f, 1f), 0f, 0f, 0f, 0.05f);
    hud1.drawShadow = false;
    hud2 = new Entity("H002", modelMap.getTexturedModelList().get(
        "H002"), new Vector3f(0.7f, 0.07f, 1f), 0f, 0f, 0f, 0.05f);
    hud2.drawShadow = false;
     hud3 = new Entity("H003", modelMap.getTexturedModelList().get(
        "H003"), new Vector3f(0.05f, 0.3f, 0.8f), 0f, 0f, 0f, 0.05f);
    hud3.drawShadow = false;

    light = new Light(new Vector3f(10f, 5f, 2000f), new Vector3f(1.0f,
        1.0f, 1.0f));
    hudRenderList = new ArrayList<>();
    hudRenderList.add(hud1);
    hudRenderList.add(hud2);
    hudRenderList.add(hud3);
    try
    {
      if(args.length > 0)
      {

        myClient = new WalkerClient(args);
        hostName = args[0];
      }
      else if(hostName.length()<1 || socketVal < 0)
      {
        hostName = "localhost";
        socketVal = 4444;
        myClient = new WalkerClient(args);
      }
      else
      {
        String [] clientArgs = new String[]{hostName, ""+socketVal};
        myClient = new WalkerClient(clientArgs);
      }
    }
    catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to: " + hostName);
      System.exit(1);
    }
    /* Perform object movement as long as the window exists */while (!Display.isCloseRequested())
    {
      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }
      if(Keyboard.isKeyDown(Keyboard.KEY_F2))
      {
        currentResolution++;
        DisplayManager.changeResolution(currentResolution);
      }
      if(Keyboard.isKeyDown(Keyboard.KEY_F1))
      {
        DisplayManager.changeFullScreen();
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
      //render HUD
      if (HUD_DEBUG && hudDelay + hudStart < System.currentTimeMillis())
      {
        hudStart = System.currentTimeMillis();

        hudRenderList.get(2).setModel(
            modelMap.setScoreText(("" + System.currentTimeMillis() + "  ")));
        hudRenderList.get(1).setModel(modelMap.setHealthText("" + health));

        hudRenderList.get(0).setModel(modelMap.setSpeedText("" + speed));
      }
      for (Entity ent : hudRenderList)
      {
        renderer.processHudEntity(ent);
        // System.out.println(ent.getModel().getRawModel().getVertexCount());
      }
      AudioManager.updateAudio();
      // Process rendering
      renderer.processSkyBox(skyBoxEntity);
      renderer.processEntity(barrierEntity);
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
    AudioManager.closeAudio();
  }

  public void getServerState(List<Entity> renderList,
      Camera camera,
      ModelMap modelMap)
  {
    String[] sceneInfo = myClient.getInputFromServer().split(";");
    renderList.clear();
    for (String object : sceneInfo)
    {
      if (!object.equals(""))
      {
        Entity tmp_Entity = null;
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
        if (object.startsWith("l"))
        {
          Entity tmp_laser = new Entity(id,
              modelMap.getTexturedModelList().get(id),
              new Vector3f(x, y, z), xr, yr, zr, s);
          tmp_laser.orientation.w(w);
          tmp_laser.drawShadow = false;
          renderList.add(tmp_laser);
        }
        else if (object.startsWith("S"))
        {
          float speed = Float.parseFloat(currentLine[11]);
          playerID = Integer.parseInt(currentLine[9]);
          tmp_Entity = new Entity(id, modelMap.getTexturedModelList().get(id),
              new Vector3f(x, y, z), xr, yr, zr, s, playerID);
          tmp_Entity.orientation.w(w);
        }
        else
        {
          tmp_Entity = new Entity(id, modelMap.getTexturedModelList().get(id),
              new Vector3f(x, y, z), xr, yr, zr, s);
          tmp_Entity.orientation.w(w);
        }

        if (object.startsWith("S")
            && playerID == myClient.ID)// &&playerID==myClient.ID
        {
          health = Integer.parseInt(currentLine[10]);
          speed = Float.parseFloat(currentLine[11]);
          Quaternion inverse = tmp_Entity.orientation.copy().inverse();
          Vector3 deltaCam = new Vector3(0, -2 * tmp_Entity.getScale(), -9
              * tmp_Entity.getScale());
          deltaCam = inverse.mult(deltaCam);
          camera.setPosition(new Vector3f(x, y, z));
          camera.move(deltaCam);
          camera.orientation = tmp_Entity.orientation.copy();
        }
        else if(object.startsWith("S")&&playerID!=myClient.ID){
        	Vector3f scaleVec=new Vector3f();
        	Vector3f.sub(new Vector3f(x, y-2, z), camera.position, scaleVec);
        	float tagScale=scaleVec.length();
          Entity playerTag=  new Entity("gCone", modelMap.getTexturedModelList().get("gCone"),
              new Vector3f(x, y-2, z), xr, yr, zr, tagScale/100);
          System.out.println(tagScale);
          renderList.add(playerTag);
          playerTag.drawShadow=false;
        }
        else if(object.startsWith("gCry")){
        	
        	
        	Entity crystal=  new Entity("gCry", modelMap.getTexturedModelList().get("gCry"),
                    new Vector3f(x, y, z), xr, yr, zr, s);
                renderList.add(crystal);
              //  crystal.drawShadow=false;
                
                
                Entity crystalTag=  new Entity("gCone", modelMap.getTexturedModelList().get("gCone"),
                        new Vector3f(x, y-s, z), xr, yr, zr, 10);
                    renderList.add(crystalTag);
                    crystalTag.drawShadow=false;
        
        	
        }

        if (tmp_Entity != null)
        {
          renderList.add(tmp_Entity);
        }
      }
    }
  }

  public void getOfflineState(List<Entity> renderList, Camera camera,
      ModelMap modelMap,String data)
  {
    String[] sceneInfo = data.split(";");
    renderList.clear();
    for (String object : sceneInfo)
    {
      if (!object.equals(""))
      {
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
          float speed = Float.parseFloat(currentLine[11]);
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
        
        /*if (object.startsWith("S"))
        {
          System.out.print("here:");
          Quaternion inverse = tmp_Entity.orientation.copy().inverse();
          Vector3 deltaCam = new Vector3(0, -2 * tmp_Entity.getScale(), -9
              * tmp_Entity.getScale());
          deltaCam = inverse.mult(deltaCam);
          camera.setPosition(new Vector3f(x, y, z));
          camera.move(deltaCam);
          camera.orientation = tmp_Entity.orientation.copy();
        }*/
        renderList.add(tmp_Entity);
      }
    }
  }
  public void sendKeyBoard()
  {
    String toSend = ";";
    if (health > 0)
    {
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

        AudioManager.playRandomLaser();
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
      {
        toSend += "KEY_SPACE;";
      }
    }
    else
    {
      if (Keyboard.isKeyDown(Keyboard.KEY_P))
      {
        currentTime=System.currentTimeMillis();
        if(currentTime-previousTime>1000)
        {
          try
          {
            Thread.sleep(1000);
          }
          catch (InterruptedException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          currentTime=previousTime;
          System.out.println("respawn me!");
          toSend += "KEY_P;";
        }
      }
    }
    myClient.sendToServer(toSend);
  }

  /**
   * Setting connection info
   * @param host
   * @param socket
   */
  public static void setConnection(String host, int socket)
  {
    hostName = host;
    socketVal = socket;
  }  

  public static void main(String[] args)
  {
    new MainLoopClient(args);
  }
}
