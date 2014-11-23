package server;

import java.awt.Point;


public class StringUtility
{
  public static String pointToString(Point p)
  {
    String s=p.x+":"+p.y+"#";
    return s;
  }
  
  public static Point stringToPoint(String s)//make this modify string (pop off each point as it goes)
  {
    Point p=new Point();
    int seperator = s.indexOf(':');//TODO make sure indexOf returns first index
    int endchar = s.indexOf('#');
    
    String xString = s.substring(0, seperator);
    String yString = s.substring(seperator + 1,endchar);
    
    p.x = Integer.parseInt(xString);
    p.y = Integer.parseInt(yString);
    
    s=s.substring(endchar+1);//test this to make sure its modifying the original string
    return p;
  }
}
