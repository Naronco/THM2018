package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Screen;

public class StateManager {
	private IState currentState;

	public void setState(IState state) {
		if (currentState == state)
			return;
		if (currentState != null)
			currentState.unload();
		state.load();
		currentState = state;
	}

	public void render(Screen screen) {
		if (currentState != null)
			currentState.render(screen);
	}

	public void update(double delta) {
		if (currentState != null)
			currentState.update(delta);
	}

	public IState getCurrentState() {
		return currentState;
	}
}
