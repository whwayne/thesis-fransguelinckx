package org.frans.thesis.musicapp;

import javazoom.jl.decoder.JavaLayerException;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFComponentMenu;
import org.frans.thesis.GUI.CFComponentMenuItemListener;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFFile;
import org.mt4j.MTApplication;

/**
 * A simple interactive component to represent an mp3-file on the tabletop GUI.
 */
public class CFSong extends CFComponent {

	/**
	 * The path the the image of a musical note.
	 */
	private String imagePath = "org" + MTApplication.separator + "frans"
			+ MTApplication.separator + "thesis" + MTApplication.separator
			+ "GUI" + MTApplication.separator + "data"
			+ MTApplication.separator + "music.png";

	/**
	 * The actual music file of this song.
	 */
	private CFFile musicFile;

	/**
	 * The class that is responsible for playing and pausing the song.
	 */
	private SoundJLayer player;

	/**
	 * Public constructor for this class. Sets up the player, the interactive
	 * component and the play-button.
	 * 
	 * @param scene
	 *            The scene to which this song belongs.
	 * @param musicFile
	 *            The mp3-file.
	 */
	public CFSong(CFScene scene, CFFile musicFile) {
		super(scene);
		this.musicFile = musicFile;
		this.setImage(imagePath);
		this.player = new SoundJLayer(musicFile.getFile().getPath());
		this.createMenu();
	}

	/**
	 * Creates the menu, which has only one button to start/stop the song.
	 */
	private void createMenu() {
		this.setComponentMenu(new CFComponentMenu(this));
		this.getComponentMenu().addMenuItem("play.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						player.play();
					}
				});
		this.getComponentMenu().addMenuItem("pause.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						player.pauseToggle();
					}
				});
		this.getComponentMenu().repositionMenuItemsInCircle();
		this.getComponentMenu().setVisible(true);
	}

	/**
	 * Returns the mp3-file.
	 */
	public CFFile getFile() {
		return musicFile;
	}

	/**
	 * Returns the player of CFSong.
	 */
	public SoundJLayer getPlayer() {
		return player;
	}

	@Override
	public void handleDroppedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRotatedCFComponent(CFComponent component) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleScaledCFComponent(CFComponent component) {
		// TODO Auto-generated method stub

	}

}

/**
 * Class that can play and pause MP3-files. Source:
 * http://thiscouldbebetter.wordpress
 * .com/2011/07/04/pausing-an-mp3-file-using-jlayer/
 * http://pastebin.com/yZnCa6Nx http://pastebin.com/2K5Bbw4g
 */
class SoundJLayer implements Runnable {
	private static class PlaybackListener extends
			JLayerPlayerPausable.PlaybackAdapter {
		@Override
		public void playbackFinished(
				JLayerPlayerPausable.PlaybackEvent playbackEvent) {
			System.err.println("PlaybackStopped()");
		}

		@Override
		public void playbackPaused(
				JLayerPlayerPausable.PlaybackEvent playbackEvent) {
			System.err.println("PlaybackPaused()");
		}

		// PlaybackListener members
		@Override
		public void playbackStarted(
				JLayerPlayerPausable.PlaybackEvent playbackEvent) {
			System.err.println("PlaybackStarted()");
		}
	}

	private String filePath;
	private String namePlayerThread = "AudioPlayerThread";
	private PlaybackListener playbackListener = new PlaybackListener();
	private JLayerPlayerPausable player;

	private Thread playerThread;

	public SoundJLayer(String filePath) {
		this.filePath = filePath;
	}

	public SoundJLayer(String filePath, String namePlayerThread) {
		this.filePath = filePath;
		this.namePlayerThread = namePlayerThread;
	}

	public void pause() {
		if (this.player != null) {
			this.player.pause();

			if (this.playerThread != null) {
				// this.playerThread.stop(); //unsafe method
				this.playerThread = null;
			}
		}
	}

	public void pauseToggle() {
		if (this.player == null) {
			this.play();
		} else {
			if (this.player.isPaused() && !this.player.isStopped()) {
				this.play();
			} else {
				this.pause();
			}
		}
	}

	public void play() {
		if (this.player == null) {
			this.playerInitialize();
		} else if (!this.player.isPaused() || this.player.isComplete()
				|| this.player.isStopped()) {
			this.stop();
			this.playerInitialize();
		}
		this.playerThread = new Thread(this, namePlayerThread);
		this.playerThread.setDaemon(true);

		this.playerThread.start();
	}

	private void playerInitialize() {
		try {
			this.player = new JLayerPlayerPausable(this.filePath);
			this.player.setPlaybackListener(this.playbackListener);
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
	}

	// IRunnable members
	@Override
	public void run() {
		try {
			this.player.resume();
		} catch (javazoom.jl.decoder.JavaLayerException ex) {
			ex.printStackTrace();
		}
	}

	public void stop() {
		if (this.player != null) {
			this.player.stop();

			if (this.playerThread != null) {
				// this.playerThread.stop(); //unsafe method
				this.playerThread = null;
			}
		}
	}
}
