package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;

public class WalkerServer
{  
  private static final int PORT=4444;
  private static ServerSocket myServerSocket;
  private static boolean listening=true;
  public static LinkedList<WalkerThread> threadList=new LinkedList<>();
  private static int IDgen=0;
  
  public static void main(String[] args) throws IOException
  {
    try
    {
      myServerSocket=new ServerSocket(PORT);
    } catch (IOException e)
    {
      System.err.println("Could not listen on port: "+PORT);
      System.exit(-1);
    }
    
    while(listening)
    {
      WalkerThread newThread=new WalkerThread(myServerSocket.accept(),IDgen);
      newThread.start();
      IDgen++;
      threadList.add(newThread);
      for(WalkerThread wt: threadList)
      {
        if(!wt.isAlive())
        {
          threadList.remove(wt);
        }
      }
    }
  }
}