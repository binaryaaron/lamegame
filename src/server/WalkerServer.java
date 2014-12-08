package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;

public class WalkerServer extends Thread
{  
  private static final int PORT=4444;
  private static ServerSocket myServerSocket;
  private static boolean listening=true;
  public static LinkedList<WalkerThread> threadList=new LinkedList<>();
  public static ArrayList<String> inputFromClient=new ArrayList<>();
  
  private static int IDgen=0;
 
  public WalkerServer() throws IOException
  {
    try
    {
      myServerSocket=new ServerSocket(PORT);
    } catch (IOException e)
    {
      System.err.println("Could not listen on port: "+PORT);
      System.exit(-1);
    }
    
    this.start();
    while ((getNumConnections())<1)
    {//don't return until server gets a connection
    }
  }
  
  public void run()
  {
    while(listening)
    {
      WalkerThread newThread=null;
      try
      {
        newThread=new WalkerThread(myServerSocket.accept(),IDgen);
        String input="";
        String output="";
        inputFromClient.add(input);
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      
      System.out.println("socket connection accepted "+IDgen);
      newThread.start();
      IDgen++;
      threadList.add(newThread);
      for(int i=0; i<threadList.size();i++)
      {
        WalkerThread wt=threadList.get(i);
        if(!wt.isAlive())
        {
          threadList.remove(wt);
        }
      }
    }
  }
  
  public synchronized int getNumConnections()
  {
    return threadList.size();
  }
}