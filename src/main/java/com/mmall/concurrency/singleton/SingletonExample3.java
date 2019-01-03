package com.mmall.concurrency.singleton;

import com.mmall.concurrency.annotations.NotRecommend;
import com.mmall.concurrency.annotations.ThreadSafe;

/**
 * 懒汉模式
 * 单例对象在第一次使用的时候被创建
 */
@ThreadSafe
@NotRecommend
public class SingletonExample3 {
    //私有的构造函数
    private SingletonExample3(){

    }
    //单例对象
    private static SingletonExample3 instance=null;
    //静态的工厂方法
    public static synchronized SingletonExample3 getInstance(){
        if(instance==null){
            instance=new SingletonExample3();
        }
        return instance;
    }
}
