package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*@Author Hans Weeks
 * 
 * Server side thread object. Created by the ServerMaster. Handed a socket
 * to a ClientThread. Sends game-state information to ServerThread as a String
 * and receives user keyboard input.*/

public class ServerThread extends Thread
{
  public boolean printlocation = true;
  public boolean printclients = false;
  public String inputFromClient = null;
  public String outputToClient = null;
  public int ID;

  private Socket myClientSocket = null;
  PrintWriter out = null;
  BufferedReader in = null;

  /*Constructor
   * Input: socket from ServerMaster and in ID number
   * Description: called from ServerMaster with socket. Does not call run
   *  (ServerMaster does)*/
  public ServerThread(Socket mySocket, int ID)
  {
    super("WalkerThread");
    this.myClientSocket = mySocket;
    this.ID = ID;
  }

  /* run
   * Description: Creates PrintWriter and BufferedReader to Client.
   */
  public void run()
  {
    try
    {
      out = new PrintWriter(myClientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(
          myClientSocket.getInputStream()));

      while ((inputFromClient = in.readLine()) != null)
      {
        try
        {
          Thread.sleep(17);// check for input 60 times a second
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    }
    catch (java.net.SocketException e)
    {
      try
      {
        myClientSocket.close();
        return;
      }
      catch (IOException e1)
      {
        e1.printStackTrace();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
  }

  /* updateServerGameState
   * Input: String updateString to send to client
   * Description: sends a string representation of all objects to render to
   *  the ClientThread. Called by MainLoopServer.*/
  public void updateServerGameState(String updateString)
  {
    while (out == null)
    {
      /* wait for print writer to initialize */
    }
    out.println(updateString);
  }

  /* getClientInput
   * Output: String input from client
   * Description: returns most recent String from ClientThread. Does not wait
   * for a new input from ClientThread.*/
  public String getClientInput()
  {
    return inputFromClient;
  }
}
