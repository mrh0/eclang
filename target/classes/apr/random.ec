use "apr:_random.h"
use "core:apr/pools.ec"
use "core:apr/thread_proc.ec"

declare rec apr_crypto_hash_t
declare fn apr_crypto_hash_init_t as (hash: @apr_crypto_hash_t): Void
declare fn apr_crypto_hash_add_t as (hash: @apr_crypto_hash_t, data: Ptr, bytes: apr_size_t): Void
declare fn apr_crypto_hash_finish_t as (hash: @apr_crypto_hash_t, result: @UInt8): Void
declare rec apr_random_t

declare fn apr_crypto_sha256_new as (p: @apr_pool_t): @@apr_crypto_hash_t
declare fn apr_random_init as (g: @apr_random_t, p: @apr_pool_t, pool_hash: @apr_crypto_hash_t, key_hash: @apr_crypto_hash_t, prng_hash: @apr_crypto_hash_t): Void
declare fn apr_random_standard_new as (p: @apr_pool_t): @@apr_random_t
declare fn apr_random_add_entropy as (g: @apr_random_t, entropy_: Ptr, bytes: apr_size_t): Void
declare fn apr_random_insecure_bytes as (g: @apr_random_t, random: Ptr, bytes: apr_size_t): apr_status_t
declare fn apr_random_secure_bytes as (g: @apr_random_t, random: Ptr, bytes: apr_size_t): apr_status_t
declare fn apr_random_barrier as (g: @apr_random_t): Void
declare fn apr_random_secure_ready as (r: @apr_random_t): apr_status_t
declare fn apr_random_insecure_ready as (r: @apr_random_t): apr_status_t
declare fn apr_random_after_fork as (proc: @apr_proc_t): Void
