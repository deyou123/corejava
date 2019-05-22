/**
   @version 1.20 2007-10-26
   @author Cay Horstmann
*/

#include <jni.h>
#include <stdlib.h>

#ifdef _WINDOWS

#include <windows.h>
static HINSTANCE loadJVMLibrary(void);
typedef jint (JNICALL *CreateJavaVM_t)(JavaVM **, void **, JavaVMInitArgs *);

#endif

int main()
{  
   JavaVMOption options[2];
   JavaVMInitArgs vm_args;
   JavaVM *jvm;
   JNIEnv *env;
   long status;

   jclass class_Welcome;
   jclass class_String;
   jobjectArray args;
   jmethodID id_main;

#ifdef _WINDOWS
   HINSTANCE hjvmlib;
   CreateJavaVM_t createJavaVM;
#endif

   options[0].optionString = "-Djava.class.path=.";

   memset(&vm_args, 0, sizeof(vm_args));
   vm_args.version = JNI_VERSION_1_2;
   vm_args.nOptions = 1;
   vm_args.options = options;

#ifdef _WINDOWS   
   hjvmlib = loadJVMLibrary();
   createJavaVM = (CreateJavaVM_t) GetProcAddress(hjvmlib, "JNI_CreateJavaVM");
   status = (*createJavaVM)(&jvm, (void **) &env, &vm_args);
#else
   status = JNI_CreateJavaVM(&jvm, (void **) &env, &vm_args);
#endif

   if (status == JNI_ERR)
   {  
      fprintf(stderr, "Error creating VM\n");
      return 1;
   }

   class_Welcome = (*env)->FindClass(env, "Welcome");
   id_main = (*env)->GetStaticMethodID(env, class_Welcome, "main", "([Ljava/lang/String;)V");

   class_String = (*env)->FindClass(env, "java/lang/String");
   args = (*env)->NewObjectArray(env, 0, class_String, NULL);
   (*env)->CallStaticVoidMethod(env, class_Welcome, id_main, args);

   (*jvm)->DestroyJavaVM(jvm);

   return 0;
}

#ifdef _WINDOWS

static int GetStringFromRegistry(HKEY key, const char *name, char *buf, jint bufsize)
{
   DWORD type, size;

   return RegQueryValueEx(key, name, 0, &type, 0, &size) == 0
      && type == REG_SZ
      && size < (unsigned int) bufsize
      && RegQueryValueEx(key, name, 0, 0, buf, &size) == 0;
}

static void GetPublicJREHome(char *buf, jint bufsize)
{
   HKEY key, subkey;
   char version[MAX_PATH];

   /* Find the current version of the JRE */
   char *JRE_KEY = "Software\\JavaSoft\\Java Runtime Environment";
   if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, JRE_KEY, 0, KEY_READ, &key) != 0) 
   {
      fprintf(stderr, "Error opening registry key '%s'\n", JRE_KEY);
      exit(1);
   }

   if (!GetStringFromRegistry(key, "CurrentVersion", version, sizeof(version))) 
   {
      fprintf(stderr, "Failed reading value of registry key:\n\t%s\\CurrentVersion\n", 
         JRE_KEY);
      RegCloseKey(key);
      exit(1);
   }

   /* Find directory where the current version is installed. */
   if (RegOpenKeyEx(key, version, 0, KEY_READ, &subkey) != 0) 
   {
     fprintf(stderr, "Error opening registry key '%s\\%s'\n", JRE_KEY, version);
      RegCloseKey(key);
      exit(1);
   }

   if (!GetStringFromRegistry(subkey, "JavaHome", buf, bufsize)) 
   {
      fprintf(stderr, "Failed reading value of registry key:\n\t%s\\%s\\JavaHome\n", 
         JRE_KEY, version);
      RegCloseKey(key);
      RegCloseKey(subkey);
      exit(1);
   }

   RegCloseKey(key);
   RegCloseKey(subkey);
}

static HINSTANCE loadJVMLibrary(void)
{
   HINSTANCE h1, h2;
   char msvcdll[MAX_PATH];
   char javadll[MAX_PATH];
   GetPublicJREHome(msvcdll, MAX_PATH);   
   strcpy(javadll, msvcdll);
   strncat(msvcdll, "\\bin\\msvcr71.dll", MAX_PATH - strlen(msvcdll));
   msvcdll[MAX_PATH - 1] = '\0';
   strncat(javadll, "\\bin\\client\\jvm.dll", MAX_PATH - strlen(javadll));
   javadll[MAX_PATH - 1] = '\0';

   h1 = LoadLibrary(msvcdll);
   if (h1 == NULL)
   {
      fprintf(stderr, "Can't load library msvcr71.dll\n");
      exit(1);
   }

   h2 = LoadLibrary(javadll);
   if (h2 == NULL)
   {
      fprintf(stderr, "Can't load library jvm.dll\n");
      exit(1);
   }
   return h2;
}

#endif
