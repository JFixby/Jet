
package com.jfixby.jet.pack;

import java.io.IOException;

import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.tool.psd2scene2d.PSDRepackSettings;
import com.jfixby.tool.psd2scene2d.PSDRepacker;
import com.jfixby.tool.psd2scene2d.PSDRepackerResult;
import com.jfixby.tool.psd2scene2d.PSDRepackingStatus;

public class Repack9PatchScene {

	public static void rePack (final File tankFolder, final File psdFile, final ID package_name) throws IOException {

		final int max_texture_size = (512 * 0 + 256);
		final int margin = 0;
		final int texturePadding = 4;
		final int atlasPageSize = 2048 * 2;
		final boolean ignore_atlas = !true;
		final boolean forceRasterDecomposition = !true;
		final int gemserkPadding = 0;

		final PSDRepackSettings settings = PSDRepacker.newSettings();

		settings.setPSDFile(psdFile);
		settings.setPackageName(package_name);
		settings.setOutputFolder(tankFolder);
		settings.setMaxTextureSize(max_texture_size);
		settings.setMargin(margin);
		settings.setIgonreAtlasFlag(ignore_atlas);
		settings.setGemserkPadding(gemserkPadding);
		settings.setAtlasMaxPageSize(atlasPageSize);
		settings.setPadding(texturePadding);
		settings.setForceRasterDecomposition(forceRasterDecomposition);
		settings.setUseIndexCompression(!true);
		settings.setUseInMemoryFileSystem(true);
// settings.usePNGQuant(!true);
		final PSDRepackingStatus status = new PSDRepackingStatus();

		final PSDRepackerResult repackingResult = PSDRepacker.repackPSD(settings, status);

		L.d(" done", package_name);
	}

}
