package com.naronco.thm2018;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.*;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Dimension2i;
import com.deviotion.ld.eggine.sound.Sound;
import com.naronco.thm2018.state.*;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import org.urish.openal.ALException;
import org.urish.openal.OpenAL;
import org.urish.openal.Source;
import org.urish.openal.SourceState;
import sun.reflect.annotation.ExceptionProxy;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Game extends Eggine {
	private StateManager stateManager = new StateManager();

	private GameState game;
	private MainMenuState mainMenu;
	private OptionsState options;
	private WinState winScene;
	private FailState lostScene;

	private OpenAL openal;

	private Source titlescreen, titlescreenHD, optionsmusic, race, winmusic, failmusic;

	private Source crashSound;

	private static final Dimension2i baseDimension = new Dimension2i(160, 120);
	private static final int scale = 6;

	public Game(OpenAL openal) {
		super(60, 60, new Window("InfinityJam", new Dimension2i(baseDimension.getWidth() * scale, baseDimension.getHeight() * scale), 1));
		this.openal = openal;

		titlescreenHD = loadMusic("res/titlescreenrev.wav");
		play(titlescreenHD);

		titlescreen = loadMusic("res/titlescreen.wav");
		race = loadMusic("res/race.wav");
		setLooping(race);
		winmusic = loadMusic("res/wintrack.wav");
		failmusic = loadMusic("res/options.wav");
		optionsmusic = loadMusic("res/options.wav");
		setLooping(optionsmusic);

		crashSound = loadMusic("res/crash.wav");

		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			System.out.println("Welcome " + user.username + "#" + user.discriminator + "!");
			game.setDiscordReady(true);
		}).build();

		DiscordRPC.discordInitialize("498190625544470528", handlers, true);

		this.game = new GameState(baseDimension, this);
		System.out.println("Created game");

		this.mainMenu = new MainMenuState(this);
		this.options = new OptionsState(this);
		this.winScene = new WinState(this);
		this.lostScene = new FailState(this);

		this.getWindow().getCanvas().requestFocus();

		stateManager.setState(mainMenu);
	}

	public void playCrash() {
		play(crashSound);
	}

	private void setLooping(Source optionsmusic) {
		try {
			if (optionsmusic != null)
				optionsmusic.setLooping(true);
		} catch (ALException e) {
			e.printStackTrace();
		}
	}

	private Source loadMusic(String name) {
		try {
			if (openal == null)
				return null;
			return openal.createSource(new File(name));
		} catch (ALException | IOException | UnsupportedAudioFileException ignore) {
			// try twice
			try {
				if (openal == null)
					return null;
				return openal.createSource(new File(name));
			} catch (ALException | IOException | UnsupportedAudioFileException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	private void play(Source source) {
		if (!Options.enableSound)
			return;
		try {
			if (source != null)
				source.play();
		} catch (ALException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(Screen screen) {
		stateManager.render(screen);
	}

	@Override
	public void update(double delta) {
		if (delta > 1 / 30.0) // slow down below 20fps
			delta = 1 / 30.0;
		getKeyboard().processQueued();
		stateManager.update(delta);
	}

	public void openGameScene() {
		getWindow().resize(baseDimension, scale);
		stateManager.setState(game);

		stopMusic();
		play(race);
	}

	public void openMainMenu() {
		getWindow().resize(new Dimension2i(baseDimension.getWidth() * scale, baseDimension.getHeight() * scale), 1);
		stateManager.setState(mainMenu);

		stopMusic();
		play(titlescreen);
	}

	public void openOptions() {
		getWindow().resize(new Dimension2i(baseDimension.getWidth() * scale, baseDimension.getHeight() * scale), 1);
		stateManager.setState(options);

		stopMusic();
		play(optionsmusic);
	}

	public void openWinScene(int badTurns) {
		getWindow().resize(new Dimension2i(baseDimension.getWidth() * scale, baseDimension.getHeight() * scale), 1);
		winScene.setBadTurns(badTurns);
		stateManager.setState(winScene);

		stopMusic();
		play(winmusic);
	}

	public void openFailScene() {
		getWindow().resize(new Dimension2i(baseDimension.getWidth() * scale, baseDimension.getHeight() * scale), 1);
		stateManager.setState(lostScene);

		stopMusic();
		play(failmusic);
	}

	private void stopMusic() {
		stopMusic(optionsmusic);
		stopMusic(titlescreenHD);
		stopMusic(titlescreen);
		stopMusic(race);
	}

	private void stopMusic(Source source) {
		try {
			if (source != null)
				source.stop();
		} catch (ALException e) {
			// works anyway
		}
	}

	public void enableSound(boolean enableSound) {
		stopMusic();
		if (enableSound) {
			if (stateManager.getCurrentState() == game) {
				play(race);
			} else if (stateManager.getCurrentState() == options) {
				play(optionsmusic);
			} else if (stateManager.getCurrentState() == mainMenu) {
				play(titlescreenHD);
			}
		}
	}
}
