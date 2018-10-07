package com.naronco.thm2018.alley;

import com.deviotion.ld.eggine.graphics.Screen;
import com.naronco.thm2018.Sprite3D;
import com.naronco.thm2018.graphics.Viewport;

public class Sprite3DAlleyObject extends AlleyObject {
	private Sprite3D sprite;

	public Sprite3DAlleyObject(Sprite3D sprite) {
		this.sprite = sprite;
	}

	@Override
	public void render(Screen screen, Viewport viewport) {
		viewport.renderSprite3D(screen, sprite);
	}

	public Sprite3D getSprite() {
		return sprite;
	}

	public void setSprite(Sprite3D sprite) {
		this.sprite = sprite;
	}
}
