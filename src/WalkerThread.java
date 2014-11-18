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
  private String inputLine;
  private String playerLocations;
  
  private Socket myClientSocket = null;
  private final int xBound = 500;
  private final int yBound = 500;
  private Point reqLocation = null;//requested location from client
  private Point currLocation= new Point();

  public WalkerThread(Socket mySocket, int ID)
  {
    super("WalkerThread");
    this.myClientSocket = mySocket;
  }

  public void run()
  {    
    try
    {
      PrintWriter out = new PrintWriter(myClientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
      int loop = 0;
      
      while((inputLine=in.readLine())!=null)
      {
        reqLocation=StringUtility.stringToPoint(inputLine);
        
        if(printlocation)System.out.println(loop+".) Client("+reqLocation.x+","+reqLocation.y+")");
        
        if ((reqLocation.x > 0) && (reqLocation.x < xBound) && (reqLocation.y > 0) && (reqLocation.y < yBound))
        {
          currLocation.x=reqLocation.x;
          currLocation.y=reqLocation.y;
        }
        else
        {//else new location is out of bounds, revert to previous location
          
        }
        
        //TODO make this a separate utility function
        playerLocations=StringUtility.pointToString(currLocation);
        synchronized(this)
        {
          for(WalkerThread wt:WalkerServer.threadList)
          {
            playerLocations+=StringUtility.pointToString(wt.currLocation);
          }
        }
        
        if(printclients)
          {
            int hostsConnected=playerLocations.length()/4;
            System.out.println("hosts connected: "+hostsConnected);
          }
        
        out.println(playerLocations);
        loop++;
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
}
