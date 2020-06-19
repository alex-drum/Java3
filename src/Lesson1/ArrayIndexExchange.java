package Lesson1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayIndexExchange<T> {
    private T[] myArr;

    public ArrayIndexExchange(T[] myArr) {
        this.myArr = myArr;
    }

    public void exchIndx (int ind1, int ind2) {
        if (ind1 < 0 || ind2 < 0) {
            System.out.println("Попытка обращения к отрицательному индексу массива.");
        } else if (ind1 >= myArr.length || ind2 >= myArr.length) {
            System.out.println("Попытка обращения к индексу за пределами массива.");
        } else {
            T value1 = myArr[ind1];
            myArr[ind1] = myArr[ind2];
            myArr[ind2] = value1;
        }
    }

    public List<T> upToArrayList() {
        return new ArrayList<>(Arrays.asList(myArr));
    }

    public void print() {
        for (T i : myArr
             ) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        ArrayIndexExchange<Integer> test1 = new ArrayIndexExchange<>(new Integer[]{1, 2, 3, 4, 5});
        test1.exchIndx(-1,5);
        test1.print();
        test1.exchIndx(0,4);
        test1.print();
        test1.exchIndx(1,3);
        test1.print();

        ArrayIndexExchange<String> test2 = new ArrayIndexExchange<>(new String[]{"one", "two", "three", "four", "five"});
        test2.exchIndx(8,5);
        test2.print();
        test2.exchIndx(0,4);
        test2.print();
        test2.exchIndx(1,3);
        test2.print();

        List<Integer> myIntList = test1.upToArrayList();
        myIntList.add(6);
        myIntList.add(7);
        System.out.print("Новый ArrayList: ");
        for (Integer i: myIntList
             ) {
            System.out.print(i + " ");
        }
    }
}
