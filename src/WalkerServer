import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;

public class WalkerServer
{
  private static final int PORT=4444;
  private static ServerSocket myServerSocket;
  private static boolean listening=true;

  public static void main(String[] args) throws IOException
  {
    try
    {
      myServerSocket=new ServerSocket(4444);
    } catch (IOException e)
    {
      System.err.println("Could not listen on port: 4444");
      System.exit(-1);
    }
    while(listening)
    {
      new WalkerThread(myServerSocket.accept()).start();//create new thread for each accepted connection and run it
    }
  }
}
