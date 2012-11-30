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
 * 
 */

#include <my_global.h>
#include <m_ctype.h>
#include <my_sys.h>

#include "at_molindo_mysqlcollations_lib_CollationCompare.h"

#include <string.h>
#include <stdio.h>

#define UNUSED(x) (void)(x)

/*
static void init(void)
{
  get_charset_by_name("",MYF(0)); // To execute init_available_charsets
}
*/

static uint collation_index(const char *name)
{
  return get_collation_number(name);
}

static int compare(uint cs_number, const uchar *a, const uchar *b)
{
  CHARSET_INFO* cs;
  int cmp;

  if (!(cs= get_charset(cs_number, MYF(0))))
    return 2;

  cmp = cs->coll->strnncollsp(cs, a, strlen((char*) a), b, strlen((char*) b), FALSE);
  if (cmp < 0) return -1;
  else if (cmp > 0) return 1;
  else return 0;
}

JNIEXPORT jint JNICALL Java_at_molindo_mysqlcollations_lib_CollationCompare_index
  (JNIEnv* env, jobject obj, jstring jname)
{

  const char *name;
  int index;

  UNUSED(obj);

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

  UNUSED(obj);

  idx = (uint) jidx;
  a = (*env)->GetStringUTFChars(env, ja, 0);
  b = (*env)->GetStringUTFChars(env, jb, 0);

  cmp = compare(idx, (uchar*) a, (uchar*) b);

  (*env)->ReleaseStringUTFChars(env, ja, a);
  (*env)->ReleaseStringUTFChars(env, jb, b);

  return cmp;
}

/*
int main(int argc, char** argv) {

  if (argc == 1) {
    int i;

    printf("printing charsets (%d):\n", MY_ALL_CHARSETS_SIZE);
    for (i=0;i<MY_ALL_CHARSETS_SIZE;i++)
    {
      CHARSET_INFO* cs;

      if ((cs = get_charset(i, MYF(0))))
        printf("collation#%d %s\n", i, cs->name);
    }
    return 1;
  }
  else if (argc == 2) {
        int idx = atoi(argv[1]);
        init();
        if (all_charsets[idx] != NULL)
        {
            printf("collation#%d %s\n", idx, all_charsets[idx]->name);
            return 0;
        }
        else 
        {
            printf("unknown charset#%d\n", idx);
            return 1;
        }
    }
    else if (argc != 4) {
        printf("expecting 3 arguments\n");
        return 1;
    } else {
        int idx, cmp;

        const char* name = argv[1];
        const char* a = argv[2];
        const char* b = argv[3];

        idx = collation_index(name);

        printf("compare(%d, '%s', '%s')\n", idx, a, b);
        cmp = compare(idx, (uchar*) a, (uchar*) b);
        printf("%d\n", cmp);

        return 0;
    }
}
*/

