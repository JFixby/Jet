
package com.jfixby.jet.desktop;

import java.io.IOException;

import com.jfixby.r3.engine.api.EngineAssembler;
import com.jfixby.r3.fokker.io.assets.GdxAssetsFileSystem;
import com.jfixby.r3.fokker.io.assets.GdxAssetsFileSystemParams;
import com.jfixby.r3.fokker.io.assets.index.GdxAssetsFileSystemIndex;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.io.IO;
import com.jfixby.scarabei.api.java.ByteArray;
import com.jfixby.scarabei.api.log.L;

public class JetDesktopAssembler implements EngineAssembler {

	@Override
	public void assembleEngine () {
// Screen.setDebugScaleFactor(1);
	}

	private static void testIndex () throws IOException {
		final File home = LocalFileSystem.ApplicationHome();
		if (!home.getName().equalsIgnoreCase("assets")) {
			L.d("Application home folder is not set to the android assets dir", home);
			return;
		}
// final String java_path = "D:\\Tinto\\Tinto-gdx\\android\\assets\\";
		final File androidAssets = home;
		final File indexFile = androidAssets.child(GdxAssetsFileSystemIndex.INDEX_FILE_NAME);
		final ByteArray data = indexFile.readBytes();
		final GdxAssetsFileSystemIndex index = IO.deserialize(GdxAssetsFileSystemIndex.class, data);
// index.print();
		final GdxAssetsFileSystemParams p = new GdxAssetsFileSystemParams();
		p.index_data = index;
		final GdxAssetsFileSystem gdx = new GdxAssetsFileSystem(p);
		gdx.checkIndex();
// gdx.ROOT().listAllChildren().print("all");
	}

}
