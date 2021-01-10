# Resources

* [C++ Concurrency in Action, Second Edition](https://www.manning.com/books/c-plus-plus-concurrency-in-action-second-edition)
* [C++ Memory model](https://en.cppreference.com/w/cpp/language/memory_model)
  *  [C++ memory Order](https://en.cppreference.com/w/cpp/atomic/memory_order)
  *  [C++ Volatile](https://en.cppreference.com/w/c/atomic/memory_order#Relationship_with_volatile)
  *  [C++ Program Progress Guarantee](https://en.cppreference.com/w/cpp/language/memory_model#Progress_guarantee)
* [C Memory model](https://en.cppreference.com/w/c/language/memory_model)
  * [C memory Order](https://en.cppreference.com/w/c/atomic/memory_order)
    

* [Sequential Consistency](https://en.wikipedia.org/wiki/Sequential_consistency)
  * ```... it is essential to understand one key property of sequential consistency: execution order of program in the same processor (or thread) is the same as the program order, while execution order of program between processors (or threads) is undefined.``` 
    
* Atomic Weapons by Herb Sutter
  * [C++ and Beyond 2012: Herb Sutter - atomic Weapons 1 of 2](https://www.youtube.com/watch?v=A8eCGOqgvH4)
  * [C++ and Beyond 2012: Herb Sutter - atomic Weapons 2 of 2](https://www.youtube.com/watch?v=KeLBd2EJLOU)
  * [slides](./AtomicWeaponsMemoryModel.pdf)
  

* Memory Model Discussions By Hans Boehm
  * [Slides from some recent talks, bottom of the page](https://hboehm.info/)
  * [HansWeakAtomics.pdf](./HansWeakAtomics.pdf) By Hans Boehm
  
## Miscellany

* [Thread coordination using Boost.Atomic](https://www.boost.org/doc/libs/1_75_0/doc/html/atomic/thread_coordination.html)
* [How can memory_order_relaxed work for incrementing atomic reference counts in smart pointers?](https://stackoverflow.com/questions/27631173/how-can-memory-order-relaxed-work-for-incrementing-atomic-reference-counts-in-sm)
* [boost atomic usage examples](https://www.boost.org/doc/libs/1_57_0/doc/html/atomic/usage_examples.html#boost_atomic.usage_examples.example_reference_counters)

```cpp
#include <boost/intrusive_ptr.hpp>
#include <boost/atomic.hpp>

class X {
public:
  typedef boost::intrusive_ptr<X> pointer;
  X() : refcount_(0) {}

private:
  mutable boost::atomic<int> refcount_;
  friend void intrusive_ptr_add_ref(const X * x)
  {
    x->refcount_.fetch_add(1, boost::memory_order_relaxed);
  }
  friend void intrusive_ptr_release(const X * x)
  {
    if (x->refcount_.fetch_sub(1, boost::memory_order_release) == 1) { //NOTE: I do not understand this !!! It must be ACQ_REL and no fence below...
      boost::atomic_thread_fence(boost::memory_order_acquire);
      delete x;
    }
  }
};
```

* [explanation of above](https://stackoverflow.com/questions/48124031/stdmemory-order-relaxed-atomicity-with-respect-to-the-same-atomic-variable)
 * single modification order:
 > The atomic ref_count has a single modification order and therefore all atomic modifications occur in some order.
* [std::shared_ptr<T>::use_count relaxed ordering](https://en.cppreference.com/w/cpp/memory/shared_ptr/use_count)* Then
 * relaxed loads
 > In multi threaded environment, the value returned by use_count is approximate (typical implementations use a memory_order_relaxed load)

__Discussion:__
The mutex makes sure that only one instance of the object is ever created. The instance method must make sure that any dereference of the object strictly "happens after" creating the instance in another thread. The use of memory_order_release after creating and initializing the object and memory_order_consume before dereferencing the object provides this guarantee.

It would be permissible to use memory_order_acquire instead of memory_order_consume, but this provides a stronger guarantee than is required
since only operations depending on the value of the pointer need to be ordered.

___from [memory_order#Relaxed_ordering](https://en.cppreference.com/w/cpp/atomic/memory_order#Relaxed_ordering):_

Typical use for relaxed memory ordering is incrementing counters, such as the reference counters of std::shared_ptr, since this only requires atomicity, but not ordering or synchronization (note that decrementing the shared_ptr counters requires acquire-release synchronization with the destructor)

__relaxed ordering__

Atomic operations tagged memory_order_relaxed are not synchronization operations; they do not impose an order among concurrent memory accesses. They only guarantee atomicity and modification order consistency.

__release-acquire ordering__

If an atomic store in thread A is tagged memory_order_release and an atomic load in thread B from the same variable is tagged memory_order_acquire, all memory writes (non-atomic and relaxed atomic) that happened-before the atomic store from the point of view of thread A, become visible side-effects in thread B. That is, once the atomic load is completed, thread B is guaranteed to see everything thread A wrote to memory. The synchronization is established only between the threads releasing and acquiring the same atomic variable. Other threads can see different order of memory accesses than either or both of the synchronized threads.

__sequentially consistent ordering__

For atomic operations tagged memory_order_seq_cst, a load operation with this memory order performs an acquire operation, a store performs a release operation, and read-modify-write performs both an acquire operation and a release operation, plus a single total order exists in which all threads observe all modifications in the same order.

Atomic operations tagged memory_order_seq_cst not only order memory the same way as release/acquire ordering (everything that happened-before a store in one thread becomes a visible side effect in the thread that did a load), but also establish a single total modification order of all atomic operations that are so tagged.

__Sequential consistency__ (definition from [Sequential consistency](https://www.boost.org/doc/libs/1_75_0/doc/html/atomic/thread_coordination.html)):

The third pattern for coordinating threads via Boost.Atomic uses seq_cst for coordination: If ...

... thread1 performs an operation A,
... thread1 subsequently performs any operation with seq_cst,
... thread1 subsequently performs an operation B,
... thread2 performs an operation C,
... thread2 subsequently performs any operation with seq_cst,
... thread2 subsequently performs an operation D,
then either "A happens-before D" or "C happens-before B" holds.

In this case it does not matter whether thread1 and thread2 operate on the same or different atomic variables, or use a "stand-alone" atomic_thread_fence operation.

* [atomic_thread_fence](https://en.cppreference.com/w/cpp/atomic/atomic_thread_fence)
* [Modification_order](https://en.cppreference.com/w/cpp/atomic/memory_order#Modification_order)
* [atomic-fetch_sub](https://en.cppreference.com/w/cpp/atomic/atomic/fetch_sub)
* [why-isnt-fetch-sub-a-release-operation](https://stackoverflow.com/questions/50500443/why-isnt-fetch-sub-a-release-operation)
* atomic_thread_fence imposes stronger synchronization constraints than an atomic store operation with the same std::memory_order. While an atomic store-release operation prevents all preceding writes from moving past the store-release, an atomic_thread_fence with memory_order_release ordering prevents all preceding writes from moving past all subsequent stores.
  *  I think this specification for fences (above) came after below discussions:
     * [Acquire and Release Fences Don't Work the Way You'd Expect](https://preshing.com/20131125/acquire-and-release-fences-dont-work-the-way-youd-expect/)
     * [Acquire and Release Semantics](https://preshing.com/20120913/acquire-and-release-semantics/#IDComment721195803)
* [chromium atomic_ref_count.h](https://chromium.googlesource.com/chromium/src/base/+/refs/heads/master/atomic_ref_count.h):
  
```cpp
// Copyright (c) 2011 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
// This is a low level implementation of atomic semantics for reference
// counting.  Please use base/memory/ref_counted.h directly instead.
#ifndef BASE_ATOMIC_REF_COUNT_H_
#define BASE_ATOMIC_REF_COUNT_H_
#include <atomic>
namespace base {
class AtomicRefCount {
 public:
  constexpr AtomicRefCount() : ref_count_(0) {}
  explicit constexpr AtomicRefCount(int initial_value)
      : ref_count_(initial_value) {}
  // Increment a reference count.
  // Returns the previous value of the count.
  int Increment() { return Increment(1); }
  // Increment a reference count by "increment", which must exceed 0.
  // Returns the previous value of the count.
  int Increment(int increment) {
    return ref_count_.fetch_add(increment, std::memory_order_relaxed);
  }
  // Decrement a reference count, and return whether the result is non-zero.
  // Insert barriers to ensure that state written before the reference count
  // became zero will be visible to a thread that has just made the count zero.
  bool Decrement() {
    // TODO(jbroman): Technically this doesn't need to be an acquire operation
    // unless the result is 1 (i.e., the ref count did indeed reach zero).
    // However, there are toolchain issues that make that not work as well at
    // present (notably TSAN doesn't like it).
    return ref_count_.fetch_sub(1, std::memory_order_acq_rel) != 1;
  }
  // Return whether the reference count is one.  If the reference count is used
  // in the conventional way, a refrerence count of 1 implies that the current
  // thread owns the reference and no other thread shares it.  This call
  // performs the test for a reference count of one, and performs the memory
  // barrier needed for the owning thread to act on the object, knowing that it
  // has exclusive access to the object.
  bool IsOne() const { return ref_count_.load(std::memory_order_acquire) == 1; }
  // Return whether the reference count is zero.  With conventional object
  // referencing counting, the object will be destroyed, so the reference count
  // should never be zero.  Hence this is generally used for a debug check.
  bool IsZero() const {
    return ref_count_.load(std::memory_order_acquire) == 0;
  }
  // Returns the current reference count (with no barriers). This is subtle, and
  // should be used only for debugging.
  int SubtleRefCountForDebug() const {
    return ref_count_.load(std::memory_order_relaxed);
  }
 private:
  std::atomic_int ref_count_;
};
}  // namespace base
#endif  // BASE_ATOMIC_REF_COUNT_H_

```

# intrusive_ptr class template

* [intrusive_ptr](https://www.boost.org/doc/libs/1_60_0/libs/smart_ptr/intrusive_ptr.html)
* [boost_atomic.usage_examples.example_reference_counters](https://www.boost.org/doc/libs/1_75_0/doc/html/atomic/usage_examples.html#boost_atomic.usage_examples.example_reference_counters)
* [intrusive_ptr.hpp](https://github.com/steinwurf/boost/blob/master/boost/smart_ptr/intrusive_ptr.hpp)
* [intrusive-ptr-in-c11](https://stackoverflow.com/questions/13912286/intrusive-ptr-in-c11)
 
# Miscellany

* [synchronization-patterns](https://jeehoonkang.github.io/2017/08/23/synchronization-patterns.html)
* [justsoftwaresolutions/threading](https://www.justsoftwaresolutions.co.uk/threading/)
  * [justsoftwaresolutions/locks-mutexes-semaphores](https://www.justsoftwaresolutions.co.uk/threading/locks-mutexes-semaphores.html)
  
* RMW Operations Synchronization ???
  * [ThreadSanitizer reports "data race on operator delete(void*)" when using embedded reference counter](https://www.titanwolf.org/Network/q/15fef25e-1071-4272-8775-307788013e2d/y)
    * `1. and 2. are synchronized since they are both atomic read-modify-write operations on the same value.` --> ???
> This looks like a false positive.
> The thread_fence in the release() method enforces all outstanding writes from fetch_sub-calls to happen-before the fence returns. Therefore, the delete on the next line cannot race with the previous writes from decreasing the refcount.
> Quoting from the book C++ Concurrency in Action:
> A release operation synchronizes-with a fence with an order of std::memory_order_acquire [...] if that release operation stores a value that's read by an atomic operation prior to the fence on the same thread as the fence.
> Since decreasing the refcount is a read-modify-write operation, this should apply here.
> To elaborate, the order of operations that we need to ensure is as follows:

> * 1 Decreasing the refcount to a value > 1
> * 2 Decreasing the refcount to 1
> * 3 Deleting the object 

> 2 and 3 are synchronized implicitly, as they happen on the same thread. 1. and 2. are synchronized since they are both atomic read-modify-write operations on the same value. If these two could race the whole refcounting would be broken in the first place. So what is left is synchronizing 1. and 3..
> This is exactly what the fence does. The write from 1. is a release operation that is, as we just discussed, synchronized with 2., a read on the same value. 3., an acquire fence on the same thread as 2., now synchronizes with the write from 1. as guaranteed by the spec. This happens without requiring an addition acquire write on the object (as was suggested by @KerrekSB in the comments), which would also work, but might be potentially less efficient due to the additional write.
> Bottom line: Don't play around with memory orderings. Even experts get them wrong and their impact on performance is often negligible. So unless you have proven in a profiling run that they kill your performance and you absolutely positively have to optimize this, just pretend they don't exist and stick with the default memory_order_seq_cst.



* [ThreadSanitizerCppManual](https://github.com/google/sanitizers/wiki/ThreadSanitizerCppManual)
* [You Can Do Any Kind of Atomic Read-Modify-Write Operation](https://preshing.com/20150402/you-can-do-any-kind-of-atomic-read-modify-write-operation/)
* [Atomic vs. Non-Atomic Operations](https://preshing.com/20130618/atomic-vs-non-atomic-operations/)
* [An Introduction to Lock-Free Programming](https://preshing.com/20120612/an-introduction-to-lock-free-programming/)
* [Independent Read-Modify-Write Ordering](https://stackoverflow.com/questions/60382799/independent-read-modify-write-ordering)
* [Atomic operation propagation/visibility (atomic load vs atomic RMW load)](https://stackoverflow.com/questions/55079321/atomic-operation-propagation-visibility-atomic-load-vs-atomic-rmw-load)
* [Synchronization and Ordering Constraints](http://modernescpp.com/index.php/synchronization-and-ordering-constraints)
* [Using an atomic read-modify-write operation in a release sequence](https://stackoverflow.com/questions/45694459/using-an-atomic-read-modify-write-operation-in-a-release-sequence)
* [compare_exchange](https://en.cppreference.com/w/cpp/atomic/atomic/compare_exchange)
* [Lock-free programming: how fresh is atomic value?](https://stackoverflow.com/questions/40819270/lock-free-programming-how-fresh-is-atomic-value)
* [Can num++ be atomic for 'int num'?](https://stackoverflow.com/questions/39393850/can-num-be-atomic-for-int-num/39396999#39396999)
 
  __FOR X86:__
> So lock add dword [num], 1 is atomic. A CPU core running that instruction would keep the cache line pinned in Modified state in its private L1 cache from when the load reads data from cache until the store commits its result back into cache. This prevents any other cache in the system from having a copy of the cache line at any point from load to store, according to the rules of the MESI cache coherency protocol (or the MOESI/MESIF versions of it used by multi-core AMD/Intel CPUs, respectively). Thus, operations by other cores appear to happen either before or after, not during.

> Without the lock prefix, another core could take ownership of the cache line and modify it after our load but before our store, so that other store would become globally visible in between our load and store. Several other answers get this wrong, and claim that without lock you'd get conflicting copies of the same cache line. This can never happen in a system with coherent caches.

> Note that the lock prefix also turns an instruction into a full memory barrier (like MFENCE), stopping all run-time reordering and thus giving sequential consistency. (See Jeff Preshing's excellent blog post. His other posts are all excellent, too, and clearly explain a lot of good stuff about lock-free programming, from x86 and other hardware details to C++ rules.)

> On a uniprocessor machine, or in a single-threaded process, a single RMW instruction actually is atomic without a lock prefix. The only way for other code to access the shared variable is for the CPU to do a context switch, which can't happen in the middle of an instruction. So a plain dec dword [num] can synchronize between a single-threaded program and its signal handlers, or in a multi-threaded program running on a single-core machine. See the second half of my answer on another question, and the comments under it, where I explain this in more detail.

> As I mentioned, the x86 lock prefix is a full memory barrier, so using num.fetch_add(1, std::memory_order_relaxed); generates the same code on x86 as num++ (the default is sequential consistency), but it can be much more efficient on other architectures (like ARM). Even on x86, relaxed allows more compile-time reordering.

See the source + assembly language code formatted nicely on the Godbolt compiler explorer. You can select other target architectures, including ARM, MIPS, and PowerPC, to see what kind of assembly language code you get from atomics for those targets.

```cpp
#include <atomic>
std::atomic<int> num;

void inc_relaxed() {
  num.fetch_add(1, std::memory_order_relaxed);
}

int load_num() { return num; }            // Even seq_cst loads are free on x86
void store_num(int val){ num = val; }
void store_num_release(int val){
  num.store(val, std::memory_order_release);
}
// Can the compiler collapse multiple atomic operations into one? No, it can't.
```

```asm
# g++ 6.2 -O3, targeting x86-64 System V calling convention. (First argument in edi/rdi)
inc_relaxed():
    lock add        DWORD PTR num[rip], 1      #### Even relaxed RMWs need a lock. There's no way to request just a single-instruction RMW with no lock, for synchronizing between a program and signal handler for example. :/ There is atomic_signal_fence for ordering, but nothing for RMW.
    ret
inc_seq_cst():
    lock add        DWORD PTR num[rip], 1
    ret
load_num():
    mov     eax, DWORD PTR num[rip]
    ret
store_num(int):
    mov     DWORD PTR num[rip], edi
    mfence                          ##### seq_cst stores need an mfence
    ret
store_num_release(int):
    mov     DWORD PTR num[rip], edi
    ret                             ##### Release and weaker doesn't.
store_num_relaxed(int):
    mov     DWORD PTR num[rip], edi
    ret
```

* [Memory Barriers Are Like Source Control Operations](https://preshing.com/20120710/memory-barriers-are-like-source-control-operations/)
* [An Introduction to Lock-Free Programming](https://preshing.com/20120612/an-introduction-to-lock-free-programming/)
* [What Every C Programmer Should Know About Undefined Behavior #1/3](http://blog.llvm.org/2011/05/what-every-c-programmer-should-know.html)
* [Memory Reordering Caught in the Act](https://preshing.com/20120515/memory-reordering-caught-in-the-act/)


