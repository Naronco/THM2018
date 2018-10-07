package com.deviotion.ld.eggine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 */

public class Keyboard implements KeyListener {

	private boolean[] keys;
	private KeyListener activeListener;

	private List<KeyEvent> queuedTyped = new ArrayList<>();
	private List<KeyEvent> queuedPressed = new ArrayList<>();
	private List<KeyEvent> queuedReleased = new ArrayList<>();

	public Keyboard() {
		this.keys = new boolean[65000];
	}

	public void processQueued() {
		if (activeListener != null) {
			for (KeyEvent typed : queuedTyped)
				activeListener.keyTyped(typed);
			for (KeyEvent pressed : queuedPressed)
				activeListener.keyPressed(pressed);
			for (KeyEvent released : queuedReleased)
				activeListener.keyReleased(released);

			queuedTyped.clear();
			queuedPressed.clear();
			queuedReleased.clear();
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
		queuedTyped.add(event);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		this.keys[event.getKeyCode()] = true;
		queuedPressed.add(event);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		this.keys[event.getKeyCode()] = false;
		queuedReleased.add(event);
	}

	public boolean isPressed(int... keys) {
		for (int key : keys) {
			if (!this.keys[key]) {
				return false;
			}
		}

		return true;
	}

	public KeyListener getActiveListener() {
		return activeListener;
	}

	public void setActiveListener(KeyListener activeListener) {
		this.activeListener = activeListener;
	}
}