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
struct Location { int line; int position; char* path; }

// Declarations
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
// declare val NULL
// declare val EOF
struct String {
	int len;
	char* data;
};
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
double min_0(double x, double y) {
	return (x<y) ? (x) : (y);
}
double max_0(double x, double y) {
	return (x>y) ? (x) : (y);
}
char* clone_0(char* str, apr_pool_t* pool) {
	const long n = strlen(str);
	const char* newStr = (char*)apr_palloc((apr_pool_t*)__ec_nc(pool, ROOT_POOL), (long)sizeof(char*)*(n+1));
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
	printf(catch_0("Test"));
	apr_pool_create(&(ROOT_POOL), NULL);
	printf("%s", clone_1("Cloned String"));
	char* a = "Hello";
	char* b = "Hello";
	bool c = strcmp(a, b) == 0;
	bool d = a==b;
	div_t divRes = div(245, 5);
	x_0(0);
	printf("Hello World");
	printf("Hello %s", "World");
	int* addr = &(G2);
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
		printf("I");
		iter = iter-1;
	}
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
		printf("test");
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
    
    int __ec_ret = main_0();
    
    apr_terminate();
    return __ec_ret;
}