/*** 
 * Generates board and fills it with objects, such as asteroids and ships.
 * Updates the position of the objects and camera.
 * Updates the GUI.
 * Send information to clients.
 * Run this once to make the server.
 */
package engineTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
import server.ServerMaster;
import server.ServerThread;
import textures.ModelTexture;
import world.BoxUtilities;

import com.ra4king.opengl.util.Utils;
import com.ra4king.opengl.util.math.Quaternion;
import com.ra4king.opengl.util.math.Vector3;

import entities.Camera;
import entities.Entity;
import entities.Globals;
import entities.Player;

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
	  private static final int nCrystals = 10;
	  private boolean gameOver=false;
	  Entity winner;
	  TexturedModel texturedLaser;
	  List<Entity> killList = new LinkedList<>();
	  long nextStep = 4;
	  long killStep = 4;

  /**
   * Creates a server and a black Window that represents the server
   * @param args
   */
  public MainLoopServer(String[] args)
  {
    DisplayManager.createDisplay();
    Loader loader = new Loader();
    modelMap = new ModelMap();
    //    Camera camera = new Camera();
    //    camera.followObj = player0;
    RawModel laser = OBJLoader.loadObjModel("missle", loader, true);
    ModelTexture laserTexture = new ModelTexture(loader.loadTexture("Missle"));
    texturedLaser = new TexturedModel("laser", laser, laserTexture);

    modelMap.getTexturedModelList().put("Play",
        modelMap.getTexturedModelList().get("S002"));

    // create lights and camera for the player. camera position should be set in
    // parsing routine
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

    List<Entity> renderList = createInitialGame(modelMap);

    HashMap<Integer, List<Entity>> missileMap = new HashMap<>();
    for (int i = 0; i < 4; i++)
    {
      missileMap.put(i, new LinkedList<>());
    }
    deadPlayers = new LinkedList<>();
    long lastTime = System.currentTimeMillis();

    while (!Display.isCloseRequested())
    {
      if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
      {
        break;
      }

      long time = System.currentTimeMillis();
      if (time - lastTime > 17)
      {
        player0.missileSound = 0;
        player1.missileSound = 0;
        player2.missileSound = 0;
        player3.missileSound = 0;
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
            parseClientInput(inputFromClient, modelMap, renderList, missileMap.get(playerID),
                new Camera(), currentPlayer);
          }
        }
        lastTime = time;
        if(gameOver){

        	endGameStep(renderList,winner);
        }
        performPhysics(renderList);
      }

      //instead of rendering, build strings and send them to the client
      outputToClient = "";// clear the String
      for (Entity ent : renderList)
      {
        outputToClient += ent.toString() + ";";
      }
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

  /**
   * Parse the clients input string
   * @param inputFromClient client's input string
   * @param modelMap game model map
   * @param renderList game render list
   * @param missileList client render list
   * @param camera client camera
   * @param player client player
   */
  public void parseClientInput(String inputFromClient, ModelMap modelMap,
      List<Entity> renderList, List<Entity> missileList,
      Camera camera, Player player)
  {
    Quaternion orientation;
    Vector3 position;
    Vector3 cameraPos;
    Vector3 missilePos;
    String[] clientInput = inputFromClient.split(";");
    for (String input : clientInput)
    {

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
      if (input.equals("KEY_F"))
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
      if (input.equals("KEY_O"))
      {
        if (gameOver && player == winner)
        {
          renderList.clear();
          crystalSize = 100f;
          gameOver = false;
          winner = null;
          renderList.addAll(createInitialGame(modelMap));
        }
      }
      if (input.equals("KEY_RSHIFT"))
      {
        if (!gameOver)
        {
          long time = System.currentTimeMillis();
          // Limit missile to 4 per second
          if (time - player.lastFired > 250)
          {
            player.lastFired = time;
            player.missileSound = 1;
            // fire a missile (100 per player active)
            if (missileList.size() > 100)
            {
              renderList.remove(missileList.get(0));
              missileList.remove(0);
            }

            Vector3 deltaMis = delta.copy();
            deltaMis.y(0 * player.getScale());
            deltaMis.z(7 * player.getScale());

            missilePos.add(inverse.mult(deltaMis));

            Entity missle = new Entity("lase", texturedLaser,
                new Vector3f(0, 0, 0), 0, 0, 0, 5f);
            // missle.setPosition(player.position);
            missle.quadTranslate(missilePos);

            missle.orientation = player.orientation.copy();
            float pv = 20f;
            missle.vel = player.vel.copy()
                .add(inverse.mult(new Vector3(0, 0, pv)));

            renderList.add(missle);
            missileList.add(missle);
          }
        }
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

  /**
   * Perform physics for the game
   *
   * @param renderList games object list
   */
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
              if(ent.score>=Globals.WINPOINTS){
            	  gameOver=true;
            	  winner=ent;}
              killList.add(other);
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
              if(other.score>=Globals.WINPOINTS){
            	  gameOver=true;
            	  winner=other;
            	  }
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

  /**
   * Add an entity to the renderlist without collision
   * @param renderList games renderlist
   * @param id type of entity
   * @param size size of entity
   */
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

  /**
   * Generate a random entity somewhere near the game
   * @param id type of entity
   * @param size size of entity
   * @return random entity
   */
  private Entity randEntity(String id, float size)
  {


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

  /**
   * Find a good position for the player
   * @param renderList games render list
   * @param id id of player ship
   * @return good position for the player
   */
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

  /**
   * Generate a random player
   * @param id ship id
   * @param clientID client id
   * @return random player
   */
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

  /**
   * Create the initial game with 4 players
   * @param modelMap game's model map
   * @return new game state
   */
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
      Entity ass = ents.get(ents.size()-1);
      ass.randomVel();
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

  /**
   * Ends the current game when a player reaches the allotted number of crystals.
   * user who wins can reset the game; everyone else "dies" and the world
   * shrinks
   * @param renderList
   * @param winner
   */
  public void endGameStep(List<Entity> renderList, Entity winner)
  {
    for (Entity ent : renderList)
    {
      if (ent.type == Entity.EntityType.PLANET || ent.type == Entity.EntityType.CRYSTAL)
      {
        ent.kill();
      }

      if (ent != winner)
      {
        ent.multPos(0.99f);
      }
    }
  }

  /**
   * What to do when the game is won
   * @param i the player can reset
   */
  public String getInput(int i)
  {
    // start with first element in walker thread, expand to multiplayer
    String input = ServerMaster.threadList.get(i).getClientInput();
    return input;
  }
}
