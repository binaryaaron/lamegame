package engineTester;

import javax.swing.JOptionPane;


public class Menu
{
  //track the display height
  private static int currentButton = 0;
  private static int finalButtonID = 3;
  
  /**
   * What happens when a player chooses a menu option
   * return <0 for exit
   * @param buttonID
   * @return
   */
  public static int choose(int buttonID)
  {
    //connect
    if(buttonID == 0)
    {
      //open JTextField panel
      //JFrame frame = new JFrame();
      String serverIP = JOptionPane.showInputDialog("Enter host name:");
      String socketString = JOptionPane.showInputDialog("Enter socket value:");
      int socket = 0;
      try
      {
        socket = Integer.valueOf(socketString);
        MainLoopClient.setConnection(serverIP, socket);
        return 1;
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      MainLoopClient.setConnection(serverIP, socket);
    }
    //options
    else if(buttonID == 1)
    {
      
    }
    //credits
    else if(buttonID == 2)
    {
      //
    }
    //exit
    else
    {
      return -1;
    }
    return 0;
  }
  
  public static int choose()
  {
    return choose(currentButton);
  }
  
  public static void up()
  {
    currentButton--;
    if(currentButton < 0) currentButton = finalButtonID;
  }
  
  public static void down()
  {
    currentButton++;
    if(currentButton > finalButtonID) currentButton = 0;
  }
  
  public static void startMainMenu()
  {
    currentButton = 0;
  }

  public static float getYPos()
  {
    return (float) ((1.0/(finalButtonID)*currentButton)-0.5f)*0.5f;
  }
  /**
   * The current button ID
   * @return
   */
  public static int getID()
  {
    return currentButton;
  }
}
