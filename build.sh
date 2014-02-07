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

#
# requires compiled MySQL sources (`cmake . && make`) at $MYSQL_SRC_HOME
#

LIB_SRC_HOME=$PWD/src/main/c
#MYSQL_SRC_HOME=$HOME/Desktop/mysql-5.5.36
JAVA_HOME=$(readlink -f /usr/bin/javac | sed "s:bin/javac::")

OBJ=/tmp/libCollationCompare.c.o
LIB=${LIB_SRC_HOME}/../resources/libCollationCompare.amd64.so
EXEC=${LIB_SRC_HOME}/../resources/compare

javah -classpath ${LIB_SRC_HOME}/../../../target/classes -d ${LIB_SRC_HOME} at.molindo.mysqlcollations.lib.CollationCompare

#cd $MYSQL_SRC_HOME
echo "building $OBJ"
#gcc -DHAVE_CONFIG_H \
#  -fPIC -Wall -Wall -Wextra -Wunused -Wwrite-strings -Wno-strict-aliasing -Werror -Wdeclaration-after-statement \
#  -g -DENABLED_DEBUG_SYNC -DSAFE_MUTEX \
#  -I${MYSQL_SRC_HOME}/include -I${MYSQL_SRC_HOME}/zlib -I${MYSQL_SRC_HOME}/storage/ndb/include \
#  -I${MYSQL_SRC_HOME}/storage/ndb/include/util -I${MYSQL_SRC_HOME}/storage/ndb/include/ndbapi \
#  -I${MYSQL_SRC_HOME}/storage/ndb/include/portlib -I${MYSQL_SRC_HOME}/storage/ndb/include/mgmapi \
#  -I${JAVA_HOME}/include \
#  -o $OBJ -c ${LIB_SRC_HOME}/libCollationCompare.c
gcc -fPIC -Wall -Wall -Wextra -Wunused -Wwrite-strings -Wno-strict-aliasing -Werror -Wdeclaration-after-statement `mysql_config --cflags` -I${JAVA_HOME}/include -o $OBJ -c ${LIB_SRC_HOME}/libCollationCompare.c

#echo "building $EXEC"
#cd $MYSQL_SRC_HOME/extra
#/usr/bin/gcc \
#    -fPIC -Wall -Wall -Wextra -Wunused -Wwrite-strings -Wno-strict-aliasing -Werror -Wdeclaration-after-statement \
#    -g -DENABLED_DEBUG_SYNC -DSAFE_MUTEX \
#    $OBJ \
#    -o $EXEC \
#    -lpthread ../mysys/libmysys.a ../dbug/libdbug.a ../mysys/libmysys.a ../dbug/libdbug.a ../strings/libstrings.a -lm -lrt -lpthread 

echo "building $LIB"
#cd $MYSQL_SRC_HOME/extra
#gcc \
#  -fPIC -Wall -Wall -Wextra -Wunused -Wwrite-strings -Wno-strict-aliasing -Werror -Wdeclaration-after-statement \
#  -g -DENABLED_DEBUG_SYNC -DSAFE_MUTEX \
#  $OBJ \
#  -fPIC -shared \
#  -o $LIB \
#  -lpthread ../mysys/libmysys.a ../dbug/libdbug.a ../mysys/libmysys.a ../dbug/libdbug.a ../strings/libstrings.a -lm -lrt -lpthread 
gcc -fPIC -Wall -Wall -Wextra -Wunused -Wwrite-strings -Wno-strict-aliasing -Werror -Wdeclaration-after-statement `mysql_config --libs` $OBJ -shared -o $LIB 

#cd $LIB_SRC_HOME/../../..
echo "done"

