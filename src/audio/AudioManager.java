package audio;

import java.io.IOException;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

public class AudioManager 
{
  /** The ogg sound effect */
  private static Audio oggEffect;
  /** The ogg song */
  private static Audio songStream;
  /** Completely silent song */
  private static Audio silentStream;
  private static Audio laser01;
  private static boolean mute = false;
  /**
   * Initialise resources
   * Thanks to LWJGL tutorial
   */
  public static void createAudio() 
  {

    try 
    {
      // you can play oggs by loading the complete thing into
      // a sound
      laser01 = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/sounds/laser01.ogg"));
      // or setting up a stream to read from. Note that the argument becomes
      // a URL here so it can be reopened when the stream is complete. Probably
      // should have reset the stream by thats not how the original stuff worked
      songStream = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("res/sounds/Ouroboros.ogg"));
      silentStream = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("res/sounds/Silence.ogg"));
    } 
    catch (IOException e) 
    {
      e.printStackTrace();
    }
  }

  /**
   * Game loop update
   */
  public static void updateAudio() 
  {
    SoundStore.get().poll(0);
  }
  
  /**
   * Plays the cool song
   * If called again, restarts the song
   */
  public static void playMusic()
  {

    songStream.playAsMusic(1.0f, 1.0f, true);
  }
  
  /**
   * Switches sound on or off
   */
  public static void muteOrUnmute()
  {
    if(!mute)
    {
      mute = true;
      //overwrites the audio stream with a silent audio file
      silentStream.playAsMusic(1.0f, 1.0f, false);
    }
    else
    {
      mute = false;
      playMusic();
    }
  }

  /**
   * close audio when done with the  program
   */
  public static void closeAudio()
  {
    AL.destroy();
  }
  
  /**
   * Plays a laser sound effect if unmuted, at a semi-random pitch
   */
  public static void playRandomLaser()
  {
    if(!mute)
    {
      float pitchRand = (float)(Math.random()*0.15f);
      laser01.playAsSoundEffect(1.0f-pitchRand, 1.0f, false);
    }
  }
}