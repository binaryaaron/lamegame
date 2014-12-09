package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;

/*@Author Hans Weeks
 * 
 * Main server object. Listens for Client Connection and creates ServerThreads
 * to communicate with ClientThreads as they connect. Created by MainLoopServer
 */

public class ServerMaster extends Thread
{  
  private static final int PORT=4444;
  private static ServerSocket myServerSocket;
  private static boolean listening=true;
  public static LinkedList<ServerThread> threadList=new LinkedList<>();
  public static ArrayList<String> inputFromClient=new ArrayList<>();  
  private static int IDgen=0;
 
  /*Constructor
   * starts ServerMaster Thread, and waits for first connection before returning*/
  public ServerMaster() throws IOException
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
  
  /*run
   * Description: listens for new client Connections. Removes dead
   * ServerThreads from threadList.
   */
  public void run()
  {
    while(listening)
    {
      ServerThread newThread=null;
      try
      {
        newThread=new ServerThread(myServerSocket.accept(),IDgen);
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
        ServerThread wt=threadList.get(i);
        if(!wt.isAlive())
        {
          threadList.remove(wt);
        }
      }
    }
  }
  
  /*getNumConnections
   * Description: synchronized method returns size of threadList*/
  public synchronized int getNumConnections()
  {
    return threadList.size();
  }
}