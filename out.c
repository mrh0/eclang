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
char* __ec_type_names[88] = {"Enum(Atom(test1), Atom(test3), Atom(test2))","CArray(Int)","Union(Short, Int, Long)","Union(Char, Int, Long, Short)","Union(Int, Long)","core:UInt","Union(UShort, Size, UInt, ULong)","core:Atom(test_atom)","Array(String)","core:Union(Float, Double, Int, Long, Short, Char)","core:Enum(Atom(test3), Atom(test2))","core:String","core:Union(Int, Long, Short, Char)","core:Float","Pointer(Int)","Pointer(Pointer(Pool))","Result(Int,Error)","Union(Null, CString)","core:None","core:Result(Int,Error)","Union(Float, Double)","core:IntDivResult","core:Enum(Atom(test1), Atom(test2), Atom(test3))","core:Union(Double, Float)","Union(Float, Double, Int, Long, Short, Char, UShort, UChar, Size, UInt, ULong)","core:VarArgC","core:Atom","Union(Int, CString)","Union(UInt, Size, ULong)","core:Pointer(Int)","core:Pointer(None)","core:Short","Union(ULong, Size)","core:Union(Pointer(Pool), Null)","Union(UChar, UShort, Size, UInt, ULong)","core:Union(Null, CString)","core:ULong","core:Atom(test1)","Union(Int, Long, Short, Char, Double, Float)","core:Array(Int)","core:Double","Pointer(Pool)","core:Union(Size, ULong)","core:Char","core:Union(Size, UInt, ULong)","Array(Int)","core:CArray(String)","core:Union(CString, Int)","core:Pointer(Any)","core:Union(Int, Long)","core:UChar","core:CString","core:Size","core:Union(UShort, Size, UInt, ULong)","core:Bool","core:DefaultWrapper(Int)","core:PoolStatus","core:UnionTest","core:Union(DefaultWrapper(Int), Int)","core:Atom(test2)","Atom(test1)","core:LongDivResult","core:Long","core:Union(Int, Long, Short)","Union(Pointer(Pool), Null)","DefaultWrapper(Int)","Union(DefaultWrapper(Int), Int)","core:Union(UShort, UChar, Size, Float, Double, Int, Long, Short, Char, UInt, ULong)","core:Error","Pointer(Any)","core:Pool","CArray(String)","core:Any","Atom(test3)","Enum(Atom(test2), Atom(test3))","core:Null","Atom(test_atom)","core:Int","core:Pointer(Pool)","core:Atom(test3)","Pointer(None)","core:UShort","Atom(test2)","core:Pointer(Pointer(Pool))","core:Array(String)","core:Union(UShort, UChar, Size, UInt, ULong)","core:CArray(Int)","core:Test"};

// Atoms
char* __ec_atom_test1 = "test1";
char* __ec_atom_test2 = "test2";
char* __ec_atom_test3 = "test3";
char* __ec_atom_test_atom = "test_atom";

// Tuples

// Arrays
typedef struct { long len; __ec_string_t* data; } __ec_array_9_t;
typedef struct { long len; int* data; } __ec_array_10_t;

// Declarations
void log_0(const char* value);
void log_1(const __ec_string_t value);
void log_2(unsigned short value);
void log_3(unsigned char value);
void log_4(size_t value);
void log_5(float value);
void log_6(double value);
void log_7(int value);
void log_8(long value);
void log_9(short value);
void log_10(char value);
void log_11(unsigned int value);
void log_12(unsigned long value);
apr_status_t createPool_2(const apr_pool_t** pool);
double min_0(double x, double y);
double max_0(double x, double y);
double clamp_0(double v, double min, double max);
void exit_1(void);
char* clone_0(const char* str, const apr_pool_t* pool);
char* clone_1(const char* str, const void* pool);
char* clone_2(const char* str);
char* genericTest_0(const char* test);
__ec_result_275000536_t throwsTest1_0(int i);
__ec_result_275000536_t throwsTest2_0(int i);
int varArgTest_0(int a, int b, int __ec_va_count, ...);
int varArgCTest_0(int a, int b, ...);
int testCallSigFunc_0(int v);
int main_0(void);
int defTest_0(int input);
char* n_0(const void* a);
char* n_1(const char* a);
int n_2(int a);
int n_3();
char* aa_0(void);
char* a_0(char* atom);
char* a_1(char* atom);
char* a_2(char* atom);
int test_0(int a, const char* b, int c);
int test_1(int a, int b, int c);
int test_2(int a, const char* b);
int test_3(int a, int b);
void x_0(int a);
void x_1(const char* a);
void y_0(const char* a);
void y_1(int a);
int z_0(int a);

// Call Signatures

// Globals
// declare rec apr_pool_t as Pool
// declare rec apr_status_t as PoolStatus
static union{apr_pool_t* v0;void* v1;} ROOT_POOL = NULL;
// declare rec div_t as IntDivResult
// declare rec ldiv_t as LongDivResult
typedef struct {
	int code;
	__ec_string_t message;
} Error;
// module test
static const int G = 69;
static int G2 = 420;
typedef struct {
	int a;
	int b;
} Test;
typedef struct {
	int a;
	union{unsigned short v0;unsigned char v1;size_t v2;float v3;double v4;int v5;long v6;short v7;char v8;unsigned int v9;unsigned long v10;} b;
} UnionTest;
static const UnionTest unionTest = (UnionTest){0, 0};

