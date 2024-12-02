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
// use ec:lib.ec

// Atoms
char* __ec_atom_test1 = "test1";
char* __ec_atom_test2 = "test2";
char* __ec_atom_test3 = "test3";
char* __ec_atom_test_atom = "test_atom";

// Arrays
typedef struct { long len; int* data; } __ec_array_135184888_t;

// Built-In
void* __ec_nc(void* left, void* right) {
    return left == NULL ? right : left;
}
typedef struct { int line; int position; char* path; } __ec_location_t;
typedef struct { size_t len; char* data; } __ec_string_t;

// Declarations
void log_0(char* value);
void log_1(__ec_string_t value);
void log_12(size_t value);
double min_0(double x, double y);
double max_0(double x, double y);
char* clone_0(char* str, apr_pool_t* pool);
char* clone_1(char* str);
char* catch_0(char* test);
int main_0(void);
int defTest_0(int input);
char* n_0(char* a);
char* aa_0(void);
char* a_0(char* atom);
int test_1(int a, char* b);
void x_0(int a);
void x_1(char* a);
void y_1(char* a);
int z_0(int a);

// Globals
// declare type rec apr_pool_t as Pool
// declare type rec apr_status_t as PoolStatus
static apr_pool_t* ROOT_POOL = NULL;
// declare type rec div_t as IntDivResult
// declare type rec ldiv_t as LongDivResult
static const int G = 69;
static int G2 = 420;
struct Test {
	int a;
	int b;
};

// Code
void log_0(char* value) {
	printf("%s\n", value);
}
void log_1(__ec_string_t value) {
	printf("%s\n", value.data);
}
void log_12(size_t value) {
	printf("%d\n", value);
}
double min_0(double x, double y) {
	return (x<y) ? (x) : (y);
}
double max_0(double x, double y) {
	return (x>y) ? (x) : (y);
}
char* clone_0(char* str, apr_pool_t* pool) {
	const size_t n = strlen(str);
	const char* newStr = (char*)apr_palloc((apr_pool_t*)__ec_nc(pool, ROOT_POOL), (size_t)sizeof(char*)*(n+1));
	strncpy(newStr, str, n);
	return newStr;
}
char* clone_1(char* str) {
	return clone_0(str, NULL);
}
char* catch_0(char* test) {
	char* v = "";
	return v;
}
int main_0(void) {
	log_0(catch_0("Test"));
	apr_pool_create(&(ROOT_POOL), NULL);
	log_0(clone_1("Cloned String"));
	int* carray = (int[]){6, 4, 2};
	__ec_array_135184888_t array1 = (__ec_array_135184888_t){3,(int[]){6, 4, 2}};
	__ec_array_135184888_t array2 = (__ec_array_135184888_t){3,(int[]){6, 4, 2}};
	log_12(array1.len);
	for (int entry=0;entry<array1.len;entry++) {
		log_0("Testing");
	}
	log_1((__ec_string_t){11,"Hello World"});
	char tchar = (char)10;
	short tshort = 10;
	int tint = 10;
	long tlong = 10;
	unsigned short tushort = 10;
	unsigned int tuint = 10;
	unsigned long tulong = 10;
	char* a = "Hello";
	char* b = "Hello";
	bool c = strcmp(a, b) == 0;
	bool d = a==b;
	div_t divRes = div(245, 5);
	x_0(0);
	log_0("Hello World");
	log_0("World");
	int* addr = &G2;
	int addr2 = *addr;
	test_1(0, "");
	struct Test testRec = (struct Test){0, 0};
	const int ee = testRec.a;
	y_1("");
	z_0(1);
	const char* k = __ec_atom_test_atom;
	const int p = G+G2;
	float f = 6.0f;
	char _char = 'a';
	char char2 = '\r';
	a_0(aa_0());
	n_0("Null");
	defTest_0(7);
	int iter = 5;
	while ((iter)>0) {
		log_0("I");
		iter = iter-1;
	}
	if (strcmp("Test", "NotTest") == 0) {
		char* ff = "DFE";
		log_0("Test1");
	}
	else if (false) {
		log_0("Test2");
	}
	else {
		log_0("Test3");
	}
	return (true) ? (1) : (0);
}
int defTest_0(int input) {
	log_0("2");
	if (true) {
		log_0("4");
		log_0("3");
	}
	else {
		log_0("test");
		int __ec_ret = 1;
		log_0("1");
		log_0("5");
		return __ec_ret;
	}
	int __ec_ret = input;
	log_0("1");
	return __ec_ret;
}
char* n_0(char* a) {
	return (char*)__ec_nc(a, "");
}
char* aa_0(void) {
	return __ec_atom_test1;
}
char* a_0(char* atom) {
	return atom;
}
int test_1(int a, char* b) {
	int d = 5;
	return a;
}
void x_0(int a) {
	log_0("Int");
}
void x_1(char* a) {
	log_0("Str");
}
void y_1(char* a) {
	x_1(a);
}
int z_0(int a) {
	return a+1;
}

// Main Entry
int main(int argc, const char *const argv[]) {
    apr_initialize();
    
    int __ec_ret = main_0();
    
    apr_terminate();
    return __ec_ret;
}