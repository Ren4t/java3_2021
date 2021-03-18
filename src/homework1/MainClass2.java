package homework1;

import java.util.ArrayList;

public class MainClass2 {

    private static <T>ArrayList<T> toArrayList(T[] array){
       ArrayList<T> arrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            arrayList.add(array[i]);
        }
       return arrayList;
    }

    public static void main(String[] args) {
        ArrayList<Integer> arrayList;
        ArrayList<String> arrayListStr;

        Integer[] array = new Integer[10];
        String[] arrayStr = new String[10];

        for (int i =0; i < array.length; i++){
            array[i] = i;
        }
        arrayList = toArrayList(array);
        System.out.println(arrayList.toString());



        for (int i = 0; i < arrayStr.length; i++) {
            arrayStr[i] = "str " + i;
        }

        arrayListStr = toArrayList(arrayStr);
        System.out.println(arrayListStr.toString());
    }
}
