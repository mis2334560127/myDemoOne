package com.thread.blockingQueue;

/**
 * 阻塞队列介绍：
 *      阻塞队列 (BlockingQueue)是Java util.concurrent包下重要的数据结构，BlockingQueue提供了线程安全的队列访问方式：
 *          当阻塞队列进行插入数据时，如果队列已满，线程将会阻塞等待直到队列非满；从阻塞队列取数据时，如果队列已空，
 *          线程将会阻塞等待直到队列非空。并发包下很多高级同步类的实现都是基于BlockingQueue实现的。
 *
 * ===================================================================================================
 * ArrayBlockingQueue ：由数组结构组成的有界阻塞队列。
 * LinkedBlockingQueue ：由链表结构组成的有界阻塞队列。
 * PriorityBlockingQueue ：支持优先级排序的无界阻塞队列。
 * DelayQueue：使用优先级队列实现的无界阻塞队列。
 * SynchronousQueue：不存储元素的阻塞队列。
 * LinkedTransferQueue：链表结构组成的无界阻塞队列。
 * LinkedBlockingDeque：链表结构组成的双向阻塞队列。
 *
 * =============================阻塞队列下的方法=================================================
 *                  |      异常          |   特定值                 |           阻塞          |               超时
 *  插入            |     add(o)         |    offer(o)             |      put(o)             |offer(o,timeout,timeunit)
 *  移除            |    remove(o)      |     poll(o)             |       take(o)           | poll(timeout,timeunit)
 *  检查            |   peek(o)         |
 *=================================================================================================
 * 队列（Queue）：先进先出(First In First Out)
 * 栈（Stack）：先进后出(First In Last Out)
 * 阻塞队列与普通队列的区别：
 *              当向空的阻塞队列中那东西时，就拿不到东西了，那么队列就会阻塞，只要队列中一有东西，该东西就会被拿走
 *              当向满的阻塞队列中放东西是，就放不进队列里面，那么就会阻塞，只要队列中有一个空位置，外面的东西就会进入到队列中
 *              普通队列就没有阻塞这个状态。
 *
 * 队列在多线程应用中，常用于生产-消费场景
 */

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 模拟买油条的场景，1个老板在做油条，3个顾客在排队买：
 */
public class BlockingQueueTest {
    //final成员变量表示常量，只能被赋值一次，赋值后值不再改变。
    private static final int queueSize = 5;
//    由数组结构组成的有界阻塞队列
    private static final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(queueSize);
    private static final int produceSpeed = 2000;//生产速度(越小越快)
    private static final int consumeSpeed = 10;//消费速度(越小越快)

    //生产者
    public static class Producer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("老板准备炸油条了，架子上还能放：" + (queueSize - queue.size()) + "根油条");
                    queue.put("1根油条");
                    System.out.println("老板炸好了1根油条，架子上还能放：" + (queueSize - queue.size()) + "根油条");
                    Thread.sleep(produceSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //消费者
    public static class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("A 准备买油条了，架子上还剩" + queue.size() + "根油条");
                    queue.take();
                    System.out.println("A 买到1根油条，架子上还剩" + queue.size() + "根油条");
                    Thread.sleep(consumeSpeed);

                    System.out.println("B 准备买油条了，架子上还剩" + queue.size() + "根油条");
                    queue.take();
                    System.out.println("B 买到1根油条，架子上还剩" + queue.size() + "根油条");
                    Thread.sleep(consumeSpeed);

                    System.out.println("C 准备买油条了，架子上还剩" + queue.size() + "根油条");
                    queue.take();
                    System.out.println("C 买到1根油条，架子上还剩" + queue.size() + "根油条");
                    Thread.sleep(consumeSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        Thread producer = new Thread(new Producer());
        Thread consumer = new Thread(new Consumer());
        producer.start();
        consumer.start();
    }
}
