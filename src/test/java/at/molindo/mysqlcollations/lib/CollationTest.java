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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;

public class CollationTest {

	@Test
	public void test() {
		Iterator<Collation> iter = CollationList.iterator();
		while (iter.hasNext()) {
			Collation collation = iter.next();

			int compare = collation.compare("foo", "Foo");
			checkCompare(collation, compare);
			System.out.println(collation.getIndex() + " " + collation.getName() + ": " + compare);
		}
	}

	private void checkCompare(Collation collation, int compare) {
		assertNotNull(collation);

		final String info = "'" + collation.getName() + "' (" + collation.getIndex() + ")";

		String name = collation.getName();

		if (name.endsWith("_bin") || name.endsWith("_cs") || name.equals("binary")) {
			assertTrue(info, compare != 0);
		} else if (name.endsWith("_ci") || name.equals("filename")) {
			assertEquals(info, 0, compare);
		} else {
			fail("unexpected collation name " + info + ": " + compare);
		}
	}
}
