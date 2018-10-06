package com.naronco.thm2018;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.*;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.graphics.IViewportDataSource;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.state.GameState;
import com.naronco.thm2018.state.StateManager;

public class Game extends Eggine {
	private StateManager stateManager = new StateManager();

	private GameState game;

	public Game() {
		super(60, 60, new Window("InfinityJam", new Dimension2d(160, 120), 4));

		this.game = new GameState(getWindow().getDimension());

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
