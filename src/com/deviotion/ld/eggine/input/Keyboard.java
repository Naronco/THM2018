package com.deviotion.ld.eggine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public class Keyboard implements KeyListener {

    private boolean[] keys;

    public Keyboard () {
        this.keys = new boolean[65000];
    }

    @Override
    public void keyTyped(KeyEvent event) {}

    @Override
    public void keyPressed(KeyEvent event) {
        this.keys[event.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent event) {
        this.keys[event.getKeyCode()] = false;
    }

    public boolean isPressed(int... keys) {
        for (int key : keys) {
            if (!this.keys[key]) {
                return false;
            }
        }

        return true;
    }

}