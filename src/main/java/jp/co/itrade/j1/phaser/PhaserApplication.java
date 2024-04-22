package jp.co.itrade.j1.phaser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

@SpringBootApplication
public class PhaserApplication {

    public static void main(String[] args) {

        Phaser phaser = new Phaser();
        System.out.println("Phase=" + phaser.getPhase());
        System.out.println("getRegisteredParties = " + phaser.getRegisteredParties());
        phaser.register();
        System.out.println("Phase=" + phaser.getPhase());
        System.out.println("getRegisteredParties = " + phaser.getRegisteredParties());
        phaser.register();
        System.out.println("Phase=" + phaser.getPhase());
        System.out.println("getRegisteredParties = " + phaser.getRegisteredParties());
        phaser.register();
        System.out.println("Phase=" + phaser.getPhase());
        System.out.println("getRegisteredParties = " + phaser.getRegisteredParties());

        try(ExecutorService executorService = Executors.newFixedThreadPool(2)){
            for(int i=0;i<2;i++){
                executorService.submit(new Task(phaser));
            }

            // try-with-resouceの外でarriveAndAwaitAdvanceを実行すると、スレッドが既に終わってしまう。
            long threadId = Thread.currentThread().getId();
            System.out.println("★arriveAndAwaitAdvance start ThreadID=" + threadId);
            System.out.println("★arriveAndAwaitAdvance Phase=" + phaser.getPhase());
            System.out.println("★arriveAndAwaitAdvance terminated = " + phaser.isTerminated());
            System.out.println("★arriveAndAwaitAdvance getRegisteredParties = " + phaser.getRegisteredParties());
            phaser.arriveAndAwaitAdvance();
            System.out.println("★arriveAndAwaitAdvance arriveAndAwaitAdvance end ThreadID=" + threadId);
            System.out.println("★arriveAndAwaitAdvance Phase=" + phaser.getPhase());
            System.out.println("★arriveAndAwaitAdvance terminated = " + phaser.isTerminated());
            System.out.println("★arriveAndAwaitAdvance getRegisteredParties = " + phaser.getRegisteredParties());

            // getRegisteredPartiesが０になっている場合、 Phaser は終了状態(termination state) になる
            // 終了状態になると、arriveAndAwaitAdvanceは待っていてくれない。
            // arriveAndAwaitAdvanceのタイミングでgetRegisteredParties=1である必要がある。
            // なので、phaser.register()の回数 = arriveAndDeregisterの回数＋１
            // +1 はarriveAndAwaitAdvance用として実行しておくと考えておくと良い。
            // https://qiita.com/opengl-8080/items/70ee3fe6a9f98b2b2c8f
        }


        SpringApplication.run(PhaserApplication.class, args);

    }
}
