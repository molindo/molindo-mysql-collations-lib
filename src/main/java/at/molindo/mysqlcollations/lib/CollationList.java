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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.molindo.utils.collections.CollectionUtils;
import at.molindo.utils.collections.IteratorUtils;
import at.molindo.utils.data.Function;

class CollationList {

	private static final Logger log = LoggerFactory.getLogger(CollationList.class);

	private static final String COLLATION_PROPERTIES_FILE = "/collations.properties";

	private static final List<Collation> COLLATIONS;
	static final int MAX_INDEX;

	static {
		try {
			Properties props = new Properties();
			props.load(CollationList.class.getResourceAsStream(COLLATION_PROPERTIES_FILE));

			List<Collation> collations = new ArrayList<Collation>(256);

			boolean addAllCollations = Boolean.getBoolean(System.getProperty(Collation.PROP_LIB_COLLATION_COMPARE_ALL));
			for (Map.Entry<Object, Object> e : props.entrySet()) {
				int idx = Integer.parseInt((String) e.getKey());
				String name = (String) e.getValue();

				Collation collation = new Collation(idx, name);
				if (collation.getCharset() != null) {
					CollectionUtils.set(collations, idx, collation);
				} else if (addAllCollations) {
					log.info("adding collation without charset: " + collation.getName());
					CollectionUtils.set(collations, idx, collation);
				} else {
					log.debug("ignoring collation withough mapped charset: " + collation.getName());
				}
			}

			COLLATIONS = Collections.unmodifiableList(collations);
			MAX_INDEX = collations.size() - 1;
		} catch (IOException e) {
			throw new RuntimeException("failed to load properties from " + COLLATION_PROPERTIES_FILE, e);
		} catch (NumberFormatException e) {
			throw new RuntimeException("failed to load properties from " + COLLATION_PROPERTIES_FILE, e);
		}
	}

	private CollationList() {
	}

	/**
	 * @return <code>null</code> if index does not map to a collation
	 */
	static Collation collation(int index) {
		return index < 0 || index > MAX_INDEX ? null : COLLATIONS.get(index);
	}

	static Iterator<Collation> iterator() {
		return IteratorUtils.filter(COLLATIONS.iterator(), new Function<Collation, Boolean>() {

			@Override
			public Boolean apply(Collation input) {
				return input != null;
			}
		});
	}
}
