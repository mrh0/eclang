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

// Atoms
char* __ec_atom_test1 = "test1";
char* __ec_atom_test2 = "test2";
char* __ec_atom_test3 = "test3";
char* __ec_atom_test_atom = "test_atom";

// Tuples

// Arrays
typedef struct { long len; __ec_string_t* data; } __ec_array_1642030774_t;
typedef struct { long len; int* data; } __ec_array_1990160809_t;

// Declarations
void log_0(const char* value);
void log_1(const __ec_string_t value);
void log_12(size_t value);
double min_0(double x, double y);
double max_0(double x, double y);
char* clone_0(const char* str, const apr_pool_t* pool);
char* clone_1(const char* str);
char* genericTest_0(const char* test);
__ec_result_2325025437_t throwsTest1_0(int i);
__ec_result_2325025437_t throwsTest2_0(int i);
int varArgTest_0(int a, int b, const __ec_string_t c);
int main_0(void);
int defTest_0(int input);
char* n_0(const char* a);
char* aa_0(void);
char* a_0(const char* atom);
int test_1(int a, const char* b);
void x_0(int a);
void x_1(const char* a);
void y_1(const char* a);
int z_0(int a);

// Globals
// declare rec apr_pool_t as Pool
// declare rec apr_status_t as PoolStatus
static apr_pool_t* ROOT_POOL = NULL;
// declare rec div_t as IntDivResult
// declare rec ldiv_t as LongDivResult
typedef struct {
	int code;
	__ec_string_t message;
} Error;
static const int G = 69;
static int G2 = 420;
typedef struct {
	int a;
	int b;
} Test;
typedef struct {
	int a;
	union{char v0;short v1;int v2;long v3;float v4;double v5;unsigned char v6;unsigned short v7;unsigned int v8;unsigned long v9;size_t v10;} b;
} UnionTest;
static const UnionTest unionTest = (UnionTest){0, 0};

// Code
void log_0(const char* value) {
	printf_0("%s\n", value);
}
void log_1(const __ec_string_t value) {
	printf_0("%s\n", value.data);
}
void log_12(size_t value) {
	printf_11("%d\n", value);
}
double min_0(double x, double y) {
	return (x<y) ? (x) : (y);
}
double max_0(double x, double y) {
	return (x>y) ? (x) : (y);
}
char* clone_0(const char* str, const apr_pool_t* pool) {
	return str;
}
char* clone_1(const char* str) {
	return clone_0(str, NULL);
}
char* genericTest_0(const char* test) {
	char* v = "";
	return v;
}
__ec_result_2325025437_t throwsTest1_0(int i) {
	return (__ec_result_2325025437_t){0, .value=i+1};
}
__ec_result_2325025437_t throwsTest2_0(int i) {
	return (__ec_result_2325025437_t){1, .error=(Error){0, (__ec_string_t){4,"Test"}}};
}
int varArgTest_0(int a, int b, const __ec_string_t c, ...) {
	return a+b;
}
int main_0(void) {
	varArgTest_0(5, 8, (__ec_string_t){5,"Hello"}, (__ec_string_t){5,"World"}, (__ec_string_t){1,"!"});
	throwsTest1_0(7);
	throwsTest2_0(7);
	log_0(genericTest_0("Test"));
	apr_pool_create(&(ROOT_POOL), NULL);
	log_0(clone_1("Cloned String"));
	const __ec_array_1642030774_t array1 = (__ec_array_1642030774_t){3,(__ec_string_t[]){(__ec_string_t){3,"One"}, (__ec_string_t){3,"Two"}, (__ec_string_t){5,"Three"}}};
	__ec_array_1990160809_t array2 = (__ec_array_1990160809_t){4,(int[]){5, 3, 6, 0}};
	const __ec_string_t* carray1 = (__ec_string_t[]){(__ec_string_t){3,"One"}, (__ec_string_t){3,"Two"}, (__ec_string_t){5,"Three"}};
	int* carray2 = (int[]){5, 3, 6, 0};
	static const int staticVal = 6;
	if (true) {
		log_12(array1.len);
	}
	for (int index=0;index<array1.len;index++) {
		log_1(array1.data[index]);
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
	Test testRec = (Test){0, 0};
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
char* n_0(const char* a) {
	return (char*)__ec_nc(a, "");
}
char* aa_0(void) {
	return __ec_atom_test1;
}
char* a_0(const char* atom) {
	return atom;
}
int test_1(int a, const char* b) {
	int d = 5;
	return a;
}
void x_0(int a) {
	log_0("Int");
}
void x_1(const char* a) {
	log_0("Str");
}
void y_1(const char* a) {
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