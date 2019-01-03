package com.mmall.concurrency.singleton;

import com.mmall.concurrency.annotations.NotRecommend;
import com.mmall.concurrency.annotations.NotThreadSafe;
import com.mmall.concurrency.annotations.ThreadSafe;

/**
 * 懒汉模式==》双重同步锁单例模式
 * 单例对象在第一次使用的时候被创建
 */
@NotThreadSafe
public class SingletonExample4 {
    //私有的构造函数
    private SingletonExample4(){

    }

    //1.memory=allocate()分配对象的内存空间
    //2.ctorInstance()初始化对象
    //3.instance=memory设置instance指向刚分配的内存

    //JVM和CPU优化发生了指令重排

    //1.memory=allocate()分配对象的内存空间
    //3.instance=memory设置instance指向刚分配的内存
    //2.ctorInstance()初始化对象



    //单例对象
    private static SingletonExample4 instance=null;
    //静态的工厂方法
    public static synchronized SingletonExample4 getInstance(){
        if(instance==null){//双重检测机制
            synchronized (SingletonExample4.class){//同步锁
                if (instance==null){
                    instance=new SingletonExample4();
                }
            }
        }
        return instance;
    }
}
