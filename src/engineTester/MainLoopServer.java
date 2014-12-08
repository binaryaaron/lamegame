/*** 
 * Thanks to youtube user ThinMatrix
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI
 */
package engineTester;

import com.ra4king.opengl.util.Utils;
import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;

import entities.*;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
import world.BoxUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MainLoopServer
{
  public WalkerServer myServer;
  private int loop = 0;
  Player player0;
  Player player1;
  Player player2;
  Player player3;
  List<Player> deadPlayers;

  private static final int nAsteroids = 200;
  TexturedModel texturedLaser;
  List<Entity> killList = new LinkedList<>();
  long nextStep = 4;
  long killStep = 4;

  public MainLoopServer(String[] args)
  {

    DisplayManager.createDisplay();
    Loader loader = new Loader();
    ModelMap modelMap = new ModelMap();
//    Camera camera = new Camera();
//    camera.followObj = player0;
    // create skybox, this is not an entity so it is seperate
    RawModel skyBox = OBJLoader.loadObjModel("SkyBox2", loader, true);
    ModelTexture skyTexture = new ModelTexture(loader.loadTexture("SkyBox2"));
    TexturedModel texturedSkyBox = new TexturedModel("SkyBox2", skyBox,
        skyTexture);
    RawModel laser = OBJLoader.loadObjModel("missle", loader, true);
    ModelTexture laserTexture = new ModelTexture(loader.loadTexture("Missle"));
    texturedLaser = new TexturedModel("laser", laser, laserTexture);
    SkyBox skyBoxEntity = new SkyBox(loader, texturedSkyBox);

    modelMap.getTexturedModelList().put("Play",
        modelMap.getTexturedModelList().get("S002"));

    // create lights and camera for the player. camera position should be set in
    // parsing routine
    Light light = new Light(new Vector3f(10f, 5f, 2000f), new Vector3f(1.0f,
        1.0f, 1.0f));
    MasterRenderer renderer = new MasterRenderer(new Camera());

    String outputToClient;

    try
    {
      myServer = new WalkerServer();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    String startString;

    startString = createInitialGameString(modelMap);

    List<Entity> renderList = parseGameStateString(startString, modelMap);
    List<Entity> missileList = new ArrayList<>();
    deadPlayers = new LinkedList<>();
    long lastTime = System.currentTimeMillis();

    float scale;
    while (!Display.isCloseRequested())
    {
      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }

      long time = System.currentTimeMillis();
      if (time - lastTime > 17)
      {
        for (int i = 0; i < WalkerServer.threadList.size(); i++)
        {
          int playerID= WalkerServer.threadList.get(i).ID;
          Player currentPlayer=null;
          switch (playerID)
          {
            case 0: currentPlayer=player0;
            break;
            case 1: currentPlayer=player1;
            break;
            case 2: currentPlayer=player2;
            break;
            case 3: currentPlayer=player3;
            
          }
            
          String inputFromClient = WalkerServer.inputFromClient.get(i);
          inputFromClient = getInput(i);
          if (inputFromClient != null)
          {
            parseClientInput(inputFromClient, modelMap, renderList, missileList, new Camera(), currentPlayer);
          }
        }
        lastTime = time;
        performPhysics(renderList);
      }

      // ///instead of rendering, build strings and send them to the client
      outputToClient = "";// clear the String
      for (Entity ent : renderList)
      {
        outputToClient += ent.toString() + ";";
      }
      // outputToClient += camera.toString();

      // for (WalkerThread wt : myServer.threadList)
      for (int i = 0; i < WalkerServer.threadList.size(); i++)
      {
        WalkerThread wt = WalkerServer.threadList.get(i);
        wt.updateServerGameState(outputToClient);
      }
    }// end of while(!display...)

    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
  }

  public void parseClientInput(String inputFromClient, ModelMap modelMap, List<Entity> renderList, List<Entity> missileList,
      Camera camera, Player player)
  {
    float scale;
    Quaternion orientation;
    Vector3 position;
    Vector3 cameraPos;
    Vector3 missilePos;
    String[] clientInput = inputFromClient.split(";");
    for (String input : clientInput)
    {

      if (input.equals("KEY_LSHIFT"))
      {
        scale = 0.01f;
      }

      orientation = player.orientation;
      position = new Vector3(player.position.x, player.position.y,
          player.position.z);
      cameraPos = position.copy();
      missilePos = position.copy();

      // in your update cod
      float speed = 0.08f;// (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) |
      // Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 20
      // : 50) * deltaTime / (float)1e9;
      float rotSpeed = 1f;

      // pitch
      int dy = Mouse.getDY();
      if (input.equals("KEY_W"))
      {
        orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(1, 0, 0)).mult(
            orientation);
      }
      if (input.equals("KEY_S"))
      {
        orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(1, 0, 0)).mult(
            orientation);
      }
      // yaw
      int dx = Mouse.getDX();
      if (input.equals("KEY_A"))
      {
        orientation = Utils.angleAxisDeg(-rotSpeed, new Vector3(0, 1, 0)).mult(
            orientation);
      }
      if (input.equals("KEY_D"))
      {
        orientation = Utils.angleAxisDeg(rotSpeed, new Vector3(0, 1, 0)).mult(
            orientation);
      }
      // roll
      if (input.equals("KEY_E")) orientation = Utils.angleAxisDeg(rotSpeed,
          new Vector3(0, 0, 1)).mult(orientation);
      if (input.equals("KEY_Q")) orientation = Utils.angleAxisDeg(-rotSpeed,
          new Vector3(0, 0, 1)).mult(orientation);
      if (input.equals("KEY_B"))
      {
        player.vel.mult(0.95f);
      }
      orientation.normalize();

      Quaternion inverse = orientation.copy().inverse();

      Vector3 delta = new Vector3();

      if (input.equals("KEY_DOWN")) delta.z(-speed);
      if (input.equals("KEY_UP"))
      {
        delta.z(delta.z() + speed);
      }

      if (input.equals("KEY_RIGHT")) delta.x(-speed);
      if (input.equals("KEY_LEFT")) delta.x(delta.x() + speed);

      if (input.equals("KEY_SPACE")) delta.y(-speed);
      if (input.equals("KEY_LCONTROL")) delta.y(delta.y() + speed);
      if (player.getHitPoints() <= 0)
      {
      if(input.equals("KEY_P"))
      {
        if(deadPlayers.contains(player))
        {
          player.respawn(new Vector3f(1100,1100,0));
          deadPlayers.remove(player);
          if(!renderList.contains(player))
          {
            renderList.add(player);
          }
        }
      }
      }
      if (input.equals("KEY_RSHIFT"))
      {

        // fire a missile
        if (missileList.size() > 200)
        {
          renderList.remove(missileList.get(0));
          missileList.remove(0);
        }

        Vector3 deltaMis = delta.copy();
        deltaMis.y(20 * player.getScale());
        deltaMis.z(-40 * player.getScale());

        missilePos.add(inverse.mult(deltaMis));

        Entity missle = new Entity("lase", texturedLaser,
            new Vector3f(0, 0, 0), 0, 0, 0, 5f);
        // missle.setPosition(player.position);
        missle.quadTranslate(missilePos);

        missle.orientation = player.orientation.copy();
        float pv = 20f;
        missle.vel = player.vel.copy().add(inverse.mult(new Vector3(0, 0, pv)));

        renderList.add(missle);
        missileList.add(missle);
      }

      Vector3 deltaCam = delta.copy();
      deltaCam.y(-2 * player.getScale());
      deltaCam.z(-9 * player.getScale());

      player.vel.add(inverse.mult(delta));
      // limit velocity of player
      if (player.vel.lengthSquared() > 400) {
        player.vel.normalize();
        player.vel.mult(20f);
      }
      cameraPos.add(inverse.mult(deltaCam));
      cameraPos.add(player.vel);
      // player.move();
      player.orientation = orientation.copy();
      camera.quadTranslate(cameraPos);
      camera.orientation = orientation.copy();
    }
  }

  private void performPhysics(List<Entity> renderList)
  {
    Entity ent = null;
    Entity other = null;
    if (nextStep >= killStep)
    {
      renderList.removeAll(killList);
      killList.clear();
      killStep+=4;
    }
    nextStep++;
    for (int i = 0; i < renderList.size(); i++)
    {
      ent = renderList.get(i);
      if (!ent.getId().equals("Plan")) ent.move();
    }
    for (int i = 0; i < renderList.size(); i++)
    {
      ent = renderList.get(i);
      PhysicsUtilities.gameWorldCollision(ent);
      for (int j = i + 1; j < renderList.size(); j++)
      {
        other = renderList.get(j);
        if (BoxUtilities.collision(ent.getBox(), other.getBox()))
        {
          PhysicsUtilities.elasticCollision(ent, other);
          if (ent.getHitPoints() <= 0)
          {
            killList.add(ent);
            if (ent.getId().startsWith("S") && !deadPlayers.contains(ent))
            {
              deadPlayers.add((Player) ent);
            }
          }
          if (other.getHitPoints() <= 0)
          {
            killList.add(other);
            if (other.getId().startsWith("S") && !deadPlayers.contains(ent))
            {
              deadPlayers.add((Player) other);
            }
          }
        }
      }
    }
  }

  private String createInitialGameString(ModelMap modelMap)
  {
    String startString = "Plan,0,0,0,0,0,0,100;gCry,1000,1000,1000,0,0,0,100;";

    player0 = new Player("S001", modelMap.getTexturedModelList().get("S001"),
        new Vector3f(1000, 1010, 0), 0, 0, 0, 0);
    player1 = new Player("S002", modelMap.getTexturedModelList().get("S002"),
        new Vector3f(1000, 1000, 0), 0, 0, 0, 1);
    player2 = new Player("S002", modelMap.getTexturedModelList().get("S002"),
        new Vector3f(1000, 990, 0), 0, 0, 0, 2);
    player3 = new Player("S002", modelMap.getTexturedModelList().get("S002"),
        new Vector3f(1000, 980, 0), 0, 0, 0, 3);
    startString += player0.toString() + ";";
    startString += player1.toString() + ";";
    startString += player2.toString() + ";";
    startString += player3.toString() + ";";
    for (int i = 0; i < nAsteroids; i++)
    {
      int a = Globals.RAND.nextInt(2) + 1;

      int y = Globals.RAND.nextInt(20) - 10;
      int r = 0;
      int x = Globals.RAND.nextInt(8000) - 1500;
      int z = Globals.RAND.nextInt(8000) - 1500;
      while (r < 1000000 || r > 4000000)
      {
        x = Globals.RAND.nextInt(8000) - 1500;
        z = Globals.RAND.nextInt(8000) - 1500;
        r = x * x + z * z;
      }

      float s = Globals.RAND.nextFloat() * 100;
      startString = startString.concat("A00" + a + "," + x + "," + y + "," + z
          + ",0,0,0," + s + ";");
    }
    return startString;
  }

  public static void main(String[] args)
  {
    new MainLoopServer(args);

  }

  private List<Entity> parseGameStateString(String testInput,
      ModelMap modelMap)
  {

    List<Entity> renderList = new ArrayList<>();
    String[] sceneInfo = testInput.split(";");
    for (String object : sceneInfo)
    {
      Entity tmp_Entity;
      Entity currentPlayer=null;
      String[] currentLine = object.split(",");
      String id;
      int playerID;
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

      if (object.startsWith("S"))
      {
        playerID = Integer.parseInt(currentLine[9]);
        System.out.println("adding player "+playerID);
        switch (playerID)
        {
          case 0: currentPlayer=player0;
          break;
          case 1: currentPlayer=player1;
          break;
          case 2: currentPlayer=player2;
          break;
          case 3: currentPlayer=player3;       
        }
        renderList.add(currentPlayer);
      }
      else
      {

        tmp_Entity = new Entity(id, modelMap.getTexturedModelList().get(
            id), new Vector3f(x, y, z), xr, yr, zr, s);
        renderList.add(tmp_Entity);
      }
    }
    return renderList;
  }

  public String getInput(int i)
  {
    // start with first element in walker thread, expand to multiplayer
    String input = WalkerServer.threadList.get(i).getClientInput();
    loop++;
    return input;
  }
}
