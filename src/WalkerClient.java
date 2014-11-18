import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.AllPermission;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;

public class WalkerClient
{
  public boolean printlocation=false;
  public static String hostName="Manticore";
  public static int socketVal=4444;
  
  private final  int xBound=500;
  private final  int yBound=500;
  
  private Point reqLocation = null;//requested location from client
  private Point currLocation= null;
  private String playerLocations;
  String outputS;
  private LinkedList<Point> playerLocList=new LinkedList<>();

  public static void main(String[] args) throws IOException
  {
    new WalkerClient(args);
  }
  
  public WalkerClient(String[] args) throws IOException
  {
    Picture myPicture=new Picture(xBound,yBound);
    Graphics myGraphics=myPicture.getOffScreenGraphics();
    myGraphics.setColor(Color.WHITE);
    myGraphics.fillRect(0, 0, xBound, yBound);
    
    //TODO consider adding an ID number to each instance of Client
    Random rand=new Random();
    Socket mySocket=null;
    PrintWriter out=null;
    BufferedReader in=null;
    
    if(args.length>0)
    {
      hostName=args[0];
    }
    
    try
    {
      mySocket=new Socket(hostName, socketVal);
      out=new PrintWriter(mySocket.getOutputStream(), true);
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
    Point startPoint=new Point(255,255);
    outputS=StringUtility.pointToString(startPoint);
    out.println(outputS);
    
    try
    {
      while((playerLocations=in.readLine())!=null)
      {
        final long startTime = System.currentTimeMillis();
        currLocation=StringUtility.stringToPoint(playerLocations);
        playerLocations=StringUtility.popString(playerLocations);
        
        if(printlocation)System.out.println("Server:("+currLocation.x+","+currLocation.y+")");
        
        playerLocList.clear();
        while(playerLocations.length()>0)
        {          
          Point p = StringUtility.stringToPoint(playerLocations);
          playerLocations=StringUtility.popString(playerLocations);
          playerLocList.add(p);
        }
        
        //////////draw map        
        //other players
        for(Point p:playerLocList)
        {
          myGraphics.setColor(Color.GREEN);
          myGraphics.fillRect(p.x - 1, p.y - 1, 3, 3);
          myGraphics.setColor(Color.RED);
          myGraphics.fillRect(p.x, p.y, 1, 1);
        }
        //this player
        myGraphics.setColor(Color.DARK_GRAY);
        myGraphics.fillRect(currLocation.x-1, currLocation.y-1, 3, 3);
        myGraphics.setColor(Color.CYAN);
        myGraphics.fillRect(currLocation.x, currLocation.y, 1, 1);
        myPicture.repaint();
        ////
        
        int xStep=rand.nextInt(3)-1;//create new random x/y steps of -1,0, or 1
        int yStep=rand.nextInt(3)-1;
        reqLocation=new Point(currLocation.x+xStep,currLocation.y+yStep);//add steps to new point
        outputS=StringUtility.pointToString(reqLocation);
        
        out.println(outputS);
        final long endTime=System.currentTimeMillis();
        System.out.println("Total execution time: "+(endTime-startTime));
      }
    } catch (NumberFormatException e)
    {
      e.printStackTrace();
    }
    out.close();
    in.close();
    mySocket.close();
  }
}
