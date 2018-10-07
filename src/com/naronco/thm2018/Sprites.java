package com.naronco.thm2018;

import com.deviotion.ld.eggine.graphics.Sprite;

import java.io.File;

public class Sprites {
	public static final Sprite car = new Sprite(new File("res/car.png"), 0xffff00ff);
	public static final Sprite gulli = new Sprite(new File("res/gulli.png"), 0xffff00ff);
	public static final Sprite trafficLight = new Sprite(new File("res/traffic_light.png"), 0xffff00ff);
	public static final Sprite lights = new Sprite(new File("res/lights.png"), 0xffff00ff);
	public static final Sprite lightsLeft = new Sprite(new File("res/lights-left.png"), 0xffff00ff);
	public static final Sprite optionsbg = new Sprite(new File("res/options-menu-bg.png"));
	public static final Sprite startbg = new Sprite(new File("res/start-menu-bg.png"));
	public static final Sprite winbg = new Sprite(new File("res/win-bg.png"));
	public static final Sprite failbg = new Sprite(new File("res/fail-bg.png"));
	public static final Sprite hdtext = new Sprite(new File("res/hdtexts.png"), 0xff000000);
}
