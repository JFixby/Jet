
package com.jfixby.jet.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.FokkerLwjglApplication;
import com.badlogic.gdx.backends.lwjgl.FokkerLwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.jfixby.jet.asset.JetLocalAssets;
import com.jfixby.r3.activity.api.act.UIEventsManager;
import com.jfixby.r3.activity.api.spawn.ActivitySpawner;
import com.jfixby.r3.activity.red.RedActivityExecutor;
import com.jfixby.r3.activity.red.act.RedUIManager;
import com.jfixby.r3.activity.red.raster.TiledRasterReader;
import com.jfixby.r3.activity.red.scene.Scene2DPackageLoader;
import com.jfixby.r3.activity.red.spawn.RedActivitySpawner;
import com.jfixby.r3.engine.api.RedTriplane;
import com.jfixby.r3.engine.api.render.R3_SYSTEM_PARAMS;
import com.jfixby.r3.engine.api.render.RenderMachine;
import com.jfixby.r3.fokker.Fokker;
import com.jfixby.r3.fokker.adaptor.GdxAdaptor;
import com.jfixby.r3.fokker.adaptor.cfg.FokkerStarterConfigAsset;
import com.jfixby.r3.fokker.adaptor.cfg.FokkerStarterConfigReader;
import com.jfixby.r3.fokker.api.FOKKER_SYSTEM_ASSETS;
import com.jfixby.r3.fokker.api.FokkerEngineParams;
import com.jfixby.r3.fokker.font.api.FokkerFonts;
import com.jfixby.r3.fokker.font.red.RedFokkerFonts;
import com.jfixby.r3.fokker.render.FokkerRenderMachine;
import com.jfixby.r3.fokker.shader.api.FokkerShaders;
import com.jfixby.r3.fokker.shader.red.RedFokkerShaders;
import com.jfixby.r3.fokker.texture.api.FokkerTextures;
import com.jfixby.r3.fokker.texture.red.RedFokkerTextures;
import com.jfixby.r3.rana.api.asset.AssetHandler;
import com.jfixby.r3.rana.api.asset.AssetsConsumer;
import com.jfixby.r3.rana.api.asset.LoadedAssets;
import com.jfixby.r3.rana.api.loader.PackagesLoader;
import com.jfixby.r3.rana.api.manager.AssetsManager;
import com.jfixby.r3.rana.api.manager.AssetsPurgeResult;
import com.jfixby.r3.rana.api.pkg.FileSystemBankSettings;
import com.jfixby.r3.rana.api.pkg.PackagesBank;
import com.jfixby.r3.rana.api.pkg.PackagesManager;
import com.jfixby.r3.rana.api.pkg.PackagesManagerConfig;
import com.jfixby.r3.rana.red.asset.RedLoadedAssets;
import com.jfixby.r3.rana.red.loader.RedPackagesLoader;
import com.jfixby.r3.rana.red.manager.RedAssetsManager;
import com.jfixby.r3.rana.red.pkg.bank.RedPackageManager;
import com.jfixby.r3.rana.red.pkg.bank.RedResourcesManagerSpecs;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileSystemSandBox;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.java.gc.GCFisher;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;
import com.jfixby.scarabei.api.sys.settings.ExecutionMode;
import com.jfixby.scarabei.api.sys.settings.SystemSettings;
import com.jfixby.scarabei.api.ver.Version;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.filesystem.sandbox.RedFileSystemSandBox;

