# `tables.ec`

## Types

### <a id="apr_table_t"></a>`apr_table_t`

```ec
declare rec apr_table_t
```

### <a id="apr_array_header_t"></a>`apr_array_header_t`

```ec
declare rec apr_array_header_t
```

### <a id="apr_table_entry_t"></a>`apr_table_entry_t`

```ec
declare rec apr_table_entry_t
```

### <a id="apr_table_do_callback_fn_t"></a>`apr_table_do_callback_fn_t`

```ec
declare type apr_table_do_callback_fn_t as (@Any, CString, CString) => Int
```

## Values

### <a id="APR_OVERLAP_TABLES_SET"></a>`APR_OVERLAP_TABLES_SET`

```ec
declare val APR_OVERLAP_TABLES_SET as Int
```

### <a id="APR_OVERLAP_TABLES_MERGE"></a>`APR_OVERLAP_TABLES_MERGE`

```ec
declare val APR_OVERLAP_TABLES_MERGE as Int
```

## Functions

### <a id="apr_table_elts"></a>`apr_table_elts`

```ec
fn apr_table_elts(t: @apr_table_t): @apr_array_header_t
```

*Binds the C function `apr_table_elts`.*

### <a id="apr_is_empty_table"></a>`apr_is_empty_table`

```ec
fn apr_is_empty_table(t: @apr_table_t): Int
```

*Binds the C function `apr_is_empty_table`.*

### <a id="apr_is_empty_array"></a>`apr_is_empty_array`

```ec
fn apr_is_empty_array(a: @apr_array_header_t): Int
```

*Binds the C function `apr_is_empty_array`.*

### <a id="apr_array_make"></a>`apr_array_make`

```ec
fn apr_array_make(p: @apr_pool_t, nelts: Int, elt_size: Int): @apr_array_header_t
```

*Binds the C function `apr_array_make`.*

### <a id="apr_array_push"></a>`apr_array_push`

```ec
fn apr_array_push(arr: @apr_array_header_t): @Any
```

*Binds the C function `apr_array_push`.*

### <a id="apr_array_pop"></a>`apr_array_pop`

```ec
fn apr_array_pop(arr: @apr_array_header_t): @Any?
```

*Binds the C function `apr_array_pop`.*

### <a id="apr_array_clear"></a>`apr_array_clear`

```ec
fn apr_array_clear(arr: @apr_array_header_t)
```

*Binds the C function `apr_array_clear`.*

### <a id="apr_array_cat"></a>`apr_array_cat`

```ec
fn apr_array_cat(dst: @apr_array_header_t, src: @apr_array_header_t)
```

*Binds the C function `apr_array_cat`.*

### <a id="apr_array_copy"></a>`apr_array_copy`

```ec
fn apr_array_copy(p: @apr_pool_t, arr: @apr_array_header_t): @apr_array_header_t
```

*Binds the C function `apr_array_copy`.*

### <a id="apr_array_copy_hdr"></a>`apr_array_copy_hdr`

```ec
fn apr_array_copy_hdr(p: @apr_pool_t, arr: @apr_array_header_t): @apr_array_header_t
```

*Binds the C function `apr_array_copy_hdr`.*

### <a id="apr_array_append"></a>`apr_array_append`

```ec
fn apr_array_append(p: @apr_pool_t, first: @apr_array_header_t, second: @apr_array_header_t): @apr_array_header_t
```

*Binds the C function `apr_array_append`.*

### <a id="apr_array_pstrcat"></a>`apr_array_pstrcat`

```ec
fn apr_array_pstrcat(p: @apr_pool_t, arr: @apr_array_header_t, sep: Char): CString
```

*Binds the C function `apr_array_pstrcat`.*

### <a id="apr_table_make"></a>`apr_table_make`

```ec
fn apr_table_make(p: @apr_pool_t, nelts: Int): @apr_table_t
```

*Binds the C function `apr_table_make`.*

### <a id="apr_table_copy"></a>`apr_table_copy`

```ec
fn apr_table_copy(p: @apr_pool_t, t: @apr_table_t): @apr_table_t
```

*Binds the C function `apr_table_copy`.*

### <a id="apr_table_clone"></a>`apr_table_clone`

```ec
fn apr_table_clone(p: @apr_pool_t, t: @apr_table_t): @apr_table_t
```

*Binds the C function `apr_table_clone`.*

### <a id="apr_table_clear"></a>`apr_table_clear`

```ec
fn apr_table_clear(t: @apr_table_t)
```

*Binds the C function `apr_table_clear`.*

### <a id="apr_table_get"></a>`apr_table_get`

```ec
fn apr_table_get(t: @apr_table_t, key: CString): CString?
```

*Binds the C function `apr_table_get`.*

### <a id="apr_table_set"></a>`apr_table_set`

```ec
fn apr_table_set(t: @apr_table_t, key: CString, value: CString)
```

*Binds the C function `apr_table_set`.*

### <a id="apr_table_setn"></a>`apr_table_setn`

```ec
fn apr_table_setn(t: @apr_table_t, key: CString, value: CString)
```

*Binds the C function `apr_table_setn`.*

### <a id="apr_table_unset"></a>`apr_table_unset`

```ec
fn apr_table_unset(t: @apr_table_t, key: CString)
```

*Binds the C function `apr_table_unset`.*

### <a id="apr_table_merge"></a>`apr_table_merge`

```ec
fn apr_table_merge(t: @apr_table_t, key: CString, value: CString)
```

*Binds the C function `apr_table_merge`.*

### <a id="apr_table_mergen"></a>`apr_table_mergen`

```ec
fn apr_table_mergen(t: @apr_table_t, key: CString, value: CString)
```

*Binds the C function `apr_table_mergen`.*

### <a id="apr_table_add"></a>`apr_table_add`

```ec
fn apr_table_add(t: @apr_table_t, key: CString, value: CString)
```

*Binds the C function `apr_table_add`.*

### <a id="apr_table_addn"></a>`apr_table_addn`

```ec
fn apr_table_addn(t: @apr_table_t, key: CString, value: CString)
```

*Binds the C function `apr_table_addn`.*

### <a id="apr_table_overlay"></a>`apr_table_overlay`

```ec
fn apr_table_overlay(p: @apr_pool_t, overlay: @apr_table_t, base: @apr_table_t): @apr_table_t
```

*Binds the C function `apr_table_overlay`.*

### <a id="apr_table_do"></a>`apr_table_do`

```ec
fn apr_table_do(comp: apr_table_do_callback_fn_t, data: @Any, t: @apr_table_t, ...): Int
```

*Binds the C function `apr_table_do`.*

### <a id="apr_table_vdo"></a>`apr_table_vdo`

```ec
fn apr_table_vdo(comp: apr_table_do_callback_fn_t, data: @Any, t: @apr_table_t, vp: va_list): Int
```

*Binds the C function `apr_table_vdo`.*

### <a id="apr_table_overlap"></a>`apr_table_overlap`

```ec
fn apr_table_overlap(a: @apr_table_t, b: @apr_table_t, flags: UInt)
```

*Binds the C function `apr_table_overlap`.*

### <a id="apr_table_compress"></a>`apr_table_compress`

```ec
fn apr_table_compress(t: @apr_table_t, flags: UInt)
```

*Binds the C function `apr_table_compress`.*
