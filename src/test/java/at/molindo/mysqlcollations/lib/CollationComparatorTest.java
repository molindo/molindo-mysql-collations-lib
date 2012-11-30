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

import org.junit.Test;

public class CollationComparatorTest {

	@Test
	public void test() {
		for (int i = 1; i < 256; i++) {
			try {
				System.out.println(i + ": " + CollationComparator.comparator(i).compare("foo", "bar"));
			} catch (IllegalArgumentException e) {
				System.out.println(i + " unknown");
			}
		}

		for (String collation : new String[] { "utf8_unicode_ci" }) {
			System.out.println(collation + ": " + CollationComparator.comparator(collation).compare("Foobar", "foo"));
		}
	}
}
