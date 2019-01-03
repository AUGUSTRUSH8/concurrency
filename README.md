# concurrency
这是一个Java并发编程学习仓库，我能叫它`awesome Java concurrent programming`吗:blush:
## 简介
>这个仓库对Java并发编程做了较为详尽的介绍，每个包都对应着一个知识点，学习自mooc平台哈，下面摘录一些学习心得
#### 缓存一致性

MESI：

M:modified

E:enclusive

S:shared

I:invalid

#### 并发模拟

postman：Http请求模拟工具

Apache Bench（AB）：apache附带的工具，测试网站性能

Jmeter：apache组织开发的压力测试工具

代码方式：countlatch和semaphore等

```java
//代码方式模拟高并发
@Slf4j
@NotThreadSafe
public class ConcurrencyTest {
    //请求总数
    public static int clientTotal=5000;
    //同时并发执行的线程数
    public static int threadTotal=200;
    public static int count;
    public static void main(String[] args) throws Exception {
        ExecutorService executorService= Executors.newCachedThreadPool();
        final Semaphore semaphore=new Semaphore(threadTotal);
        final CountDownLatch countDownLatch=new CountDownLatch(clientTotal);
        for(int i=0;i<clientTotal;i++){
            executorService.execute(()->{
                try{
                    semaphore.acquire();
                    add();
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count:{}",count);
    }
    public static void add(){
        count++;
    }
}
```

#### 线程安全性

- 原子性--提供了互斥访问，同一时刻只能有一个线程来对它进行操作
- 可见性
- 有序性

compareAndSet()---通常用于AtomicBoolean类当中， 当希望某个代码只执行一次，则可设个bool量，之前为false，执行后变为true

**Unsafe.compareAndSwapInt**

CAS方法底层是个本地方法，即是说不是Java语言写的

**LongAdder**：这个类和**AtomicLong**有点像，但是有区别的，前面这个改进就在锁分段这个思想上，这么多位是不用全锁住的，不停的CAS也是有性能损耗的，但是它有的时候也有损失，所以在并发比较高的时候可以考虑使用前者，并发一般的时候还是使用原生的Atomic类以保证安全和无损

```java
@Slf4j
@ThreadSafe
public class AtomicExample3 {
    //请求总数
    public static int clientTotal=5000;
    //同时并发执行的线程数
    public static int threadTotal=200;
    public static LongAdder count=new LongAdder();
    public static void main(String[] args) throws Exception {
        ExecutorService executorService= Executors.newCachedThreadPool();
        final Semaphore semaphore=new Semaphore(threadTotal);
        final CountDownLatch countDownLatch=new CountDownLatch(clientTotal);
        for(int i=0;i<clientTotal;i++){
            executorService.execute(()->{
                try{
                    semaphore.acquire();
                    add();
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count:{}",count);
    }
    public static void add(){
        count.increment();
    }
}
```

**AtomicReference类**：

```java
@Slf4j
@ThreadSafe
public class AtomicExample4 {
    private static AtomicReference<Integer> count=new AtomicReference<>(0);

    public static void main(String[] args) {
        count.compareAndSet(0,2);
        count.compareAndSet(0,1);
        count.compareAndSet(1,3);
        count.compareAndSet(2,4);
        count.compareAndSet(3,5);
        log.info("count:{}",count.get());
    }
}
```

**AtomicIntegerFieldUpdater**类（用的不多），对对象属性有要求，必须是volatile修饰，且不能是static字段

```
@Slf4j
@ThreadSafe
public class AtomicExample5 {
    private static AtomicIntegerFieldUpdater<AtomicExample5> updater=AtomicIntegerFieldUpdater.newUpdater(AtomicExample5.class,"count");
    @Getter
    private volatile int count=100;

    public static void main(String[] args) {
        AtomicExample5 example5=new AtomicExample5();
        if(updater.compareAndSet(example5,100,120)){
            log.info("update success,{}",example5.getCount());
        }
        if(updater.compareAndSet(example5,100,120)){
            log.info("update success2,{}",example5.getCount());
        }else {
            log.info("update fail,{}",example5.getCount());
        }
    }
}
```

