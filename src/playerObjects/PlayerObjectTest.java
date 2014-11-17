package playerObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;

import static org.junit.Assert.*;

public class PlayerObjectTest
{
  Player p1;
  Player p2;


  @Before public void setUp() throws Exception
  {
     p1 = new PlayerObject();
     p2 = new PlayerObject();
  }

  @After public void tearDown() throws Exception
  {
    p1 = null;
    p2 = null;
  }

  @Test public void testGetHealth() throws Exception
  {
    assertTrue(p1.getHealth() >= 0);
  }

  @Test public void testSetHealth() throws Exception
  {
    p1.setHealth((short)0);
    assertTrue(p1.getHealth() == 0);


  }

  @Test public void testGetPosition() throws Exception
  {


  }

  @Test public void testSetPosition() throws Exception
  {

  }

  @Test public void testGetVelocity() throws Exception
  {

  }

  @Test public void testSetVelocity() throws Exception
  {

  }

  @Test public void testIncreaseVelocity() throws Exception
  {
  }




  @Test public void testGetPlayerId() throws Exception
  {
    assertTrue(p1.getPlayerId() == 1);
    assertTrue(p2.getPlayerId() == 2);
  }

  @Test public void testCompareTo() throws Exception
  {

  }

  @Test public void testToString() throws Exception
  {

  }
}