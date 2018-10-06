package com.naronco.thm2018.graphics;

public interface IViewportDataSource {
	int getSkyColor();
	
	int getFloorColor(double x, double y);
	
	int getCeilingColor(double x, double y);
}
