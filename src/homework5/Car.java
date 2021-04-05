package homework5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;
    CyclicBarrier cb;
    Semaphore sm;
    CountDownLatch cdlStart;
    CountDownLatch cdlStop;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(CountDownLatch cdlStart, CountDownLatch cdlStop, Semaphore sm, CyclicBarrier cb, Race race, int speed) {
        this.cdlStart = cdlStart;
        this.cdlStop = cdlStop;
        this.sm = sm;
        this.cb = cb;
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            cb.await();
            System.out.println(this.name + " готов");
            cb.await();
            cdlStart.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this, sm);
            cdlStop.countDown();
        }
    }
}
