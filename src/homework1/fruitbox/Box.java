package homework1.fruitbox;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    //private T t;
    private ArrayList<T> boxList;

    public Box(){
        this.boxList = new ArrayList<>();
    }

    void add(T t){
        boxList.add(t);
    }

    public float getWeight(){
        return boxList.size() * boxList.get(0).weight;
    }

    public boolean compare (Box box){
        return Math.abs(this.getWeight()-box.getWeight()) < 0.0001 ;
    }

    public ArrayList<T> getBoxList() {
        return boxList;
    }

    public void overflow (Box<T> box){
        this.boxList = box.getBoxList();
    }
}
