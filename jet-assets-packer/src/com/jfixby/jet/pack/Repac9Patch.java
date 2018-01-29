
package com.jfixby.jet.pack;

import java.io.IOException;

import com.github.wrebecca.bleed.RebeccaTextureBleeder;
import com.jfixby.jet.asset.JetLocalAssets;
import com.jfixby.psd.unpacker.api.PSDUnpacker;
import com.jfixby.psd.unpacker.core.RedPSDUnpacker;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.debug.Debug;
import com.jfixby.scarabei.api.debug.DebugTimer;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FileFilter;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.java.gc.GCFisher;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;
import com.jfixby.scarabei.api.sys.Sys;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.desktop.image.RedImageAWT;
import com.jfixby.texture.slicer.api.TextureSlicer;
import com.jfixby.texture.slicer.red.RedTextureSlicer;
import com.jfixby.tool.psd2scene2d.CompressionInfo;
import com.jfixby.tool.psd2scene2d.PSDRepackSettings;
import com.jfixby.tool.psd2scene2d.PSDRepacker;
import com.jfixby.tool.psd2scene2d.PSDRepackerResult;
import com.jfixby.tool.psd2scene2d.PSDRepackingStatus;
import com.jfixby.tools.bleed.api.TextureBleed;
import com.jfixby.tools.gdx.texturepacker.GdxTexturePacker;
import com.jfixby.tools.gdx.texturepacker.api.TexturePacker;

public class Repac9Patch {

	private static boolean deleteGarbage = false;
// static final

	public static void main (final String[] args) throws IOException {

		ScarabeiDesktop.deploy();

// PNGQuant.installComponent(new PNGQuantJavaWrapper("D:\\[DEV]\\[GIT]\\pngquant\\PNGQuant\\pngquant.exe"));

		PSDUnpacker.installComponent(new RedPSDUnpacker());
		TexturePacker.installComponent(new GdxTexturePacker());
		TextureSlicer.installComponent(new RedTextureSlicer());
// TextureBleed.installComponent(new MaskTextureBleeder());
		ImageAWT.installComponent(new RedImageAWT());
		TextureBleed.installComponent(new RebeccaTextureBleeder());
// TexturePacker.installComponent(new RebeccaTexturePacker());

		final DebugTimer packageTimer = Debug.newTimer();
		final DebugTimer totalTimer = Debug.newTimer();

		final File input_folder = LocalFileSystem.ApplicationHome().parent().child("jet-assets").child("raw");

		L.d("input_folder", input_folder);
		final File logfile = LocalFileSystem.ApplicationHome().child("RepackPSDScene.log");
		logfile.delete();
		final FileFilter filter = new FileFilter() {
			@Override
			public boolean fits (final File child) {
				final String name = child.getName().toLowerCase();
				// return name.contains("GameMainUI".toLowerCase())
				// && name.endsWith(".psd");
// return name.contains("001".toLowerCase()) && !name.contains("preloader".toLowerCase()) && name.endsWith(".psd");
				return name.endsWith(".psd");
			}
		};
		final FilesList psd_files = input_folder.listDirectChildren().filter(filter);
		if (psd_files.size() == 0) {
			L.d(psd_files);
			L.d("No files found.");
			Sys.exit();
		}
// Sys.exit();
// banks\local\com.jfixby.jet.assets.local
		final File output_folder = LocalFileSystem.ApplicationHome().parent().child("jet-assets").child("banks").child("local")
			.child(JetLocalAssets.LOCAL_BANK_NAME).child("tank-0");
		output_folder.makeFolder();
		final List<CompressionInfo> compressedPNG = Collections.newList();
		;
		// output_folder.clearFolder();

		totalTimer.reset();
		for (final File psd_file : psd_files) {
			packageTimer.reset();
			L.d("------------------------------------------------------------------------------------------");
			final String prefix = "com.jfixby.jet.";
			String package_name_string = prefix + psd_file.getName().replaceAll(" animated", "").replaceAll("border ", "scene-");
			package_name_string = package_name_string.substring(0, package_name_string.length() - ".psd".length());

			final ID package_name = Names.newID(package_name_string);

			final int max_texture_size = (512 * 0 + 256);
			final int margin = 0;
			final int texturePadding = 4;
			final int atlasPageSize = 2048 * 2;

			final boolean forceRasterDecomposition = !true;
			final int gemserkPadding = 0;
			L.d("     psd_file", psd_file);
			L.d("output_folder", output_folder);
			L.d(" package_name", package_name_string);
			L.d("max_texture_size", max_texture_size);

			final PSDRepackingStatus status = new PSDRepackingStatus();
			try {

				GCFisher.getMemoryStatistics().print("memory stats");

				final boolean ignore_atlas = !true;

				final PSDRepackSettings settings = PSDRepacker.newSettings();

				settings.setPSDFile(psd_file);
				settings.setPackageName(package_name);
				settings.setOutputFolder(output_folder);
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

				final PSDRepackerResult repackingResult = PSDRepacker.repackPSD(settings, status);

				compressedPNG.addAll(repackingResult.listCompressions());

			} catch (final Throwable e) {
				e.printStackTrace();
				if (deleteGarbage) {
					final Collection<File> related_folders = status.getRelatedFolders();
					for (final File file : related_folders) {
						file.delete();
						L.d("DELETE", file);
					}
				}
				Sys.exit();

			}

			L.d(" done", package_name_string);
			packageTimer.printTime("PERFORMANCE-TEST: " + package_name_string);
			totalTimer.printTime("PERFORMANCE-TEST: PROGRESS");
			logfile.writeString("package, " + package_name_string + ", " + packageTimer.getTime() + "\n", true);
			logfile.writeString("total, , " + totalTimer.getTime() + "\n", true);

		}
		totalTimer.printTime("PERFORMANCE-TEST: TOTAL");
		// PackGdxFileSystem.main(null);

	}

}
