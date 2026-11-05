#include <assert.h>
#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include "geometry.h"

/* The layout the library was compiled with is what C sees. */
_Static_assert(sizeof(geo__Header) == 5, "packed");
_Static_assert(offsetof(geo__Header, size) == 1, "packed");
_Static_assert(sizeof(geo__Flags) == sizeof(unsigned int), "bit-fields");
_Static_assert(sizeof(geo__Value) == 4, "union");
_Static_assert(sizeof(((geo__Point*)0)->name) == 8, "a C array");

static int twice(int x) { return x * 2; }

static void* thread(void* arg) {
    (void)arg;
    /* A thread C started: it gets a pool of its own. */
    printf("%s\n", greet("thread"));
    geometry_clear();
    return NULL;
}

int main(void) {
    geo__Point p = {3, 4, "p"};
    int d = distance(p);
    printf("%d %d %s\n", d, geo_calls(), originName());
    printf("%s\n", greet("world"));
    geo__Header h = header(9);
    printf("%d %d\n", h.tag, h.size);
    geo__Flags f = flags();
    printf("%d %d\n", f.a, f.b);
    printf("%x %d\n", floatBits(1.0f), value(1.0f).i == floatBits(1.0f));
    printf("%d\n", apply(twice, 4));
    pthread_t t;
    pthread_create(&t, NULL, thread, NULL);
    pthread_join(t, NULL);
    for (int i = 0; i < 1000; i++) { greet("again"); geometry_clear(); }
    printf("%s\n", originName());
    return 0;
}
