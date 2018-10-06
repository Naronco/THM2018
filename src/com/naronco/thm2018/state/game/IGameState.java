package com.naronco.thm2018.state.game;

import com.naronco.thm2018.state.IState;

public interface IGameState extends IState {
	int getFloorColor(double x, double y);
}
