declare val APR_MEMNODE_T_SIZE as Int
declare val APR_ALLOCATOR_MAX_FREE_UNLIMITED as Int

declare rec apr_allocator_t
declare rec apr_memnode_t

declare fn apr_allocator_create as (allocator: @@apr_allocator_t): apr_status_t
declare fn apr_allocator_destroy as (allocator: @apr_allocator_t)
declare fn apr_allocator_alloc as (allocator: @apr_allocator_t, size: apr_size_t): @apr_memnode_t
declare fn apr_allocator_free as (allocator: @apr_allocator_t, memnode: @apr_memnode_t)
declare fn apr_allocator_owner_set as (allocator: @apr_allocator_t, pool: @apr_pool_t)
declare fn apr_allocator_owner_get as (allocator: @apr_allocator_t): @apr_pool_t
declare fn apr_allocator_max_free_set as (allocator: @apr_allocator_t, size: apr_size_t)
declare fn apr_allocator_mutex_set as (allocator: @apr_allocator_t, mutex: @apr_thread_mutex_t)
declare fn apr_allocator_mutex_get as (allocator: @apr_allocator_t): @apr_thread_mutex_t
