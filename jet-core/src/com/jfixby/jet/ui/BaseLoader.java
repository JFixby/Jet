
package com.jfixby.jet.ui;

import com.jfixby.r3.activity.api.ActivityManager;
import com.jfixby.r3.activity.api.ComponentsFactory;
import com.jfixby.r3.activity.api.camera.CanvasCamera;
import com.jfixby.r3.activity.api.camera.CanvasCameraSpecs;
import com.jfixby.r3.activity.api.camera.SIMPLE_CAMERA_POLICY;
import com.jfixby.r3.activity.api.camera.ScreenDimentions;
import com.jfixby.r3.activity.api.layer.Layer;
import com.jfixby.r3.activity.api.scene.Scene2DComponent;
import com.jfixby.r3.activity.api.scene.Scene2DSpawningConfig;
import com.jfixby.r3.activity.api.user.ScreenChangeListener;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.debug.StateSwitcher;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.api.taskman.Job;
import com.jfixby.scarabei.api.time.ResetableTimeStream;
import com.jfixby.scarabei.api.time.Time;

public class BaseLoader implements ScreenChangeListener {
	private final StateSwitcher<BASE_LOADER_STATE> state;

	public BaseLoader () {
		this.state = Debug.newStateSwitcher(BASE_LOADER_STATE.UNKNOWN);
		this.state.setDebugFlag(true);
		this.state.setDebugName("tinto progress");
	}

	private Layer root;
	private Scene2DComponent scene;
	private CanvasCamera camera;
	private BackGround bg;

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

		final CanvasCameraSpecs newCameraSpecs = components_factory.getCameraDepartment().newCameraSpecs();
		newCameraSpecs.setSimpleCameraPolicy(SIMPLE_CAMERA_POLICY.EXPAND_CAMERA_VIEWPORT_ON_SCREEN_RESIZE);
		this.camera = components_factory.getCameraDepartment().newCamera(newCameraSpecs);

		this.scene = components_factory.getSceneDepartment().newScene(config);
// root.attachComponent(attachment);
		this.root.attachComponent(this);
		this.root.attachComponent(this.scene);

// scene.getRoot().findComponent(relative);
		{
// final RelativePath comp_path = Utils.newRelativePath("top_bar");
// final Layer root_layer = this.scene.getRoot().findComponent(comp_path);
// this.topBar = new TopBar(root_layer);
		}

		{
// final RelativePath comp_path = Utils.newRelativePath("bg/bg");
// final Raster comp = this.scene.getRoot().findComponent(comp_path);
// this.bg = new BackGround(comp);
		}

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
		this.root.print();
// Sys.exit();
	}

	@Override
	public void onScreenChanged (final ScreenDimentions viewport_update) {
// this.topBar.onScreenUpdate(viewport_update);
// this.bg.onScreenUpdate(viewport_update);
	}

}
