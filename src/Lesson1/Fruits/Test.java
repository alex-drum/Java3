package Lesson1.Fruits;

public class Test {

    public static void main(String[] args) {
        Apple apple = new Apple();

        Box<Apple> appleBox1 = new Box<>();
        appleBox1.put(apple, 3);
        appleBox1.put(new Apple(),3);
        System.out.print("Вес коробки № " + appleBox1.getId() + ": ");
        System.out.println(appleBox1.getWeight());

        Box<Apple> appleBox2 = new Box<>(new Apple(), 5);
        System.out.print("Вес коробки № " + appleBox2.getId() + ": ");
        System.out.println(appleBox2.getWeight());

        Box<Orange> orangeBox1 = new Box<>(new Orange(), 5);
        System.out.print("Вес коробки № " + orangeBox1.getId() + ": ");
        System.out.println(orangeBox1.getWeight());

        Box<Orange> orangeBox2 = new Box<>();
        orangeBox2.put(new Orange(), 5);
        System.out.print("Вес коробки № " + orangeBox2.getId() + ": ");
        System.out.println(orangeBox1.getWeight());

        appleBox1.reloadTo(appleBox2);
        System.out.print("Вес коробки № " + appleBox1.getId() + " после пересыпания: ");
        System.out.println(appleBox1.getWeight());
        System.out.print("Вес коробки № " + appleBox2.getId() + " после пересыпания: ");
        System.out.println(appleBox2.getWeight());

        boolean compResult = appleBox1.compare(orangeBox1);
        System.out.print("Вес коробок № " + appleBox1.getId() + " и № "
                + orangeBox1.getId() + " одинаков: ");
        System.out.println(compResult);

        compResult = orangeBox1.compare(orangeBox2);
        System.out.print("Вес коробок № " + orangeBox1.getId() + " и № "
                + orangeBox2.getId() + " одинаков: ");
        System.out.println(compResult);

//        appleBox1.reloadTo(orangeBox1); // Error: incompatible types!!!

//        appleBox1.empty();
//        System.out.print("Вес коробки № " + appleBox1.getId() + " после опустошения: ");
//        System.out.println(appleBox1.getWeight());
//
//        appleBox2.empty();
//        System.out.print("Вес коробки № " + appleBox2.getId() + " после опустошения: ");
//        System.out.println(appleBox2.getWeight());
//
//        compResult = appleBox1.compare(appleBox2);
//        System.out.print("Вес коробок № " + appleBox1.getId() + " и № "
//                + appleBox2.getId() + " одинаков: ");
//        System.out.println(compResult);
//
//        orangeBox1.empty();
//        System.out.print("Вес коробки № " + orangeBox1.getId() + " после опустошения: ");
//        System.out.println(orangeBox1.getWeight());
//        compResult = orangeBox1.compare(orangeBox2);
//        System.out.print("Вес коробок № " + orangeBox1.getId() + " и № "
//                + orangeBox2.getId() + " одинаков: ");
//        System.out.println(compResult);
    }
}

