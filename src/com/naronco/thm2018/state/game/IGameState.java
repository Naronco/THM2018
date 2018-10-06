package com.naronco.thm2018.state.game;

import com.deviotion.ld.eggine.graphics.Screen;
import com.naronco.thm2018.state.IState;

public interface IGameState extends IState {
	int getFloorColor(double x, double y);
	
	int getCeilingColor(double x, double y);

	void render2D(Screen screen);
}
