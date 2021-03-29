package homework1;


import java.util.ArrayList;

public class MainClass1 {


    private static <T>void changesPlaces(T[] arrays, int placeA, int placeB ){

        T tmp;
        if (placeA < arrays.length && placeB < arrays.length) {
            tmp = arrays[placeA];
            arrays[placeA] = arrays[placeB];
            arrays[placeB] = tmp;
        } else {
            System.out.println("invalid: index > array length");
        }
    }

    public static void main(String[] args) {
        Integer[] arrayInt = new Integer[10];
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < arrayInt.length; i++) {
            arrayInt[i] = i;
        }

        changesPlaces(arrayInt, 5, 2);

        for (int i = 0; i < arrayInt.length; i++) {
            System.out.print(arrayInt[i] + " ");
        }
        System.out.println();

        String[] arrayStr = new String[10];
        for (int i = 0; i < arrayStr.length; i++) {
            arrayStr[i] = "str " + i;
        }

        changesPlaces(arrayStr, 1, 2);

        for (int i = 0; i < arrayStr.length; i++) {
            System.out.print(arrayStr[i] + " ");
        }
    }

}
