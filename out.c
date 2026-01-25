#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdalign.h>
#include <apr_general.h>
#include <apr_pools.h>

// Uses
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <apr_general.h>
#include <apr_pools.h>
// use core:lib.ec

// Built-In
void* __ec_nc(void* left, void* right) {
    return left == NULL ? right : left;
}
typedef struct { int line; int position; char* path; } __ec_location_t;
typedef struct { size_t len; char* data; } __ec_string_t;

// Type Names
char* __ec_type_names[22] = {"core:Pointer(Any)","core:Atom","Pointer(Any)","core:UChar","core:Pointer(None)","core:UInt","core:Short","core:CString","core:Any","core:Null","core:Size","core:Bool","core:Int","core:ULong","core:String","Pointer(None)","core:UShort","core:Double","core:Float","core:Long","core:Char","core:None"};

// Atoms

// Tuples

// Arrays

// Declarations
void log_0(const char* value);
void log_1(int value);
int main_0(void);

// Call Signatures

// Globals

// Code
void log_0(const char* value) {
	printf_0("%s\n", value);
}
void log_1(int value) {
	printf_0("%d\n", value);
}
int main_0(void) {
	for (int i=0;i<100;i++) {
		if (i%3==0&&i%5==0) {
			log_0("FizzBuzz");
		}
		else if (i%3==0) {
			log_0("Fizz");
		}
		else if (i%5==0) {
			log_0("Buzz");
		}
		else {
			log_1(i);
		}
	}
	return 0;
}

// Main Entry
int main(int argc, const char *const argv[]) {
    apr_initialize();
    
    int __ec_ret = main_0();
    
    apr_terminate();
    return __ec_ret;
}