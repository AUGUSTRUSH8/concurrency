package com.mmall.concurrency.singleton;

import com.mmall.concurrency.annotations.Recommend;
import com.mmall.concurrency.annotations.ThreadSafe;

/**
 * 枚举模式:最安全
 */
@ThreadSafe
@Recommend
public class SingletonExample7 {
    //私有的构造函数
    private SingletonExample7(){
    }
    public static SingletonExample7 getInstance(){
        return Singleton.INSTANCE.getInstance();
    }
    private enum  Singleton{
        INSTANCE;
        private SingletonExample7 singleton;
        //JVM保证此方法只被调用一次
        Singleton(){
            singleton=new SingletonExample7();
        }
        public SingletonExample7 getInstance(){
            return singleton;
        }
    }
}
