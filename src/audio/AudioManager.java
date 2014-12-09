package audio;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

public class AudioManager 
{
  /** The ogg sound effect */
  private static Audio oggEffect;
  /** The wav sound effect */
  private static Audio wavEffect;
  /** The aif source effect */
  private static Audio aifEffect;
  /** The ogg stream thats been loaded */
  private static Audio songStream;
  /** The mod stream thats been loaded */
  private static Audio modStream;

  private static Audio laser01;

  /**
   * Initialise resources
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

      //oggStream.playAsMusic(1.0f, 1.0f, true);
     
      // can load mods (XM, MOD) using ibxm which is then played through OpenAL. MODs
      // are always streamed based on the way IBXM works
      //modStream = AudioLoader.getStreamingAudio("MOD", ResourceLoader.getResource("res/sounds/SMB-X.XM"));

      // playing as music uses that reserved source to play the sound. The first
      // two arguments are pitch and gain, the boolean is whether to loop the content
      //modStream.playAsMusic(1.0f, 1.0f, true);

      // you can play aifs by loading the complete thing into
      // a sound
      //aifEffect = AudioLoader.getAudio("AIF", ResourceLoader.getResourceAsStream("res/sounds/burp.aif"));

      // you can play wavs by loading the complete thing into
      // a sound
      //wavEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/sounds/laser01.wav"));
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
//    while (Keyboard.next()) 
//    {
//      if (Keyboard.getEventKeyState()) 
//      {
//        if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) 
//        {
//          // play as a one off sound effect
//          playRandomLaser();
//        }
//        if (Keyboard.getEventKey() == Keyboard.KEY_R) 
//        {
//          // replace the music thats currently playing with
//          // the ogg
//          
//        }
//      }
//    }
    // polling is required to allow streaming to get a chance to
    // queue buffers.
    
    SoundStore.get().poll(0);
  }
public static void playMusic(){
	
	songStream.playAsMusic(1.0f, 1.0f, true);
}
  
  
  public static void closeAudio()
  {
    AL.destroy();
  }
  public static void playRandomLaser()
  {
    float pitchRand = (float)(Math.random()*0.15f);
    laser01.playAsSoundEffect(1.0f-pitchRand, 1.0f, false);
  }
}