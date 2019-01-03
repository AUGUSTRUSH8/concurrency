package com.mmall.concurrency.singleton;


import com.mmall.concurrency.annotations.ThreadSafe;

/**
 * 饿汉模式
 * 单例对象在类装载的时候被创建
 */
@ThreadSafe
public class SingletonExample2 {
    //私有的构造函数
    private SingletonExample2(){

    }
    //单例对象
    private static SingletonExample2 instance=new SingletonExample2();
    //静态的工厂方法
    public static SingletonExample2 getInstance(){
        return instance;
    }
}
