package Lesson1.Fruits;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private ArrayList<T> box;
    private float unitWeight;
    private static int id;
    private int thisBoxId;

    public Box() {
        this.box = new ArrayList<>();
        ++id;
        thisBoxId = id;
    }

    public Box(T fruit, int quantity) {
        this.box = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            box.add(fruit);
            unitWeight = fruit.getWeight();
        }
        ++id;
        thisBoxId = id;
    }

    public int getId() {
        return thisBoxId;
    }

    public void put(T fruit, int quantity) {
        for (int i = 0; i < quantity; i++) {
            box.add(fruit);
        }
        unitWeight = fruit.getWeight();
    }

    public float getWeight() {
        return box.size() * unitWeight;
    }

    public boolean compare(Box anotherBox) {
        return this.getWeight() == anotherBox.getWeight();
    }

    public void reloadTo(Box<T> anotherBox) {
        anotherBox.box.addAll(this.box);
        this.empty();

    }

    public void empty() {
        this.box.clear();
    }
}
