package net;

import java.awt.Point;
import java.awt.image.ReplicateScaleFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.instrument.Instrumentation;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class WalkerThread extends Thread
{ 
  public boolean printlocation=true;
  public boolean printclients=false;
  private String inputFromClient;
//  private LinkedList<Point> playersLocationList=new LinkedList<>();
  private String outputToClient;
  
  private Socket myClientSocket = null;
  PrintWriter out=null;
  BufferedReader in=null;

  public WalkerThread(Socket mySocket, int ID)
  {
    super("WalkerThread");
    this.myClientSocket = mySocket;
  }

  public void run()
  {    
    try
    {
      out=new PrintWriter(myClientSocket.getOutputStream());
      in=new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
      int loop = 0;
      
      while((inputFromClient=in.readLine())!=null)
      {
        loop++;
        //for initial server test, call the getter method to update from this loop
        outputToClient=inputFromClient;//test version, this is not testing or updating the input, just bouncing it
        System.out.println("input from client= " +inputFromClient);
        out.println(outputToClient);
      }      
    } 
    catch (java.net.SocketException e)
    {
      try
      {
        myClientSocket.close();
        return;
      } catch (IOException e1)
      {
        e1.printStackTrace();
      }
    }catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
  }
  
  public String updateServerGameState(String updateString)//TODO maybe call this a getter
  {
    outputToClient=updateString;
    out.println(outputToClient);
    return inputFromClient;
  }
}
