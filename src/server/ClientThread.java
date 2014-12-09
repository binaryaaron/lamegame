package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*@Author Hans Weeks
 * 
 * Client side thread object. Created by the MainLoopClient. Creates socket
 * to connect to host. Sends keyboard information to ServerThread as a String
 * and receives game-state to draw from ServerThread as a String.*/

public class ClientThread extends Thread
{
  public boolean printlocation = false;
  public static String hostName = "localhost"
      + "";
  public static int socketVal = 4444;
  public int ID=-1;

  Socket mySocket = null;
  PrintWriter out = null;
  BufferedReader in = null;

  private volatile String inputFromServer = "";
  private static final boolean DEBUG = false;

  /* Constructor 
   * Input:socketVal and hostName as String[] args
   * Description:creates socket, opens print writer and buffered reader over
   * socket and starts thread.*/
  public ClientThread(String[] args) throws IOException
  {
    if (args.length > 0)
    {
      hostName = args[0];
    }

    try
    {
      mySocket = new Socket(hostName, socketVal);
      out = new PrintWriter(mySocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
    }
    catch (UnknownHostException e)
    {
      System.err.println("Don't know about host: " + hostName);
      System.exit(1);
    }
    catch (IOException e)
    {
      System.err.println("Couldn't get I/O for the connection to: " + hostName);
      System.exit(1);
    }

    this.start();
  }

  /* Run 
   * Description: listens to input from ServerThread. First input from
   * Server is int value of clientID (same values as ServerThread's ID). Updates
   * value of String inputFromServer.*/
  public void run()
  {
//    try
//    {
//      System.out.println("catching ID");
//      ID = Integer.parseInt(in.readLine());
//      System.out.println("ID=" + ID);
//    }
//    catch (NumberFormatException e2)
//    {
//      e2.printStackTrace();
//    }
//    catch (IOException e2)
//    {
//      e2.printStackTrace();
//    }

    try
    {
      while ((inputFromServer = in.readLine()) != null)
      {
        if(ID==-1)
        {
          ID=Integer.parseInt(inputFromServer.substring(0,1));
          System.out.println("caught ID "+ID);
        }
        if (DEBUG) System.out.println("input from server: " + inputFromServer);
      }
    }
    catch (IOException e1)
    {
      e1.printStackTrace();
    }

    try
    {
      out.close();
      in.close();
      mySocket.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /* sendToServer
   * Input:String representation of clients keyboard input to send
   * to Server. Description:called by MainLoopClient to update ServerThread of
   * User input on client side.*/
  public void sendToServer(String toSend)
  {
    out.println(toSend);
  }

  /* getInputFromServer
   * Output: String input from server
   * Description: returns most recent String from ServerThread. Does not wait
   * for a new input from ServerThread.*/
  public synchronized String getInputFromServer()
  {
    while (inputFromServer=="")
    {
      //wait for input to be initialized
    }
    return inputFromServer.substring(2);
  }

  public static void setConnection(String server, int socket)
  {
    hostName = server;
    socketVal = socket;
  }
}