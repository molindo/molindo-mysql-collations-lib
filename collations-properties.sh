#!/bin/bash
#
# Copyright 2010 Molindo GmbH
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


PROPERTIES=src/main/resources/collations.properties
MYSQL=mysql

echo "updating $PROPERTIES"

echo 'select ID,COLLATION_NAME from information_schema.COLLATIONS where IS_COMPILED = "Yes" order by Id;' | $MYSQL | sed 's/\t/=/g' | tail -n +2 > $PROPERTIES

# 'filename; is the collation used internally for filenames
# see http://www.skysql.com/blogs/kolbe/demystifying-identifier-mapping
echo '17=filename' >> $PROPERTIES

echo "done"

