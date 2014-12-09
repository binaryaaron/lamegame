/*** 
 * Thanks to youtube user ThinMatrix
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI
 */
package engineTester;

import com.ra4king.opengl.util.Utils;
import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector;
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
import server.ServerMaster;
import server.ServerThread;
import skyBox.SkyBox;
import textures.ModelTexture;
import world.BoxUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainLoopServer
{
  public ServerMaster myServer;
  private int loop = 0;
  float crystalSize = 100f;
  Player player0;
  Player player1;
  Player player2;
  Player player3;
  ModelMap modelMap;
  List<Player> deadPlayers;

  private static final int nAsteroids = 200;
  private static final int nCrystals = 5;
  TexturedModel texturedLaser;
  List<Entity> killList = new LinkedList<>();
  long nextStep = 4;
  long killStep = 4;

  public MainLoopServer(String[] args)
  {

    DisplayManager.createDisplay();
    Loader loader = new Loader();
    modelMap = new ModelMap();
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
      myServer = new ServerMaster();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    String startString;

    List<Entity> renderList = createInitialGame(modelMap);

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
        for (int i = 0; i < ServerMaster.threadList.size(); i++)
        {
          int playerID= ServerMaster.threadList.get(i).ID;
          Player currentPlayer=null;
          switch (playerID)
          {
            case 0:
              currentPlayer = player0;
              break;
            case 1:
              currentPlayer = player1;
              break;
            case 2:
              currentPlayer = player2;
              break;
            case 3:
              currentPlayer = player3;

          }

          String inputFromClient = ServerMaster.inputFromClient.get(i);
          inputFromClient = getInput(i);
          if (inputFromClient != null)
          {
            parseClientInput(inputFromClient, modelMap, renderList, missileList,
                new Camera(), currentPlayer);
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
      for (int i = 0; i < ServerMaster.threadList.size(); i++)
      {
        ServerThread wt = ServerMaster.threadList.get(i);
        String IDAndOutput=wt.ID+";"+outputToClient;
        wt.updateServerGameState(IDAndOutput);
      }
    }// end of while(!display...)

    renderer.cleanUp();
    loader.cleanUp();
    DisplayManager.closeDisplay();
  }

  public void parseClientInput(String inputFromClient, ModelMap modelMap,
      List<Entity> renderList, List<Entity> missileList,
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
        if (input.equals("KEY_P"))
        {
          if (deadPlayers.contains(player))
          {
            player.respawn(goodPlayerPos(renderList, player.getId()));
            deadPlayers.remove(player);
            if (!renderList.contains(player))
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
      if (player.vel.lengthSquared() > 400)
      {
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
    int crystalsNeeded = 0;
    Entity ent = null;
    Entity other = null;
    if (nextStep >= killStep)
    {
      renderList.removeAll(killList);
      killList.clear();
      killStep += 1;
    }
    nextStep++;

    for (int i = 0; i < renderList.size(); i++)
    {
      ent = renderList.get(i);
      if (!(ent.type == Entity.EntityType.PLANET)) ent.move();
    }
    for (int i = 0; i < renderList.size(); i++)
    {
      ent = renderList.get(i);
      PhysicsUtilities.gameWorldCollision(ent);
      for (int j = i + 1; j < renderList.size(); j++)
      {
        other = renderList.get(j);
        if (ent.type == Entity.EntityType.PLANET)
        {
          PhysicsUtilities.planetCollision(ent, other);
        }
        else if (other.type == Entity.EntityType.PLANET)
        {
          PhysicsUtilities.planetCollision(other, ent);
        }
        else if (BoxUtilities.collision(ent.getBox(), other.getBox()))
        {
          if (ent.type == Entity.EntityType.SHIP
              && other.type == Entity.EntityType.CRYSTAL)
          {
            if (ent.entScoreStep != nextStep)
            {
              ent.score++;
              ent.entScoreStep = nextStep;
              killList.add(other);
              crystalsNeeded++;
            }
          }
          else if (ent.type == Entity.EntityType.CRYSTAL
              && other.type == Entity.EntityType.SHIP)
          {
            if (other.entScoreStep != nextStep)
            {
              other.score++;
              other.entScoreStep = nextStep;
              killList.add(ent);
              crystalsNeeded++;
            }
          }
          else
          {
            PhysicsUtilities.elasticCollision(ent, other);
          }
        }
      }
      if (ent.getHitPoints() <= 0)
      {
        killList.add(ent);
        if (ent.type == Entity.EntityType.SHIP && !deadPlayers.contains(ent))
        {
          deadPlayers.add((Player) ent);
        }
      }
    }

    for (int i = 0; i < crystalsNeeded; i++)
    {
      addEntity(renderList, "CryP", crystalSize);
      crystalSize *= 0.9;
    }
  }

  private void addEntity(List<Entity> renderList, String id, float size)
  {
    while (true)
    {
      Entity crys = randEntity(id, size);
      boolean canAdd = true;
      for (Entity ent : renderList)
      {
        if (BoxUtilities.collision(ent.getBox(), crys.getBox()))
        {
          canAdd = false;
          break;
        }
      }
      if (canAdd)
      {
        renderList.add(crys);
        return;
      }
    }
  }

  private Entity randEntity(String id, float size)
  {

    int a = Globals.RAND.nextInt(2) + 1;

    int r = 0;
    int x = Globals.RAND.nextInt(8000) - 1500;
    int z = Globals.RAND.nextInt(8000) - 1500;
    while (r < 1000000 || r > 4000000)
    {
      x = Globals.RAND.nextInt(8000) - 1500;
      z = Globals.RAND.nextInt(8000) - 1500;
      r = x * x + z * z;
    }
    int y = Globals.RAND.nextInt(3000) - 1500;

    Entity ent = new Entity(id,
        modelMap.getTexturedModelList().get(id),
        new Vector3f(x, y, z), 0, 0, 0, size);

    return ent;
  }

  private Vector3f goodPlayerPos(List<Entity> renderList, String id)
  {
    while (true)
    {
      Player player = randPlayer(id, 0);
      boolean canAdd = true;
      for (Entity ent : renderList)
      {
        if (ent.type == Entity.EntityType.PLANET)
        {
          if (PhysicsUtilities.planetCollide(ent, player))
          {
            canAdd = false;
            break;
          }
        }
        else if (BoxUtilities.collision(ent.getBox(), player.getBox()))
        {
          canAdd = false;
          break;
        }
      }
      if (canAdd)
      {
        return player.position;
      }
    }
  }

  private Player randPlayer(String id, int clientID)
  {
    int r = 1000 + Globals.RAND.nextInt(1000);
    int x = (int) (r * Math.cos(Globals.RAND.nextDouble()));
    int z = (int) (r * Math.sin(Globals.RAND.nextDouble()));
    int y = Globals.RAND.nextInt(8000) - 4000;
    Player player = new Player(id,
        modelMap.getTexturedModelList().get(id),
        new Vector3f(x, y, z), 0, 0, 0, clientID);
    return player;
  }

  private List<Entity> createInitialGame(ModelMap modelMap)
  {
    Entity planet = new Entity("Plan", modelMap.getTexturedModelList().get("Plan"), new Vector3f(0,0,0), 0, 0, 0, 100);

    List<Entity> ents = new ArrayList<>(nAsteroids + 20);
    ents.add(planet);

    player0 = randPlayer("S001", 0);
    player1 = randPlayer("S002", 1);
    player2 = randPlayer("S001", 2);
    player3 = randPlayer("S002", 3);

    ents.add(player0);
    ents.add(player1);
    ents.add(player2);
    ents.add(player3);

    player0.respawn(goodPlayerPos(ents, player0.getId()));
    player1.respawn(goodPlayerPos(ents, player0.getId()));
    player2.respawn(goodPlayerPos(ents, player0.getId()));
    player3.respawn(goodPlayerPos(ents, player0.getId()));

    System.out.println("Players set up");
    for (int i = 0; i < nAsteroids; i++)
    {
      int a = Globals.RAND.nextInt(2) + 1;
      float s = Globals.RAND.nextFloat() * 100;
      addEntity(ents, "A00" + a, s);
    }
    for (int i = 0; i < nCrystals; i++)
    {
      addEntity(ents, "CryP", crystalSize);
    }

    return ents;
  }

  public static void main(String[] args)
  {
    new MainLoopServer(args);

  }

  public String getInput(int i)
  {
    // start with first element in walker thread, expand to multiplayer
    String input = ServerMaster.threadList.get(i).getClientInput();
    loop++;
    return input;
  }
}
