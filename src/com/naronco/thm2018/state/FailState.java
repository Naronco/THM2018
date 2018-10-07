package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Screen;
import com.naronco.thm2018.Game;
import com.naronco.thm2018.Sprites;

import java.awt.event.KeyEvent;

public class FailState implements IState {
	private Game game;

	public FailState(Game game) {
		this.game = game;
	}

	@Override
	public void load() {
	}

	@Override
	public void unload() {
	}

	@Override
	public void render(Screen screen) {
		screen.renderSprite(0, 0, Sprites.failbg);
	}

	@Override
	public void update(double delta) {
		if (game.getKeyboard().isPressed(KeyEvent.VK_ESCAPE)) {
			game.openMainMenu();
		}
	}
}
