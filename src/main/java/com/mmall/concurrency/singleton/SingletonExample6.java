package com.mmall.concurrency.singleton;


import com.mmall.concurrency.annotations.ThreadSafe;
import sun.awt.geom.AreaOp;

/**
 * 饿汉模式
 * 单例对象在类装载的时候被创建
 */
@ThreadSafe
public class SingletonExample6 {
    //私有的构造函数
    private SingletonExample6(){

    }
    //单例对象
    //注意以下两个static的顺序，后者在前会出错
    private static SingletonExample6 instance=null;
    static{
        instance=new SingletonExample6();
    }

    //静态的工厂方法
    public static SingletonExample6 getInstance(){
        return instance;
    }

    public static void main(String[] args) {
        System.out.println(getInstance().hashCode());
        System.out.println(getInstance().hashCode());
    }
}
