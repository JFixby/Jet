
package com.jfixby.jet.pack;

import java.io.IOException;

import com.jfixby.jet.asset.JetLocalAssets;
import com.jfixby.r3.assets.packer.go.SystemAssetsBankBuilder;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.LocalFileSystem;
import com.jfixby.scarabei.red.desktop.ScarabeiDesktop;

public class DeployDesktopBank {

	public static void main (final String[] args) throws IOException {
		ScarabeiDesktop.deploy();

		final File localAssets = LocalFileSystem.ApplicationHome().parent().child("jet-desktop").child("assets");

		File bankJet = localAssets.child(JetLocalAssets.LOCAL_BANK_NAME.toString());
		bankJet.makeFolder();
		bankJet.clearFolder();
		bankJet = SystemAssetsBankBuilder.writeBankHeader(localAssets, JetLocalAssets.LOCAL_BANK_NAME.toString());

		final File jetPackages = bankJet.child("tank-0");
		jetPackages.makeFolder();

	}

}
