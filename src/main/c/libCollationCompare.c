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

/*
 * see https://dev.mysql.com/doc/internals/en/charsets.html
 */

#include <my_global.h>
#include <m_ctype.h>
#include <my_sys.h>
#include <m_string.h>

#include "at_molindo_mysqlcollations_lib_CollationCompare.h"

#define NOTUSED(x) (void)(x)

static uint collation_index(const char *name)
{
  return get_collation_number(name);
}

static int compare(uint cs_number, const uchar *a, uint length_a, const uchar *b, uint length_b)
{
  CHARSET_INFO* cs;
  int cmp;

  if (!(cs= get_charset(cs_number, MYF(0))))
    return 2;

  cmp = cs->coll->strnncollsp(cs, a, length_a, b, length_b, FALSE);
  if (cmp < 0) return -1;
  else if (cmp > 0) return 1;
  else return 0;
}

JNIEXPORT jint JNICALL Java_at_molindo_mysqlcollations_lib_CollationCompare_index
  (JNIEnv* env, jobject obj, jstring jname)
{

  const char *name;
  int index;

  NOTUSED(obj);

  name = (*env)->GetStringUTFChars(env, jname, 0);

  index = collation_index(name);

  (*env)->ReleaseStringUTFChars(env, jname, name);

  return index;
}

JNIEXPORT jint JNICALL Java_at_molindo_mysqlcollations_lib_CollationCompare_compare
    (JNIEnv *env, jobject obj, jint jidx, jstring ja, jstring jb) {

  uint idx;
  int cmp;

  const char *a;
  const char *b;

  jsize a_length, b_length;

  NOTUSED(obj);

  idx = (uint) jidx;
  a = (*env)->GetStringUTFChars(env, ja, 0);
  b = (*env)->GetStringUTFChars(env, jb, 0);

  a_length = (*env)->GetStringUTFLength(env, ja);
  b_length = (*env)->GetStringUTFLength(env, jb);

  cmp = compare(idx, (uchar*) a, a_length, (uchar*) b, b_length);

  (*env)->ReleaseStringUTFChars(env, ja, a);
  (*env)->ReleaseStringUTFChars(env, jb, b);

  return cmp;
}

JNIEXPORT jint JNICALL Java_at_molindo_mysqlcollations_lib_CollationCompare_compareBytes
  (JNIEnv *env, jclass obj, jint jidx, jbyteArray ja, jbyteArray jb) {

  uint idx;
  int cmp;

  jbyte *a, *b;

  jsize a_length, b_length;

  NOTUSED(obj);

  idx = (uint) jidx;
  a = (*env)->GetByteArrayElements(env, ja, 0);
  b = (*env)->GetByteArrayElements(env, jb, 0);

  a_length = (*env)->GetArrayLength(env, ja);
  b_length = (*env)->GetArrayLength(env, jb);

  cmp = compare(idx, (uchar*) a, a_length, (uchar*) b, b_length);

  (*env)->ReleaseByteArrayElements(env, ja, a, JNI_ABORT);
  (*env)->ReleaseByteArrayElements(env, jb, b, JNI_ABORT);

  return cmp;

  return 0;
}


