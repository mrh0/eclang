// pthread.h: threads. core:thread.ec uses these, as apr_thread_create keeps its handle in a pool the thread must not outlive.
use "pthread.h"
use "lib:pthread"

declare type pthread_t as ULong

// Starts a thread running start(arg). 0, or an error number.
declare fn pthread_create as (out thread: pthread_t, attr: @Any?, start: (@Any) => @Any?, arg: @Any): Int
// Waits for the thread to end. 0, or an error number.
declare fn pthread_join as (thread: pthread_t, result: @@Any?): Int
// The thread frees what it holds itself when it ends. 0, or an error number.
declare fn pthread_detach as (thread: pthread_t): Int
