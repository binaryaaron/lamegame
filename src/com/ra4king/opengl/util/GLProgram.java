package com.ra4king.opengl.util;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * @author Roi Atalla
 */
public abstract class GLProgram {
	private int fps;
	
	public GLProgram(boolean vsync) {
		try {
			Display.setFullscreen(true);
			Display.setVSyncEnabled(vsync);
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public GLProgram(String name, int width, int height, boolean resizable) {
		Display.setTitle(name);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		
		Display.setResizable(resizable);
		
		fps = 60;
	}
	
	public void setFPS(int fps) {
		this.fps = fps;
	}
	
	public int getFPS() {
		return fps;
	}
	
	public final void run() {
		run(false);
	}
	
	public final void run(boolean core) {
		run(core, new PixelFormat());
	}
	
	public final void run(boolean core, PixelFormat format) {
		run(format, core ? new ContextAttribs(3, 3).withProfileCore(true) : null);
	}
	
	public final void run(int major, int minor) {
		run(major, minor, false);
	}
	
	public final void run(int major, int minor, boolean core) {
		run(major, minor, core, new PixelFormat());
	}
	
	public final void run(int major, int minor, boolean core, PixelFormat format) {
		run(format, core ? new ContextAttribs(major, minor).withProfileCore(core) : new ContextAttribs(major, minor));
	}
	
	public final void run(PixelFormat format) {
		run(format, null);
	}
	
	public final void run(ContextAttribs attribs) {
		run(new PixelFormat(), attribs);
	}
	
	public final void run(PixelFormat format, ContextAttribs attribs) {
		try {
			Display.create(format, attribs);
		} catch(Exception exc) {
			exc.printStackTrace();
			System.exit(1);
		}
		
		gameLoop();
	}
	
	private void gameLoop() {
		try {
			init();
			
			Utils.checkGLError("init");
			
			resized();
			
			Utils.checkGLError("resized");
			
			long lastTime, lastFPS;
			lastTime = lastFPS = System.nanoTime();
			int frames = 0;
			
			long updateTime = 0, renderTime = 0;
			
			while(!Display.isCloseRequested() && !shouldStop()) {
				long deltaTime = System.nanoTime() - lastTime;
				lastTime += deltaTime;
				
				if(Display.wasResized())
					resized();
				
				while(Keyboard.next()) {
					if(Keyboard.getEventKeyState())
						keyPressed(Keyboard.getEventKey(), Keyboard.getEventCharacter());
					else
						keyReleased(Keyboard.getEventKey(), Keyboard.getEventCharacter());
				}
				
				long initial = System.nanoTime();
				update(deltaTime);
				updateTime += System.nanoTime() - initial;
				
				Utils.checkGLError("update");
				
				initial = System.nanoTime();
				render();
				
				Display.update();
				renderTime += System.nanoTime() - initial;

				Utils.checkGLError("render");
				
				frames++;
				if(System.nanoTime() - lastFPS >= 1e9) {
					System.out.println("FPS: ".concat(String.valueOf(frames)) + "\tUpdate: " + (updateTime / frames) + "ns/" + String.format("%.2fms", updateTime / (frames * 1e6)) + "\tRender: " + (renderTime / frames) + "ns/" + String.format("%.2fms", renderTime / (frames * 1e6)));
					lastFPS += 1e9;
					updateTime = renderTime = 0;
					
					frames = 0;
				}
				
				Display.sync(fps);
			}
		} catch(Throwable exc) {
			exc.printStackTrace();
		} finally {
			destroy();
		}
	}
	
	public int getWidth() {
		return Display.getWidth();
	}
	
	public int getHeight() {
		return Display.getHeight();
	}
	
	public abstract void init();
	
	public void resized() {
		glViewport(0, 0, getWidth(), getHeight());
	}
	
	public boolean shouldStop() {
		return Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
	}
	
	public void keyPressed(int key, char c) {}
	
	public void keyReleased(int key, char cs) {}
	
	public void update(long deltaTime) {}
	
	public abstract void render();
	
	public void destroy() {
		Display.destroy();
		System.exit(0);
	}
	
	protected String readFromFile(String file) {
		try {
			return Utils.readFully(getClass().getResourceAsStream(file));
		} catch(Exception exc) {
			throw new RuntimeException("Failure reading file " + file, exc);
		}
	}
}
