import java.awt.Point;
import java.awt.image.ReplicateScaleFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.instrument.Instrumentation;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class WalkerThread extends Thread
{ 
  public boolean printlocation=true;
  public boolean printclients=false;
  public ServerPackage myServerPackage=null;//contains info for connected client
  private LinkedList<ServerPackage> allPackageList=new LinkedList<>();
  
  private Socket myClientSocket = null;
  private final int xBound = 500;
  private final int yBound = 500;
  private Point reqLocation = null;

  public WalkerThread(Socket mySocket, int ID)
  {
    super("WalkerThread");
    this.myClientSocket = mySocket;
    myServerPackage=new ServerPackage(ID);
  }

  public void run()
  {    
    try
    {
      ObjectOutputStream outToClient=new ObjectOutputStream(myClientSocket.getOutputStream());
      ObjectInputStream inFromClient=new ObjectInputStream(myClientSocket.getInputStream());
      int loop = 0;
      
      while((myServerPackage=(ServerPackage)inFromClient.readObject())!=null)
      {        
        reqLocation=myServerPackage.currentP;
        if(printlocation)System.out.println("Client("+reqLocation.x+","+reqLocation.y+")");
        if(printlocation)System.out.println("loop " + loop);
        
        if ((reqLocation.x > 0) && (reqLocation.x < xBound) && (reqLocation.y > 0) && (reqLocation.y < yBound))
        {
          //if program gets here myServerPackage.currentP is in bounds, no change needed
        }
        else
        {//else new location is out of bounds, revert to previous location
          myServerPackage.currentP.x=myServerPackage.prevP.x;//if the location is out of bounds, pop it off the list, leaving the last valid location
          myServerPackage.currentP.y=myServerPackage.prevP.y;
        }
        
        ////send ServerPackages of all clients to this client
        allPackageList.clear();
        
        
        synchronized (myServerPackage)
        {
          for(WalkerThread wt:WalkerServer.threadList)
          {
            allPackageList.add(wt.myServerPackage);
          }
        }
        allPackageList.addFirst(myServerPackage);
        // outToClient.writeObject(allPackageList);
        if(printclients)System.out.println("hosts connected: "+allPackageList.size());
        System.out.println("size of allPackageList"+MemoryUtil.shallowSizeOf(allPackageList));
        outToClient.writeUnshared(allPackageList);

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
    catch (NumberFormatException | ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }
}
