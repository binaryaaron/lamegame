package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class WalkerClient extends Thread
{
  public boolean printlocation=false;
  public static String hostName="localhost";
  public static int socketVal=4444;
  public int ID;
  
  Socket mySocket=null;
  PrintWriter out=null;
  BufferedReader in=null;
  
  private volatile String inputFromServer="";
  private String outputToServer;

//  public static void main(String[] args) throws IOException
//  {
//    new WalkerClient(args);
//  }

  public WalkerClient(String[] args) throws IOException
  {
    if(args.length>0)
    {
      hostName=args[1];
    }

    try
    {
      mySocket=new Socket(hostName, socketVal);
      out=new PrintWriter(mySocket.getOutputStream(),true);
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
    
    this.start();
  }
  
  public void run()
  {
    try
    {
      System.out.println("catching ID");
      ID=Integer.parseInt(in.readLine());
      System.out.println("ID="+ID);
    }
    catch (NumberFormatException e2)
    {
      e2.printStackTrace();
    }
    catch (IOException e2)
    {
      e2.printStackTrace();
    }
   
    try
    {
      while((inputFromServer=in.readLine())!=null)
      {
//        System.out.println(inputFromServer);
      }
    } catch (IOException e1)
    {
      e1.printStackTrace();
    }

    try
    {
      out.close();
      in.close();
      mySocket.close();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
//    System.out.println("clientUpdate2:"+inputFromServer);
  }

  public void sendToServer(String toSend)
  {
    out.println(toSend);
  }
  
  public static void setConnection(String server, int socket)
  {
    hostName = server;
    socketVal = socket;
  }
  
  public void firstSend(String output)//TODO unnecessary method?
  {
    outputToServer=output;
    out.println(outputToServer);
  }
  
  public synchronized String getInputFromServer()
  {
    return inputFromServer;
  }
}