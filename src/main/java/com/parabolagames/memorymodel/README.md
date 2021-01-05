* [Java Concurrency in Practice](https://jcip.net/)
* [JSR 133 (Java Memory Model) FAQ](http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html)
* [The JSR-133 Cookbook for Compiler Writers](http://gee.cs.oswego.edu/dl/jmm/cookbook.html)
* [The "Double-Checked Locking is Broken" Declaration](http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html)
* [Synchronization and the Java Memory Model](http://gee.cs.oswego.edu/dl/cpj/jmm.html)
* [The Java Memory Model](http://www.cs.umd.edu/~pugh/java/memoryModel/)



* String Hash Code (Notice that ```hash``` variable is not ```volatile```):
```java
    /** Cache the hash code for the string */
    private int hash; // Default to 0
```

```java
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            hash = h = isLatin1() ? StringLatin1.hashCode(value)
                                  : StringUTF16.hashCode(value);
        }
        return h;
    }
```