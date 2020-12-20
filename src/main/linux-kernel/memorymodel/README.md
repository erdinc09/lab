# Resources
* [Memory Barriers: a Hardware View for Software Hackers](./Memory_Barriers_a_Hardware_View_for_Software_Hackers.pdf)
  * Must-read paper for memory barriers.
* [memory-barriers.txt](https://github.com/torvalds/linux/blob/master/Documentation/memory-barriers.txt)
  * Bible for memory barriers!
* [Linux-Kernel Memory Model](http://open-std.org/JTC1/SC22/WG21/docs/papers/2018/p0124r6.html)
* [Memory Barriers,mb(),rmb(),smp_mb(),smp_rmb()...](http://bruceblinn.com/linuxinfo/MemoryBarriers.html)
  * ```As mentioned above, both compilers and processors can optimize the execution of instructions in a way that necessitates the use of a memory barrier. A memory barrier that affects both the compiler and the processor is a hardware memory barrier, and a memory barrier that only affects the compiler is a software memory barrier.```
  * ```The barrier() macro is the only software memory barrier, and it is a full memory barrier. All other memory barriers in the Linux kernel are hardware barriers. A hardware memory barrier is an implied software barrier.```
* [what is '__asm__ __volatile__ (“” : : : “memory”)'](https://stackoverflow.com/questions/14950614/working-of-asm-volatile-memory)
  * In linux kernel, ```barier()``` macrois defined as ```__asm__ __volatile__ (“” : : : “memory”)``` in [include/linux/compiler.h](https://github.com/torvalds/linux/blob/master/include/linux/compiler.h)
    
As stated in [Memory Barriers: a Hardware View for Software Hackers](./Memory_Barriers_a_Hardware_View_for_Software_Hackers.pdf) for each architecture barriers must be implemented differently.

In linux kernel basic (implemented for the weakest arc, alpha) definitions reside in:
 * [include/linux/compiler.h](https://github.com/torvalds/linux/blob/master/include/linux/compiler.h)
 * [/include/asm-generic/barrier.h](https://github.com/torvalds/linux/blob/master/include/asm-generic/barrier.h)

We compare one of the strongest memory model X86 and  weakest alpha:

* X86, in [/arch/x86/include/asm/barrier.h](https://github.com/torvalds/linux/blob/master/arch/x86/include/asm/barrier.h) header file:
``` c
#define dma_rmb()	barrier()
#define dma_wmb()	barrier()

#ifdef CONFIG_X86_32
#define __smp_mb()	asm volatile("lock; addl $0,-4(%%esp)" ::: "memory", "cc")
#else
#define __smp_mb()	asm volatile("lock; addl $0,-4(%%rsp)" ::: "memory", "cc")
#endif
#define __smp_rmb()	dma_rmb()
#define __smp_wmb()	barrier()
#define __smp_store_mb(var, value) do { (void)xchg(&var, value); } while (0)
```
* As seen in X86, ```__smp_rmb(),__smp_wmb()``` are defined as compiler barriers only. Because we know from the [Memory Barriers: a Hardware View for Software Hackers](./Memory_Barriers_a_Hardware_View_for_Software_Hackers.pdf)
  "Loads Are Not Reordered After Loads" and "Stores Are Not Reordered After Loads" in X86.

* In Alpha, however, in [/arch/alpha/include/asm/barrier.h](https://github.com/torvalds/linux/blob/master/arch/alpha/include/asm/barrier.h)  ```mb(),smb(),wmb()``` are defined as hardware memory barriers as seen:
```c
/* SPDX-License-Identifier: GPL-2.0 */
#ifndef __BARRIER_H
#define __BARRIER_H

#define mb()	__asm__ __volatile__("mb": : :"memory")
#define rmb()	__asm__ __volatile__("mb": : :"memory")
#define wmb()	__asm__ __volatile__("wmb": : :"memory")

#define __smp_load_acquire(p)						\
({									\
	compiletime_assert_atomic_type(*p);				\
	__READ_ONCE(*p);						\
})

#ifdef CONFIG_SMP
#define __ASM_SMP_MB	"\tmb\n"
#else
#define __ASM_SMP_MB
#endif

#include <asm-generic/barrier.h>

#endif		/* __BARRIER_H */
```
 and in [/include/asm-generic/barrier.h](https://github.com/torvalds/linux/blob/master/include/asm-generic/barrier.h), SMP equivalents are defined as default to ```mb(),smb(),wmb()``` which are the 

```c
#ifndef __smp_mb
#define __smp_mb()	mb()
#endif

#ifndef __smp_rmb
#define __smp_rmb()	rmb()
#endif

#ifndef __smp_wmb
#define __smp_wmb()	wmb()
#endif
```
which makes SMP equivalents are hardware barriers. Because we know from the [Memory Barriers: a Hardware View for Software Hackers](./Memory_Barriers_a_Hardware_View_for_Software_Hackers.pdf)
alpha reorders almost everything!


# More Resources ...
* [why-do-we-need-both-read-and-write-barriers](https://stackoverflow.com/questions/61307639/why-do-we-need-both-read-and-write-barriers)