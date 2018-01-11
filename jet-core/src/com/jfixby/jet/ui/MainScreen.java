
package com.jfixby.jet.ui;

import com.jfixby.r3.activity.api.ActivityManager;
import com.jfixby.r3.activity.api.DefaultUnit;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;

public class MainScreen extends DefaultUnit {
	final BaseLoader baseLoader = new BaseLoader();
// public static final ID preloader_scene_id = Names.newID("com.jfixby.tinto.ui.loader.base.psd");

	@Override
	public void onCreate (final ActivityManager unitManager) {
		super.onCreate(unitManager);
		final ID preloader_scene_id = Names.newID("com.jfixby.jet.patch-9").child("psd");
		this.baseLoader.deploy(unitManager, preloader_scene_id);
		this.baseLoader.show();
	}

	@Override
	public void onStart () {
		super.onStart();
	}

	@Override
	public void onResume () {
		super.onResume();
	}

	@Override
	public void onPause () {
		super.onPause();
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
	}
}
