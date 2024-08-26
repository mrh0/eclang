#include <stdio.h>
struct Test {
	int a;
	int b;
};
int main (void) {
	printf("Hello World");
	test_1(0, "");
	y_1("");
	z_0(1);
	if (true) {
		printf("Test");
	}
	return 0;
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
