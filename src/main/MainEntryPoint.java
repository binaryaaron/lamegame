package main;

import engineTester.MainLoopClient;
import engineTester.MainLoopServer;

import java.io.File;

/**
 * Provides a main entry point for our game. Will take args from the
 * JVM to denote if this is a server instance and an address for a server.
 */
public class MainEntryPoint
{
  public static void main(String[] args)
  {
    System.out.println(new File(".").getAbsoluteFile());

    System.out.println("Args = " + args);
    for (String s : args)
    {
      System.out.println(s);
    }
    if (args.length <= 0)
    {
      System.out.println("Please enter either 'server' or 'client' to start the game");
    }
    else if (args[0].equals("server"))
    {
        System.out
            .println("Starting server on current machine");
        new MainLoopServer(args);
    }
  else if (args[0].equals("client"))
  {
    System.out.println("Starting client");
    new MainLoopClient(args);
  }
    else
    {
      System.out.println("no args; Exiting");
    }
    System.out.println("Exiting program");
  }
}