**AtomicBoolean类**，一个很常用的代码片段，保证一段代码只执行一次

```java
@Slf4j
@ThreadSafe
public class AtomicExample6 {
    private static AtomicBoolean isHappened=new AtomicBoolean(false);
    //请求总数
    public static int clientTotal=5000;
    //同时并发执行的线程数
    public static int threadTotal=200;
    public static void main(String[] args) throws Exception{
        ExecutorService executorService= Executors.newCachedThreadPool();
        final Semaphore semaphore=new Semaphore(threadTotal);
        final CountDownLatch countDownLatch=new CountDownLatch(clientTotal);
        for(int i=0;i<clientTotal;i++){
            executorService.execute(()->{
                try{
                    semaphore.acquire();
                    test();
                    semaphore.release();
                }catch (Exception e){
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("isHappened:{}",isHappened.get());
    }
    private static void test(){
        if(isHappened.compareAndSet(false,true)){
            log.info("execute");
        }
    }
}
```

- 原子性--锁
  - synchronized--依赖JVM
  - Lock：依赖特殊的CPU指令，代码实现，ReentrantLock

**Synchronized**：修饰代码块

```java
@Slf4j
public class SynchronizedExample1 {
    //修饰一个代码块
    public void test1(int j){
        synchronized (this){
            for(int i=0;i<10;i++){
                log.info("test1{}-{}",j,i);
            }
        }
    }
    //修饰一个方法
    public synchronized void test2(int j){
        for(int i=0;i<10;i++){
            log.info("test2{}-{}",j,i);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample1 example1=new SynchronizedExample1();
        ExecutorService executorService= Executors.newCachedThreadPool();
        executorService.execute(()->{
            example1.test1(1);
        });
        executorService.execute(()->{
            example1.test1(2);
        });
    }
}
```

修饰类和静态方法

```java
@Slf4j
public class SynchronizedExample2 {
    //修饰一个类
    public static void test1(int j){
        synchronized (SynchronizedExample2.class){
            for(int i=0;i<10;i++){
                log.info("test1{}-{}",j,i);
            }
        }
    }
    //修饰一个静态方法
    public static synchronized void test2(int j){
        for(int i=0;i<10;i++){
            log.info("test2{}-{}",j,i);
        }
    }

    public static void main(String[] args) {
        SynchronizedExample2 example1=new SynchronizedExample2();
        ExecutorService executorService= Executors.newCachedThreadPool();
        executorService.execute(()->{
            example1.test1(1);
        });
        executorService.execute(()->{
            example1.test1(2);
        });
    }
}
```

- Synchronized：不可中断锁，适合竞争不激烈，可读性好

- Lock：可中断锁，多样化同步，竞争激烈时能维持常态

- Atomic：竞争激烈时能维持常态，比Lock性能好；只能同步一个值

**可见性**：

导致的原因：

- 线程交叉执行
- 重排序结合线程交叉执行
- 共享变量更新后的值没有在工作内存和主内存间及时更新

**volatile**：通过加入内存屏障和禁止重排序优化来实现的（**它的原理还得重新看！**）

- 对volatile变量写操作的时候，会在写操作后加入一条store屏障指令，将本地内存中的共享变量刷新到主内存
- 对volatile变量读操作的时候，会在读操作后加入一条load屏障指令，从主内存中读取共享变量
- volatile变量最适合作为状态标志量

**有序性 **：

只要虚拟机判断指令不满足所有的8条**happens-before原则**，则可以对它进行重排序

**安全发布对象**：

- 在静态初始化函数中初始化一个对象引用
- 将对象的引用保存到volatile类型域或者AtomicReference对象中
- 将对象的引用保存到某个正确构造对象的final域中
- 将对象的引用保存到一个由锁保护的域中

以上四种方式就是单例的几种方式

单例：**懒汉模式&饿汉模式**

饿汉式要注意的问题就是看它的私有构造函数加载是不是很复杂，如果很复杂，将造成较大的资源浪费

**final **：

注意final修饰原型数据的时候，数据是不可变的，当它修饰引用类型的时候，只是说引用不可再指向别的引用，但引用对象的值却是可以改变的
