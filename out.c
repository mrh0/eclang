#include <stdio.h>
#include <string.h>
#include <math.h>

// Atoms
const char* __ec_atom_test1 = "test1";
const char* __ec_atom_test2 = "test2";
const char* __ec_atom_test3 = "test3";
const char* __ec_atom_test_atom = "test_atom";

// Code
struct Test {
	int a;
	int b;
};
int main (void) {
	printf("Hello World");
	printf("Hello %s", "World");
	test_1(0, "");
	y_1("");
	z_0(1);
	const char* k = __ec_atom_test_atom;
	a_0(aa_0());
	if (strcmp("Test", "NotTest") == 0) {
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
char* aa_0 (void) {
	return __ec_atom_test1;
}
char* a_0 (char* atom) {
	return atom;
}
int test_1 (int a, char* b) {
	const int d = 5;
	return a;
}
void x_0 (int a) {
	printf("Int");
}
void x_1 (char* a) {
	printf("Str");
}
void y_1 (char* a) {
	x_1(a);
}
int z_0 (int a) {
	return a+1;
}
