package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Screen;

public interface IState {
	void load();

	void unload();

	void render(Screen screen);

	void update(double delta);
}
