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
    //help
    else if(buttonID == 1)
    {
      String controlsString = "Collect diamonds to win!!\n\n" +
          "CONTROLS:\n"+
          "WASD:: Turn\n"+
          "F:: Brake\n"+
          "Arrow Keys:: Move\n"+
          "QE:: Tilt\n"+
          "L-Control:: Sink\n"+
          "Space:: Rise\n"+
          "R-Shift:: Shoot\n"+
          "F1:: Fullscreen\n"+
          "F2:: Resolution\n"+
          "M:: Mute\n"+
          "Escape:: Exit Game";
      JOptionPane.showMessageDialog(null,controlsString);
      return 1;
    }
    //credits
    else if(buttonID == 2)
    {
      String creditsString = "\nAaron Gonzalez: Project Manager, Executive Help\n\n"+
          "Robert Nicholson: Graphics Co-Lead\n\n"+
          "Weston Ortiz: Physics Expert\n\n"+
          "Paige Stephen Romero: Graphics Lead\n\n"+
          "Hans Weeks: Server Master";
      JOptionPane.showMessageDialog(null,creditsString);
      
      return 2;
    }
    //exit
    else
    {
      return 3;
    }
  }
  
  /**
   * What happens when a button is clicked
   * @return
   */
  public static int choose()
  {
    return choose(currentButton);
  }
  
  /**
   * Moving up on the menu
   */
  public static void up()
  {
    currentButton--;
    if(currentButton < 0) currentButton = finalButtonID;
  }
  
  /**
   * Moving down on the menu
   */
  public static void down()
  {
    currentButton++;
    if(currentButton > finalButtonID) currentButton = 0;
  }
  
  /**
   * Initialize the menu
   * If we want to go back to the menu after losing, call this
   */
  public static void startMainMenu()
  {
    currentButton = 0;
  }

  /**
   * Returns the position of the button on a 1.0 to -1.0 point system
   * @return
   */
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
