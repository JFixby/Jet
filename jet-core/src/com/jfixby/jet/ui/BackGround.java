
package com.jfixby.jet.ui;

import com.jfixby.r3.activity.api.camera.ScreenDimentions;
import com.jfixby.r3.activity.api.raster.Raster;

public class BackGround {

	private final Raster comp;

	public BackGround (final Raster comp) {
		this.comp = comp;
	}

	public void onScreenUpdate (final ScreenDimentions viewport_update) {
		this.comp.setWidth(viewport_update.getScreenWidth());
		this.comp.setHeight(viewport_update.getScreenHeight());
	}

}
