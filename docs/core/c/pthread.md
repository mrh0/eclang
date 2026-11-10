# `pthread.ec`

## Types

### <a id="pthread_t"></a>`pthread_t`

```ec
declare type pthread_t as ULong
```

## Functions

### <a id="pthread_create"></a>`pthread_create`

```ec
fn pthread_create(out thread: pthread_t, attr: @Any?, start: (@Any) => @Any?, arg: @Any): Int
```

*Binds the C function `pthread_create`.*

### <a id="pthread_join"></a>`pthread_join`

```ec
fn pthread_join(thread: pthread_t, result: @@Any?): Int
```

*Binds the C function `pthread_join`.*

### <a id="pthread_detach"></a>`pthread_detach`

```ec
fn pthread_detach(thread: pthread_t): Int
```

*Binds the C function `pthread_detach`.*