// Code
void log_0(const char* value) {
	printf_0("%s\n", value);
}
void log_1(const __ec_string_t value) {
	printf_0("%s\n", value.data);
}
void log_2(unsigned short value) {
	printf_1("%d\n", value);
}
void log_3(unsigned char value) {
	printf_2("%d\n", value);
}
void log_4(size_t value) {
	printf_3("%d\n", value);
}
void log_5(float value) {
	printf_4("%d\n", value);
}
void log_6(double value) {
	printf_5("%d\n", value);
}
void log_7(int value) {
	printf_6("%d\n", value);
}
void log_8(long value) {
	printf_7("%d\n", value);
}
void log_9(short value) {
	printf_8("%d\n", value);
}
void log_10(char value) {
	printf_9("%d\n", value);
}
void log_11(unsigned int value) {
	printf_10("%d\n", value);
}
void log_12(unsigned long value) {
	printf_11("%d\n", value);
}
apr_status_t createPool_2(const apr_pool_t** pool) {
	return apr_pool_create(pool, NULL);
}
double min_0(double x, double y) {
	return (x<y) ? (x) : (y);
}
double max_0(double x, double y) {
	return (x>y) ? (x) : (y);
}
double clamp_0(double v, double min, double max) {
	return max_0(min_0(v, max), min);
}
void exit_1(void) {
	return exit_0(0);
}
char* clone_0(const char* str, const apr_pool_t* pool) {
	return str;
}
char* clone_1(const char* str, const void* pool) {
	return str;
}
char* clone_2(const char* str) {
	return clone_1(str, NULL);
}
char* genericTest_0(const char* test) {
	char* v = "";
	return v;
}
__ec_result_275000536_t throwsTest1_0(int i) {
	return (__ec_result_275000536_t){0, .value=i+1};
}
__ec_result_275000536_t throwsTest2_0(int i) {
	return (__ec_result_275000536_t){1, .error=(Error){0, (__ec_string_t){4,"Test"}}};
}
int varArgTest_0(int a, int b, int __ec_va_count, ...) {
	return a+b;
}
int varArgCTest_0(int a, int b, ...) {
	return a+b;
}
int testCallSigFunc_0(int v) {
	return 1;
}
int main_0(void) {
	varArgTest_0(5, 8, 3, (__ec_string_t){5,"Hello"}, (__ec_string_t){5,"World"}, (__ec_string_t){1,"!"});
	varArgCTest_0(5, 8, (__ec_string_t){5,"Hello"}, (__ec_string_t){5,"World"}, (__ec_string_t){1,"!"});
	throwsTest1_0(7);
	throwsTest2_0(7);
	const char* testCast = (char*)(__ec_string_t){7,"Testing"};
	log_0(genericTest_0("Test"));
	apr_pool_create(&(ROOT_POOL), NULL);
	log_0(clone_2("Cloned String"));
	const __ec_array_9_t array1 = (__ec_array_9_t){3,(__ec_string_t[]){(__ec_string_t){3,"One"}, (__ec_string_t){3,"Two"}, (__ec_string_t){5,"Three"}}};
	__ec_array_10_t array2 = (__ec_array_10_t){4,(int[]){5, 3, 6, 0}};
	const __ec_string_t* carray1 = (__ec_string_t[]){(__ec_string_t){3,"One"}, (__ec_string_t){3,"Two"}, (__ec_string_t){5,"Three"}};
	int* carray2 = (int[]){5, 3, 6, 0};
	static const int staticVal = 6;
	if (true) {
		log_4(array1.len);
	}
	for (int index=0;index<array1.len;index++) {
		log_1(array1.data[index]);
	}
	log_1((__ec_string_t){11,"Hello World"});
	char tchar = 10;
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
	test_2(0, "");
	Test testRec = (Test){0, 0};
	const int ee = testRec.a;
	y_0("");
	z_0(1);
	const char* k = __ec_atom_test_atom;
	const int p = G+G2;
	float f = 6;
	char _char = 'a';
	char char2 = '\r';
	a_0(aa_0());
	n_1("Null");
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
char* n_0(const void* a) {
	return "";
}
char* n_1(const char* a) {
	return (char*)__ec_nc(a, "");
}
int n_2(int a) {
	return 0;
}
int n_3() {
	return 0;
}
char* aa_0(void) {
	return __ec_atom_test1;
}
char* a_0(char* atom) {
	return atom;
}
char* a_1(char* atom) {
	return atom;
}
char* a_2(char* atom) {
	return atom;
}
int test_0(int a, const char* b, int c) {
	int d = 5;
	return c;
}
int test_1(int a, int b, int c) {
	int d = 5;
	return c;
}
int test_2(int a, const char* b) {
	int d = 5;
	return a;
}
int test_3(int a, int b) {
	int d = 5;
	return a;
}
void x_0(int a) {
	log_0("Int");
}
void x_1(const char* a) {
	log_0("Str");
}
void y_0(const char* a) {
	x_1(a);
}
void y_1(int a) {
	x_0(a);
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