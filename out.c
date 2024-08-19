#include <stdio.h>
struct Test {
	int a;
	int b;
};
int main (void) {
	printf("Hello World");
	test_6(0, "");
	return 0;
}
int test_0 (int a, int b, int c) {
	return 1;
}
int test_1 (char* a, int b, int c) {
	return 1;
}
int test_2 (int a, char* b, int c) {
	return 1;
}
int test_3 (char* a, char* b, int c) {
	return 1;
}
int test_4 (int a, int b) {
	return 1;
}
int test_5 (char* a, int b) {
	return 1;
}
int test_6 (int a, char* b) {
	return 1;
}
int test_7 (char* a, char* b) {
	return 1;
}
int test_8 (int a) {
	return a;
}
