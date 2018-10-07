package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.Font;
import com.deviotion.ld.eggine.graphics.Screen;
import com.naronco.thm2018.Game;
import com.naronco.thm2018.Sprites;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainMenuState implements IState, KeyListener {
	private Game game;

	private static final int MENU_LENGTH = 3;
	private int menuIndex;

	public MainMenuState(Game game) {
		this.game = game;
	}

	@Override
	public void load() {
		menuIndex = 0;
		game.getKeyboard().setActiveListener(this);
	}

	@Override
	public void unload() {
		game.getKeyboard().setActiveListener(null);
	}

	@Override
	public void render(Screen screen) {
		screen.renderSprite(0, 0, Sprites.startbg);
		screen.renderText(115 - 12 * menuIndex, 200 + 58 * menuIndex, Font.standard, ">");
	}

	private void clickItem(int index) {
		switch (index) {
			case 0:
				game.openGameScene();
				break;
			case 1:
				game.openOptions();
				break;
			case 2:
				game.close();
				break;
			default:
				throw new IllegalArgumentException("Index out of menu bounds");
		}
	}

	@Override
	public void update(double delta) {
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
			menuIndex = (menuIndex + MENU_LENGTH - 1) % MENU_LENGTH;
		} else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
			menuIndex = (menuIndex + 1) % MENU_LENGTH;
		} else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
			clickItem(menuIndex);
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
	}
}
