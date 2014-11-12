import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class WalkerClient
{
  public static String hostName = "Manticore";//This is default name, overwrite with command line argument such as IP address of Server
  public static int socketVal = 4444;//arbitrary value, does not have any service associated
  private static Point location = new Point(1, 1);
  private static Picture myPicture;
  
  public static void main(String[] args)
    throws IOException
  {
    myPicture = new Picture(500, 500);
    Graphics myGraphics = myPicture.getOffScreenGraphics();
    myGraphics.setColor(Color.white);
    myGraphics.fillRect(0, 0, 500, 500);
    
    Random rand = new Random();
    Socket mySocket = null;
    
    ObjectOutputStream outToServer = null;
    ObjectInputStream inFromServer = null;

    if(args.length>0)//change hostName by commandLine
    {
      hostName=args[0];
    }
    
    try//open client socket, and in/out object writers
    {
      mySocket = new Socket(hostName, socketVal);
      outToServer = new ObjectOutputStream(mySocket.getOutputStream());
      inFromServer = new ObjectInputStream(mySocket.getInputStream());
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
    
    String toServer = "1:1";//holds x,y coordinates as x:y    
    outToServer.writeObject(toServer);
    
    try
    {
      String fromServer;//receives x:y String from server
      while ((fromServer = (String)inFromServer.readObject()) != null)
      {
        //parse fromServer String into x and y values
        int seperator = fromServer.indexOf(':');
        String xString = fromServer.substring(0, seperator);
        String yString = fromServer.substring(seperator + 1);
        location.x = Integer.parseInt(xString);
        location.y = Integer.parseInt(yString);
        System.out.println("Server:(" + xString + "," + yString + ")");
        
        //draw current location
        myGraphics.setColor(Color.black);
        myGraphics.fillRect(location.x - 1, location.y - 1, 3, 3);
        myGraphics.setColor(Color.cyan);
        myGraphics.fillRect(location.x, location.y, 1, 1);
        myPicture.repaint();
        
        //generate step in x and y as -1,0,1 values
        int xStep = rand.nextInt(3) - 1;
        int yStep = rand.nextInt(3) - 1;
        location.x += xStep;
        location.y += yStep;
        toServer = location.x + ":" + location.y;
        //send location request to server
        outToServer.writeObject(toServer);
      }
    }
    catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    outToServer.close();
    inFromServer.close();    
    mySocket.close();
  }
}
