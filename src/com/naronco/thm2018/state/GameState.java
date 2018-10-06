package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.graphics.IViewportDataSource;
import com.naronco.thm2018.graphics.Viewport;

public class GameState implements IState, IViewportDataSource {
	StateManager parts = new StateManager();
	private Viewport viewport;

	private Vector2d carPos = new Vector2d(0, 0);

	double time = 0;

	public GameState() {
		this.viewport = new Viewport();
		viewport.setDataSource(this);
	}

	@Override
	public void load() {
		parts.setState(new DecisionGameState(this));
	}

	@Override
	public void unload() {
	}

	@Override
	public void render(Screen screen) {
		viewport.render(screen);

		parts.render(screen);
	}

	@Override
	public void update(double delta) {
		time += delta;

		parts.update(delta);

		Vector2d camPos = carPos.add(new Vector2d(Math.sin(-viewport.getRotation()), -Math.cos(-viewport.getRotation())).multiply(7));
		viewport.setCameraPosition(camPos);
	}

	@Override
	public int getSkyColor() {
		return 0x0080ff;
	}

	public int getBaseFloorColor(double x, double y) {
		double stripeLength = 5.0;

		if (x <= 4) {
			boolean isStripe = (x < 0.4) && ((int) Math.floor(y / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : 0x696A6A;
		} else {
			return 0x00ff00;
		}
	}

	@Override
	public int getFloorColor(double x, double y) {
		return ((IGameState) parts.getCurrentState()).getFloorColor(x, y);
	}

	public Viewport getViewport() {
		return viewport;
	}

	public Vector2d getCarPos() {
		return carPos;
	}

	public void setCarPos(Vector2d carPos) {
		this.carPos = carPos;
	}
}
