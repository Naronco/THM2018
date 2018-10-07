package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Font;
import com.deviotion.ld.eggine.graphics.Screen;
import com.naronco.thm2018.Game;
import com.naronco.thm2018.Sprites;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WinState implements IState, KeyListener {
	private Game game;
	private double animation;
	private int badTurns;

	public WinState(Game game) {
		this.game = game;
	}

	@Override
	public void load() {
		animation = 0;
		game.getKeyboard().setActiveListener(this);
	}

	@Override
	public void unload() {
		game.getKeyboard().setActiveListener(null);
	}

	@Override
	public void render(Screen screen) {
		screen.renderSprite(0, 0, Sprites.winbg);
		screen.renderText(32, 32, Font.standard, "You Win!");
		screen.renderText(32, 48, Font.standard, "You must know the town very well.");
		screen.renderText(32, 64, Font.standard, "Bad turns: " + badTurns);
	}

	@Override
	public void update(double delta) {
		animation += delta;
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
			game.openMainMenu();
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
	}

	public void setBadTurns(int badTurns) {
		this.badTurns = badTurns;
	}
}
