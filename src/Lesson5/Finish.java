package Lesson5;

import java.util.concurrent.CountDownLatch;

public class Finish extends Stage {
    private static CountDownLatch finishCdl = new CountDownLatch(MainClass.CARS_COUNT);
    public static CountDownLatch getFinishCdl() {
        return finishCdl;
    }

    public Finish() {
    }

    @Override
    public void go(Car c) {
        finishCdl.countDown();
        if (finishCdl.getCount() == MainClass.CARS_COUNT - 1) {
            System.out.println(c.getName() + " WIN!");
        }
    }
}
