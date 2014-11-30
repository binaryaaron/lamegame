package server;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.AllPermission;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;

import org.lwjgl.input.Keyboard;

import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;

public class WalkerClient extends Thread
{
  public boolean printlocation=false;
  public static String hostName="Manticore";
  public static int socketVal=4444;
  
  Socket mySocket=null;
  PrintWriter out=null;
  BufferedReader in=null;
  
  private volatile String inputFromServer="Play,0,0,4,0,0,0,0.3";
  private String outputToServer;
  
  private boolean serverResponded=false;

  public static void main(String[] args) throws IOException
  {
    new WalkerClient(args);
  }
  
  public WalkerClient(String[] args) throws IOException
  {    
    if(args.length>0)
    {
      hostName=args[0];
    }
    
    try
    {
      mySocket=new Socket(hostName, socketVal);
      out=new PrintWriter(mySocket.getOutputStream(),true);
      in=new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
    } catch (UnknownHostException e)
    {
      System.err.println("Don't know about host: "+hostName);
      System.exit(1);
    } catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to: "+hostName);
      System.exit(1);
    }
    
    //initialize ServerPackage
    
    this.start();
  }
  
  public void run()
  {
    while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
    {
      if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
      {
        out.println("KEY_LSHIFT");
      }
  
      if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
      {
        System.out.println("KEY_RIGHT");
        out.println("KEY_RIGHT");
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
      {
        out.println("KEY_LEFT");
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_UP))
      {
        out.println("KEY_UP");
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
      {
        out.println("KEY_DOWN");
      }
      // /
      if (Keyboard.isKeyDown(Keyboard.KEY_D))
      {
        out.println("KEY_D");
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_A))
      {
        out.println("KEY_A");
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_W))
      {
        out.println("KEY_W");
      }
      if (Keyboard.isKeyDown(Keyboard.KEY_S))
      {
        out.println("KEY_S");
      }
      try
      {
        Thread.sleep(25);
      } catch (InterruptedException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
   
//    try
//    {
//      while((inputFromServer=in.readLine())!=null)
//      {
//        serverResponded=true;
//        System.out.println(inputFromServer);        
//      }
//    } catch (IOException e1)
//    {
//      e1.printStackTrace();
//    }

    try
    {
      out.close();
      in.close();
      mySocket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    System.out.println("clientUpdate2:"+inputFromServer);
  }
  
  public void firstSend(String output)//TODO unnecessary method?
  {
    outputToServer=output;
    out.println(outputToServer);
  }
  
  public synchronized String updateClientGameState(String updateString)
  {
    serverResponded=false;
    outputToServer=updateString;
    out.println(outputToServer);

    return inputFromServer;
  }
}