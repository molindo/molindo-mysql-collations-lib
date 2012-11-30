/**
 * Copyright 2010 Molindo GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.molindo.mysqlcollations.lib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;

import at.molindo.utils.io.StreamUtils;

final class CollationCompare {

	static {
		String dirName = System.getProperty(CollationComparator.PROP_LIB_COLLATION_COMPARE_DIR, ".");
		String fileName = System.getProperty(CollationComparator.PROP_LIB_COLLATION_COMPARE_NAME,
				"libCollationCompare.so");

		File dir = new File(dirName);
		if (!dir.isDirectory()) {
			throw new RuntimeException("directory does not exist: " + dir.getAbsolutePath());
		}
		File file = new File(dir, fileName);
		if (file.exists()) {
			file.delete();
		}

		// copy from classpath
		InputStream in = CollationCompare.class.getResourceAsStream("/" + fileName);
		OutputStream out = null;

		if (in == null) {
			throw new RuntimeException("can't find /" + fileName + " on classpath");
		}

		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			StreamUtils.copy(in, out);
		} catch (IOException e) {
			throw new RuntimeException("can't write library to " + file.getAbsolutePath());
		} finally {
			StreamUtils.close(in, out);
		}

		Runtime.getRuntime().load(file.getAbsolutePath());
		System.setProperty(CollationComparator.PROP_LIB_COLLATION_COMPARE_LOADED, file.getAbsolutePath());

		// TODO should be in memory and safe to delete immediately
		file.deleteOnExit();
	}

	private CollationCompare() {
	}

	/**
	 * @param collation
	 *            name of collation
	 * @return an index > 0 if collation name exists
	 */
	static native int index(String collation);

	/**
	 * @return one of {-1,0,1} for comparison, something else in case of error
	 * 
	 * @see Comparator#compare(Object, Object)
	 */
	static native int compare(int idx, String a, String b);

}
