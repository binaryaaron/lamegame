package main;

/**
 * Provides a main entry point for our game. Will take args from the
 * JVM to denote if this is a server instance. mostly pseudocode for now
 */
public class MainEntryPoint
{
  public static void main(String[] args)
  {
    if (args[0] == "server")
    {
      System.out.println("Starting a server instance");
      //GameServer server = new GameServer();
      //server.start();
    }
    else
    {
      System.out.println("Starting a client instance");
      ////GameClient client = new GameClient();
      ////client.start();
    }
  }
}
