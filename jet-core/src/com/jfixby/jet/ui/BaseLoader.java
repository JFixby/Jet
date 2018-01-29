
package com.jfixby.jet.ui;

import com.jfixby.r3.activity.api.ActivityManager;
import com.jfixby.r3.activity.api.ComponentsFactory;
import com.jfixby.r3.activity.api.layer.Layer;
import com.jfixby.r3.activity.api.scene.Scene2DComponent;
import com.jfixby.r3.activity.api.scene.Scene2DSpawningConfig;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.debug.StateSwitcher;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.api.taskman.Job;
import com.jfixby.scarabei.api.time.ResetableTimeStream;
import com.jfixby.scarabei.api.time.Time;

public class BaseLoader {
	private final StateSwitcher<BASE_LOADER_STATE> state;

	public BaseLoader () {
		this.state = Debug.newStateSwitcher(BASE_LOADER_STATE.UNKNOWN);
		this.state.setDebugFlag(true);
		this.state.setDebugName("tinto progress");
	}

	private Layer root;
	private Scene2DComponent scene;

	public void deploy (final ActivityManager unitManager, final ID scene_id) {
		this.root = unitManager.getComponentsFactory().newLayer();
		unitManager.getRootLayer().attachComponent(this.root);
		this.root.hide();

		final Scene2DSpawningConfig config = new Scene2DSpawningConfig();
		// config.setStructureID(Names.newID("com.jfixby.tinto.loader.psd"));
		config.structureID = (scene_id);
		Debug.checkNull(this.time);
		config.animationsTimeStream = this.time;

		final ComponentsFactory components_factory = unitManager.getComponentsFactory();

		this.scene = components_factory.getSceneDepartment().newScene(config);
		this.root.attachComponent(this.scene);

		this.state.switchState(BASE_LOADER_STATE.UNKNOWN);
		final List<Job> jobs_sequence = Collections.newList();
		final int N = 100;
// for (int i = 0; i <= N; i++) {
// final Job j = this.newStep(i, N);
// final Promise<Void> promise = TaskManager.executeAsynchronously("update progress", j);
// ;
// }

	}

	final ResetableTimeStream time = Time.newResetableTimeStream(Sys.SystemTime());;

	public void show () {

		this.root.show();

		this.time.reset();
		this.scene.startAllAnimations();

// this.deployUI();
// this.resetProgress();
// this.root.print();
	}

}
