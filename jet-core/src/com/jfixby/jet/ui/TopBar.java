
package com.jfixby.jet.ui;

import com.jfixby.r3.activity.api.camera.ScreenDimentions;
import com.jfixby.r3.activity.api.layer.Layer;
import com.jfixby.r3.activity.api.raster.Raster;

public class TopBar {

	private final Layer top_bar_layer;
	private final Raster l3;

	public TopBar (final Layer top_bar_layer) {
		this.top_bar_layer = top_bar_layer;
		this.l3 = top_bar_layer.findComponent("Layer 3");
	}

	public void onScreenUpdate (final ScreenDimentions viewport_update) {
		this.l3.setWidth(viewport_update.getScreenWidth());
		this.l3.setHeight(50);
	}

}
