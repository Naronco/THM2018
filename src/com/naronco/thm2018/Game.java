package com.naronco.thm2018;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.*;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.thm2018.state.GameState;
import com.naronco.thm2018.state.StateManager;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;

public class Game extends Eggine {
	private StateManager stateManager = new StateManager();

	private GameState game;

	public Game() {
		super(60, 60, new Window("InfinityJam", new Dimension2d(160, 120), 6));

		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			System.out.println("Welcome " + user.username + "#" + user.discriminator + "!");
			game.setDiscordReady(true);
		}).build();
		DiscordRPC.discordInitialize("498190625544470528", handlers, true);

		this.game = new GameState(getWindow().getDimension(), getKeyboard());
		System.out.println("Created game");

		stateManager.setState(game);
	}

	@Override
	public void render(Screen screen) {
		stateManager.render(screen);
	}

	@Override
	public void update(double delta) {
		stateManager.update(delta);
	}
}
