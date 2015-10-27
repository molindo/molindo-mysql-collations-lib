#!/bin/bash -ex
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

#
# requires libmysqlclient-dev
#

LIB_SRC_HOME=$PWD/src/main/c
JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:/bin/javac::")

OBJ=/tmp/libCollationCompare.c.o
LIB=${LIB_SRC_HOME}/../resources/libCollationCompare.amd64.so

javah -classpath ${LIB_SRC_HOME}/../../../target/classes -d ${LIB_SRC_HOME} at.molindo.mysqlcollations.lib.CollationCompare

echo "building $OBJ"
gcc -fPIC -Wall -Wall -Wextra -Wunused -Wwrite-strings -Wno-strict-aliasing -Werror -Wdeclaration-after-statement `mysql_config --cflags` -I${JAVA_HOME}/include -o $OBJ -c ${LIB_SRC_HOME}/libCollationCompare.c

echo "building $LIB"
gcc -fPIC -Wall -Wall -Wextra -Wunused -Wwrite-strings -Wno-strict-aliasing -Werror -Wdeclaration-after-statement `mysql_config --libs` $OBJ -shared -o $LIB -lmysqlclient

echo "done"
