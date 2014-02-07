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

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Comparator;
import java.util.Iterator;

import at.molindo.utils.data.StringUtils;

import com.mysql.jdbc.CharsetMapping;

public class Collation implements Comparator<String> {

	private static final String PREFIX = Collation.class.getName() + ".";

	public static final String PROP_LIB_COLLATION_COMPARE_NAME = PREFIX + "name";
	public static final String PROP_LIB_COLLATION_COMPARE_DIR = PREFIX + "dir";
	public static final String PROP_LIB_COLLATION_COMPARE_ALL = PREFIX + "all";

	static final String PROP_LIB_COLLATION_COMPARE_LOADED = PREFIX + "loaded";

	public static void setLibraryDirectoryName(String name) {
		if (System.getProperty(PROP_LIB_COLLATION_COMPARE_LOADED) != null) {
			throw new IllegalStateException("library already loaded");
		}
		System.setProperty(Collation.PROP_LIB_COLLATION_COMPARE_DIR, name);
	}

	public static void setLibraryFileName(String name) {
		if (System.getProperty(PROP_LIB_COLLATION_COMPARE_LOADED) != null) {
			throw new IllegalStateException("library already loaded");
		}
		System.setProperty(Collation.PROP_LIB_COLLATION_COMPARE_NAME, name);
	}

	/**
	 * @throws NullPointerException
	 *             if collation is null
	 * @throws IllegalArgumentException
	 *             if collation is unknown
	 */
	public static Collation get(String collation) {
		if (collation == null) {
			throw new NullPointerException("collation");
		}

		int idx = CollationCompare.index(collation);
		if (idx <= 0) {
			throw new IllegalArgumentException("unknown collation: " + collation);
		}

		return get(idx);
	}

	/**
	 * returns a comparator without checking if collation index exists
	 */
	public static Collation get(final int idx) {
		return CollationList.collation(idx);
	}

	public static Iterator<Collation> iterator() {
		return CollationList.iterator();
	}

	/**
	 * get Java Charset from MySQL collation name
	 * 
	 * @return may return null
	 */
	private static Charset charset(String collationName) {
		try {
			String charsetName = StringUtils.beforeFirst(collationName, "_");
			String mappedName = CharsetMapping.MYSQL_TO_JAVA_CHARSET_MAP.get(charsetName);
			if (mappedName != null) {
				charsetName = mappedName;
			}
			return Charset.forName(charsetName);
		} catch (IllegalCharsetNameException e) {
			return null;
		} catch (UnsupportedCharsetException e) {
			return null;
		}
	}

	private final int _index;
	private final String _name;
	private final Charset _charset;

	Collation(int idx, String name) {
		if (idx <= 0) {
			throw new IllegalArgumentException("idx <= 0 (" + idx + ")");
		}

		_index = idx;
		_name = name;
		_charset = charset(name);
	}

	public int getIndex() {
		return _index;
	}

	public String getName() {
		return _name;
	}

	public Charset getCharset() {
		return _charset;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if Strings can't be compared with this collation index
	 */
	@Override
	public int compare(String o1, String o2) {
		if (_charset == null) {
			throw new IllegalStateException("can't compare collation without mapped Java charset: " + _name);
			// cmp = CollationCompare.compare(_index, o1, o2);
		}

		int cmp = CollationCompare.compareBytes(_index, o1.getBytes(_charset), o2.getBytes(_charset));

		if (cmp < -1 || cmp > 1) {
			throw new IllegalArgumentException("can't compare strings '" + o1 + "' and '" + o2 + "' using collation#"
					+ _index + " (" + cmp + ")");
		} else {
			return cmp;
		}
	}
}
