package renderEngine;

import java.awt.Font;
import java.io.InputStream;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class TextDraw
{
  public int fps;
  private String text;
  private int x;
  private int y;
  private Color color;
  private boolean antiAlias = false;
  private static final Font awtFont = new Font("Times New Roman", Font.BOLD, 36);
  private static TrueTypeFont font = new TrueTypeFont(awtFont, false);

  public TextDraw()
  {
    text = "";
    fps = 0;
    x = 0;
    y = 0;
    color = Color.black;

  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public int getX()
  {
    return x;
  }

  public void setX(int x)
  {
    this.x = x;
  }

  public int getY()
  {
    return y;
  }

  public void setY(int y)
  {
    this.y = y;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public TrueTypeFont getFont()
  {
    return font;
  }

  public void init()
  {

    // font = new TrueTypeFont(awtFont,antiAlias);

  }

  public void render()
  {

    // font = new TrueTypeFont(awtFont,antiAlias);

    GL11.glDrawBuffer(GL11.GL_BACK);
    GL11.glEnable(GL11.GL_TEXTURE_2D);

    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glPushMatrix();
    GL11.glLoadIdentity();
    GL11.glOrtho(0, 800, 600, 0, 1, -1);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glDisable(GL11.GL_CULL_FACE);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDisable(GL11.GL_LIGHTING);
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    GL11.glLoadIdentity();
    // Color.white.bind();
    font.drawString(x, y, text);

    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glPopMatrix();
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    // GL11.glDisable(GL11.GL_BLEND);
    GL11.glDrawBuffer(GL11.GL_FRONT_AND_BACK);
  }

}
