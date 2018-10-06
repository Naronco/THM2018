package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Screen;

public class StateManager {
	private IState currentIState;

	public void setState(IState IState) {
		if (currentIState != null)
			currentIState.unload();
		IState.load();
		currentIState = IState;
	}

	public void render(Screen screen) {
		if (currentIState != null)
			currentIState.render(screen);
	}

	public void update(double delta) {
		if (currentIState != null)
			currentIState.update(delta);
	}

	public IState getCurrentState() {
		return currentIState;
	}
}
