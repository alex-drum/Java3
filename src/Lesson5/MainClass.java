package Lesson5;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MainClass {
    public static final int CARS_COUNT = 4;
    public static int getCarsCount() {
        return CARS_COUNT;
    }
    private static CountDownLatch cdlStartRace = new CountDownLatch(1);

    public static CountDownLatch getCdlStartRace() {
        return cdlStartRace;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(), new Road(40), new Finish());
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        ArrayList<Thread> threadsList = new ArrayList<>(CARS_COUNT);
        for (int i = 0; i < cars.length; i++) {
            threadsList.add(new Thread(cars[i]));
        }
        for (Thread t: threadsList) {
            t.join();
        }
        for (Thread t: threadsList) {
            t.start();
        }
        while (true) {
            if (cars[cars.length-1].getCdlCount() == 0) {
                try {
                    Thread.sleep(500);
                    System.out.print("НА СТАРТ...");
                    Thread.sleep(500);
                    System.out.print("ВНИМАНИЕ...");
                    Thread.sleep(500);
                    System.out.print("МАРШ!!!!\n");
                    System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                    cdlStartRace.countDown();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            Finish.getFinishCdl().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}

