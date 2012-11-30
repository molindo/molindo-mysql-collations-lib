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

import java.util.Comparator;

public class CollationComparator implements Comparator<String> {

	private static final String PREFIX = CollationComparator.class.getName() + ".";

	public static final String PROP_LIB_COLLATION_COMPARE_NAME = PREFIX + "name";
	public static final String PROP_LIB_COLLATION_COMPARE_DIR = PREFIX + "dir";
	static final String PROP_LIB_COLLATION_COMPARE_LOADED = PREFIX + "loaded";

	public static void setLibraryDirectoryName(String name) {
		if (System.getProperty(PROP_LIB_COLLATION_COMPARE_LOADED) != null) {
			throw new IllegalStateException("library already loaded");
		}
		System.setProperty(CollationComparator.PROP_LIB_COLLATION_COMPARE_DIR, name);
	}

	public static void setLibraryFileName(String name) {
		if (System.getProperty(PROP_LIB_COLLATION_COMPARE_LOADED) != null) {
			throw new IllegalStateException("library already loaded");
		}
		System.setProperty(CollationComparator.PROP_LIB_COLLATION_COMPARE_NAME, name);
	}

	/**
	 * @throws NullPointerException
	 *             if collation is null
	 * @throws IllegalArgumentException
	 *             if collation is unknown
	 */
	public static Comparator<String> comparator(String collation) {
		if (collation == null) {
			throw new NullPointerException("collation");
		}

		int idx = CollationCompare.index(collation);
		if (idx <= 0) {
			throw new IllegalArgumentException("unknown collation: " + collation);
		}

		return comparator(idx);
	}

	/**
	 * returns a comparator without checking if collation index exists
	 */
	public static Comparator<String> comparator(final int idx) {
		return new CollationComparator(idx);
	}

	private final int _index;

	public CollationComparator(int index) {
		if (index <= 0) {
			throw new IllegalArgumentException("illegal collation index: " + index);
		}
		_index = index;
	}

	@Override
	public int compare(String o1, String o2) {
		int cmp = CollationCompare.compare(_index, o1, o2);
		if (cmp < -1 || cmp > 1) {
			throw new IllegalArgumentException("can't compare strings '" + o1 + "' and '" + o2 + "' using collation#"
					+ _index + " (" + cmp + ")");
		} else {
			return cmp;
		}
	}

}
