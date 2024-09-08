#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdbool.h>
#include <apr_general.h>
#include <apr_pools.h>

// Atoms
static char* __ec_atom_test1 = "test1";
static char* __ec_atom_test2 = "test2";
static char* __ec_atom_test3 = "test3";
static char* __ec_atom_test_atom = "test_atom";

// Built-In
void* __ec_nc(void* left, void* right) {
    return left == NULL ? right : left;
}
// Declarations
int main_0(void);
char* n_0(char* a);
char* aa_0(void);
char* a_0(char* atom);
int test_1(int a, char* b);
void x_0(int a);
void x_1(char* a);
void y_1(char* a);
int z_0(int a);

// Records
struct Test {
	int a;
	int b;
};

// Code
int main_0(void) {
	x_0(0);
	printf("Hello World");
	printf("Hello %s", "World");
	test_1(0, "");
	y_1("");
	z_0(1);
	const char* k = __ec_atom_test_atom;
	a_0(aa_0());
	n_0("Null");
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
    apr_pool_t *pool;
    apr_pool_create(&pool, NULL);
    
    int result = main_0();
    
    apr_pool_destroy(pool);
    apr_terminate();
    return result;
}