package jp.co.itrade.j1.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;

public class Task implements Runnable{

    private final Phaser phaser;

    public Task(Phaser phaser) {
        this.phaser = phaser;
//        this.phaser.register();
    }

    @Override
    public void run() {

        long threadId = Thread.currentThread().getId();
        System.out.println("START ThreadID=" + threadId);

        int randamNum = new Random().nextInt(10);
        try {
            Thread.sleep(randamNum * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("arriveAndDeregister start ThreadID=" + threadId);
        System.out.println("arriveAndDeregister Phase=" + phaser.getPhase());
        System.out.println("arriveAndDeregister terminated = " + phaser.isTerminated());
        System.out.println("arriveAndDeregister getRegisteredParties = " + phaser.getRegisteredParties());
        // コメントアウトすると、スレッド１が待ち続ける。Registしているから。
        phaser.arriveAndDeregister();
        System.out.println("arriveAndDeregister arriveAndDeregister end ThreadID=" + threadId);
        System.out.println("arriveAndDeregister Phase=" + phaser.getPhase());
        System.out.println("arriveAndDeregister terminated = " + phaser.isTerminated());
        System.out.println("arriveAndDeregister getRegisteredParties = " + phaser.getRegisteredParties());

//        try {
//            cyclicBarrier.await(10, TimeUnit.SECONDS);
//        } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
//            throw new RuntimeException(e);
//        }


    }
}
