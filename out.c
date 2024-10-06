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
// use ec:lib.ec

// Atoms

// Built-In
void* __ec_nc(void* left, void* right) {
    return left == NULL ? right : left;
}
struct Location { int line; int position; char* path; };

// Declarations
double min_0(double x, double y);
double max_0(double x, double y);
char* clone_0(char* str, apr_pool_t* pool);
int main_0(void);

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
int main_0(void) {
	for (int i=0;i<100;i++) {
		if (i%3==0&&i%5==0) {
			printf("FizzBuzz\n");
		}
		else if (i%3==0) {
			printf("Fizz\n");
		}
		else if (i%5==0) {
			printf("Buzz\n");
		}
		else {
			printf("%d\n", i);
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