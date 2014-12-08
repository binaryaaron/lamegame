package main;

import engineTester.MainLoopClient;
import engineTester.MainLoopServer;

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
      new MainLoopServer(args);
    }
    else
    {
      new MainLoopClient(args);
    }
  }
}
