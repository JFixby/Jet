
package com.jfixby.jet.pack.nine;

import java.io.IOException;

import com.jfixby.r3.rana.api.pkg.io.PackageDescriptor;
import com.jfixby.r3.rana.red.pkg.bank.PackageUtils;
import com.jfixby.r3.string.io.StringPackageEntry;
import com.jfixby.r3.string.io.StringsPackage;
import com.jfixby.scarabei.api.collections.Collection;
import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.List;
import com.jfixby.scarabei.api.file.File;
import com.jfixby.scarabei.api.file.FilesList;
import com.jfixby.scarabei.api.json.Json;
import com.jfixby.scarabei.api.log.L;
import com.jfixby.scarabei.api.names.ID;
import com.jfixby.scarabei.api.names.Names;

public class StringPacker {
	static String RU = "русский";
	static String EN = "english";
	static String IT = "italiano";

	public static final void packStrings (final File tank, final FilesList langFiles, final ID package_id) throws IOException {
		final File package_folder = tank.child(package_id.toString());
		package_folder.makeFolder();
		package_folder.clearFolder();

		final File package_content_folder = package_folder.child(PackageDescriptor.PACKAGE_CONTENT_FOLDER);
		package_content_folder.makeFolder();
		final StringsPackage root = new StringsPackage();
		final List<ID> packed_texts = Collections.newList();

		{
			final ID parent = package_id.parent();
			for (final File langFile : langFiles.filter(file -> file.getName().contains(parent.toString()))) {
				final ID id = Names.newID(langFile.nameWithoutExtension());
				L.d(id);

				packed_texts.add(id);
				final StringPackageEntry entry = new StringPackageEntry();
				entry.id = id.toString();
				root.entries.add(entry);
				entry.string_data = langFile.readToString();

			}

		}

		final String localizations_list_file_name = package_id.child(StringsPackage.PACKAGE_FILE_EXTENSION).toString();
		final File root_file = package_content_folder.child(localizations_list_file_name);
		final String root_data = Json.serializeToString(root).toString();
		root_file.writeString(root_data);
		final Collection<ID> required = Collections.newList();
		PackageUtils.producePackageDescriptor(package_folder, StringsPackage.PACKAGE_FORMAT, "1.0", packed_texts, required,
			localizations_list_file_name);

	}

}
