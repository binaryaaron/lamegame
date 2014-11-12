import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class WalkerThread
  extends Thread
{
  private Socket myClientSocket = null;
  private final int xBound = 500;
  private final int yBound = 500;
  private Point location = new Point(1, 1);
  private Point reqLocation = new Point(1, 1);
  
  public WalkerThread(Socket mySocket)
  {
    super("WalkerThread");
    this.myClientSocket = mySocket;
  }
  
  public void run()
  {
    try
    {
      ObjectOutputStream outToClient = new ObjectOutputStream(this.myClientSocket.getOutputStream());
      ObjectInputStream inFromClient = new ObjectInputStream(this.myClientSocket.getInputStream());
      int loop = 0;
      String inputLine;
      
      //main communication loop
      while ((inputLine = (String)inFromClient.readObject()) != null)
      {
        System.out.println("loop " + loop);
        //parse location request from server into x and y values
        int seperator = inputLine.indexOf(':');
        String xString = inputLine.substring(0, seperator);
        String yString = inputLine.substring(seperator + 1);
        this.reqLocation.x = Integer.parseInt(xString);
        this.reqLocation.y = Integer.parseInt(yString);
        //check requested location for valid (must be greater than 0 and less than frame size)
        if ((this.reqLocation.x > 0) && (this.reqLocation.x < 500) && (this.reqLocation.y > 0) &&(this.reqLocation.y < 500))
        {
          //if valid, update location
          this.location.x = this.reqLocation.x;
          this.location.y = this.reqLocation.y;
        }
        //return location to client
        String outputLine = this.location.x + ":" + this.location.y;        
        outToClient.writeObject(outputLine);
        loop++;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (NumberFormatException|ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }
}
