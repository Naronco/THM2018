package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Font;
import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.graphics.SpriteSheet;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.thm2018.Game;
import com.naronco.thm2018.Options;
import com.naronco.thm2018.Sprites;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OptionsState implements IState, KeyListener {
	private Game game;

	private static final int MENU_LENGTH = 3;
	private int menuIndex;

	private SpriteSheet texts;

	public OptionsState(Game game) {
		this.game = game;

		texts = new SpriteSheet(Sprites.hdtext, new Dimension2d(256, 60));
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
		screen.renderSprite(0, 0, Sprites.optionsbg);
		if (menuIndex == 2) {
			screen.renderText(25, 52, Font.standard, ">");
		} else {
			screen.renderText(50 - menuIndex * 12, 175 + menuIndex * 70, Font.standard, "< >");
		}

		screen.renderSpriteTile(331, 148, texts, Options.difficulty);
		screen.renderSpriteTile(251, 215, texts, 2 + (Options.enableSound ? 0 : 1));
	}

	@Override
	public void update(double delta) {

	}

	private void clickItem(int index) {
		if (index == 2) {
			game.openMainMenu();
		} else moveMenu(index, 1);
	}

	private void moveMenu(int index, int direction) {
		if (index == 0) {
			Options.difficulty = (Options.difficulty + 2 + direction) % 2;
		} else if (index == 1) {
			Options.enableSound = !Options.enableSound;
			game.enableSound(Options.enableSound);
		}
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
		} else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
			moveMenu(menuIndex, -1);
		} else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
			moveMenu(menuIndex, 1);
		} else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
			clickItem(menuIndex);
		} else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
			clickItem(2);
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
	}
}
