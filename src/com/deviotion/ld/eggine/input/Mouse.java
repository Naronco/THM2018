package com.deviotion.ld.eggine.input;

import com.deviotion.ld.eggine.math.Vector2d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public class Mouse implements MouseListener, MouseMotionListener {

    private int screenScale;
    private Vector2d clickLocation;
    private Vector2d currentLocation;
    private Vector2d scaledLocation;
    private boolean leftClicking;
    private boolean rightClicking;
    private boolean middleClicking;
    private boolean dragging;

    public Mouse (int screenScale) {
        this.screenScale = screenScale;
        this.clickLocation = new Vector2d(0, 0);
        this.currentLocation = new Vector2d(0, 0);
        this.scaledLocation = new Vector2d(0, 0);
        this.leftClicking = false;
        this.rightClicking = false;
        this.middleClicking = false;
        this.dragging = false;
    }

    public Vector2d getLocation () {
        return this.scaledLocation;
    }
    public Vector2d getClickLocation () {
        return this.clickLocation;
    }
    public boolean isLeftClicking () {
        return this.leftClicking;
    }
    public boolean isRightClicking () {
        return this.rightClicking;
    }
    public boolean isMiddleClicking () {
        return this.middleClicking;
    }
    public boolean isClicking () {
        return this.leftClicking || this.middleClicking || this.rightClicking;
    }
    public boolean isDragging () {
        return this.dragging;
    }

    @Override
    public void mouseClicked (MouseEvent event) {}

    @Override
    public void mousePressed (MouseEvent event) {
        this.clickLocation.setX(event.getX());
        this.clickLocation.setY(event.getY());

        if (event.getButton() == 1) {
            this.leftClicking = true;
        } else if (event.getButton() == 3) {
            this.rightClicking = true;
        } else if (event.getButton() == 2) {
            this.middleClicking = true;
        }
    }

    @Override
    public void mouseReleased (MouseEvent event) {
        if (event.getButton() == 1) {
            this.leftClicking = false;
        } else if (event.getButton() == 3) {
            this.rightClicking = false;
        } else if (event.getButton() == 2) {
            this.middleClicking = false;
        }

        this.dragging = false;
    }

    @Override
    public void mouseEntered (MouseEvent event) {}

    @Override
    public void mouseExited (MouseEvent event) {}

    @Override
    public void mouseDragged (MouseEvent event) {
        this.mouseMoved(event);
    }

    @Override
    public void mouseMoved (MouseEvent event) {
        this.currentLocation.setX(event.getX());
        this.currentLocation.setY(event.getY());

        this.scaledLocation.setX(event.getX() / this.screenScale);
        this.scaledLocation.setY(event.getY() / this.screenScale);

        if (this.isClicking() && this.clickLocation.distance(this
                .currentLocation) > 0) {
            this.dragging = true;

            this.leftClicking = false;
            this.rightClicking = false;
            this.middleClicking = false;
        }
    }

}