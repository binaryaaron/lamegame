import java.awt.Point;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class ServerPackage implements Serializable
{ 
  public final int PACKID;
  public Point currentP;
  public Point prevP;
    
  public ServerPackage(int ID)
  {
   PACKID=ID;
   currentP=new Point(255,255);
   prevP=new Point(255,255); 
  }
}
