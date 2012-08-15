package org.frans.thesis.musicapp;

import javazoom.jl.decoder.JavaLayerException;

import org.frans.thesis.GUI.CFComponent;
import org.frans.thesis.GUI.CFComponentMenu;
import org.frans.thesis.GUI.CFComponentMenuItemListener;
import org.frans.thesis.GUI.CFScene;
import org.frans.thesis.service.CFFile;
import org.mt4j.MTApplication;

public class CFSong extends CFComponent {

	private CFFile file;

	// private String imagePath = "org" + MTApplication.separator + "frans"
	// + MTApplication.separator + "thesis" + MTApplication.separator
	// + "GUI" + MTApplication.separator + "data"
	// + MTApplication.separator;
	private SoundJLayer sound;

	public CFSong(MTApplication application, CFScene scene, CFFile file) {
		super(application, scene);
		this.file = file;
		System.out.println(file.getFile().getPath());
		sound = new SoundJLayer(file.getFile().getPath());
		this.setNoStroke(true);
		this.setNoFill(true);
		this.createMenu();
		this.getComponentMenu().setVisible(true);
	}

	private void createMenu() {
		this.setComponentMenu(new CFComponentMenu(this, this.getCFScene()
				.getMTApplication()));
		this.getComponentMenu().addMenuItem("play.png",
				new CFComponentMenuItemListener() {

					@Override
					public void processEvent() {
						sound.pauseToggle();
					}
				});
	}

	public CFFile getFile() {
		return file;
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
