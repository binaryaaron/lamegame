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
  /**
   * Main entry point for code, run either server or client based on call
   * @param args
   */
  public static void main(String[] args)
  {
    System.out.println(new File(".").getAbsoluteFile());

    System.out.println("Args = " + args);
    for (String s : args)
    {
      System.out.println(s);
    }
    try
    {
      if (args.length <= 0)
      {
        new MainLoopClient(args);
      }
      else if (args[0].equals("server"))
      {
        System.out.println("Starting server on current machine");
        new MainLoopServer(args);
      }
      else if (args[1].equals("client"))
      {
        System.out.println("Starting client");
        new MainLoopClient(args);
      }
      else
      {
        System.out.println("no args; Exiting");
      }
      System.out.println("Exiting program");
    } catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println("Please enter the proper number of args");
    }

  }
}
