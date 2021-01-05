package com.parabolagames.misc;

public class LazySingleton {
    public static LazySingleton getInstance(){
        return LazySingletonHolder.INSTANCE;
    }

    //https://stackoverflow.com/questions/24538509/does-the-java-classloader-load-inner-classes
    public static class LazySingletonHolder {
      static final LazySingleton INSTANCE = new LazySingleton();
    }
}
