import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
  
  public  ServerPackage myServerPackage=null;
  private static LinkedList<ServerPackage> allPackageList=new LinkedList<>();
  
  private static Point location=new Point(1, 1);
  private final  int xBound=500;
  private final  int yBound=500;
  private int myID;

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
    ObjectOutputStream outToServer=null;
    ObjectInputStream inFromServer=null;
    if(args.length>0)
    {
      hostName=args[0];
    }
    
    try
    {
      mySocket=new Socket(hostName, socketVal);
      outToServer=new ObjectOutputStream(mySocket.getOutputStream());
      inFromServer=new ObjectInputStream(mySocket.getInputStream());
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
    boolean firstPackage=true;
    Point startPoint=new Point(255,255);
    myServerPackage=new ServerPackage(-1);//-1 represents unknown ID number
    myServerPackage.currentP=startPoint;
    outToServer.writeObject(myServerPackage);
    try
    {
      while((allPackageList=(LinkedList<ServerPackage>) inFromServer.readObject())!=null)
      {
        if(firstPackage)//get ID from first Package
        {
          myID=allPackageList.getFirst().PACKID;
          firstPackage=false;
        }
        myServerPackage=allPackageList.getFirst();
        location.x=myServerPackage.currentP.x;
        location.y=myServerPackage.currentP.y;
        if(printlocation)System.out.println("Server:("+location.x+","+location.y+")");
        
        //////////draw map        
        //other players
        for (ServerPackage sp : allPackageList)
        {
          System.out.println("elements to draw: " + allPackageList.size());
//          if(sp.PACKID==myID)break;//don't redraw current player
          Point point = sp.currentP;
          myGraphics.setColor(Color.GREEN);
          myGraphics.fillRect(point.x - 1, point.y - 1, 3, 3);
          myGraphics.setColor(Color.RED);
          myGraphics.fillRect(point.x, point.y, 1, 1);
        }
      //this player
        myGraphics.setColor(Color.DARK_GRAY);
        myGraphics.fillRect(location.x-1, location.y-1, 3, 3);
        myGraphics.setColor(Color.CYAN);
        myGraphics.fillRect(location.x, location.y, 1, 1);
        myPicture.repaint();
        ////
        
        int xStep=rand.nextInt(3)-1;//create new random x/y steps of -1,0, or 1
        int yStep=rand.nextInt(3)-1;
        Point newLoc=new Point(location.x+xStep,location.y+yStep);//add steps to new point
        myServerPackage.prevP.x=myServerPackage.currentP.x;//pass currentP to prevP
        myServerPackage.prevP.y=myServerPackage.currentP.y;
        myServerPackage.currentP.x=newLoc.x;//update currentP
        myServerPackage.currentP.y=newLoc.y;
        
        outToServer.writeUnshared(myServerPackage);
//        outToServer.reset();
      }
    } catch (NumberFormatException e)
    {
      e.printStackTrace();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    outToServer.close();
    inFromServer.close();
    mySocket.close();
  }
}
