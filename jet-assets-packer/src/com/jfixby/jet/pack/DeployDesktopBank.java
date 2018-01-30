
package com.jfixby.jet.pack;

import java.io.IOException;

import com.github.wrebecca.bleed.RebeccaTextureBleeder;
import com.jfixby.jet.asset.JetLocalAssets;
import com.jfixby.jet.pack.nine.SceneRepacker;
import com.jfixby.jet.pack.nine.StringPacker;
import com.jfixby.jet.pack.nine.TextRepacker;
import com.jfixby.psd.unpacker.api.PSDUnpacker;
import com.jfixby.psd.unpacker.core.RedPSDUnpacker;
import com.jfixby.r3.assets.packer.go.SystemAssetsBankBuilder;
import com.jfixby.scarabei.api.desktop.ImageAWT;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;
import com.jfixby.scarabei.red.desktop.image.RedImageAWT;
import com.jfixby.texture.slicer.api.TextureSlicer;
import com.jfixby.texture.slicer.red.RedTextureSlicer;
import com.jfixby.tools.bleed.api.TextureBleed;
import com.jfixby.tools.gdx.texturepacker.GdxTexturePacker;
import com.jfixby.tools.gdx.texturepacker.api.TexturePacker;

public class DeployDesktopBank {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();
		PSDUnpacker.installComponent(new RedPSDUnpacker());
		TexturePacker.installComponent(new GdxTexturePacker());
		TextureSlicer.installComponent(new RedTextureSlicer());
// TextureBleed.installComponent(new MaskTextureBleeder());
		ImageAWT.installComponent(new RedImageAWT());
		TextureBleed.installComponent(new RebeccaTextureBleeder());
		// ---------------------------------------

		final File localAssets = LocalFileSystem.ApplicationHome().parent().child("jet-desktop").child("assets");

		File bankJet = localAssets.child(JetLocalAssets.LOCAL_BANK_NAME.toString());
		bankJet.makeFolder();
		bankJet.clearFolder();
		bankJet = SystemAssetsBankBuilder.writeBankHeader(localAssets, JetLocalAssets.LOCAL_BANK_NAME.toString());

		final File tankFolder = bankJet.child("tank-0");
		tankFolder.makeFolder();

		final int fokkerStarterConfigWidth = 1024;
		final int fokkerStarterConfigHeight = 800;
		final String title = "Jet Wallet";
		SystemAssetsBankBuilder.createStarterConfig(tankFolder, JetLocalAssets.FOKKER_STARTER_ASSET_ID, fokkerStarterConfigWidth,
			fokkerStarterConfigHeight, title);

		final File HOME = LocalFileSystem.ApplicationHome();
		{
			final File psdFile = HOME.parent().child("jet-assets").child("raw").child("patch-9.psd");
			final ID package_name = Names.newID("com.jfixby.jet.patch-9");
			SceneRepacker.rePack(tankFolder, psdFile, package_name);
		}
		{
			final File psdFile = HOME.parent().child("jet-assets").child("raw").child("scene-base-purple.psd");
			final ID package_name = Names.newID("com.jfixby.jet.scene-base-purple");
			SceneRepacker.rePack(tankFolder, psdFile, package_name);
		}

		{

			final ID package_name = Names.newID("com.jfixby.jet.txt");
			TextRepacker.rePack(tankFolder, package_name);
		}

		{
			final File rawText = HOME.parent().child("jet-assets").child("raw").child("text");
			final ID package_name = Names.newID("com.jfixby.jet.string");
			StringPacker.packStrings(tankFolder, rawText.listDirectChildren(), package_name);

		}

	}

}
