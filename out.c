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
#include <ctype.h>
#include <apr_general.h>
#include <apr_pools.h>
// use lib.ec

// Atoms
char* __ec_atom_test1 = "test1";
char* __ec_atom_test2 = "test2";
char* __ec_atom_test3 = "test3";
char* __ec_atom_test_atom = "test_atom";

// Built-In
void* __ec_nc(void* left, void* right) {
    return left == NULL ? right : left;
}

// Declarations
double min_0(double x, double y);
double max_0(double x, double y);
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
// declare val NULL
// declare val EOF
// declare rec div_t as IntDivResult
// declare rec ldiv_t as LongDivResult
static const int G = 69;
static int G2 = 420;
struct Test {
	int a;
	int b;
};

// Code
double min_0(double x, double y) {
	return (x<y) ? (x) : (y);
}
double max_0(double x, double y) {
	return (x>y) ? (x) : (y);
}
int main_0(void) {
	struct div_t divRes = div_0(245, 5);
	x_0(0);
	printf("Hello World");
	printf("Hello %s", "World");
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
	if (strcmp("Test", "NotTest") == 0) {
		char* ff = "DFE";
		printf("Test1");
	}
	else if (false) {
		printf("Test2");
	}
	else {
		printf("Test3");
	}
	return (true) ? (1) : (0);
}
int defTest_0(int input) {
	printf("2");
	if (true) {
		printf("4");
		printf("3");
	}
	else {
		printf(("test"));
		int __ec_ret = 1;
		printf("1");
		printf("5");
		return __ec_ret;
	}
	int __ec_ret = input;
	printf("1");
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
	printf("Int");
}
void x_1(char* a) {
	printf("Str");
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
    //apr_pool_t *pool;
    //apr_pool_create(&pool, NULL);
    
    int result = main_0();
    
    //apr_pool_destroy(pool);
    apr_terminate();
    return result;
}