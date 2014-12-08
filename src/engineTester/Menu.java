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
        return 0;
      }
      catch(Exception e)
      {
        System.out.println("Incorrect server input");
        return -1;
      }
    }
    //options
    else if(buttonID == 1)
    {
      String controlsString = "CONTROLS:\n"+
          "WASD:: Turn\n"+
          "B:: Brake\n"+
          "Arrow Keys:: Move\n"+
          "QE:: Tilt\n"+
          "L-Control:: Sink\n"+
          "Space:: Rise\n"+
          "R-Shift:: Shoot\n"+
          "F1:: Fullscreen\n"+
          "F2:: Resolution\n"+
          "Escape:: Exit Game";
      JOptionPane.showMessageDialog(null,controlsString);
      return 1;
    }
    //credits
    else if(buttonID == 2)
    {
      String creditsString = "\nAaron Gonzalez: Git Manager, Misc. Help\n\n"+
          "Robert Nicholson: Graphics Co-Lead\n\n"+
          "Weston Ortiz: Physics Expert\n\n"+
          "Paige Stephen Romero: Graphics Lead\n\n"+
          "Hans Week: Server Master";
      JOptionPane.showMessageDialog(null,creditsString);
      
      return 2;
    }
    //exit
    else
    {
      return 3;
    }
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