public class RunJetDesktop {
	public static void main (final String[] arg) throws Throwable {
		ScarabeiDesktop.deploy();

		LoadedAssets.installComponent(new RedLoadedAssets());
		AssetsManager.installComponent(new RedAssetsManager());
		PackagesLoader.installComponent(new RedPackagesLoader());
		FileSystemSandBox.installComponent(new RedFileSystemSandBox());

		final RedResourcesManagerSpecs resman_spec = new RedResourcesManagerSpecs();

		final File assets_cache_folder = LocalFileSystem.ApplicationHome().child("assets-cache");
		final File assets_folder = LocalFileSystem.ApplicationHome().child("assets");

		resman_spec.assets_cache_folder = assets_cache_folder;
		PackagesManager.installComponent(new RedPackageManager(resman_spec));

		{// install banks from assets folder
			final Collection<FileSystemBankSettings> assetsFoldebanks = PackagesManager.findBanks(assets_folder);
			final Collection<PackagesBank> assetsFolderbanks = PackagesManager.loadBanks(assetsFoldebanks);
			PackagesManager.installBanks(assetsFolderbanks);
		}
		{// install banks stated in config
			final PackagesManagerConfig config = PackagesManager.readPackagesManagerConfig();
			final Collection<PackagesBank> localBanks = PackagesManager.loadBanks(config.localBanks());
			PackagesManager.installBanks(localBanks);
		}

// PackagesManager.printAllResources();
// PackagesManager.invoke().printAllIndexes();

		final PackagesBank resources = PackagesManager.getBank(Names.newID(JetLocalAssets.LOCAL_BANK_NAME));
		resources.printAllIndexes();

		PackagesLoader.registerPackageReader(new FokkerStarterConfigReader());

		final ID starterID = Names.newID(JetLocalAssets.FOKKER_STARTER_ASSET_ID);

		final AssetsConsumer consumer = new AssetsConsumer() {};
		AssetsManager.autoResolveAsset(starterID);
		final AssetHandler starterAsset = LoadedAssets.obtainAsset(starterID, consumer);
		Debug.checkNull("starterAsset", starterAsset);
		L.d("starterAsset", starterAsset);
		final FokkerStarterConfigAsset starterConfig = starterAsset.asset();
		starterConfig.print();
		LoadedAssets.releaseAsset(starterAsset, consumer);
		final AssetsPurgeResult purgeResult = AssetsManager.purge();
		purgeResult.print();

		final GdxAdaptor adaptor = prepareGdxAdaptor();

		final FokkerLwjglApplicationConfiguration cfg = new FokkerLwjglApplicationConfiguration();

		cfg.title = starterConfig.getTitle();
		cfg.useGL30 = starterConfig.getUseLG30Flag();
		cfg.width = starterConfig.getWindowWidth();
		cfg.height = starterConfig.getWindowHeight();

		final ApplicationListener gdx_listener = adaptor.getGDXApplicationListener();

		new FokkerLwjglApplication(gdx_listener, cfg);

	}

	private static GdxAdaptor prepareGdxAdaptor () {
		SystemSettings.setExecutionMode(ExecutionMode.EARLY_DEVELOPMENT);
		SystemSettings.setFlag(R3_SYSTEM_PARAMS.PrintLogMessageOnMissingSprite, true);
		SystemSettings.setFlag(FokkerEngineParams.Settings.PrintLogMessageOnDuplicateAssetRequest, false);
		SystemSettings.setFlag(R3_SYSTEM_PARAMS.ExitOnMissingSprite, false);
		SystemSettings.setFlag(R3_SYSTEM_PARAMS.AllowMissingRaster, true);
		SystemSettings.setSystemAssetID(R3_SYSTEM_PARAMS.RASTER_IS_MISING, FOKKER_SYSTEM_ASSETS.RASTER_IS_MISING);

		SystemSettings.setStringParameter(FokkerEngineParams.FokkerTextureFilter.Mag, TextureFilter.Nearest + "");
		SystemSettings.setStringParameter(FokkerEngineParams.FokkerTextureFilter.Min, TextureFilter.Nearest + "");
		SystemSettings.setStringParameter(FokkerEngineParams.Assets.DefaultFont, "Arial");
		SystemSettings.setIntParameter(FokkerEngineParams.Assets.DEFAULT_LOGO_FADE_TIME, 2000L);
		SystemSettings.setStringParameter(FokkerEngineParams.Assets.CLEAR_SCREEN_COLOR_ARGB, "#FF070e0c");
		SystemSettings.setStringParameter(FokkerEngineParams.Assets.CLEAR_SCREEN_COLOR_ARGB, "#FFFF0eFc");
		SystemSettings.setIntParameter(GCFisher.DefaultBaitSize, 10 * 1024 * 1024);

		SystemSettings.setStringParameter(Version.Tags.PackageName, JetVersion.packageName);
		SystemSettings.setStringParameter(Version.Tags.VersionCode, JetVersion.versionCode + "");
		SystemSettings.setStringParameter(Version.Tags.VersionName, JetVersion.versionName);

		ActivitySpawner.installComponent(new RedActivitySpawner());

		RedTriplane.installComponent(new Fokker());
		FokkerShaders.installComponent(new RedFokkerShaders());
		FokkerTextures.installComponent(new RedFokkerTextures());
		RenderMachine.installComponent(new FokkerRenderMachine());
		FokkerFonts.installComponent(new RedFokkerFonts());

		UIEventsManager.installComponent(new RedUIManager());

// final ID starter = Names.newID("com.jfixby.tinto.ui.loader.TintoPreLoader");
// final ID starter = Names.newID("com.jfixby.jet.ui.MainScreen");
		final ID starter = Names.newID("com.jfixby.jet.ui.MainScreen");// com.jfixby.jet.patch-9.psd

		RedTriplane.setGameStarter(starter);

		PackagesLoader.registerPackageReader(new Scene2DPackageLoader());
		PackagesLoader.registerPackageReader(new TiledRasterReader());

		final RedActivityExecutor executor = new RedActivityExecutor(new JetDesktopAssembler());

		final GdxAdaptor adaptor = new GdxAdaptor(executor);

		return adaptor;
	}

}
