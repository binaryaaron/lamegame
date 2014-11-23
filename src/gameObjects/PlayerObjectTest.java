package gameObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerObjectTest
{
  PlayerObject p1;
  PlayerObject p2;


  @Before public void setUp() throws Exception
  {
     p1 = new PlayerObject();
     p2 = new PlayerObject();
  }

  @After public void tearDown() throws Exception
  {
    p1.removePlayer();
    p2.removePlayer();
  }

  @Test public void testGetPosition() throws Exception
  {


  }

  @Test public void testSetPosition() throws Exception
  {

  }

  @Test public void testGetVelocity() throws Exception
  {
    assertEquals(p1.getVelocity().getX(), 0, 0);
  }

  @Test public void testSetVelocity() throws Exception
  {

  }

  @Test public void testIncreaseVelocity() throws Exception
  {
  }




  @Test public void testGetPlayerId() throws Exception
  {
    System.out.println(p1);
    assertTrue(p1.getPlayerId() == 1);
    assertTrue(p2.getPlayerId() == 2);
  }

  @Test public void testCompareTo() throws Exception
  {

  }

  @Test public void testToString() throws Exception
  {
    p1.toString();

  }
}