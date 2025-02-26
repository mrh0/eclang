use "apr_ring.h"
use "core:apr/general.ec"

// APR_RING_ENTRY(elem)
// APR_RING_HEAD(head, elem)
// APR_RING_SENTINEL(hp, elem, link)   (struct elem *)((char *)(&(hp)->next) - APR_OFFSETOF(struct elem, link))
// APR_RING_FIRST(hp)   (hp)->next
// APR_RING_LAST(hp)   (hp)->prev
// APR_RING_NEXT(ep, link)   (ep)->link.next
// APR_RING_PREV(ep, link)   (ep)->link.prev
// APR_RING_INIT(hp, elem, link)
// APR_RING_EMPTY(hp, elem, link)   (APR_RING_FIRST((hp)) == APR_RING_SENTINEL((hp), elem, link))
// APR_RING_ELEM_INIT(ep, link)
// APR_RING_SPLICE_BEFORE(lep, ep1, epN, link)
// APR_RING_SPLICE_AFTER(lep, ep1, epN, link)
// APR_RING_INSERT_BEFORE(lep, nep, link)   APR_RING_SPLICE_BEFORE((lep), (nep), (nep), link)
// APR_RING_INSERT_AFTER(lep, nep, link)   APR_RING_SPLICE_AFTER((lep), (nep), (nep), link)
// APR_RING_SPLICE_HEAD(hp, ep1, epN, elem, link)
// APR_RING_SPLICE_TAIL(hp, ep1, epN, elem, link)
// APR_RING_INSERT_HEAD(hp, nep, elem, link)   APR_RING_SPLICE_HEAD((hp), (nep), (nep), elem, link)
// APR_RING_INSERT_TAIL(hp, nep, elem, link)   APR_RING_SPLICE_TAIL((hp), (nep), (nep), elem, link)
// APR_RING_CONCAT(h1, h2, elem, link)
// APR_RING_PREPEND(h1, h2, elem, link)
// APR_RING_UNSPLICE(ep1, epN, link)
// APR_RING_REMOVE(ep, link)   APR_RING_UNSPLICE((ep), (ep), link)
// APR_RING_FOREACH(ep, head, elem, link)
// APR_RING_FOREACH_SAFE(ep1, ep2, head, elem, link)
// APR_RING_CHECK_ONE(msg, ptr)
// APR_RING_CHECK(hp, elem, link, msg)
// APR_RING_CHECK_CONSISTENCY(hp, elem, link)
// APR_RING_CHECK_ELEM(ep, elem, link, msg)
// APR_RING_CHECK_ELEM_CONSISTENCY(ep, elem, link)
