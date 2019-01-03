package com.mmall.concurrency.singleton;

import com.mmall.concurrency.annotations.NotThreadSafe;

/**
 * 懒汉模式
 * 单例对象在第一次使用的时候被创建
 */
@NotThreadSafe
public class SingletonExample1 {
    //私有的构造函数
    private SingletonExample1(){

    }
    //单例对象
    private static SingletonExample1 instance=null;
    //静态的工厂方法
    public static SingletonExample1 getInstance(){
        if(instance==null){
            instance=new SingletonExample1();
        }
        return instance;
    }
}
